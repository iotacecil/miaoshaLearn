package com.cloud.miaosha.redis;

public class MiaoshaUserKey extends BasePrefix{

	public static final int TOKEN_EXPIRE = 3600*24 * 2;

	// 构造函数里加上过期时间
	public MiaoshaUserKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}
	// 调用构造函数
	public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE,"tk");
	public static MiaoshaUserKey getById = new MiaoshaUserKey(0, "id");

}
