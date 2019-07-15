package com.cloud.miaosha.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MiaoshaMQConfig {
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_KEY);
    }
    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    private static final Logger log = LoggerFactory.getLogger(MiaoshaMQConfig.class);

    // 死信的交换机名
    public static final String DEAD_LETTER_EXCHANGE = "dead_exchange";
    public static final String DEAD_LETTER_KEY = "dead_key";

    // 直接模式
    @Bean
    public Queue miaoshaQueue(){
        Map<String, Object> args = new HashMap<>();
        // 设置该Queue的死信的信箱
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        // 设置死信routingKey
        args.put("x-dead-letter-routing-key", DEAD_LETTER_KEY);
        args.put("x-message-ttl", 1);

        return new Queue(MIAOSHA_QUEUE, true, false, false, args);
    }

    @Bean
    public Binding maintainBinding() {
        return BindingBuilder.bind(miaoshaQueue()).to(DirectExchange.DEFAULT)
                .with(MIAOSHA_QUEUE);
    }


    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Binding deadLetterBindding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_LETTER_KEY);
    }


//    @Bean
//    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
//        connectionFactory.setPublisherConfirms(true);
//        connectionFactory.setPublisherReturns(true);
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> new MsgSendConfirmCallBack());
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
//        return rabbitTemplate;
//    }
//
//
//
//    public class MsgSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {
//        @Override
//        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//            if (ack) {
//                System.out.println("消息确认成功cause" + cause);
//            } else {
//                //处理丢失的消息
//                //补redis
//                System.out.println("消息确认失败:" + correlationData.getId() + "#cause" + cause);
//            }
//        }
//    }




}
