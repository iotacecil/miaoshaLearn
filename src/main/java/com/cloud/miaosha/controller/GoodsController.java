package com.cloud.miaosha.controller;

import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.result.Result;
import com.cloud.miaosha.service.GoodsService;
import com.cloud.miaosha.service.MiaoshaUserService;
import com.cloud.miaosha.vo.GoodVo;
import com.cloud.miaosha.vo.LoginVo;
import com.sun.deploy.net.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

	private static Logger log = LoggerFactory.getLogger(GoodsController.class);

	@Autowired
	MiaoshaUserService userService;
//
	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;
    @RequestMapping("/to_list")
    public String toLogin(Model model,MiaoshaUser user) {
        model.addAttribute("user",user);

        // 秒杀商品列表
        List<GoodVo> goodVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList",goodVos);

        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model,MiaoshaUser user,
                         @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", user);

        GoodVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        // 0：没开始 2：结束 1：进行中
        int miaoshaStatus = 0;
        // 倒计时
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }


}
