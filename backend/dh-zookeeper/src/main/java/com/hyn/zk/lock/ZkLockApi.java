package com.hyn.zk.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ZkLockApi implements AutoCloseable, Watcher {

    private ZooKeeper zkClient;

    String zkNode = null;

    public ZkLockApi() {
        try {
            zkClient = new ZooKeeper("127.0.0.1:2181", 1000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getLock(String businessCode) {

        String businessCodePath = "/" + businessCode;

        try {
            Stat stat = zkClient.exists(businessCodePath, false);
            if (stat == null) {
                zkClient.create(businessCodePath, businessCode.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zkNode = zkClient.create(businessCodePath + "/" + businessCode + "_", businessCode.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("create node : " + zkNode);
            List<String> businessDataChild = zkClient.getChildren(businessCodePath, false);
            Optional<String> firstSeqData = businessDataChild.stream().sorted().collect(Collectors.toList()).stream().findFirst();
            if (firstSeqData.isPresent() && zkNode.endsWith(firstSeqData.get())) {
                return true;
            }

            //监听前一个节点
            String lastNode = firstSeqData.get();
            for (String node : businessDataChild) {
                if (zkNode.endsWith(lastNode)) {
                    zkClient.exists(businessCodePath + "/" + lastNode, true);
                    break;
                } else {
                    lastNode = node;
                }
            }
            synchronized (this) {
                wait(10000);
            }
            return true;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() {
        try {
            zkClient.delete(zkNode, -1);
            zkClient.close();
            log.info(zkNode + " unlock!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        log.info("ProductWatcherApi start...");
        if (Event.EventType.NodeDeleted.equals(watchedEvent.getType())) {
            synchronized (this) {
                notify();
            }
        }
        log.info("ProductWatcherApi end...");
    }
}
