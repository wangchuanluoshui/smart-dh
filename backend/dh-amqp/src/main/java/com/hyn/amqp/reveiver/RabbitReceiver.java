package com.hyn.amqp.reveiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * @Classname RabbitReceiver
 * @Description TODO
 * @Date 2021-01-02 23:29
 * @Created by 62538
 */
@Component
public class RabbitReceiver {

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "queue-1", durable = "true"),
                    exchange = @Exchange(name = "exchange-1", durable = "true",
                            type = "topic",
                            ignoreDeclarationExceptions = "true"),
                    key = "springboot.*"
            )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws IOException {

        System.out.println("消费消息：" + message.getPayload());
        //处理成功后
        channel.basicAck((Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG), false);
    }
}
