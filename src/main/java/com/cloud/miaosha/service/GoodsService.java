package com.cloud.miaosha.service;

import com.cloud.miaosha.dao.GoodsDao;
import com.cloud.miaosha.vo.GoodVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    GoodsDao goodsDao;

    public List<GoodVo> listGoodsVo(){
        return goodsDao.listGoodsVo();

    }

    public GoodVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }
}
