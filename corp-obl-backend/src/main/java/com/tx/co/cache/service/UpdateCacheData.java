package com.tx.co.cache.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.service.CompanyService;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.office.service.OfficeService;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.service.TopicService;
import com.tx.co.common.utils.UtilStatic;
import com.tx.co.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.cache.Cache;
import javax.cache.CacheManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.tx.co.common.constants.AppConstants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

public abstract class UpdateCacheData {

    private javax.cache.CacheManager cacheManager;
    private CompanyService companyService;
    private OfficeService officeService;
    private TopicService topicService;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    @Autowired
    public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}
    
    @Autowired
	public void setOfficeService(OfficeService officeService) {
		this.officeService = officeService;
	}
    
    @Autowired
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	/**
     * @return get the Companies from the cache in order to not execute the query to the database
     */
    public List<Company> getCompaniesFromCache() {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        List<Company> companyListCache = (List<Company>) storageDataCacheManager.get(COMPANY_LIST_CACHE);
        		
        Collections.sort(companyListCache, (a, b) -> a.getDescription().compareToIgnoreCase(b.getDescription()));
        
        return companyListCache;
    }

	/**
     * @return get the Offices from the cache in order to not execute the query to the database
     */
    public List<Office> getOfficesFromCache() {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        return (List<Office>) storageDataCacheManager.get(OFFICE_LIST_CACHE);
    }
    
    /**
     * @return get the Topics from the cache in order to not execute the query to the database
     */
    public List<Topic> getTopicsFromCache() {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        return (List<Topic>) storageDataCacheManager.get(TOPIC_LIST_CACHE);
    }
    
    /**
     * @param company
     * @param updateFromDB
     */
    public void updateCompaniesCache(Company company, Boolean updateFromDB) {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        List<Company> companyList = getCompaniesFromCache();

        // Object to update or to save as new one
        int indexToUpdateOrInsert = UtilStatic.getIndexByPropertyCompanyList(company.getIdCompany(), companyList); 
        
        if(updateFromDB) {
        	Optional<Company> companyFromDB = companyService.findByIdCompany(company.getIdCompany());
        	if(companyFromDB.isPresent()) {
        		company = companyFromDB.get();
        	}
        }

        if(indexToUpdateOrInsert == -1) {
            companyList.add(company);
        } else if(!company.getEnabled()) {
            companyList.remove(indexToUpdateOrInsert);
        } else {
            companyList.set(indexToUpdateOrInsert, company);
        }

        storageDataCacheManager.put(COMPANY_LIST_CACHE, companyList);
    }
    
    /**
     * @param office
     * @param updateFromDB
     */
    public void updateOffficesCache(Office office, Boolean updateFromDB) {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        List<Office> officeList = getOfficesFromCache();

        // Object to update or to save as new one
        int indexToUpdateOrInsert = UtilStatic.getIndexByPropertyOfficeList(office.getIdOffice(), officeList); 
        
        if(updateFromDB) {
        	Optional<Office> officeFromDB = officeService.findByIdOffice(office.getIdOffice());
        	if(officeFromDB.isPresent()) {
        		office = officeFromDB.get();
        	}
        }

        if(indexToUpdateOrInsert == -1) {
            officeList.add(office);
        } else if(!office.getEnabled()) {
            officeList.remove(indexToUpdateOrInsert);
        } else {
            officeList.set(indexToUpdateOrInsert, office);
        }

        storageDataCacheManager.put(OFFICE_LIST_CACHE, officeList);
    }
    
    /**
     * @param topic
     * @param updateFromDB
     */
    public void updateOffficesCache(Topic topic, Boolean updateFromDB) {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        List<Topic> topicList = getTopicsFromCache();

        // Object to update or to save as new one
        int indexToUpdateOrInsert = UtilStatic.getIndexByPropertyTopicList(topic.getIdTopic(), topicList); 
        
        if(updateFromDB) {
        	Optional<Topic> topicFromDB = topicService.findByIdTopic(topic.getIdTopic());
        	if(topicFromDB.isPresent()) {
        		topic = topicFromDB.get();
        	}
        }

        if(indexToUpdateOrInsert == -1) {
            topicList.add(topic);
        } else if(!topic.getEnabled()) {
            topicList.remove(indexToUpdateOrInsert);
        } else {
            topicList.set(indexToUpdateOrInsert, topic);
        }

        storageDataCacheManager.put(OFFICE_LIST_CACHE, topicList);
    }

    /**
     * @param idCompany
     * @return the existing Company on the cache
     */
    public Company getCompanyById(Long idCompany) {
        Company company = null;
        if(!isEmpty(getCompaniesFromCache())) {
            for (Company companyLoop : getCompaniesFromCache()) {
                if(idCompany.compareTo(companyLoop.getIdCompany()) == 0) {
                    company = companyLoop;
                    break;
                }
            }
        }
        return company;
    }

    /**
     * @return get the Users from the cache in order to not execute the query to the database
     */
    public List<User> getUsersFromCache() {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        return (List<User>) storageDataCacheManager.get(USER_LIST_CACHE);
    }
    
    public User getUserFromUsername(String username) {
    	
    	List<User> userList = getUsersFromCache();
    	User userRetrived = null;
    	for (User user : userList) {
			if(user.getUsername().equals(username)) {
				userRetrived = user;
				break;
			}
		}
    	return userRetrived;
    }
}
