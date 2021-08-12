package com.hyn.zk.controller;

import com.hyn.zk.config.CuratorLockApi;
import com.hyn.zk.lock.ZkLockApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/zklock")
@Slf4j
public class ZkLockController {


    @GetMapping(value = "/getLockByBussinessCode")
    @ResponseBody
    public String getZkLock(@RequestParam("bussinessCode") String bussinessCode) {
        ZkLockApi zkLockApi = new ZkLockApi();
        log.info("bussinessCode : " + bussinessCode);
        String resultCode = zkLockApi.getLock(bussinessCode) ? "Get lock success!" : "Get lock fail!";
        zkLockApi.close();
        return resultCode;
    }

    @GetMapping(value = "/getLockByBussinessCode2")
    @ResponseBody
    public String getCuratorLock(@RequestParam("bussinessCode") String bussinessCode) {
        CuratorLockApi curatorLockApi = new CuratorLockApi();
        return curatorLockApi.getLock(bussinessCode) ? "Get lock success!" : "Get lock fail!";
    }
}
