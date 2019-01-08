package com.cloud.miaosha.rabbitmq;

import com.cloud.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoshaSender {
    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage message) {
        // direct模式
        String msg = RedisService.beanToString(message);
        log.info("send message: "+msg);
        amqpTemplate.convertAndSend(MiaoshaMQConfig.MIAOSHA_QUEUE,msg);
    }
}
