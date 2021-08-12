package com.hyn.zk.config;

import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProductWatcherApi extends WatcherApi {

    private static final Logger logger = LoggerFactory.getLogger(ProductWatcherApi.class);

    @Override
    public void process(WatchedEvent event) {
        logger.info("ProductWatcherApi start...");
        if (Event.EventType.NodeDeleted.equals(event.getType())) {
            synchronized (this) {
                notify();
            }
        }
        logger.info("ProductWatcherApi end...");
    }
}
