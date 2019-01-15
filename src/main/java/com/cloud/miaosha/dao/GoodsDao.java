package com.cloud.miaosha.dao;


import com.cloud.miaosha.domain.MiaoshaGoods;
import com.cloud.miaosha.vo.GoodVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("update miaosha_goods set stock_count = stock_count -1 where goods_id = #{gooddsId} and stock_count >0")
    public int reduceStock(MiaoshaGoods g);

    @Update("update miaosha_goods set stock_count = #{stockCount} where goods_id = #{gooddsId}")
    public int resetStock(MiaoshaGoods g);
}
