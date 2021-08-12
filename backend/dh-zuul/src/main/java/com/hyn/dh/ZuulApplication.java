package com.hyn.dh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.RestController;

/*
 * @Classname Application
 * @Description TODO
 * @Date 2020/6/13 15:12
 * @Created by 62538
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@RestController
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class);
    }

}
