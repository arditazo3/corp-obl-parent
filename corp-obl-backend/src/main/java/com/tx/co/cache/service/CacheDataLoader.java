package com.tx.co.cache.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.repository.CompanyRepository;
import com.tx.co.back_office.company.service.ICompanyConsultantService;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.office.repository.OfficeRepository;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;
import com.tx.co.back_office.topic.service.ITopicService;
import com.tx.co.user.domain.User;
import com.tx.co.user.repository.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tx.co.common.constants.AppConstants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Base class of loading the data from the database
 *
 * @author Ardit Azo
 */
@Service
public abstract class CacheDataLoader {

	private static final Logger logger = LogManager.getLogger(CacheDataLoader.class);
	
    private javax.cache.CacheManager cacheManager;
    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    private OfficeRepository officeRepository;
    private ITopicService topicService;
    private ICompanyConsultantService companyConsultantService;

    // Split the string with operator ; to get all the languages
    @Value("${web.app.language}")
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
    
    @Autowired
    public void setOfficeRepository(OfficeRepository officeRepository) {
		this.officeRepository = officeRepository;
	}

    @Autowired
    public void setTopicService(ITopicService topicService) {
		this.topicService = topicService;
	}
    
    @Autowired
	public void setCompanyConsultantService(ICompanyConsultantService companyConsultantService) {
		this.companyConsultantService = companyConsultantService;
	}

	/**
     * Store the data to the cache before to start the application
     */
    @PostConstruct
    public void init() {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);
        
        // Clear the cache
        ((Collection<String>) cacheManager.getCacheNames()).stream().map(cacheManager::getCache).forEach(Cache::clear);

        // Load all the users
        List<User> userList = (List<User>) userRepository.findAll();
        storageDataCacheManager.put(USER_LIST_CACHE, userList);

        // Load all the languages used on the web app
        List<String> languagesList = new ArrayList<>();
        String[] languagesArray = languagesString.split(";");
        if(!isEmpty(languagesArray)) {
            languagesList.addAll(Arrays.stream(languagesArray).collect(Collectors.toList()));
            storageDataCacheManager.put(LANGUAGE_LIST_CACHE, languagesList);
        }

        // Load all the companies
        List<Company> companyList = companyRepository.findAllByOrderByDescriptionAsc();
        storageDataCacheManager.put(COMPANY_LIST_CACHE, companyList);
        
        // Load all the offices
        List<Office> officeList = officeRepository.findAllByOrderByDescriptionAsc();
        storageDataCacheManager.put(OFFICE_LIST_CACHE, officeList);

        // Load all the topics
        List<Topic> topicsList = topicService.findAllOrderByDescriptionAsc();
        storageDataCacheManager.put(TOPIC_LIST_CACHE, topicsList);
        
        loadConsultantByCompany(storageDataCacheManager);
        
        List<TopicConsultant> topicConsultantsList = topicService.findAllOrderByTopicDescription();
        storageDataCacheManager.put(TOPIC_CONSULTANT_LIST_CACHE, topicConsultantsList);
    }
    
    private void loadConsultantByCompany(final Cache<String, Object> storageDataCacheManager) {
    	
    	List<CompanyConsultant> companyConsultants = companyConsultantService.findAll();
    	
    	Map<Long, List<CompanyConsultant>> companyConsultantMap = new HashMap<>();
    	if(!isEmpty(companyConsultants)) {
    		
    		for (CompanyConsultant companyConsultant : companyConsultants) {
    			Long idCompany = companyConsultant.getCompany().getIdCompany();
    			
				if(isEmpty(companyConsultantMap.get(idCompany))) {
					List<CompanyConsultant> companyConsultantsList = new ArrayList<>();
					companyConsultantsList.add(companyConsultant);
					companyConsultantMap.put(idCompany, companyConsultantsList);
				} else {
					companyConsultantMap.get(idCompany).add(companyConsultant);
				}
			}
    	}
    	storageDataCacheManager.put(COMPANY_CONSULTANT_LIST_CACHE, companyConsultantMap);
    }

	
}
