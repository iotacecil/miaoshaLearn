package com.cloud.miaosha.rabbitmq;

import com.cloud.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send message"+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void sendTopic(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send topic message"+msg);
        // queue1和2都能匹配上都能收到
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY1,msg+"1");
        // 只有queue2能匹配上
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY2,msg+"2");
    }


    public void sendFanout(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send topic message"+msg);
        // queue1和2都能都能收到
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg+"1");
    }

    public void sendHeader(Object message){
        String msg = RedisService.beanToString(message);
        log.info("send header message"+msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1","value1" );
        properties.setHeader("header2","value2" );
        Message obj = new Message(msg.getBytes(),properties);
        amqpTemplate.convertAndSend(MQConfig.HEADER_EXCHANGE,"",obj);
    }


}
