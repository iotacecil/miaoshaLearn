package com.cloud.miaosha.service;

import com.cloud.miaosha.domain.MiaoshaOrder;
import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.domain.OrderInfo;
import com.cloud.miaosha.redis.MiaoshaKey;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.vo.GoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    /**
     * 输入用户和商品 返回订单
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodVo goods) {

        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
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
}
