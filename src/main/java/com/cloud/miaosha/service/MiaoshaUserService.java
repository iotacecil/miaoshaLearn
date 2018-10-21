package com.cloud.miaosha.service;

import com.cloud.miaosha.controller.LoginController;
import com.cloud.miaosha.dao.MiaoshaUserDao;
import com.cloud.miaosha.domain.MiaoshaUser;


import com.cloud.miaosha.exception.GlobalException;
import com.cloud.miaosha.redis.MiaoshaUserKey;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.result.CodeMsg;
import com.cloud.miaosha.util.MD5Util;
import com.cloud.miaosha.util.UUIDUtil;
import com.cloud.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Service
public class MiaoshaUserService {

	private static Logger log = LoggerFactory.getLogger(MiaoshaUserService.class);

	public static final String COOKI_NAME_TOKEN = "token";

	@Autowired
	MiaoshaUserDao miaoshaUserDao;

	@Autowired
	RedisService redisService;
//
	public MiaoshaUser getById(long id) {
		return miaoshaUserDao.getById(id);
	}

	public boolean login(HttpServletResponse response,LoginVo loginVo){
		if(loginVo == null){
			throw new GlobalException( CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		MiaoshaUser user = getById(Long.parseLong(mobile));
		if(user == null) {
			//用户/手机号不存在

			throw new GlobalException( CodeMsg.MOBILE_NOT_EXIST);
		}
		//数据库中的密码,salt
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
//		用前端密码+数据库salt是否等于数据库密码
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		log.info(calcPass);
		log.info(dbPass);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException( CodeMsg.PASSWORD_ERROR);
		}
		String token = UUIDUtil.uuid();
		//生成cookie
		addCookie(response,token , user);
		return true;
	}
	public MiaoshaUser getByToken(HttpServletResponse response, String token) {

		if(StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
//		延长有效期

		if(user != null) {

			addCookie(response, token,user);
		}
		return user;
	}
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

	private void addCookie(HttpServletResponse response,String token, MiaoshaUser user) {


		redisService.set(MiaoshaUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
