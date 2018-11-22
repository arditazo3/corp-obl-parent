package com.tx.co.common.api.provider;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tx.co.back_office.company.api.model.CompanyConsultantResult;
import com.tx.co.back_office.company.api.model.CompanyResult;
import com.tx.co.back_office.company.api.model.CompanyTopicResult;
import com.tx.co.back_office.company.api.model.CompanyUserResult;
import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.back_office.company.domain.CompanyUser;
import com.tx.co.back_office.office.api.model.OfficeResult;
import com.tx.co.back_office.office.api.model.OfficeTaskTemplates;
import com.tx.co.back_office.office.api.model.OfficeTaskTemplatesResult;
import com.tx.co.back_office.office.api.model.TaskTempOffices;
import com.tx.co.back_office.office.api.model.TaskTempOfficesResult;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.api.model.TaskOfficeRelationsResult;
import com.tx.co.back_office.task.api.model.TaskOfficeResult;
import com.tx.co.back_office.task.api.model.TaskResult;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.model.TaskOfficeRelations;
import com.tx.co.back_office.tasktemplate.api.model.ObjectSearchTaskTemplate;
import com.tx.co.back_office.tasktemplate.api.model.ObjectSearchTaskTemplateResult;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateResult;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplateattachment.api.model.TaskTemplateAttachmentResult;
import com.tx.co.back_office.tasktemplateattachment.model.TaskTemplateAttachment;
import com.tx.co.back_office.topic.api.model.TopicConsultantResult;
import com.tx.co.back_office.topic.api.model.TopicResult;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;
import com.tx.co.cache.service.UpdateCacheData;

import static com.tx.co.common.constants.AppConstants.*;
import com.tx.co.common.translation.api.model.TranslationResult;
import com.tx.co.common.translation.domain.Translation;
import com.tx.co.common.utils.UtilStatic;
import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchived;
import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchivedResult;
import com.tx.co.front_end.expiration.api.model.ExpirationActivityAttachmentResult;
import com.tx.co.front_end.expiration.api.model.ExpirationActivityResult;
import com.tx.co.front_end.expiration.api.model.ExpirationResult;
import com.tx.co.front_end.expiration.api.model.TaskOfficeExpirationsResult;
import com.tx.co.front_end.expiration.api.model.TaskOfficeExpirations;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.domain.ExpirationActivity;
import com.tx.co.front_end.expiration.domain.ExpirationActivityAttachment;
import com.tx.co.security.exception.GeneralException;
import com.tx.co.user.api.model.UserResult;
import com.tx.co.user.domain.User;

public abstract class ObjectResult extends UpdateCacheData {

	/**
	 * Map a {@link User} instance to a {@link UserResult} instance.
	 *
	 * @param user
	 * @return UserResult
	 */
	public UserResult toUserResult(User user) {
		UserResult result = new UserResult();
		result.setusername(user.getUsername());
		result.setFullName(user.getFullName());
		result.setEmail(user.getEmail());
		result.setLang(user.getLang());
		result.setAuthorities(user.getAuthorities());
		result.setEnabled(user.isEnabled());
		return result;
	}

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

