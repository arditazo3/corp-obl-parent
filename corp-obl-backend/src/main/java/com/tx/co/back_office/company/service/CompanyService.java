package com.tx.co.back_office.company.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.repository.CompanyRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.user.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import javax.cache.CacheManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tx.co.common.constants.AppConstants.COMPANY_LIST_CACHE;
import static com.tx.co.common.constants.AppConstants.STORAGE_DATA_CACHE;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Service for {@link com.tx.co.back_office.company.domain.Company}s.
 *
 * @author Ardit Azo
 */
@Service
public class CompanyService extends UpdateCacheData implements ICompanyService {

    private static final Logger logger = LogManager.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;


    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * @return get all the Companies
     */
    @Override
    public List<Company> findAllCompany() {
        List<Company> companyList = new ArrayList<>();

        List<Company> userListFromCache = getCompaniesFromCache();
        if(!isEmpty(getCompaniesFromCache())) {
            companyList = userListFromCache;
        } else {
            companyRepository.findAll().forEach(companyList::add);
        }

        logger.info("The number of the companies: " + companyList.size());

        return companyList;
    }

    @Override
    public Company saveCompany(Company company) {
        Company companyStored = companyRepository.save(company);

        updateCompaniesCache(companyStored);

        return companyStored;
    }

    @Override
    public Optional<Company> findByIdCompany(Long idCompany) {
        return companyRepository.findById(idCompany);
    }

    @Override
    public void deleteCompany(Long idCompany) {

        Company company = findByIdCompany(idCompany).get();

        // disable the company
        company.setEnabled(false);

        companyRepository.save(company);

        updateCompaniesCache(company);
    }




}
