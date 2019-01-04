package com.cloud.miaosha.vo;

import com.cloud.miaosha.domain.OrderInfo;

public class OrderDetailVo {
	private GoodVo goods;
	private OrderInfo order;
	public GoodVo getGoods() {
		return goods;
	}
	public void setGoods(GoodVo goods) {
		this.goods = goods;
	}
	public OrderInfo getOrder() {
		return order;
	}
	public void setOrder(OrderInfo order) {
		this.order = order;
	}
}
