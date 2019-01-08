package com.cloud.miaosha.service;

import com.cloud.miaosha.dao.GoodsDao;
import com.cloud.miaosha.domain.MiaoshaGoods;
import com.cloud.miaosha.vo.GoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    // 商品列表
    public List<GoodVo> listGoodsVo(){
        return goodsDao.listGoodsVo();

    }
    // 商品详情
    public GoodVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }
    // 减库存
    public boolean reduceStock(GoodVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGooddsId(goods.getId());
        int rst = goodsDao.reduceStock(g);
        System.out.println("dao层减少库存"+rst);
        return rst > 0;
    }
}
