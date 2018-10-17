package com.cloud.miaosha.controller;

import com.cloud.miaosha.domain.User;
//import com.cloud.miaosha.redis.RedisService;
//import com.cloud.miaosha.redis.UserKey;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.redis.UserKey;
import com.cloud.miaosha.result.CodeMsg;
import com.cloud.miaosha.result.Result;
import com.cloud.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

	@Autowired
	UserService userService;
	
	@Autowired
    RedisService redisService;
	

    
//    @RequestMapping("/error")
//    @ResponseBody
//    public Result<String> error() {
//        return Result.error(CodeMsg.SESSION_ERROR);
//    }
    

    
    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
    	User user = userService.getById(1);
        return Result.success(user);
    }
    
    
    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
    	userService.tx();
        return Result.success(true);
    }
    
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
//        Long v1 = redisService.get("key1",Long.class);
//        String name = redisService.get("key2",String.class);
    	User  user  = redisService.get(UserKey.getById, "1", User.class);
        return Result.success(user);
//        return Result.success(name);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
    	User user  = new User();
    	user.setId(1);
    	user.setName("1111");
    	redisService.set(UserKey.getById,"1",user);//UserKey:id1

        return Result.success(true);
    }

//
}
