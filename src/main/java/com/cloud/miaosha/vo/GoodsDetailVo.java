package com.cloud.miaosha.vo;

import com.cloud.miaosha.domain.MiaoshaUser;

public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodVo goods ;
    private MiaoshaUser user;
    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }
    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }
    public int getRemainSeconds() {
        return remainSeconds;
    }
    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
    public GoodVo getGoods() {
        return goods;
    }
    public void setGoods(GoodVo goods) {
        this.goods = goods;
    }
    public MiaoshaUser getUser() {
        return user;
    }
    public void setUser(MiaoshaUser user) {
        this.user = user;
    }
}