package com.cloud.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService{
	@Autowired

	JedisPool jedisPool;
	@SuppressWarnings("unchecked")//屏蔽警告
	private <T> T stringToBean(String str,Class<T> clazz){
		//1. 参数校验
		if(str == null || str.length() <= 0 || clazz == null) {
			return null;
		}
		//2 如果是int，string，Long
		if(clazz == int.class || clazz == Integer.class) {
			return (T)Integer.valueOf(str);
		}else if(clazz == String.class) {
			return (T)str;
		}else if(clazz == long.class || clazz == Long.class) {
			return  (T)Long.valueOf(str);
		}else {
			//fastJson 其他List类型要再写
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}

	}
	public <T> boolean set(KeyPrefix prefix,String key,T value){
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
			String str = beanToString(value);
			if(str == null||str.length()<=0)return false;
			String prefixKey = prefix.getPrefix()+key;
			System.out.println(prefixKey);
			int expire = prefix.expireSeconds();
			//永不过期
			if(expire<=0){
				jedis.set(prefixKey,str);

			}else{
				jedis.setex(prefixKey,expire,str);
			}
			return true;
		}finally{
			returnToPool(jedis);
		}
	}
//任意类型转化成字符串

	private <T> String beanToString(T value){
		//2. 添加空判断
		if(value == null)return null;
		//3. 如果是数字，字符串，Long
		Class<?> clazz = value.getClass();
		if(clazz == int.class || clazz == Integer.class) {
			return ""+value;
		}else if(clazz == String.class) {
			return (String)value;
		}else if(clazz == long.class || clazz == Long.class) {
			return ""+value;
		}else {
			return JSON.toJSONString(value);
		}

	}
	public<T> T get(KeyPrefix prefix,String key,Class<T> clazz){
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();

			String prefixKey = prefix.getPrefix()+key;
			String value =  jedis.get(prefixKey);
			T t = stringToBean(value,clazz);
			return t;
		}finally{
			returnToPool(jedis);
		}
	}
	private void returnToPool(Jedis jedis){
		if(jedis != null) {
			jedis.close();
		}
	}
		public <T> boolean exists(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.exists(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}
	public <T> Long incr(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.incr(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}

	/**
	 * 减少值
	 * */
	public <T> Long decr(KeyPrefix prefix, String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			 String realKey  = prefix.getPrefix() + key;
			return  jedis.decr(realKey);
		 }finally {
			  returnToPool(jedis);
		 }
	}

}
//
//@Service
//public class RedisService {
//
//	@Autowired
//	JedisPool jedisPool;
//
//	/**
//	 * 获取当个对象
//	 * */
//	public <T> T get(KeyPrefix prefix, String key,  Class<T> clazz) {
//		 Jedis jedis = null;
//		 try {
//			 jedis =  jedisPool.getResource();
//			 //生成真正的key
//			 String realKey  = prefix.getPrefix() + key;
//			 String  str = jedis.get(realKey);
//			 T t =  stringToBean(str, clazz);
//			 return t;
//		 }finally {
//			  returnToPool(jedis);
//		 }
//	}
//
//	/**
//	 * 设置对象
//	 * */
//	public <T> boolean set(KeyPrefix prefix, String key,  T value) {
//		 Jedis jedis = null;
//		 try {
//			 jedis =  jedisPool.getResource();
//			 String str = beanToString(value);
//			 if(str == null || str.length() <= 0) {
//				 return false;
//			 }
//			//生成真正的key
//			 String realKey  = prefix.getPrefix() + key;
//			 int seconds =  prefix.expireSeconds();
//			 if(seconds <= 0) {
//				 jedis.set(realKey, str);
//			 }else {
//				 jedis.setex(realKey, seconds, str);
//			 }
//			 return true;
//		 }finally {
//			  returnToPool(jedis);
//		 }
//	}
//
//	/**
//	 * 判断key是否存在
//	 * */
//	public <T> boolean exists(KeyPrefix prefix, String key) {
//		 Jedis jedis = null;
//		 try {
//			 jedis =  jedisPool.getResource();
//			//生成真正的key
//			 String realKey  = prefix.getPrefix() + key;
//			return  jedis.exists(realKey);
//		 }finally {
//			  returnToPool(jedis);
//		 }
//	}
//
//	/**
//	 * 增加值
//	 * */
//
//
//	private <T> String beanToString(T value) {
//		if(value == null) {
//			return null;
//		}
//		Class<?> clazz = value.getClass();
//		if(clazz == int.class || clazz == Integer.class) {
//			 return ""+value;
//		}else if(clazz == String.class) {
//			 return (String)value;
//		}else if(clazz == long.class || clazz == Long.class) {
//			return ""+value;
//		}else {
//			return JSON.toJSONString(value);
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	private <T> T stringToBean(String str, Class<T> clazz) {
//		if(str == null || str.length() <= 0 || clazz == null) {
//			 return null;
//		}
//		if(clazz == int.class || clazz == Integer.class) {
//			 return (T)Integer.valueOf(str);
//		}else if(clazz == String.class) {
//			 return (T)str;
//		}else if(clazz == long.class || clazz == Long.class) {
//			return  (T)Long.valueOf(str);
//		}else {
//			return JSON.toJavaObject(JSON.parseObject(str), clazz);
//		}
//	}
//
//	private void returnToPool(Jedis jedis) {
//		 if(jedis != null) {
//			 jedis.close();
//		 }
//	}
//
//}