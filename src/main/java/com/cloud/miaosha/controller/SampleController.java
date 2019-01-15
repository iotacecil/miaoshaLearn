package com.cloud.miaosha.controller;

import com.cloud.miaosha.domain.User;
import com.cloud.miaosha.rabbitmq.MQSender;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.redis.UserKey;
import com.cloud.miaosha.result.Result;
import com.cloud.miaosha.service.OrderService;
import com.cloud.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//import com.cloud.miaosha.redis.RedisService;
//import com.cloud.miaosha.redis.UserKey;

@Controller
@RequestMapping("/demo")
public class SampleController {

	@Autowired
	UserService userService;
	
	@Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

	@Autowired
	MQSender sender;

    @RequestMapping("/dbnull")
    @ResponseBody
    public Result<Boolean> insertnull(){
        boolean insertnull = orderService.insertnull();
        return Result.success(insertnull);

    }

	@RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
	    sender.send("rabbitMQ消息测试");
	    return Result.success("rabbitMQ消息测试");

    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic(){
	    // 发两条消息
        sender.sendTopic("topic消息测试");
        return Result.success("topic消息测试");
    }
    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout(){
        sender.sendFanout("广播 消息测试");
        return Result.success("广播消息测试");
    }

    @RequestMapping("/mq/header")
    @ResponseBody
    public Result<String> header(){
        sender.sendHeader("header 消息测试");
        return Result.success("header消息测试");
    }
    
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
