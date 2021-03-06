package com.cloud.miaosha.service;

import com.cloud.miaosha.dao.OrderDao;
import com.cloud.miaosha.domain.MiaoshaOrder;
import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.domain.OrderInfo;
import com.cloud.miaosha.redis.OrderKey;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.vo.GoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Date;

@Service
public class OrderService {

	public void deleteOrders() {
		orderDao.deleteOrders();
		orderDao.deleteMiaoshaOrders();
	}

	@Autowired
	OrderDao orderDao;


	public boolean insertnull(){
		MiaoshaOrder obj = new MiaoshaOrder();
		obj.setUserId(1111l);
		if(obj.getTestNull() == null){
			obj.setTestNull(Types.INTEGER);
			obj.setTestNull(Types.INTEGER);
		}
		return orderDao.insertMiaoshaOrder(obj) > 0;

	}

	@Autowired
	RedisService redisService;

	public OrderInfo getOrderById(long orderId) {
		return orderDao.getOrderById(orderId);
	}

	// 根据用户ID和商品ID查找相应订单
	public  MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId) {
		return redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId , MiaoshaOrder.class);
//		return orderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
	}

	// 根据用户和商品信息创建订单信息
	@Transactional
	public OrderInfo createOrder(MiaoshaUser user, GoodVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setGoodsName(goods.getGoodsName());
		orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		// 数据库insert order表 mybatis成功之后会把id加到对象中
		orderDao.insert(orderInfo);
		MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
		miaoshaOrder.setGoodsId(goods.getId());
		miaoshaOrder.setOrderId(orderInfo.getId());
		miaoshaOrder.setUserId(user.getId());
		// 数据库 miaoshaOrder表
		orderDao.insertMiaoshaOrder(miaoshaOrder);

		redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getId()+"_"+goods.getId(),miaoshaOrder);
		return orderInfo;
	}
	
}
