package com.cloud.miaosha.controller;

import com.cloud.miaosha.domain.MiaoshaOrder;
import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.rabbitmq.MiaoshaMessage;
import com.cloud.miaosha.rabbitmq.MiaoshaSender;
import com.cloud.miaosha.redis.GoodsKey;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.result.CodeMsg;
import com.cloud.miaosha.result.Result;
import com.cloud.miaosha.service.GoodsService;
import com.cloud.miaosha.service.MiaoshaService;
import com.cloud.miaosha.service.OrderService;
import com.cloud.miaosha.vo.GoodVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{

	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

	@Autowired
	RedisService redisService;

	@Autowired
	MiaoshaSender sender;

	private Map<Long,Boolean> localOverMap = new HashMap<Long, Boolean>();

	@Autowired
	MiaoshaService miaoshaService;
	@RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> list(MiaoshaUser user,
					   @RequestParam("goodsId")long goodsId) {
		// 没登陆
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		// redis中预减库存
		Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
		if(stock < 0){
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
//    	从用户订单查询是否已经对这个物品下过单了
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		// 入队
		MiaoshaMessage msg = new MiaoshaMessage();
		msg.setUser(user);
		msg.setGoodsId(goodsId);
		sender.sendMiaoshaMessage(msg);
		return Result.success(0);
	}

	// 系统初始化 读数据库库存，写到redis
	@Override
	public void afterPropertiesSet() throws Exception {
		List<GoodVo> goodslist = goodsService.listGoodsVo();
		if(goodslist!=null){
			for(GoodVo goods : goodslist){
				redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId() ,goods.getStockCount() );
			}
		}
	}

	// 客户端轮询接口 判断是否秒杀到
	/*
	 orderID:成功
	 -1：秒杀失败
	 0：排队中
	 */
	@RequestMapping(value = "/result",method = RequestMethod.GET)
	@ResponseBody
	public Result<Long> miaoshaResult(Model model,MiaoshaUser user,@RequestParam("goodsId")long goodsId){
		model.addAttribute("user",user);
		if(user == null){
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		long rst = miaoshaService.getMiaoshaResult(user.getId(),goodsId);
		return Result.success(rst);
	}

//    @RequestMapping("/do_miaosha")
//    public String list(Model model, MiaoshaUser user,
//                       @RequestParam("goodsId")long goodsId) {
//    	model.addAttribute("user", user);
//    	// 没登陆
//    	if(user == null) {
//    		return "login";
//    	}
//    	//判断库存
//    	GoodVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//    	int stock = goods.getStockCount();
//    	if(stock <= 0) {
//    		model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
//    		return "miaosha_fail";
//    	}
////    	从用户订单查询是否已经对这个物品下过单了
//    	MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//    	if(order != null) {
//    		model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
//    		return "miaosha_fail";
//    	}
//    	//1.减库存 2.下订单 3.写入秒杀订单 这三步是一个是事务
//    	OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//    	model.addAttribute("orderInfo", orderInfo);
//    	model.addAttribute("goods", goods);
//        return "order_detail";
//    }
}
