package com.hyn.amqp.controller;

import com.hyn.amqp.sender.RabbitSender;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * @Classname AmqpController
 * @Description TODO
 * @Date 2021-01-03 0:02
 * @Created by 62538
 */
@Api(value = "amqp", tags = "队列管理模块")
@RestController
@RequestMapping("amqp")
public class AmqpController {

    @Autowired
    RabbitSender rabbitSender;

    @GetMapping(value = "/")
    public String testSender() {
        for (int i = 0; i < 100; i++) {
            Map headerMap = new HashMap();
            headerMap.put("cook", UUID.randomUUID().toString());
            headerMap.put("username", "hyn");
            rabbitSender.send("这是第" + i + "条消息！", headerMap);
        }
        return "success";
    }

}
