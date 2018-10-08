package com.tx.co.back_office.tasktemplate.service;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.tasktemplate.api.model.ObjectSearchTaskTemplate;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplate.repository.TaskTemplateRepository;
import com.tx.co.back_office.topic.domain.Topic;
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
	private EntityManager em;

	@Autowired
	public void setTaskTemplateRepository(TaskTemplateRepository taskTemplateRepository) {
		this.taskTemplateRepository = taskTemplateRepository;
	}

	@Autowired
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public TaskTemplate saveUpdateTaskTemplate(TaskTemplate taskTemplate) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TaskTemplate taskTemplateStored = null;

		// New Task template
		if(isEmpty(taskTemplate.getIdTaskTemplate())) {
			taskTemplate.setCreationDate(new Date());
			taskTemplate.setCreatedBy(username);
			taskTemplate.setEnabled(true);
			taskTemplateStored = taskTemplate;
		} else { // Existing Task template
			taskTemplateStored = getTaskTemplateById(taskTemplate.getIdTaskTemplate());

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

		updateTaskTemplateCache(taskTemplateStored, false);

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
		List<Task> tasks = new ArrayList<>();

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			tasks = convertToTaskForTable(taskTemplateRepository.findAllOrderByDescriptionAsc());
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			List<String> authorities = new ArrayList<>();
			for (Authority authority : userLoggedIn.getAuthorities()) {
				authorities.add(authority.name());
			}
			tasks = convertToTaskForTable(taskTemplateRepository.getTaskTemplatesByRole(authorities));
		}
		return tasks;
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
					
					descriptionTask += getTranslationByLangLikeTablename(new TranslationPairKey(taskLoop.getExpirationType(), lang)).getDescription() + " - " + String.valueOf(taskLoop.getDay());
					
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
		return tasks;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> searchTaskTemplate(ObjectSearchTaskTemplate objectSearchTaskTemplate) {

		User userLoggedIn = getTokenUserDetails().getUser();
		List<String> authorities = new ArrayList<>();

		String querySql = "select tt.* from co_tasktemplate tt " + 
				"left join co_topic t on tt.topic_id = t.id " + 
				"left join co_topicconsultant tc on t.id = tc.topic_id " + 
				"left join co_companyconsultant cc on tc.consultantcompany_id = cc.id " + 
				"left join co_company c on cc.company_id = c.id " + 
				"left join co_companyuser cu on c.id = cu.company_id " + 
				"left join co_user u on cu.username = u.username " +
				"left join co_userrole ur on u.username = ur.username " + 
				"where tt.enabled <> 0 ";

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			querySql += "and ur.roleuid in (:authorities) ";

			for (Authority authority : userLoggedIn.getAuthorities()) {
				authorities.add(authority.name());
			}
		}

		Query query;

		if(isEmpty(objectSearchTaskTemplate.getCompanies()) && isEmpty(objectSearchTaskTemplate.getTopics())) {


			querySql += "and tt.description like :description "
					+ "group by tt.id order by tt.description asc";
			query = em.createQuery(querySql);

			query.setParameter("description", "%" + objectSearchTaskTemplate.getDescriptionTaskTemplate() + "%");
		} else if(isEmpty(objectSearchTaskTemplate.getCompanies()) || isEmpty(objectSearchTaskTemplate.getTopics())) {
			throw new GeneralException("Fill in all the fields");
		} else {
			querySql += "and tt.description like :description and "
					+ "(t.id in :topicsList and c.id in :companiesList) "
					+ "group by tt.id order by tt.description asc";
			query = em.createNativeQuery(querySql, TaskTemplate.class);

			query.setParameter("description", "%" + objectSearchTaskTemplate.getDescriptionTaskTemplate() + "%");
			query.setParameter("topicsList",   objectSearchTaskTemplate.getTopics().stream()
					.map(Topic::getIdTopic).collect(Collectors.toList()));
			
			query.setParameter("companiesList", objectSearchTaskTemplate.getCompanies().stream()
					.map(Company::getIdCompany).collect(Collectors.toList()));
		}

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			query.setParameter("authorities", authorities);
		}

		return convertToTaskForTable(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskTemplate> searchTaskTemplateByDescr(String description) {

		String querySql = "select tt from TaskTemplate tt "
				+ "where tt.enabled <> 0 ";
		Query query;

		if(!isEmpty(description)) {
			querySql += "and tt.description like :description "
					+ "order by tt.description asc";
			query = em.createQuery(querySql);

			query.setParameter("description", "%" + description + "%");
		} else {
			return new ArrayList<>();
		}
		return query.getResultList();
	}
}
