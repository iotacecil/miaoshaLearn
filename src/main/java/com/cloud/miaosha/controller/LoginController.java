package com.cloud.miaosha.controller;

import com.alibaba.druid.util.StringUtils;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.result.CodeMsg;
import com.cloud.miaosha.result.Result;
import com.cloud.miaosha.service.MiaoshaUserService;

import com.cloud.miaosha.util.ValidatorUtil;
import com.cloud.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	MiaoshaUserService userService;
//
	@Autowired
	RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }
//
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response,@Valid  LoginVo loginVo) {
    	log.info(loginVo.toString());
//        登录
        userService.login(response,loginVo);
//        if(cm.getCode() == 0)
//            return Result.success("登录成功");
//        else
//            return Result.error(cm);
        System.out.println("登陆成功");

    	return Result.success("登录成功");

    }
//    @RequestMapping("/do_login")
//    @ResponseBody
//    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
//    	log.info(loginVo.toString());
//    	//参数校验
//
//    	//登录
//    	userService.login(response, loginVo);
//    	return Result.success(true);
//    }
}