		if (!isEmpty(company.getCompanyUsers())) {
			result.setUsersAssociated(new ArrayList<>());
			for (CompanyUser companyUser : company.getCompanyUsers()) {
				result.getUsersAssociated().add(toCompanyUserResult(companyUser));
			}
		}
		if (!isEmpty(company.getOffice())) {
			result.setOffices(new ArrayList<>());
			for (Office office : company.getOffice()) {
				result.getOffices().add(toOfficeResult(office));
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
		if (isEmpty(companyResult)) {
			throw new GeneralException(EMPTY_FORM);
		}
		if (!isEmpty(companyResult.getIdCompany())) {
			company.setIdCompany(companyResult.getIdCompany());
		}
		if (!isEmpty(companyResult.getDescription())) {
			company.setDescription(companyResult.getDescription().trim());
		}
		if (!isEmpty(companyResult.getUsersAssociated())) {
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
		result.setUser(companyUser.getUser());
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
		companyUser.setUser(companyUserResult.getUser());
		companyUser.setCompany(company);
		companyUser.setUsername(companyUserResult.getUser().getUsername());
		if (isEmpty(companyUserResult.getCompanyAdmin())) {
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
		if (!isEmpty(companyUserResultList)) {
			for (CompanyUserResult companyUserResult : companyUserResultList) {
				companyUserList.add(toCompanyUser(company, companyUserResult));
			}
		}
		return companyUserList;
	}

	public OfficeResult toOfficeResult(Office office) {
		OfficeResult result = new OfficeResult();
		result.setIdOffice(office.getIdOffice());
		result.setDescription(office.getDescription());
		return result;
	}

	/**
	 * Map a {@link Office} instance to a {@link OfficeResult} instance.
	 *
	 * @param office
	 * @return OfficeResult
	 */
	public OfficeResult toOfficeWithCompanyResult(Office office) {
		OfficeResult result = new OfficeResult();
		result.setIdOffice(office.getIdOffice());
		result.setDescription(office.getDescription());
		if (!isEmpty(office.getCompany())) {
			result.setCompany(toCompanyResult(office.getCompany()));
		}
		return result;
	}

	/**
	 * Map a {@link Office} instance to a {@link OfficeResult} instance.
	 *
	 * @param office
	 * @return OfficeResult
	 */
	public OfficeResult toOfficeWithTaskOfficeRelationsResult(Office office,
			Set<TaskOfficeRelations> taskOfficeRelations) {

		OfficeResult result = toOfficeWithCompanyResult(office);

		if (!isEmpty(taskOfficeRelations)) {
			List<User> userProviders = new ArrayList<>();
			List<User> userBeneficiaries = new ArrayList<>();
			for (TaskOfficeRelations taskOfficeRelation : taskOfficeRelations) {
				if (taskOfficeRelation.getRelationType().compareTo(CONTROLLER) == 0) {
					userProviders.add(getUserFromUsername(taskOfficeRelation.getUsername()));
				} else if (taskOfficeRelation.getRelationType().compareTo(CONTROLLED) == 0) {
					userBeneficiaries.add(getUserFromUsername(taskOfficeRelation.getUsername()));
				}
			}
			result.setUserProviders(userProviders);
			result.setUserBeneficiaries(userBeneficiaries);
		}
		return result;
	}

	/**
	 * @param officeResult
	 * @return
	 */
	public Office toOffice(OfficeResult officeResult) {
		Office office = new Office();
		if (isEmpty(officeResult)) {
			throw new GeneralException(EMPTY_FORM);
		}
		if (!isEmpty(officeResult.getIdOffice())) {
			office.setIdOffice(officeResult.getIdOffice());
		}
		if (!isEmpty(officeResult.getDescription())) {
			office.setDescription(officeResult.getDescription().trim());
		}
		if (!isEmpty(officeResult.getCompany())) {
			office.setCompany(toCompany(officeResult.getCompany()));
		} else {
			throw new GeneralException(FULLFIT_FORM);
		}
		if (!isEmpty(officeResult.getUserProviders())) {
			List<User> users = new ArrayList<>();
			for (User user : officeResult.getUserProviders()) {
				users.add(user);
			}
			office.setUserProviders(users);
		}
		if (!isEmpty(officeResult.getUserBeneficiaries())) {
			List<User> users = new ArrayList<>();
			for (User user : officeResult.getUserBeneficiaries()) {
				users.add(user);
			}
			office.setUserBeneficiaries(users);
		}

		return office;
	}

	/**
	 * Map a {@link Topic} instance to a {@link TopicResult} instance.
	 *
	 * @param topic
	 * @return OfficeResult
	 */
	public TopicResult toTopicObjectResult(Topic topic) {
		TopicResult result = new TopicResult();
		result.setIdTopic(topic.getIdTopic());
		result.setDescription(topic.getDescription());

		return result;
	}

	/**
	 * Map a {@link Topic} instance to a {@link TopicResult} instance.
	 *
	 * @param topic
	 * @return TopicResult
	 */
	public TopicResult toTopicWithTranslationsResult(Topic topic) {

		TopicResult result = toTopicObjectResult(topic);

		toTopicIncludeOfficesResult(topic, result);
		
		toTopicIncludeTranslationsResult(topic, result);

		return result;
	}
	
	/**
	 * Map a {@link Topic} instance to a {@link TopicResult} instance.
	 *
	 * @param topic
	 * @return TopicResult
	 */
	public TopicResult toTopicResult(Topic topic) {

		TopicResult result = toTopicObjectResult(topic);

		toTopicIncludeTranslationsResult(topic, result);

		toTopicIncludeConsultantsResult(topic, result);

		return result;
	}

	/**
	 * @param topic
	 * @param result
	 */
	public void toTopicIncludeOfficesResult(Topic topic, TopicResult result) {
		if (!isEmpty(topic.getCompanyTopic())) {
			List<CompanyResult> companyResultsList = new ArrayList<>();
			for (CompanyTopic companyTopic : topic.getCompanyTopic()) {
				companyResultsList.add(toCompanyResult(companyTopic.getCompany()));
			}
			result.setCompanyList(companyResultsList);
		}
	}

	/**
	 * @param topic
	 * @param result
	 */
	public void toTopicIncludeTranslationsResult(Topic topic, TopicResult result) {
		if (!isEmpty(topic.getTranslationList())) {
			List<TranslationResult> translationResultsList = new ArrayList<>();
			for (Translation translation : topic.getTranslationList()) {
				translationResultsList.add(toTranslationResult(translation));
			}
			result.setTranslationList(translationResultsList);
		}
	}

	/**
	 * @param topic
	 * @param result
	 */
	public void toTopicIncludeConsultantsResult(Topic topic, TopicResult result) {
		if (!isEmpty(topic.getTopicConsultants())) {

			List<TopicConsultantResult> topicConsultantsList = new ArrayList<>();
			for (TopicConsultant topicConsultant : topic.getTopicConsultants()) {
				topicConsultantsList.add(toTopicConsultantResult(topicConsultant));
			}
			result.setTopicConsultantList(topicConsultantsList);

			List<CompanyConsultantResult> consultantList = new ArrayList<>();
			for (CompanyConsultant companyConsultant : topic.getCompanyConsultantsList()) {
				consultantList.add(toCompanyConsultantResult(companyConsultant));
			}
			result.setConsultantList(consultantList);

			result.setConsultantList(consultantList);
		}
	}

	/**
	 * @param topicResult
	 * @return
	 */
	public Topic toTopicSingleObject(TopicResult topicResult) {
		Topic topic = new Topic();
		if (isEmpty(topicResult)) {
			throw new GeneralException(EMPTY_FORM);
		}
		if (!isEmpty(topicResult.getIdTopic())) {
			topic.setIdTopic(topicResult.getIdTopic());
		}
		if (!isEmpty(topicResult.getDescription())) {
			topic.setDescription(topicResult.getDescription().trim());
		}
		return topic;
	}
	
	/**
	 * @param topicResult
	 * @return
	 */
	public Topic toTopic(TopicResult topicResult) {
		
		Topic topic = toTopicSingleObject(topicResult);
		
		if (!isEmpty(topicResult.getCompanyList())) {
			for (CompanyResult companyResult : topicResult.getCompanyList()) {
				CompanyTopicResult companyTopicResult = new CompanyTopicResult();
				companyTopicResult.setCompany(companyResult);
				companyTopicResult.setTopic(topicResult);
				topic.getCompanyTopic().add(toCompanyTopic(companyTopicResult));
			}
		} else {
			throw new GeneralException(FULLFIT_FORM);
		}

		return topic;
	}

	/**
	 * @param topicResult,
	 *            hasTranslations
	 * @return
	 */
	public Topic toTopicSingleObjectWithTranslation(TopicResult topicResult) {

		Topic topic = toTopicSingleObject(topicResult);

		if (!isEmpty(topicResult.getTranslationList())) {
			for (TranslationResult translationResult : topicResult.getTranslationList()) {
				if (!isEmpty(translationResult.getDescription())) {
					topic.getTranslationList().add(toTranslation(translationResult));
				}
			}
			if (isEmpty(topic.getTranslationList())) {
				throw new GeneralException(FULLFIT_FORM);
			}
		} else {
			throw new GeneralException(FULLFIT_FORM);
		}

		return topic;
	}
	
	/**
	 * @param topicResult,
	 *            hasTranslations
	 * @return
	 */
	public Topic toTopicWithTranslation(TopicResult topicResult) {

		Topic topic = toTopic(topicResult);

		if (!isEmpty(topicResult.getTranslationList())) {
			for (TranslationResult translationResult : topicResult.getTranslationList()) {
				if (!isEmpty(translationResult.getDescription())) {
					topic.getTranslationList().add(toTranslation(translationResult));
				}
			}
			if (isEmpty(topic.getTranslationList())) {
				throw new GeneralException(FULLFIT_FORM);
			}
		} else {
			throw new GeneralException(FULLFIT_FORM);
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
		if (!isEmpty(translationResult.getIdTranslation())) {
			translation.setIdTranslation(translationResult.getIdTranslation());
		}
		if (!isEmpty(translationResult.getEntityId())) {
			translation.setEntityId(translationResult.getEntityId());
		}
		if (!isEmpty(translationResult.getTablename())) {
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
	 * Map a {@link CompanyConsultant} instance to a {@link CompanyConsultantResult}
	 * instance.
	 *
	 * @param companyConsultant
	 * @return CompanyConsultantResult
	 */
	public CompanyConsultantResult toCompanyConsultantResult(CompanyConsultant companyConsultant) {
		CompanyConsultantResult result = new CompanyConsultantResult();

		result.setIdCompanyConsultant(companyConsultant.getIdCompanyConsultant());
		result.setName(companyConsultant.getName());
		result.setEmail(companyConsultant.getEmail());
		if (!isEmpty(companyConsultant.getPhone1())) {
			result.setPhone1(companyConsultant.getPhone1());
		}
		if (!isEmpty(companyConsultant.getPhone2())) {
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
		if (isEmpty(companyConsultantResult)) {
			throw new GeneralException(EMPTY_FORM);
		}
		if (!isEmpty(companyConsultantResult.getIdCompanyConsultant())) {
			companyConsultant.setIdCompanyConsultant(companyConsultantResult.getIdCompanyConsultant());
		}
		if (!isEmpty(companyConsultantResult.getName())) {
			companyConsultant.setName(companyConsultantResult.getName().trim());
		}
		if (!isEmpty(companyConsultantResult.getEmail())) {
			companyConsultant.setEmail(companyConsultantResult.getEmail().trim());
		}
		if (!isEmpty(companyConsultantResult.getPhone1())) {
			companyConsultant.setPhone1(companyConsultantResult.getPhone1().trim());
		}
		if (!isEmpty(companyConsultantResult.getPhone2())) {
			companyConsultant.setPhone2(companyConsultantResult.getPhone2().trim());
		}
		if (!isEmpty(companyConsultantResult.getCompany())) {
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

		if (!isEmpty(companyTopicResult.getIdCompanyTopic())) {
			companyTopic.setIdCompanyTopic(companyTopicResult.getIdCompanyTopic());
		}
		if (isEmpty(companyTopicResult.getCompany())) {
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

		if (isEmpty(topicConsultantResult.getConsultant())) {
			throw new GeneralException("The Company Consultant is empty");
		} else {
			topicConsultant.setCompanyConsultant(toCompanyConsultant(topicConsultantResult.getConsultant()));
		}
		if (isEmpty(topicConsultantResult.getTopic())) {
			throw new GeneralException("The Topic Consultant is empty");
		} else {
			topicConsultant.setTopic(toTopicSingleObjectWithTranslation(topicConsultantResult.getTopic()));
		}
		return topicConsultant;
	}

	/**
	 * Map a {@link TopicConsultant} instance to a {@link TopicConsultantResult}
	 * instance.
	 *
	 * @param topicConsultant
	 * @return TopicConsultantResult
	 */
	public TopicConsultantResult toTopicConsultantResult(TopicConsultant topicConsultant) {
		TopicConsultantResult result = new TopicConsultantResult();

		result.setConsultant(toCompanyConsultantResult(topicConsultant.getCompanyConsultant()));

		return result;
	}

	/**
	 * @param taskTemplateResult
	 * @return
	 */
	public TaskTemplate toTaskTemplate(TaskTemplateResult taskTemplateResult) {
		TaskTemplate taskTemplate = new TaskTemplate();
		if (isEmpty(taskTemplateResult)) {
			throw new GeneralException(EMPTY_FORM);
		}
		if (!isEmpty(taskTemplateResult.getIdTaskTemplate())) {
			taskTemplate.setIdTaskTemplate(taskTemplateResult.getIdTaskTemplate());
		}
		if (!isEmpty(taskTemplateResult.getDescription())) {
			taskTemplate.setDescription(taskTemplateResult.getDescription().trim());
		}
		if (!isEmpty(taskTemplateResult.getIsRapidConfiguration())) {
			taskTemplate.setIsRapidConfiguration(taskTemplateResult.getIsRapidConfiguration());
		}
		if (!isEmpty(taskTemplateResult.getRecurrence())) {
			taskTemplate.setRecurrence(taskTemplateResult.getRecurrence());
		}
		if (!isEmpty(taskTemplateResult.getExpirationType())) {
			taskTemplate.setExpirationType(taskTemplateResult.getExpirationType());
		}
		if (!isEmpty(taskTemplateResult.getDay())) {
			taskTemplate.setDay(taskTemplateResult.getDay());
		}
		if (!isEmpty(taskTemplateResult.getDaysOfNotice())) {
			taskTemplate.setDaysOfNotice(taskTemplateResult.getDaysOfNotice());
		}
		if (!isEmpty(taskTemplateResult.getFrequenceOfNotice())) {
			taskTemplate.setFrequenceOfNotice(taskTemplateResult.getFrequenceOfNotice());
		}
		if (!isEmpty(taskTemplateResult.getDaysBeforeShowExpiration())) {
			taskTemplate.setDaysBeforeShowExpiration(taskTemplateResult.getDaysBeforeShowExpiration());
		}
		if (!isEmpty(taskTemplateResult.getExpirationClosableBy())) {
			taskTemplate.setExpirationClosableBy(taskTemplateResult.getExpirationClosableBy());
		}
		if (!isEmpty(taskTemplateResult.getTopic())) {
			taskTemplate.setTopic(toTopicSingleObject(taskTemplateResult.getTopic()));
		} else {
			throw new GeneralException(FULLFIT_FORM);
		}
		return taskTemplate;
	}

	/**
	 * Map a {@link TaskTemplate} instance to a {@link TaskTemplateResult} instance.
	 *
	 * @param taskTemplate
	 * @return TaskTemplateResult
	 */
	public TaskTemplateResult toTaskTemplateResult(TaskTemplate taskTemplate) {
		TaskTemplateResult result = new TaskTemplateResult();
		result.setIdTaskTemplate(taskTemplate.getIdTaskTemplate());
		result.setDescription(taskTemplate.getDescription());
		result.setIsRapidConfiguration(taskTemplate.getIsRapidConfiguration());
		result.setRecurrence(taskTemplate.getRecurrence());
		result.setExpirationType(taskTemplate.getExpirationType());
		result.setDay(taskTemplate.getDay());
		result.setDaysOfNotice(taskTemplate.getDaysOfNotice());
		result.setFrequenceOfNotice(taskTemplate.getFrequenceOfNotice());
		result.setDaysBeforeShowExpiration(taskTemplate.getDaysBeforeShowExpiration());
		result.setExpirationClosableBy(taskTemplate.getExpirationClosableBy());
		if (!isEmpty(taskTemplate.getTopic())) {
			result.setTopic(toTopicWithTranslationsResult(taskTemplate.getTopic()));
		}
		if (!isEmpty(taskTemplate.getTaskTemplateAttachments())) {
			List<TaskTemplateAttachmentResult> attachmentResults = new ArrayList<>();
			for (TaskTemplateAttachment taskTemplateAttachment : taskTemplate.getTaskTemplateAttachments()) {
				attachmentResults.add(toTaskTemplateAttachmentResult(taskTemplateAttachment));
			}
			result.setTaskTemplateAttachmentResults(attachmentResults);
		}
		if (!isEmpty(taskTemplate.getDescriptionLangList())) {
			result.setDescriptionLangList(taskTemplate.getDescriptionLangList());
		}
		if (!isEmpty(taskTemplate.getCounterOffices())) {
			result.setCounterOffices(taskTemplate.getCounterOffices());
		}
		return result;
	}

	/**
	 * Map a {@link TaskTemplate} instance to a {@link TaskTemplateResult} instance.
	 *
	 * @param taskTemplate
	 * @return TaskTemplateResult
	 */
	public TaskTemplateResult toTaskTemplateWithTaskResult(TaskTemplate taskTemplate) {
		TaskTemplateResult result = toTaskTemplateResult(taskTemplate);

		if (!isEmpty(taskTemplate.getTasks())) {
			List<TaskResult> taskResults = new ArrayList<>();
			for (Task task : taskTemplate.getTasks()) {
				taskResults.add(toTaskResult(task));
			}
			result.setTaskResults(taskResults);
		}
		return result;
	}

	/**
	 * @param task
	 * @return TaskResult
	 */
	public TaskResult toTaskResult(Task task) {
		TaskResult taskResult = new TaskResult();

		taskResult.setIdTask(task.getIdTask());
		taskResult.setRecurrence(task.getRecurrence());
		taskResult.setExpirationType(task.getExpirationType());
		taskResult.setDay(task.getDay());
		taskResult.setDaysOfNotice(task.getDaysOfNotice());
		taskResult.setFrequenceOfNotice(task.getFrequenceOfNotice());
		taskResult.setDaysBeforeShowExpiration(task.getDaysBeforeShowExpiration());
		taskResult.setDescriptionLangList(task.getDescriptionLangList());
		taskResult.setCounterCompany(task.getCounterCompany());

		if (!isEmpty(task.getTaskTemplate())) {
			taskResult.setTaskTemplate(toTaskTemplateResult(task.getTaskTemplate()));
			taskResult.setIdTaskTemplate(task.getTaskTemplate().getIdTaskTemplate());
		}
		if (!isEmpty(task.getTaskOfficesFilterEnabled())) {
			List<TaskOfficeResult> taskOfficeResults = new ArrayList<>();
			for (TaskOffice taskOffice : task.getTaskOfficesFilterEnabled()) {
				taskOfficeResults.add(toTaskOfficeWithTaskOfficeRelationResult(taskOffice));
			}
			taskResult.setTaskOffices(taskOfficeResults);
		}
		return taskResult;
	}

	/**
	 * @param task
	 * @return TaskResult
	 */
	public TaskResult toTaskResultOnly(Task task) {
		TaskResult taskResult = new TaskResult();

		taskResult.setIdTask(task.getIdTask());
		taskResult.setRecurrence(task.getRecurrence());
		taskResult.setExpirationType(task.getExpirationType());
		taskResult.setDay(task.getDay());
		taskResult.setDaysOfNotice(task.getDaysOfNotice());
		taskResult.setFrequenceOfNotice(task.getFrequenceOfNotice());
		taskResult.setDaysBeforeShowExpiration(task.getDaysBeforeShowExpiration());

		return taskResult;
	}

	/**
	 * @param taskResult
	 * @return
	 */
	public Task toTask(TaskResult taskResult) {
		Task task = new Task();

		task.setIdTask(taskResult.getIdTask());
		task.setRecurrence(taskResult.getRecurrence());
		task.setExpirationType(taskResult.getExpirationType());
		task.setDay(taskResult.getDay());
		task.setDaysOfNotice(taskResult.getDaysOfNotice());
		task.setFrequenceOfNotice(taskResult.getFrequenceOfNotice());
		task.setDaysBeforeShowExpiration(taskResult.getDaysBeforeShowExpiration());
		if (!isEmpty(taskResult.getTaskTemplate())) {
			task.setTaskTemplate(toTaskTemplate(taskResult.getTaskTemplate()));	
		}
		return task;
	}

	/**
	 * @param taskResult
	 * @return
	 */
	public Task toTaskWithTaskOffices(TaskResult taskResult) {
		Task task = toTask(taskResult);

		if (!isEmpty(taskResult.getTaskOffices())) {
			List<TaskOffice> taskOffices = new ArrayList<>();
			for (TaskOfficeResult taskOfficeResult : taskResult.getTaskOffices()) {
				taskOfficeResult.setTaskTemplate(taskResult.getTaskTemplate());
				taskOfficeResult.setTask(taskResult);
				taskOffices.add(toTaskOffice(taskOfficeResult));
			}
			task.setTaskOffices(new HashSet<TaskOffice>(taskOffices));
		}
		if (!isEmpty(taskResult.getOffice())) {
			task.setOffice(toOffice(taskResult.getOffice()));
		}

		return task;
	}

	/**
	 * @param taskTemplateAttachment
	 * @return TaskTemplateAttachmentResult
	 */
	public TaskTemplateAttachmentResult toTaskTemplateAttachmentResult(TaskTemplateAttachment taskTemplateAttachment) {
		TaskTemplateAttachmentResult taskTemplateAttachmentResult = new TaskTemplateAttachmentResult();

		taskTemplateAttachmentResult.setIdTaskTemplateAttachment(taskTemplateAttachment.getIdTaskTemplateAttachment());
		taskTemplateAttachmentResult.setFileName(taskTemplateAttachment.getFileName());
		taskTemplateAttachmentResult.setFilePath(taskTemplateAttachment.getFilePath());
		taskTemplateAttachmentResult.setFileType(taskTemplateAttachment.getFileType());
		taskTemplateAttachmentResult.setFileSize(taskTemplateAttachment.getFileSize());
		return taskTemplateAttachmentResult;
	}

	/**
	 * @param objectSearchTaskTemplateResult
	 * @return
	 */
	public ObjectSearchTaskTemplate toObjectSearchTaskTemplate(
			ObjectSearchTaskTemplateResult objectSearchTaskTemplateResult) {
		ObjectSearchTaskTemplate objectSearchTaskTemplate = new ObjectSearchTaskTemplate();

		if (isEmpty(objectSearchTaskTemplateResult.getDescriptionTaskTemplate())) {
			objectSearchTaskTemplate.setDescriptionTaskTemplate("");
		} else {
			objectSearchTaskTemplate
			.setDescriptionTaskTemplate(objectSearchTaskTemplateResult.getDescriptionTaskTemplate());
		}

		if (!isEmpty(objectSearchTaskTemplateResult.getCompanies())) {
			List<Company> companies = new ArrayList<>();
			for (CompanyResult companyResult : objectSearchTaskTemplateResult.getCompanies()) {
				companies.add(toCompany(companyResult));
			}
			objectSearchTaskTemplate.setCompanies(companies);
		}
		if (!isEmpty(objectSearchTaskTemplateResult.getTopics())) {
			List<Topic> topics = new ArrayList<>();
			for (TopicResult topicResult : objectSearchTaskTemplateResult.getTopics()) {
				topics.add(toTopicSingleObject(topicResult));
			}
			objectSearchTaskTemplate.setTopics(topics);
		}
		return objectSearchTaskTemplate;
	}

	/**
	 * @param taskTempOfficesResult
	 * @return
	 */
	public TaskTempOffices toTaskTempOffices(TaskTempOfficesResult taskTempOfficesResult) {

		TaskTempOffices taskTempOffices = new TaskTempOffices();

		if (isEmpty(taskTempOfficesResult.getDescriptionTaskTemplate())) {
			taskTempOffices.setDescriptionTaskTemplate("");
		} else {
			taskTempOffices.setDescriptionTaskTemplate(taskTempOfficesResult.getDescriptionTaskTemplate());
		}
		if (!isEmpty(taskTempOfficesResult.getOffices())) {
			List<Office> offices = new ArrayList<>();
			for (OfficeResult officeResult : taskTempOfficesResult.getOffices()) {
				offices.add(toOffice(officeResult));
			}
			taskTempOffices.setOffices(offices);
		}
		return taskTempOffices;
	}

	/**
	 * @param officeTaskTemplates
	 * @return
	 */
	public OfficeTaskTemplatesResult toOfficeTaskTemplates(OfficeTaskTemplates officeTaskTemplates) {

		OfficeTaskTemplatesResult officeTasksResult = new OfficeTaskTemplatesResult();

		officeTasksResult.setOffice(toOfficeWithTaskOfficeRelationsResult(officeTaskTemplates.getOffice(), null));

		if (!isEmpty(officeTaskTemplates.getTaskTemplates())) {
			List<TaskTemplateResult> taskTemplateResults = new ArrayList<>();
			for (TaskTemplate taskTemplate : officeTaskTemplates.getTaskTemplates()) {
				taskTemplateResults.add(toTaskTemplateResult(taskTemplate));
			}
			officeTasksResult.setTaskTemplates(taskTemplateResults);
		}
		return officeTasksResult;
	}

	/**
	 * @param taskOfficeResult
	 * @return
	 */
	public TaskOffice toTaskOffice(TaskOfficeResult taskOfficeResult) {
		TaskOffice taskOffice = new TaskOffice();

		taskOffice.setIdTaskOffice(taskOfficeResult.getIdTaskOffice());
		taskOffice.setTaskTemplate(toTaskTemplate(taskOfficeResult.getTaskTemplate()));
		taskOffice.setTask(toTask(taskOfficeResult.getTask()));
		
		if (!isEmpty(taskOfficeResult.getOffice())) {
			taskOffice.setOffice(toOffice(taskOfficeResult.getOffice()));	
			
			if (!isEmpty(taskOfficeResult.getOffice().getUserProviders())) {
				List<TaskOfficeRelations> taskOfficeRelations = new ArrayList<>();
				for (User user : taskOfficeResult.getOffice().getUserProviders()) {
					TaskOfficeRelations taskOfficeRelation = new TaskOfficeRelations();
					taskOfficeRelation.setUsername(user.getUsername());
					taskOfficeRelation.setRelationType(CONTROLLER);
					taskOfficeRelation.setTaskOffice(taskOffice);

					taskOfficeRelations.add(taskOfficeRelation);
				}
				taskOffice.getTaskOfficeRelations().addAll(new HashSet<TaskOfficeRelations>(taskOfficeRelations));
			}
			if (!isEmpty(taskOfficeResult.getOffice().getUserBeneficiaries())) {
				List<TaskOfficeRelations> taskOfficeRelations = new ArrayList<>();
				for (User user : taskOfficeResult.getOffice().getUserBeneficiaries()) {
					TaskOfficeRelations taskOfficeRelation = new TaskOfficeRelations();
					taskOfficeRelation.setUsername(user.getUsername());
					taskOfficeRelation.setRelationType(CONTROLLED);
					taskOfficeRelation.setTaskOffice(taskOffice);

					taskOfficeRelations.add(taskOfficeRelation);
				}
				taskOffice.getTaskOfficeRelations().addAll(new HashSet<TaskOfficeRelations>(taskOfficeRelations));
			}
		}
		
		taskOffice.setStartDate(taskOfficeResult.getStartDate());
		taskOffice.setEndDate(taskOfficeResult.getEndDate());

		return taskOffice;
	}

	/**
	 * @param taskOffice
	 * @return
	 */
	public TaskOfficeResult toTaskOfficeResult(TaskOffice taskOffice) {
		TaskOfficeResult taskOfficeResult = new TaskOfficeResult();

		taskOfficeResult.setIdTaskOffice(taskOffice.getIdTaskOffice());
		taskOfficeResult.setOffice(
				toOfficeWithTaskOfficeRelationsResult(taskOffice.getOffice(), taskOffice.getTaskOfficeRelations()));
		taskOfficeResult.setTaskTemplate(toTaskTemplateResult(taskOffice.getTaskTemplate()));
		taskOfficeResult.setTask(toTaskResultOnly(taskOffice.getTask()));

		return taskOfficeResult;
	}

	/**
	 * @param taskOffice
	 * @return
	 */
	public TaskOfficeResult toTaskOfficeWithTaskOfficeRelationResult(TaskOffice taskOffice) {
		TaskOfficeResult taskOfficeResult = toTaskOfficeResult(taskOffice);

		if (!isEmpty(taskOffice.getTaskOfficeRelations())) {
			List<TaskOfficeRelationsResult> taskOfficeRelationsResults = new ArrayList<>();
			for (TaskOfficeRelations taskOfficeRelation : taskOffice.getTaskOfficeRelations()) {
				taskOfficeRelationsResults.add(toTaskOfficeRelationResult(taskOfficeRelation));
			}
			taskOfficeResult.setTaskOfficeRelations(taskOfficeRelationsResults);
		}
		return taskOfficeResult;
	}

	/**
	 * @param taskOfficeRelationsResult
	 * @return
	 */
	public TaskOfficeRelations toTaskOfficeRelation(TaskOfficeRelationsResult taskOfficeRelationsResult) {
		TaskOfficeRelations taskOfficeRelation = new TaskOfficeRelations();

		taskOfficeRelation.setIdTaskOfficeRelation(taskOfficeRelationsResult.getIdTaskOfficeRelation());
		taskOfficeRelation.setTaskOffice(toTaskOffice(taskOfficeRelationsResult.getTaskOffice()));
		taskOfficeRelation.setUsername(taskOfficeRelationsResult.getUsername());
		taskOfficeRelation.setRelationType(taskOfficeRelationsResult.getRelationType());

		return taskOfficeRelation;
	}

	/**
	 * @param taskOfficeRelation
	 * @return
	 */
	public TaskOfficeRelationsResult toTaskOfficeRelationResult(TaskOfficeRelations taskOfficeRelation) {
		TaskOfficeRelationsResult taskOfficeRelationResult = new TaskOfficeRelationsResult();

		taskOfficeRelationResult.setIdTaskOfficeRelation(taskOfficeRelation.getIdTaskOfficeRelation());
		taskOfficeRelationResult.setTaskOffice(toTaskOfficeResult(taskOfficeRelation.getTaskOffice()));
		taskOfficeRelationResult.setUsername(taskOfficeRelation.getUsername());
		taskOfficeRelationResult.setRelationType(taskOfficeRelation.getRelationType());

		return taskOfficeRelationResult;
	}

	/**
	 * @param dateExpirationOfficesHasArchivedResult
	 * @return
	 */
	public DateExpirationOfficesHasArchived toDateExpirationOfficesHasArchived(
			DateExpirationOfficesHasArchivedResult dateExpirationOfficesHasArchivedResult) {

		DateExpirationOfficesHasArchived dateExpirationOfficesHasArchived = new DateExpirationOfficesHasArchived();

		dateExpirationOfficesHasArchived.setDateStart(dateExpirationOfficesHasArchivedResult.getDateStart());
		dateExpirationOfficesHasArchived.setDateEnd(dateExpirationOfficesHasArchivedResult.getDateEnd());
		if (!isEmpty(dateExpirationOfficesHasArchivedResult.getOffices())) {
			List<Office> offices = new ArrayList<>();
			for (OfficeResult officeResult : dateExpirationOfficesHasArchivedResult.getOffices()) {
				offices.add(toOffice(officeResult));
			}
			dateExpirationOfficesHasArchived.setOffices(offices);
		}
		dateExpirationOfficesHasArchived.setShowArchived(dateExpirationOfficesHasArchivedResult.getShowArchived());
		dateExpirationOfficesHasArchived.setUserRelationType(dateExpirationOfficesHasArchivedResult.getUserRelationType());

		return dateExpirationOfficesHasArchived;
	}

	/**
	 * @param taskOfficeExpirations
	 * @return
	 */
	public TaskOfficeExpirationsResult toTaskOfficeExpirationsResult(TaskOfficeExpirations taskOfficeExpirations) {

		TaskOfficeExpirationsResult taskOfficeExpirationsResult = new TaskOfficeExpirationsResult();

		taskOfficeExpirationsResult.setDescription(taskOfficeExpirations.getDescription());
		taskOfficeExpirationsResult.setIdTaskTemplate(taskOfficeExpirations.getIdTaskTemplate());
		taskOfficeExpirationsResult.setTotalCompleted(taskOfficeExpirations.getTotalCompleted());
		taskOfficeExpirationsResult.setTotalArchived(taskOfficeExpirations.getTotalArchived());
		taskOfficeExpirationsResult.setTotalExpired(taskOfficeExpirations.getTotalExpired());
		taskOfficeExpirationsResult.setTotalExpirations(taskOfficeExpirations.getTotalExpirations());

		String colorDefined = UtilStatic.buildColor(taskOfficeExpirationsResult);
		taskOfficeExpirationsResult.setColorDefined(colorDefined);
		taskOfficeExpirationsResult.setExpirationDate(taskOfficeExpirations.getExpirationDate());
		taskOfficeExpirationsResult.setTask(toTaskResult(taskOfficeExpirations.getTask()));
		taskOfficeExpirationsResult.setOffice(toOfficeWithCompanyResult(taskOfficeExpirations.getOffice()));
		taskOfficeExpirationsResult.setStatusExpirationOnChange(taskOfficeExpirations.getStatusExpirationOnChange());

		if (!isEmpty(taskOfficeExpirations.getExpirations())) {
			List<ExpirationResult> expirationResults = new ArrayList<>();

			// sort expirations
			List<Expiration> expirationsToSort = new ArrayList<>(taskOfficeExpirations.getExpirations());
			Collections.sort(expirationsToSort, new Comparator<Expiration>() {
				@Override
				public int compare(Expiration e1, Expiration e2) {

					return new CompareToBuilder()
							.append(e1.getOffice().getIdOffice(), e2.getOffice().getIdOffice()) // sort by office
							.append(e1.getUsername(), e2.getUsername()) // sort by username
							.toComparison();
				}
			});	

			for (Expiration expiration : expirationsToSort) {
				expirationResults.add(toExpirationWithActivitiesResult(expiration));
			}
			taskOfficeExpirationsResult.setExpirations(expirationResults);
		}
		return taskOfficeExpirationsResult;
	}

	/**
	 * @param taskOfficeExpirationsResult
	 * @return
	 */
	public TaskOfficeExpirations toTaskOfficeExpirations(TaskOfficeExpirationsResult taskOfficeExpirationsResult) {

		TaskOfficeExpirations taskOfficeExpirations = new TaskOfficeExpirations();

		taskOfficeExpirations.setDescription(taskOfficeExpirationsResult.getDescription());
		taskOfficeExpirations.setIdTaskTemplate(taskOfficeExpirationsResult.getIdTaskTemplate());
		taskOfficeExpirations.setTotalCompleted(taskOfficeExpirationsResult.getTotalCompleted());
		taskOfficeExpirations.setTotalExpirations(taskOfficeExpirationsResult.getTotalExpirations());

		taskOfficeExpirations.setExpirationDate(taskOfficeExpirationsResult.getExpirationDate());
		taskOfficeExpirations.setTask(toTask(taskOfficeExpirationsResult.getTask()));
		taskOfficeExpirations.setOffice(toOffice(taskOfficeExpirationsResult.getOffice()));
		taskOfficeExpirations.setStatusExpirationOnChange(taskOfficeExpirationsResult.getStatusExpirationOnChange());

		if (!isEmpty(taskOfficeExpirationsResult.getExpirations())) {
			List<Expiration> expirations = new ArrayList<>();
			for (ExpirationResult expirationResult : taskOfficeExpirationsResult.getExpirations()) {
				expirations.add(toExpiration(expirationResult));
			}
			taskOfficeExpirations.setExpirations(new HashSet<>(expirations));
		}
		return taskOfficeExpirations;
	}

	public Expiration toExpiration(ExpirationResult expirationResult) {
		Expiration expiration = new Expiration();

		expiration.setIdExpiration(expirationResult.getIdExpiration());
		expiration.setExpirationClosableBy(Integer.valueOf(expirationResult.getExpirationClosableBy()));
		expiration.setUsername(expirationResult.getUsername());
		expiration.setExpirationDate(expirationResult.getExpirationDate());
		expiration.setCompleted(expirationResult.getCompleted());
		expiration.setApproved(expirationResult.getApproved());
		expiration.setRegistered(expirationResult.getRegistered());
		expiration.setStatusExpirationOnChange(expirationResult.getStatusExpirationOnChange());
		expiration.setUserRelationType(expirationResult.getUserRelationType());

		if (!isEmpty(expirationResult.getOffice())) {
			expiration.setOffice(toOffice(expirationResult.getOffice()));	
		}
		if (!isEmpty(expirationResult.getTask())) {
			expiration.setTask(toTask(expirationResult.getTask()));	
		}
		if (!isEmpty(expirationResult.getTaskTemplate())) {
			expiration.setTaskTemplate(toTaskTemplate(expirationResult.getTaskTemplate()));	
		}
		return expiration;
	}

	public ExpirationResult toExpirationResult(Expiration expiration) {

		ExpirationResult expirationResult = new ExpirationResult();
		expirationResult.setIdExpiration(expiration.getIdExpiration());
		// rfratti edited after converting Expiration.expirationClosableBy into Integer
		// (as DB)
		expirationResult.setExpirationClosableBy(expiration.getExpirationClosableBy() + "");
		expirationResult.setUsername(expiration.getUsername());
		expirationResult.setExpirationDate(expiration.getExpirationDate());
		expirationResult.setCompleted(expiration.getCompleted());
		expirationResult.setApproved(expiration.getApproved());
		expirationResult.setRegistered(expiration.getRegistered());
		expirationResult.setOffice(toOfficeWithCompanyResult(expiration.getOffice()));
		expirationResult.setTask(toTaskResultOnly(expiration.getTask()));
		expirationResult.setTaskTemplate(toTaskTemplateResult(expiration.getTaskTemplate()));
		expirationResult.setExpirationDetail(UtilStatic.buildExpirationDetail(expiration));
		expirationResult.setUserRelationType(expiration.getUserRelationType());

		return expirationResult;
	}

	public ExpirationResult toExpirationWithActivitiesResult(Expiration expiration) {

		ExpirationResult expirationResult = toExpirationResult(expiration);

		expirationResult.setExpirationActivities(toExpirationActivityResult(expiration.getExpirationActivities()));

		return expirationResult;
	}

	public ExpirationActivityResult toExpirationActivitySingleResult(ExpirationActivity expirationActivity) {
		ExpirationActivityResult expirationActivityResult = new ExpirationActivityResult();

		expirationActivityResult.setIdExpirationActivity(expirationActivity.getIdExpirationActivity());
		expirationActivityResult.setBody(expirationActivity.getBody());
		expirationActivityResult.setDescriptionLastActivity(UtilStatic.buildDescriptionLastActivity(expirationActivity));

		return expirationActivityResult;
	}

	public List<ExpirationActivityResult> toExpirationActivityResult(Set<ExpirationActivity> expirationActivities) {
		List<ExpirationActivityResult> expirationActivityResults = new ArrayList<>();

		if (!isEmpty(expirationActivities)) {

			// sort activities
			List<ExpirationActivity> expirationActivitiesToSort = new ArrayList<>(expirationActivities);
			Collections.sort(expirationActivitiesToSort, new Comparator<ExpirationActivity>() {
				@Override
				public int compare(ExpirationActivity ea1, ExpirationActivity ea2) {
					if (isEmpty(ea1.getModifiedBy())) return -1;
					if (isEmpty(ea2.getModifiedBy())) return 1;

					return ea2.getModificationDate().before(ea1.getModificationDate()) ? -1 : 1;
				}
			});	

			for (ExpirationActivity expirationActivity : expirationActivitiesToSort) {

				ExpirationActivityResult expirationActivityResult = new ExpirationActivityResult();

				expirationActivityResult.setIdExpirationActivity(expirationActivity.getIdExpirationActivity());
				expirationActivityResult.setBody(expirationActivity.getBody());
				expirationActivityResult.setDescriptionLastActivity(UtilStatic.buildDescriptionLastActivity(expirationActivity));
				if (!isEmpty(expirationActivity.getExpirationActivityAttachments())) {
					List<ExpirationActivityAttachmentResult> expirationActivityAttachmentResults = new ArrayList<>();
					for (ExpirationActivityAttachment expirationActivityAttachment : expirationActivity.getExpirationActivityAttachments()) {
						expirationActivityAttachmentResults.add(toExpirationActivityAttachmentResult(expirationActivityAttachment));
					}
					expirationActivityResult.setExpirationActivityAttachments(expirationActivityAttachmentResults);
				}
				expirationActivityResults.add(expirationActivityResult);
			}
		}
		return expirationActivityResults;
	}

	public ExpirationActivity toExpirationActivity(ExpirationActivityResult expirationActivityResult) {
		ExpirationActivity expirationActivity = new ExpirationActivity();

		expirationActivity.setIdExpirationActivity(expirationActivityResult.getIdExpirationActivity());
		expirationActivity.setBody(expirationActivityResult.getBody());
		expirationActivity.setExpiration(toExpiration(expirationActivityResult.getExpiration()));
		if (!isEmpty(expirationActivityResult.getExpirationActivityAttachments())) {
			List<ExpirationActivityAttachment> expirationActivityAttachments = new ArrayList<>();
			for (ExpirationActivityAttachmentResult expirationActivityAttachmentResult : expirationActivityResult.getExpirationActivityAttachments()) {
				expirationActivityAttachments.add(toExpirationActivityAttachment(expirationActivityAttachmentResult));
			}
			expirationActivity.setExpirationActivityAttachments(new HashSet<ExpirationActivityAttachment>(expirationActivityAttachments));
		}
		return expirationActivity;
	}

	public ExpirationActivityAttachmentResult toExpirationActivityAttachmentResult(ExpirationActivityAttachment expirationActivityAttachment) {
		ExpirationActivityAttachmentResult expirationActivityAttachmentResult = new ExpirationActivityAttachmentResult();

		expirationActivityAttachmentResult.setFileName(expirationActivityAttachment.getFileName());
		expirationActivityAttachmentResult.setFilePath(expirationActivityAttachment.getFilePath());
		expirationActivityAttachmentResult.setFileSize(expirationActivityAttachment.getFileSize());
		expirationActivityAttachmentResult.setFileType(expirationActivityAttachment.getFileType());

		return expirationActivityAttachmentResult;
	}

	public ExpirationActivityAttachment toExpirationActivityAttachment(ExpirationActivityAttachmentResult expirationActivityAttachmentResult) {
		ExpirationActivityAttachment expirationActivityAttachment = new ExpirationActivityAttachment();

		expirationActivityAttachment.setIdExpirationActivityAttachment(expirationActivityAttachmentResult.getIdExpirationActivityAttachment());
		expirationActivityAttachment.setFileName(expirationActivityAttachmentResult.getFileName());
		expirationActivityAttachment.setFilePath(expirationActivityAttachmentResult.getFilePath());
		expirationActivityAttachment.setFileType(expirationActivityAttachmentResult.getFileType());
		expirationActivityAttachment.setFileSize(expirationActivityAttachmentResult.getFileSize());
		return expirationActivityAttachment;
	}
}
