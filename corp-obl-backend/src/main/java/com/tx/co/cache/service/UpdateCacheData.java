package com.tx.co.cache.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.service.CompanyService;
import com.tx.co.common.utils.UtilStatic;
import com.tx.co.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.List;
import java.util.Optional;

import static com.tx.co.common.constants.AppConstants.COMPANY_LIST_CACHE;
import static com.tx.co.common.constants.AppConstants.STORAGE_DATA_CACHE;
import static com.tx.co.common.constants.AppConstants.USER_LIST_CACHE;
import static org.springframework.util.ObjectUtils.isEmpty;

public abstract class UpdateCacheData {

    private javax.cache.CacheManager cacheManager;
    private CompanyService companyService;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    @Autowired
    public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	/**
     * @return get the Companies from the cache in order to not execute the query to the database
     */
    public List<Company> getCompaniesFromCache() {

        final Cache<String, Object> storageDataCacheManager = cacheManager.getCache(STORAGE_DATA_CACHE);

        return (List<Company>) storageDataCacheManager.get(COMPANY_LIST_CACHE);
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
}
