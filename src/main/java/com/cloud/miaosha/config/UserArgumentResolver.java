package com.cloud.miaosha.config;

import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	MiaoshaUserService userService;
	
	public boolean supportsParameter(MethodParameter parameter) {
		// 获取参数类型 是User类型才会做resolveArgument
		Class<?> clazz = parameter.getParameterType();
		return clazz==MiaoshaUser.class;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//
//		// 浏览器不同 token可能在cookie里也可能在参数里
//		String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
//		String cookieToken = getCookieValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);
//		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//			// 返回登陆界面
//			return null;
//		}
//		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//		return userService.getByToken(response, token);
		return UserContext.getUser();
	}
}
