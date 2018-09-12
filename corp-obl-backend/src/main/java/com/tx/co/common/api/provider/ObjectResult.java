package com.tx.co.common.api.provider;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import com.tx.co.back_office.company.api.model.CompanyConsultantResult;
import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.api.model.CompanyTopicResult;
import com.tx.co.back_office.company.api.model.CompanyUserResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.back_office.company.domain.CompanyUser;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.topic.api.model.TopicConsultantResult;
import com.tx.co.back_office.topic.api.model.TopicResult;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;
import com.tx.co.common.translation.api.model.TranslationResult;
import com.tx.co.common.translation.domain.Translation;
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

	/**
	 * @param companyResult
	 * @return
	 */
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

	/**
	 * @param companyUser
	 * @return
	 */
	public CompanyUserResult toCompanyUserResult(CompanyUser companyUser) {
		CompanyUserResult result = new CompanyUserResult();
		result.setIdCompanyUser(companyUser.getIdCompanyUser());
		result.setUsername(companyUser.getUsername());
		result.setCompanyAdmin(companyUser.getCompanyAdmin());
		return result;
	}

	/**
	 * @param company
	 * @param companyUserResult
	 * @return
	 */
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

	/**
	 * @param company
	 * @param companyUserResultList
	 * @return
	 */
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

	/**
	 * @param officeResult
	 * @return
	 */
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
		if(!isEmpty(topic.getCompanyTopic())) {
			List<CompanyResult> companyResultsList = new ArrayList<>();
			for (CompanyTopic companyTopic : topic.getCompanyTopic()) {
				companyResultsList.add(toCompanyResult(companyTopic.getCompany()));
			}
			result.setCompanyList(companyResultsList);
		}
		if(!isEmpty(topic.getTranslationList())) {
			List<TranslationResult> translationResultsList = new ArrayList<>();
			for (Translation translation : topic.getTranslationList()) {
				translationResultsList.add(toTranslationResult(translation));
			}
			result.setTranslationList(translationResultsList);
		}
		return result;
	}

	/**
	 * @param topicResult
	 * @return
	 */
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
		if(!isEmpty(topicResult.getCompanyList())) {
			for (CompanyResult companyResult : topicResult.getCompanyList()) {
				CompanyTopicResult companyTopicResult = new CompanyTopicResult();
				companyTopicResult.setCompany(companyResult);
				companyTopicResult.setTopic(topicResult);
				topic.getCompanyTopic().add(toCompanyTopic(companyTopicResult));
			}
		} else {
			throw new GeneralException("Fulfill the form");
		}
		if(!isEmpty(topicResult.getTranslationList())) {
			for (TranslationResult translationResult : topicResult.getTranslationList()) {
				if(!isEmpty(translationResult.getDescription())) {
					topic.getTranslationList().add(toTranslation(translationResult));
				}
			}
			if(isEmpty(topic.getTranslationList())) {
				throw new GeneralException("Fulfill the form");	
			}
		} else {
			throw new GeneralException("Fulfill the form");
		}

		return topic;
	}

	/**
	 * @param translationResult
	 * @return
	 */
	public Translation toTranslation(TranslationResult translationResult) {
		Translation translation = new Translation();

		translation.setDescription(translationResult.getDescription());
		translation.setLang(translationResult.getLang());
		if(!isEmpty(translationResult.getIdTranslation())) {
			translation.setIdTranslation(translationResult.getIdTranslation());
		}
		if(!isEmpty(translationResult.getEntityId())) {
			translation.setEntityId(translationResult.getEntityId());
		}
		if(!isEmpty(translationResult.getTablename())) {
			translation.setTablename(translationResult.getTablename());
		}
		return translation;
	}

	/**
	 * @param translation
	 * @return
	 */
	public TranslationResult toTranslationResult(Translation translation) {
		TranslationResult translationResult = new TranslationResult();

		translationResult.setDescription(translation.getDescription());
		translationResult.setLang(translation.getLang());
		translationResult.setIdTranslation(translation.getIdTranslation());
		translationResult.setEntityId(translation.getEntityId());
		translationResult.setTablename(translation.getTablename());

		return translationResult;
	}
	
	/**
	 * Map a {@link CompanyConsultant} instance to a {@link CompanyConsultantResult} instance.
	 *
	 * @param companyConsultant
	 * @return CompanyConsultantResult
	 */
	public CompanyConsultantResult toCompanyConsultantResult(CompanyConsultant companyConsultant) {
		CompanyConsultantResult result = new CompanyConsultantResult();
		
		result.setIdCompanyConsultant(companyConsultant.getIdCompanyConsultant());
		result.setName(companyConsultant.getName());
		result.setEmail(companyConsultant.getEmail());
		if(!isEmpty(companyConsultant.getPhone1())) {
			result.setPhone1(companyConsultant.getPhone1());
		}
		if(!isEmpty(companyConsultant.getPhone2())) {
			result.setPhone2(companyConsultant.getPhone2());
		}
		result.setCompany(toCompanyResult(companyConsultant.getCompany()));

		return result;
	}
	
	/**
	 * @param companyConsultantResult
	 * @return
	 */
	public CompanyConsultant toCompanyConsultant(CompanyConsultantResult companyConsultantResult) {
		CompanyConsultant companyConsultant = new CompanyConsultant();
		if(isEmpty(companyConsultantResult)) {
			throw new GeneralException("The form is empty");
		}
		if(!isEmpty(companyConsultantResult.getIdCompanyConsultant())) {
			companyConsultant.setIdCompanyConsultant(companyConsultantResult.getIdCompanyConsultant());
		}
		if(!isEmpty(companyConsultantResult.getName())) {
			companyConsultant.setName(companyConsultantResult.getName().trim());
		}
		if(!isEmpty(companyConsultantResult.getEmail())) {
			companyConsultant.setEmail(companyConsultantResult.getEmail().trim());
		}
		if(!isEmpty(companyConsultantResult.getPhone1())) {
			companyConsultant.setPhone1(companyConsultantResult.getPhone1().trim());
		}
		if(!isEmpty(companyConsultantResult.getPhone2())) {
			companyConsultant.setPhone2(companyConsultantResult.getPhone2().trim());
		}
		if(!isEmpty(companyConsultantResult.getCompany())) {
			companyConsultant.setCompany(toCompany(companyConsultantResult.getCompany()));
		}

		return companyConsultant;
	}
	
	/**
	 * Map a {@link CompanyTopic} instance to a {@link CompanyTopicResult} instance.
	 *
	 * @param companyTopic
	 * @return CompanyTopicResult
	 */
	public CompanyTopicResult toCompanyTopicResult(CompanyTopic companyTopic) {
		CompanyTopicResult result = new CompanyTopicResult();
		
		result.setIdCompanyTopic(companyTopic.getIdCompanyTopic());
		result.setCompany(toCompanyResult(companyTopic.getCompany()));
		result.setTopic(toTopicResult(companyTopic.getTopic()));

		return result;
	}
	
	/**
	 * @param companyTopicResult
	 * @return
	 */
	public CompanyTopic toCompanyTopic(CompanyTopicResult companyTopicResult) {
		CompanyTopic companyTopic = new CompanyTopic();

		if(!isEmpty(companyTopicResult.getIdCompanyTopic())) {
			companyTopic.setIdCompanyTopic(companyTopicResult.getIdCompanyTopic());
		}
		if(isEmpty(companyTopicResult.getCompany())) {
			throw new GeneralException("The company is empty");
		} else {
			companyTopic.setCompany(toCompany(companyTopicResult.getCompany()));
		}
		return companyTopic;
	}

	
	/**
	 * @param topicConsultantResult
	 * @return
	 */
	public TopicConsultant toTopicConsultant(TopicConsultantResult topicConsultantResult) {
		TopicConsultant topicConsultant = new TopicConsultant();

		if(isEmpty(topicConsultantResult.getConsultant())) {
			throw new GeneralException("The Company Consultant is empty");
		} else {
			topicConsultant.setCompanyConsultant(toCompanyConsultant(topicConsultantResult.getConsultant()));
		}
		if(isEmpty(topicConsultantResult.getTopic())) {
			throw new GeneralException("The Topic Consultant is empty");
		} else {
			topicConsultant.setTopic(toTopic(topicConsultantResult.getTopic()));
		}
		return topicConsultant;
	}
	
	/**
	 * Map a {@link TopicConsultant} instance to a {@link TopicConsultantResult} instance.
	 *
	 * @param topicConsultant
	 * @return TopicConsultantResult
	 */
	public TopicConsultantResult toTopicConsultantResult(TopicConsultant topicConsultant) {
		TopicConsultantResult result = new TopicConsultantResult();
		
		result.setConsultant(toCompanyConsultantResult(topicConsultant.getCompanyConsultant()));
		result.setTopic(toTopicResult(topicConsultant.getTopic()));

		return result;
	}
}
