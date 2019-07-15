package com.cloud.miaosha.rabbitmq;

import com.cloud.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.cloud.miaosha.rabbitmq.MiaoshaMQConfig.MIAOSHA_QUEUE;

@Service
public class MiaoshaSender implements RabbitTemplate.ReturnCallback {
    private static Logger log = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    RabbitTemplate rabbitTemplate;


    public void sendMiaoshaMessage(MiaoshaMessage message) {


        this.rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.out.println("HelloSender消息发送失败" + cause + correlationData.toString());
            } else {
                System.out.println("HelloSender 消息发送成功 ");
            }
        });
//        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        // this.rabbitTemplate.setConfirmCallback(this);
        String msg = RedisService.beanToString(message);
        log.info("send message: "+msg);
//        Channel
        rabbitTemplate.convertAndSend(MIAOSHA_QUEUE, msg);
        System.out.println("發送消息");


//

        // direct模式

    }

    /**
     * Returned message callback.
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("!!!!!sender return success" + message.toString() + "===" + replyText + "===" + exchange + "===" + routingKey);
    }
}
