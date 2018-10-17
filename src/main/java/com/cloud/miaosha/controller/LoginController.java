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
//	@Autowired
//	RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }
//
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(HttpServletResponse response,@Valid  LoginVo loginVo) {
    	log.info(loginVo.toString());
//    	参数校验
        String password = loginVo.getPassword();
        String mobile = loginVo.getMobile();
        if(StringUtils.isEmpty(mobile)){
            return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(StringUtils.isEmpty(password)){
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobile))
            return Result.error(CodeMsg.MOBILE_ERROR);
//        登录
        CodeMsg cm = userService.login(loginVo);
        if(cm.getCode() == 0)
            return Result.success("登录成功");
        else
            return Result.error(cm);
//    	userService.login(response, loginVo);
//    	return Result.success(true);

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
