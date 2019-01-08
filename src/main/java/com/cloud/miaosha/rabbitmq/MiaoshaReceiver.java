package com.cloud.miaosha.rabbitmq;

import com.cloud.miaosha.domain.MiaoshaOrder;
import com.cloud.miaosha.domain.MiaoshaUser;
import com.cloud.miaosha.domain.OrderInfo;
import com.cloud.miaosha.redis.RedisService;
import com.cloud.miaosha.service.GoodsService;
import com.cloud.miaosha.service.MiaoshaService;
import com.cloud.miaosha.service.OrderService;
import com.cloud.miaosha.vo.GoodVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaReceiver {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @RabbitListener(queues = MiaoshaMQConfig.MIAOSHA_QUEUE)
    public void maishaReceive(String message){
        log.info("receive message:"+message);
        MiaoshaMessage msg = RedisService.stringToBean(message, MiaoshaMessage.class);
        long goodsId = msg.getGoodsId();
        MiaoshaUser user = msg.getUser();
        // 判断真的库存
        GoodVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	int stock = goods.getStockCount();
    	if(stock <= 0) {
    		return;
    	}
    	// 判断秒杀过没有
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return;
        }
        //1.减库存 2.下订单 3.写入秒杀订单 这三步是一个是事务
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
    }
}
