package com.tx.co.cache.config;

import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * The configuration of the class with the config file ehcache.xml
 *
 * @author aazo
 */
@Configuration
public class StorageDataCache {

    private Map<String, Object> storageDataCache;

    public Map<String, Object> getStorageDataCache() {
        return storageDataCache;
    }

    public void setStorageDataCache(Map<String, Object> storageDataCache) {
        this.storageDataCache = storageDataCache;
    }
}
