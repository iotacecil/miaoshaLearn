package com.cloud.miaosha.controller;

import com.cloud.miaosha.domain.MiaoshaOrder;
import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.domain.OrderInfo;
import com.cloud.miaosha.result.CodeMsg;
import com.cloud.miaosha.result.Result;
import com.cloud.miaosha.service.GoodsService;
import com.cloud.miaosha.service.MiaoshaService;
import com.cloud.miaosha.service.OrderService;
import com.cloud.miaosha.vo.GoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoshaService miaoshaService;
	@RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
	@ResponseBody
	public Result<OrderInfo> list(MiaoshaUser user,
					   @RequestParam("goodsId")long goodsId) {
		// 没登陆
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		//判断库存
		GoodVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStockCount();
		if(stock <= 0) {
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
//    	从用户订单查询是否已经对这个物品下过单了
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		//1.减库存 2.下订单 3.写入秒杀订单 这三步是一个是事务
		OrderInfo orderInfo = miaoshaService.miaosha(user, goods);

		return Result.success(orderInfo);
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
