package com.cloud.miaosha.controller;


import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.domain.OrderInfo;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.result.CodeMsg;
import com.cloud.miaosha.result.Result;
import com.cloud.miaosha.service.GoodsService;
import com.cloud.miaosha.service.MiaoshaUserService;
import com.cloud.miaosha.service.OrderService;
import com.cloud.miaosha.vo.GoodVo;
import com.cloud.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user,
									  @RequestParam("orderId") long orderId) {
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
    		return Result.error(CodeMsg.ORDER_NOT_EXIST);
    	}
    	// 用订单的商品id 查商品信息
    	long goodsId = order.getGoodsId();
    	GoodVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return Result.success(vo);
    }
    
}
