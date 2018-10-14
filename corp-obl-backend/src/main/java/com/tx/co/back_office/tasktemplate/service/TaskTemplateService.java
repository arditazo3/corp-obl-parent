package com.tx.co.back_office.tasktemplate.service;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static org.springframework.util.ObjectUtils.isEmpty;
import static com.tx.co.common.constants.AppConstants.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.repository.TaskOfficeRepository;
import com.tx.co.back_office.tasktemplate.api.model.ObjectSearchTaskTemplate;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplate.repository.TaskTemplateRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.common.translation.api.model.TranslationPairKey;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.exception.GeneralException;
import com.tx.co.user.domain.User;

/**
 * Service for {@link com.tx.co.back_office.tasktemplate.domain.TaskTemplate}s.
 *
 * @author aazo
 */
@Service
public class TaskTemplateService extends UpdateCacheData implements ITaskTemplateService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(TaskTemplateService.class);

	private TaskTemplateRepository taskTemplateRepository;
	private TaskOfficeRepository taskOfficeRepository;
	private EntityManager em;

	@Autowired
	public void setTaskTemplateRepository(TaskTemplateRepository taskTemplateRepository) {
		this.taskTemplateRepository = taskTemplateRepository;
	}

	@Autowired
	public void setTaskOfficeRepository(TaskOfficeRepository taskOfficeRepository) {
		this.taskOfficeRepository = taskOfficeRepository;
	}

	@Autowired
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public TaskTemplate saveUpdateTaskTemplate(TaskTemplate taskTemplate, Office office) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TaskTemplate taskTemplateStored = null;

		// New Task template
		if(isEmpty(taskTemplate.getIdTaskTemplate())) {
			taskTemplate.setCreationDate(new Date());
			taskTemplate.setCreatedBy(username);
			taskTemplate.setEnabled(true);
			taskTemplateStored = taskTemplate;
			
			logger.info("Creating the new Task template");
		} else { // Existing Task template
			taskTemplateStored = getTaskTemplateById(taskTemplate.getIdTaskTemplate());

			if(!isEmpty(taskTemplate.getEnabled())) {
				taskTemplateStored.setEnabled(taskTemplate.getEnabled());	
			}
			
			logger.info("Updating the Task template with id: " + taskTemplateStored.getIdTaskTemplate());
		}

		taskTemplateStored.setDescription(taskTemplate.getDescription());
		taskTemplateStored.setDay(taskTemplate.getDay());
		taskTemplateStored.setDaysBeforeShowExpiration(taskTemplate.getDaysBeforeShowExpiration());
		taskTemplateStored.setDaysOfNotice(taskTemplate.getDaysOfNotice());
		taskTemplateStored.setExpirationClosableBy(taskTemplate.getExpirationClosableBy());
		taskTemplateStored.setExpirationType(taskTemplate.getExpirationType());
		taskTemplateStored.setFrequenceOfNotice(taskTemplate.getFrequenceOfNotice());
		taskTemplateStored.setRecurrence(taskTemplate.getRecurrence());
		taskTemplateStored.setTopic(taskTemplate.getTopic());
		taskTemplateStored.setModificationDate(new Date());
		taskTemplateStored.setModifiedBy(username);

		taskTemplateStored = taskTemplateRepository.save(taskTemplateStored);

		// Came from office task template, relation one to one task template - task
		if (!isEmpty(office)) {
			TaskOffice taskOffice = taskOfficeRepository.getTaskOfficeByTaskTemplateAndOffice(taskTemplateStored, office);
			if (!isEmpty(taskOffice)) {
				taskTemplateStored.getTasks().add(taskOffice.getTask());
			}
		}

		updateTaskTemplateCache(taskTemplateStored, false);

		logger.info("Stored the office with id: " + taskTemplateStored.getIdTaskTemplate());
		
		return taskTemplateStored;
	}



	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public Optional<TaskTemplate> findByIdTaskTemplate(Long idTaskTemplate) {
		return taskTemplateRepository.findById(idTaskTemplate);
	}

	@Override
	public List<Task> getTasksForTable() {

		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();
		List<Task> tasks = new ArrayList<>();

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			tasks = convertToTaskForTable(taskTemplateRepository.findAllOrderByDescriptionAsc());
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			tasks = convertToTaskForTable(taskTemplateRepository.getTaskTemplatesByRole(username));
		}
		
		logger.info("Tasks for table - Number of items: " + tasks.size());
		
		return tasks;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> searchTaskTemplate(ObjectSearchTaskTemplate objectSearchTaskTemplate) {

		logger.info("Searching Task Templates");
		
		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();
		List<String> authorities = new ArrayList<>();

		String querySql = "select tt from TaskTemplate tt " + 
				"left join tt.topic t " + 
				"left join t.topicConsultants tc " + 
				"left join tc.companyConsultant cc " + 
				"left join cc.company c " + 
				"left join c.companyUsers cu " + 
				"left join cu.user u on cu.username = u.username " +
				"where tt.enabled <> 0 ";

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			querySql += "and u.username = :username ";

			for (Authority authority : userLoggedIn.getAuthorities()) {
				authorities.add(authority.name());
			}
		}

		Query query;

		if(isEmpty(objectSearchTaskTemplate.getCompanies()) && isEmpty(objectSearchTaskTemplate.getTopics())) {


			querySql += "and tt.description like :description "
					+ "group by tt.idTaskTemplate order by tt.description asc";
			query = em.createQuery(querySql);

			query.setParameter(DESCRIPTION_QUERY_PARAM, "%" + objectSearchTaskTemplate.getDescriptionTaskTemplate() + "%");
		} else if(isEmpty(objectSearchTaskTemplate.getCompanies()) || isEmpty(objectSearchTaskTemplate.getTopics())) {
			throw new GeneralException("Fill in all the fields");
		} else {
			querySql += "and tt.description like :description and "
					+ "(t in :topicsList and c in :companiesList) "
					+ "group by tt.idTaskTemplate order by tt.description asc";
			query = em.createQuery(querySql, TaskTemplate.class);

			query.setParameter(DESCRIPTION_QUERY_PARAM, "%" + objectSearchTaskTemplate.getDescriptionTaskTemplate() + "%");
			query.setParameter("topicsList",   objectSearchTaskTemplate.getTopics());

			query.setParameter("companiesList", objectSearchTaskTemplate.getCompanies());
		}

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			query.setParameter("username", username);
		}

		return convertToTaskForTable(query.getResultList());
	}

	public List<Task> convertToTaskForTable(List<TaskTemplate> taskTemplates) {

		User userLoggedIn = getTokenUserDetails().getUser();
		String lang = userLoggedIn.getLang();

		List<Task> tasks = new ArrayList<>();
		for (TaskTemplate taskTemplate : taskTemplates) {

			int index = 1;
			if(!isEmpty(taskTemplate.getTasks())) {
				for (Task taskLoop : taskTemplate.getTasks()) {

					List<Company> coumpanyCounterList = new ArrayList<>();
					for (TaskOffice taskOfficeLoop : taskLoop.getTaskOffices()) {
						coumpanyCounterList.add(taskOfficeLoop.getOffice().getCompany());
					}
					List<Company> uniqueCompanies = coumpanyCounterList.stream()
							.collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(Company::getIdCompany))), ArrayList::new));

					taskLoop.setCounterCompany(uniqueCompanies.size());

					String descriptionTask = getTranslationByLangLikeTablename(new TranslationPairKey("configurationinterval", lang)).getDescription();

					descriptionTask += String.valueOf(index) + ": ";

					descriptionTask += getTranslationByLangLikeTablename(new TranslationPairKey(taskLoop.getRecurrence(), lang)).getDescription() + " - ";

					descriptionTask += getTranslationByLangLikeTablename(new TranslationPairKey(taskLoop.getExpirationType(), lang)).getDescription() + " - " + taskLoop.getDay();

					taskLoop.setDescriptionTask(descriptionTask);

					tasks.add(taskLoop);

					index++;
				}
			} else {
				Task task = new Task();
				task.setTaskTemplate(taskTemplate);
				tasks.add(task);
			}
		}
		
		logger.info("Number of items: " + tasks.size());
		
		return tasks;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TaskTemplate> searchTaskTemplateByDescr(String description) {

		logger.info("Searching Office Task Templates by Description");
		
		if(isEmpty(description)) {
			description = "";
		}
		
		String querySql = "select tt from TaskTemplate tt "
				+ "where tt.enabled <> 0 ";
		Query query;

			querySql += "and tt.description like :description "
					+ "order by tt.description asc";
			query = em.createQuery(querySql);

			query.setParameter(DESCRIPTION_QUERY_PARAM, "%" + description + "%");
		return setDescriptionTaskTemplate(query.getResultList());
	}

	private List<TaskTemplate> setDescriptionTaskTemplate(List<TaskTemplate> taskTemplates) {
		
		User userLoggedIn = getTokenUserDetails().getUser();
		String lang = userLoggedIn.getLang();
		
		if(!isEmpty(taskTemplates)) {
			for (TaskTemplate taskTemplate : taskTemplates) {

				String descriptionTaskTemplate = taskTemplate.getDescription() + " - ";

				descriptionTaskTemplate += getTranslationByLangLikeTablename(new TranslationPairKey(taskTemplate.getExpirationType(), lang)).getDescription() + " - " + taskTemplate.getDay();

				taskTemplate.setDescriptionTaskTemplate(descriptionTaskTemplate);

				List<Office> officeCounterList = new ArrayList<>();
				
				Task singleTask = taskTemplate.getTasks().iterator().next();
				
				for (TaskOffice taskOfficeLoop : singleTask.getTaskOffices()) {
					officeCounterList.add(taskOfficeLoop.getOffice());
				}
				
				List<Office> uniqueOffices = officeCounterList.stream()
						.collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(Office::getIdOffice))), ArrayList::new));

				taskTemplate.setCounterOffices(uniqueOffices.size());
			}
		}
		
		logger.info("Number of items: " + taskTemplates.size());
		
		return taskTemplates;
	}
	
	@Override
	public void deleteTaskTemplate(TaskTemplate taskTemplate) {

		taskTemplate.setEnabled(false);

		logger.info("Deleted the TaskTemplate with id: " + taskTemplate.getIdTaskTemplate());
		
		saveUpdateTaskTemplate(taskTemplate, null);
	}
}
