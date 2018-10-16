package com.cloud.miaosha.service;

import com.cloud.miaosha.dao.MiaoshaUserDao;
import com.cloud.miaosha.domain.MiaoshaUser;
//import com.cloud.miaosha.exception.GlobalException;
//import com.cloud.miaosha.redis.MiaoshaUserKey;
//import com.cloud.miaosha.redis.RedisService;
//import com.cloud.miaosha.redis.tokenKey;
//import com.cloud.miaosha.result.CodeMsg;
//import com.cloud.miaosha.util.MD5Util;
//import com.cloud.miaosha.util.UUIDUtil;
//import com.cloud.miaosha.vo.LoginVo;
//import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Service
public class MiaoshaUserService {


	public static final String COOKI_NAME_TOKEN = "token";

	@Autowired
	MiaoshaUserDao miaoshaUserDao;

//	@Autowired
//	RedisService redisService;
//
	public MiaoshaUser getById(long id) {
		return miaoshaUserDao.getById(id);
	}


//	public MiaoshaUser getByToken(HttpServletResponse response, String token) {
//		if(StringUtils.isEmpty(token)) {
//			return null;
//		}
//		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
//		//延长有效期
////		if(user != null) {
////			addCookie(response, token, user);
////		}
//		return user;
//	}
//
//
//	public boolean login( HttpServletResponse response,@Valid LoginVo loginVo) {
//		if(loginVo == null) {
//			System.out.println("loginvonull");
//			throw new GlobalException(CodeMsg.SERVER_ERROR);
//		}
//		String mobile = loginVo.getMobile();
//		String formPass = loginVo.getPassword();
//		//判断手机号是否存在
//
//		MiaoshaUser user = getById(Long.parseLong(mobile));
//		if(user == null) {
//
//			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
//		}
//		//验证密码
//		String dbPass = user.getPassword();
//		String saltDB = user.getSalt();
//		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
//		if(!calcPass.equals(dbPass)) {
//			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
//		}
//		//生成cookie
//		String token= UUIDUtil.uuid();
//		redisService.set(tokenKey.token, token, user);
//		Cookie cookie = new Cookie(COOKI_NAME_TOKEN,token);
//		cookie.setMaxAge(tokenKey.token.expireSeconds());
//		cookie.setPath("./");
//		response.addCookie(cookie);
//		//写到response要HttpResponse
////		addCookie(response, token, user);
//		return true;
//	}
// public boolean login(HttpServletResponse response, LoginVo loginVo) {
//		if(loginVo == null) {
//			throw new GlobalException(CodeMsg.SERVER_ERROR);
//		}
//		String mobile = loginVo.getMobile();
//		String formPass = loginVo.getPassword();
//		//判断手机号是否存在
//
//		MiaoshaUser user = getById(Long.parseLong(mobile));
//		if(user == null) {
//			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
//		}
//		//验证密码
//		String dbPass = user.getPassword();
//		String saltDB = user.getSalt();
//		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
//		if(!calcPass.equals(dbPass)) {
//			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
//		}
//		//生成cookie
//		String token	 = UUIDUtil.uuid();
//		addCookie(response, token, user);
//		return true;
//	}

//	private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
//		redisService.set(MiaoshaUserKey.token, token, user);
//		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
//		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
//		cookie.setPath("/");
//		response.addCookie(cookie);
//	}

}
