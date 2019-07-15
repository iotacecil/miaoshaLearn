package com.cloud.miaosha.rabbitmq;

import com.cloud.miaosha.service.GoodsService;
import com.cloud.miaosha.service.MiaoshaService;
import com.cloud.miaosha.service.OrderService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.cloud.miaosha.rabbitmq.MiaoshaMQConfig.DEAD_LETTER_KEY;

@Service
public class MiaoshaReceiver {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    private static Logger log = LoggerFactory.getLogger(MQSender.class);


    @RabbitListener(queues = DEAD_LETTER_KEY)
    public void rev(String message, Message mg, Channel channel) throws IOException, InterruptedException {
        System.out.println("死信");
        System.out.println(message);
    }


    @RabbitListener(queues = MiaoshaMQConfig.MIAOSHA_QUEUE)
    public void maishaReceive(String message, Message mg, Channel channel) throws IOException, InterruptedException {
        Thread.sleep(10000);
        System.out.println("收到消息");
        log.info("receive message:"+message);
        channel.basicReject(mg.getMessageProperties().getDeliveryTag(), true);

        channel.basicReject(mg.getMessageProperties().getDeliveryTag(), true);
//
//        MiaoshaMessage msg = RedisService.stringToBean(message, MiaoshaMessage.class);
//        long goodsId = msg.getGoodsId();
//        MiaoshaUser user = msg.getUser();
//        // 判断真的库存
//        GoodVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//    	int stock = goods.getStockCount();
//    	if(stock <= 0) {
////    		channel.basicNack(mg.getMessageProperties().getDeliveryTag(), false,false);
//            channel.basicReject(mg.getMessageProperties().getDeliveryTag(), true);
//            System.out.println("消费失败");
//    	}
//    	// 判断秒杀过没有
//        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//        if(order != null) {
//            return;
//        }
//        //1.减库存 2.下订单 3.写入秒杀订单 这三步是一个是事务
//        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
//        System.out.println("手动ack删除");
//        channel.basicAck(mg.getMessageProperties().getDeliveryTag(),false );

    }
}
