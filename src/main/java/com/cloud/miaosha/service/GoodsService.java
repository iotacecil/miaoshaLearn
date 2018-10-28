package com.cloud.miaosha.service;

import com.cloud.miaosha.dao.GoodsDao;
import com.cloud.miaosha.domain.Goods;
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
    public void reduceStock(GoodVo goods) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setId(goods.getId());
        goodsDao.reduceStock(g);
    }
}
