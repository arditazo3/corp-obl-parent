package com.tx.co.cache.event;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEventLogger implements CacheEventListener<String, Object> {

    private static final Logger logger = LoggerFactory.getLogger(CacheEventLogger.class);

    /**
     * Write to the log the change of the single instance of the cache @{@link org.ehcache.CacheManager}
     *
     * @param cacheEvent
     */
    @Override
    public void onEvent(CacheEvent<? extends String, ? extends Object> cacheEvent) {
        logger.debug("Event: {} Key: {} old value: {} new value: {} ", cacheEvent.getType(), cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}
