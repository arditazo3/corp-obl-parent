package com.tx.co.back_office.tasktemplate.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.api.model.ObjectSearchTaskTemplate;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplate.repository.TaskTemplateRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
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
			tasks = covertToTaskForTable(taskTemplateRepository.findAllOrderByDescriptionAsc());
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			// FIXME
			return new ArrayList<>();
		}
		return tasks;
	}

	public List<Task> covertToTaskForTable(List<TaskTemplate> taskTemplates) {

		List<Task> tasks = new ArrayList<>();
		for (TaskTemplate taskTemplate : taskTemplates) {

			if(!isEmpty(taskTemplate.getTasks())) {
				for (Task taskLoop : taskTemplate.getTasks()) {
					tasks.add(taskLoop);
				}
			} else {
				Task task = new Task();
				task.setTaskTemplate(taskTemplate);
				tasks.add(task);
			}
		}
		return tasks;
	}

	@Override
	public List<Task> searchTaskTemplate(ObjectSearchTaskTemplate objectSearchTaskTemplate) {
		
		
		String querySql = "select tt from TaskTemplate tt "
				+ "left join tt.topic top "
				+ "left join top.companyTopic ct "
				+ "left join ct.company c "
				+ "where tt.enabled <> 0 and top.enabled <> 0 and ct.enabled <> 0 and c.enabled <> 0";
		Query query;
		
		if(isEmpty(objectSearchTaskTemplate.getCompanies()) && isEmpty(objectSearchTaskTemplate.getTopics())) {
			querySql += "and tt.description like :description "
					+ "group by tt.idTaskTemplate "
					+ "order by tt.description asc";
			query = em.createQuery(querySql);
			
			query.setParameter("description", "%" + objectSearchTaskTemplate.getDescriptionTaskTemplate() + "%");
		} else {
			querySql += "and tt.description like :description and "
					+ "(top in :topicsList and c in :companiesList) "
					+ "group by tt.idTaskTemplate "
					+ "order by tt.description asc";
			query = em.createQuery(querySql);
			
			query.setParameter("description", "%" + objectSearchTaskTemplate.getDescriptionTaskTemplate() + "%");
			query.setParameter("topicsList", objectSearchTaskTemplate.getTopics());
			query.setParameter("companiesList", objectSearchTaskTemplate.getCompanies());
		}
		return covertToTaskForTable(query.getResultList());
	}
}
