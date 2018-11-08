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
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;

import org.apache.commons.collections4.CollectionUtils;
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
import com.tx.co.back_office.task.service.ITaskService;
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
	private ITaskService taskService;

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

	@Autowired
	public void setTaskService(ITaskService taskService) {
		this.taskService = taskService;
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
	//	taskTemplateStored.setTaskTemplateAttachments(taskTemplate.getTaskTemplateAttachments());

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
			tasks = convertToTaskForTable(taskTemplateRepository.findAllOrderByDescriptionAsc(), new ArrayList<>());
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			tasks = convertToTaskForTable(taskTemplateRepository.getTaskTemplatesByRole(username), new ArrayList<>());
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

		String querySql = "select tt from TaskTemplate tt " + 
				"left join tt.topic t " + 
				"left join t.companyTopic ct " + 
				"left join ct.company c " + 
				"left join c.companyUsers cu " + 
				"left join cu.user u on cu.username = u.username " +
				"where tt.enabled <> 0 ";

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			querySql += "and u.username = :username ";
		}

		Query query;

		if(isEmpty(objectSearchTaskTemplate.getCompanies()) && isEmpty(objectSearchTaskTemplate.getTopics())) {


			querySql += "and tt.description like :description "
					+ "group by tt.idTaskTemplate order by tt.description asc";
			query = em.createQuery(querySql);

			query.setParameter(DESCRIPTION_QUERY_PARAM, "%" + objectSearchTaskTemplate.getDescriptionTaskTemplate() + "%");
		} else {
			if(!isEmpty(objectSearchTaskTemplate.getCompanies())) {
				querySql += "and c in :companiesList ";
			}
			if(!isEmpty(objectSearchTaskTemplate.getTopics())) {
				querySql += "and t in :topicsList ";
			}
			
			querySql += "and tt.description like :description  "
					+ "group by tt.idTaskTemplate order by tt.description asc";
		}
		
		query = em.createQuery(querySql, TaskTemplate.class);

		query.setParameter(DESCRIPTION_QUERY_PARAM, "%" + objectSearchTaskTemplate.getDescriptionTaskTemplate() + "%");
		
		if(!isEmpty(objectSearchTaskTemplate.getTopics())) {
			query.setParameter("topicsList",   objectSearchTaskTemplate.getTopics());
		}

		if(!isEmpty(objectSearchTaskTemplate.getCompanies())) {
			query.setParameter("companiesList", objectSearchTaskTemplate.getCompanies());	
		}

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			query.setParameter("username", username);
		}

		return convertToTaskForTable(query.getResultList(), objectSearchTaskTemplate.getCompanies());
	}

	public List<Task> convertToTaskForTable(List<TaskTemplate> taskTemplates, List<Company> getCompanies) {

		User userLoggedIn = getTokenUserDetails().getUser();
		String lang = userLoggedIn.getLang();

		List<Task> tasks = new ArrayList<>();
		for (TaskTemplate taskTemplate : taskTemplates) {

			int index = 1;
			if(!isEmpty(taskTemplate.getTasks())) {
				for (Task taskLoop : taskTemplate.getTasks()) {

					List<Company> coumpanyCounterList = new ArrayList<>();
					
					for (TaskOffice taskOfficeLoop : taskLoop.getTaskOfficesFilterEnabled()) {
						coumpanyCounterList.add(taskOfficeLoop.getOffice().getCompany());
					}
					List<Company> uniqueCompanies = coumpanyCounterList.stream()
							.collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(Company::getIdCompany))), ArrayList::new));

					if(!isEmpty(getCompanies) && !CollectionUtils.containsAny(uniqueCompanies, getCompanies)) {  
						continue;
					}
					
					taskLoop.setCounterCompany(uniqueCompanies.size());

					String descriptionTask = buildDescription(taskLoop, lang, index);

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

		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();

		String querySql = "select tt from TaskTemplate tt ";

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_INLAND) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			querySql += "left join tt.topic t " + 
					"left join t.topicConsultants tc " + 
					"left join tc.companyConsultant cc " + 
					"left join cc.company c " + 
					"left join c.companyUsers cu " + 
					"left join cu.user u on cu.username = u.username " +
					"where tt.enabled <> 0 " +
					"and u.username = :username " + 
					"and ";
		} else {
			querySql += "where ";
		}

		Query query;

		querySql += "tt.description like :description " +
				"group by tt.idTaskTemplate " + 
				"order by tt.description asc ";
		query = em.createQuery(querySql);

		query.setParameter(DESCRIPTION_QUERY_PARAM, "%" + description + "%");
		
		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_INLAND) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			query.setParameter("username", username);
		}

		return setDescriptionTaskTemplate(query.getResultList());
	}

	private List<TaskTemplate> setDescriptionTaskTemplate(List<TaskTemplate> taskTemplates) {

		User userLoggedIn = getTokenUserDetails().getUser();
		String lang = userLoggedIn.getLang();

		if(!isEmpty(taskTemplates)) {
			
			/*
			 * Remove task template that doesn't contain only one task
			 * Relation one to one
			 * */
			taskTemplates = taskTemplates.stream()
    				.filter(taskTemplate -> ( !isEmpty(taskTemplate.getTasks()) && taskTemplate.getTasks().size() == 1 )).collect(Collectors.toList());
    				
			for (TaskTemplate taskTemplate : taskTemplates) {

				String descriptionTaskTemplate = buildDescription(taskTemplate, lang, 0);

				taskTemplate.setDescriptionTaskTemplate(descriptionTaskTemplate);

				List<Office> officeCounterList = new ArrayList<>();

				if(!isEmpty(taskTemplate.getTasks())) {
					
					Task singleTask = taskTemplate.getTasks().iterator().next();

					for (TaskOffice taskOfficeLoop : singleTask.getTaskOfficesFilterEnabled()) {
						officeCounterList.add(taskOfficeLoop.getOffice());
					}

					List<Office> uniqueOffices = officeCounterList.stream()
							.collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(Office::getIdOffice))), ArrayList::new));

					taskTemplate.setCounterOffices(uniqueOffices.size());
				}
			}
		}

		logger.info("Number of items: " + taskTemplates.size());

		return taskTemplates;
	}

	@Override
	public void deleteTaskTemplate(TaskTemplate taskTemplate) {

		try {
			Long idTaskTemplate = taskTemplate.getIdTaskTemplate();
			
    		logger.info("Deleting the TaskTemplate with id: " + idTaskTemplate);
    		
    		
    		Optional<TaskTemplate> taskTemplateOptional = findByIdTaskTemplate(idTaskTemplate);

    		if(!taskTemplateOptional.isPresent()) {
    			throw new NotFoundException();
    		}

    		// The modification of User
    		String username = getTokenUserDetails().getUser().getUsername();

    		TaskTemplate taskTemplateStored = taskTemplateOptional.get();
    		// disable the taskTemplateStored
    		taskTemplateStored.setEnabled(false);
    		taskTemplateStored.setModificationDate(new Date());
    		taskTemplateStored.setModifiedBy(username);

    		taskTemplateRepository.save(taskTemplateStored);

    		if(!isEmpty(taskTemplateStored.getTasks())) {
    			for (Task task : taskTemplateStored.getTasks()) {
    				taskService.deleteTask(task);
				}
    		}
    		
    		logger.info("Delete the Task Template with id: " + idTaskTemplate);
    	} catch (Exception e) {
    		logger.error(e);
    		throw new GeneralException("Task Template not found");
    	}
	}
	
	/**
	 * @param object
	 * @param lang
	 * @param index
	 * @return the description
	 */
	public String buildDescription(Object object, String lang, int index) {
		
		String description = "";
		
		if(!isEmpty(object)) {
			if(object instanceof Task) {
				Task task = (Task) object;
				
				description = getTranslationByLangLikeTablename(new TranslationPairKey("configurationinterval", lang)).getDescription();

				if(index != 0) {
					description += " " + index + ": ";
				}

				description += getTranslationByLangLikeTablename(new TranslationPairKey(task.getRecurrence(), lang)).getDescription() + " - ";

				description += getTranslationByLangLikeTablename(new TranslationPairKey(task.getExpirationType(), lang)).getDescription();
			
				if (task.getExpirationType().compareTo(EXP_FIX_DAY) == 0 && !isEmpty(task.getDay())) {
					if(task.getRecurrence().compareTo(REC_YEARLY) == 0) {
						String dayString = task.getDay().toString();
						if(dayString.length() == 8) {
							String[] splitYearMonthDay = {dayString.substring(0, 4), dayString.substring(4, 6), dayString.substring(6)};
							description += " - " + splitYearMonthDay[2] + "/" + splitYearMonthDay[1];
						}
					} else if(task.getRecurrence().compareTo(REC_WEEKLY) == 0) {
						description += " - " + getTranslationByLangLikeTablename(new TranslationPairKey("period_weekly_exp_fixed_day#".concat(task.getDay().toString()), lang)).getDescription();
					} else {
						description += " - " + task.getDay();
					}
				}
			} else if(object instanceof TaskTemplate) {
				TaskTemplate taskTemplate = (TaskTemplate) object;
				
				description = taskTemplate.getDescription() + " - ";

				description += getTranslationByLangLikeTablename(new TranslationPairKey(taskTemplate.getExpirationType(), lang)).getDescription();
				
				if (taskTemplate.getExpirationType().compareTo(EXP_FIX_DAY) == 0) {
					if(taskTemplate.getRecurrence().compareTo(REC_YEARLY) == 0) {
						String dayString = taskTemplate.getDay().toString();
						if(dayString.length() == 8) {
							String[] splitYearMonthDay = {dayString.substring(0, 4), dayString.substring(4, 6), dayString.substring(6)};
							description += " - " + splitYearMonthDay[2] + "/" + splitYearMonthDay[1];
						}
					} else if(taskTemplate.getRecurrence().compareTo(REC_WEEKLY) == 0) {
						description += " - " + getTranslationByLangLikeTablename(new TranslationPairKey("period_weekly_exp_fixed_day#".concat(taskTemplate.getDay().toString()), lang)).getDescription();
					} else {
						description += " - " + taskTemplate.getDay();
					}
				}
			}
		}
		
		return description;
	}
}
