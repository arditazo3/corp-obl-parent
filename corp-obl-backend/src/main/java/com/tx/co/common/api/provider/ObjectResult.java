package com.tx.co.common.api.provider;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.api.model.CompanyTopicResult;
import com.tx.co.back_office.company.api.model.CompanyUserResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.back_office.company.domain.CompanyUser;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.topic.api.model.TopicResult;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.security.exception.GeneralException;

public abstract class ObjectResult {

	/**
     * Map a {@link Company} instance to a {@link CompanyResult} instance.
     *
     * @param company
     * @return UserResult
     */
    public CompanyResult toCompanyResult(Company company) {
        CompanyResult result = new CompanyResult();
        result.setIdCompany(company.getIdCompany());
        result.setDescription(company.getDescription());
        
        if(!isEmpty(company.getCompanyUsers())) {
        	result.setUsersAssociated(new ArrayList<>());
        	for (CompanyUser companyUser : company.getCompanyUsers()) {
        		result.getUsersAssociated().add(toCompanyUserResult(companyUser));
			}
        }
        return result;
    }

    public Company toCompany(CompanyResult companyResult) {
        Company company = new Company();
        if(isEmpty(companyResult)) {
            throw new GeneralException("The form is empty");
        }
        if(!isEmpty(companyResult.getIdCompany())) {
            company.setIdCompany(companyResult.getIdCompany());
        }
        if(!isEmpty(companyResult.getDescription())) {
            company.setDescription(companyResult.getDescription().trim());
        }
        if(!isEmpty(companyResult.getUsersAssociated())) {
        	for (CompanyUserResult companyUserResylt : companyResult.getUsersAssociated()) {
        		company.getCompanyUsers().add(toCompanyUser(company, companyUserResylt));
			}
        }

        return company;
    }
    
    public CompanyUserResult toCompanyUserResult(CompanyUser companyUser) {
    	CompanyUserResult result = new CompanyUserResult();
    	result.setIdCompanyUser(companyUser.getIdCompanyUser());
    	result.setUsername(companyUser.getUsername());
    	result.setCompanyAdmin(companyUser.getCompanyAdmin());
    	return result;
    }
    
    public CompanyUser toCompanyUser(Company company, CompanyUserResult companyUserResult) {
    	CompanyUser companyUser = new CompanyUser();
    	companyUser.setIdCompanyUser(companyUserResult.getIdCompanyUser());
    	companyUser.setUsername(companyUserResult.getUsername());
    	companyUser.setCompany(company);
    	if(isEmpty(companyUserResult.getCompanyAdmin())) {
    		companyUser.setCompanyAdmin(false);
    	} else {
    		companyUser.setCompanyAdmin(companyUserResult.getCompanyAdmin());
    	}
    	return companyUser;
    }
    
    public List<CompanyUser> toCompanyUserList(Company company, List<CompanyUserResult> companyUserResultList) {
    	List<CompanyUser> companyUserList = new ArrayList<>();
    	if(!isEmpty(companyUserResultList)) {
    		for (CompanyUserResult companyUserResult : companyUserResultList) {
    			companyUserList.add(toCompanyUser(company, companyUserResult));
			}
    	}
    	return companyUserList;
    }
	
    /**
     * Map a {@link Office} instance to a {@link OfficeResult} instance.
     *
     * @param office
     * @return OfficeResult
     */
    public OfficeResult toOfficeResult(Office office) {
    	OfficeResult result = new OfficeResult();
        result.setIdOffice(office.getIdOffice());
        result.setDescription(office.getDescription());
        if(!isEmpty(office.getCompany())) {
        	
        	result.setCompany(toCompanyResult(office.getCompany()));
        }
        return result;
    }
    
    public Office toOffice(OfficeResult officeResult) {
    	Office office = new Office();
    	if(isEmpty(officeResult)) {
    		throw new GeneralException("The form is empty");
    	}
    	if(!isEmpty(officeResult.getIdOffice())) {
    		office.setIdOffice(officeResult.getIdOffice());
        }
        if(!isEmpty(officeResult.getDescription())) {
        	office.setDescription(officeResult.getDescription().trim());
        }
        if(!isEmpty(officeResult.getCompany())) {
        	office.setCompany(toCompany(officeResult.getCompany()));
        } else {
        	throw new GeneralException("Fulfill the form");
        }
        return office;
    }
    
    /**
     * Map a {@link Topic} instance to a {@link TopicResult} instance.
     *
     * @param topic
     * @return OfficeResult
     */
    public TopicResult toTopicResult(Topic topic) {
    	TopicResult result = new TopicResult();
        result.setIdTopic(topic.getIdTopic());
        result.setDescription(topic.getDescription());
//        if(!isEmpty(topic.getCompanyTopic())) {
//        	
//        	result.setCompany(toCompanyResult(topic.getCompany()));
//        }
        return result;
    }
    
    public Topic toTopic(TopicResult topicResult) {
    	Topic topic = new Topic();
    	if(isEmpty(topicResult)) {
    		throw new GeneralException("The form is empty");
    	}
    	if(!isEmpty(topicResult.getIdTopic())) {
    		topic.setIdTopic(topicResult.getIdTopic());
        }
        if(!isEmpty(topicResult.getDescription())) {
        	topic.setDescription(topicResult.getDescription().trim());
        }
        if(!isEmpty(topicResult.getCompanyTopicList())) {
        	for (CompanyTopicResult companyTopicResult : topicResult.getCompanyTopicList()) {
        		topic.getCompanyTopic().add(toCompanyTopic(companyTopicResult));
			}
        } else {
        	throw new GeneralException("Fulfill the form");
        }
        return topic;
    }
    
    public CompanyTopic toCompanyTopic(CompanyTopicResult companyTopicResult) {
    	CompanyTopic companyTopic = new CompanyTopic();

    	if(!isEmpty(companyTopicResult.getIdCompanyTopic())) {
    		companyTopic.setIdCompanyTopic(companyTopicResult.getIdCompanyTopic());
        }
    	if(isEmpty(companyTopicResult.getCompany())) {
    		throw new GeneralException("The company is empty");
    	}
    	if(isEmpty(companyTopicResult.getTopic())) {
    		throw new GeneralException("The topic is empty");
    	}
        return companyTopic;
    }
    
}
