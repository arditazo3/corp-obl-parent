package com.tx.co.cache.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.List;

import static com.tx.co.common.constants.AppConstants.COMPANY_LIST_CACHE;
import static com.tx.co.common.constants.AppConstants.STORAGE_DATA_CACHE;
import static com.tx.co.common.constants.AppConstants.USER_LIST_CACHE;

public abstract class UpdateCacheData {

    private javax.cache.CacheManager cacheManager;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * @return get the Companies from the cache in order to not execute the query to the database
     */
    public List<Company> getCompaniesFromCache() {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        return (List<Company>) storageDataCacheManager.get(COMPANY_LIST_CACHE);
    }

    public void updateCompaniesCache(Company company) {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        List<Company> companyList = getCompaniesFromCache();
        companyList.add(company);
        storageDataCacheManager.put(COMPANY_LIST_CACHE, companyList);
    }

    /**
     * @return get the Users from the cache in order to not execute the query to the database
     */
    public List<User> getUsersFromCache() {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        return (List<User>) storageDataCacheManager.get(USER_LIST_CACHE);
    }
}
