package com.cloud.miaosha.controller;


import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.result.Result;
import com.cloud.miaosha.service.MiaoshaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
    private static Logger log = LoggerFactory.getLogger(GoodsController.class);
    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model,MiaoshaUser user){
        return Result.success(user);
    }

}
