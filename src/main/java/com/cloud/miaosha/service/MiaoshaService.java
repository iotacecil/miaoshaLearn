package com.cloud.miaosha.service;

import com.cloud.miaosha.domain.MiaoshaOrder;
import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.domain.OrderInfo;
import com.cloud.miaosha.redis.MiaoshaKey;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.util.MD5Util;
import com.cloud.miaosha.util.UUIDUtil;
import com.cloud.miaosha.vo.GoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    public void reset(List<GoodVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }

    /**
     * 输入用户和商品 返回订单
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodVo goods) {

        //减库存 下订单 写入秒杀订单
        boolean success = false;
        try {
            success = goodsService.reduceStock(goods);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("想要减少库存+:"+success);
        if(success){
            //order_info maiosha_order
            return orderService.createOrder(user, goods);
        }else{
            // 如果失败  说明秒杀失败 做标记 防止一直轮询
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getMiaoshaResult(Long userid, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userid, goodsId);
        if(order != null){
            return order.getOrderId();
        }else{
            // 判断是排队中还是失败了
            boolean isOver = getGoodsOver(goodsId);
            if(isOver) {
                return -1;
            }else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }

    public String createMiaoshaPath(MiaoshaUser user,long goodsId) {
        if(user == null || goodsId <=0){
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisService.set(MiaoshaKey.getMiaoshaPath,"" +user.getId()+"_"+goodsId,str );
        return str;
    }

    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
        if(user == null || path == null){
            return false;
        }
        String pathRec = redisService.get(MiaoshaKey.getMiaoshaPath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(pathRec);
    }

    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if(user == null || goodsId <=0){
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }

        // 生成随机验证码 保存到key为用户,商品id用于用户输入的验证
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片	
        return image;
    }
    // 计算表达式的结果
    private static int calc(String exp) {
        try{
            ScriptEngineManager manger = new ScriptEngineManager();
            ScriptEngine engine = manger.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    // 加减乘的验证码
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public static void main(String[] args) {
        System.out.println(calc("1+2*4"));
    }


    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verify) {
        if(user == null || goodsId <=0){
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if(codeOld == null || codeOld - verify != 0){
            return false;
        }
        // 从redis删除 否则还可以用
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId);
        return true;
    }
}
