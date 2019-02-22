package com.cloud.miaosha.access;

import com.alibaba.fastjson.JSON;
import com.cloud.miaosha.config.UserContext;
import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.redis.AccessKey;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.result.CodeMsg;
import com.cloud.miaosha.result.Result;
import com.cloud.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Service
public class AccessIntercepter extends HandlerInterceptorAdapter{

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       if(handler instanceof HandlerMethod){
           // 1. 取用户
           MiaoshaUser user = getUser(request, response);
           // 2. 用户存到threadlocal
           UserContext.setUser(user);
           // 3. 获取注解参数
           HandlerMethod hm = (HandlerMethod)handler;
           AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
           if(accessLimit == null){
               return true;
           }
           int second = accessLimit.seconds();
           int maxcount = accessLimit.maxCount();
           boolean needLogin = accessLimit.needLogin();

           // 限制访问的key
           String key = request.getRequestURI();

           // 如果用户没登陆
           if(needLogin){
               if(user == null){
                   render(response, CodeMsg.SESSION_ERROR);
                   System.out.println("错误");
                   return false;
               }
               // 如果需要登陆 key + 用户id
               key += "_"+user.getId();
           }
           // else key就只有path
           AccessKey ak = AccessKey.withExpire(5);
           Integer count = redisService.get(ak, key, Integer.class);
           if(count == null){
               redisService.set(ak, key, 1);
           }else if(count < maxcount){
               redisService.incr(ak, key);
           }else{
               render(response, CodeMsg.ACCESS_LIMIT_REACHED);
               return false;
           }
       }
        return true;
    }
    private void render(HttpServletResponse response, CodeMsg cm)throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str  = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
    private MiaoshaUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookiName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
