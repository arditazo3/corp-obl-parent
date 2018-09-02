package com.tx.co.cache.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.repository.CompanyRepository;
import com.tx.co.user.domain.User;
import com.tx.co.user.repository.UserRepository;
import org.ehcache.impl.internal.classes.commonslang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.tx.co.common.constants.AppConstants.*;

/**
 * Base class of loading the data from the database
 *
 * @author Ardit Azo
 */
@Service
public abstract class CacheDataLoader {

    private javax.cache.CacheManager cacheManager;
    private UserRepository userRepository;
    private CompanyRepository companyRepository;

    // Split the string with operator ; to get all the languages
    @Value("${web.app.langualge}")
    private String languagesString = "";

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Store the data to the cache before to start the application
     */
    @PostConstruct
    public void init() {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        // Load all the users
        List<User> userList = (List<User>) userRepository.findAll();
        storageDataCacheManager.put(USER_LIST_CACHE, userList);

        // Load all the languages used on the web app
        List<String> languagesList = new ArrayList<>();
        String[] languagesArray = languagesString.split(";");
        if(!ArrayUtils.isEmpty(languagesArray)) {
            languagesList.addAll(Arrays.stream(languagesArray).collect(Collectors.toList()));
            storageDataCacheManager.put(LANGUAGE_LIST_CACHE, languagesList);
        }

        // Load all the companies
        List<Company> companyList = (List<Company>) companyRepository.findAllByOrderByDescriptionAsc();
        storageDataCacheManager.put(COMPANY_LIST_CACHE, companyList);
    }
}
