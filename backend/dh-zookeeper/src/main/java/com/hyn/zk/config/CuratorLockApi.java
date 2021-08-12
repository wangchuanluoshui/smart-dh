package com.hyn.zk.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CuratorLockApi {
    @Value("${zookeeper.address}")
    private String connectString;

    public boolean getLock(String bussinessCode) {
        boolean result = false;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        curatorFramework.start();
        InterProcessLock interProcessLock = new InterProcessMutex(curatorFramework, "/" + bussinessCode);
        try {
            if (interProcessLock.acquire(0, TimeUnit.SECONDS)) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                log.info("release lock！");
                interProcessLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
