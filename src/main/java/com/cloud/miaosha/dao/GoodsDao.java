package com.cloud.miaosha.dao;


import com.cloud.miaosha.vo.GoodVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {

    /**
     * 查找商品信息和秒杀信息(库存和秒杀时间)
     */
    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date  from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodVo> listGoodsVo();
    @Select("select g.*,mg.miaosha_price,mg.stock_count,mg.start_date,mg.end_date  from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodId}" )
    GoodVo getGoodsVoByGoodsId( long goodsId);
}
