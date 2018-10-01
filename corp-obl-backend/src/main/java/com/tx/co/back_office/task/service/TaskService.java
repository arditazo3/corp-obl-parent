package com.tx.co.back_office.task.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.service.ICompanyService;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.model.TaskOfficeRelations;
import com.tx.co.back_office.task.repository.TaskOfficeRelationRepository;
import com.tx.co.back_office.task.repository.TaskOfficeRepository;
import com.tx.co.back_office.task.repository.TaskRepository;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.service.ITopicService;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.user.domain.User;

/**
 * Service for {@link TaskService}s.
 *
 * @author aazo
 */
@Service
public class TaskService extends UpdateCacheData implements ITaskService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(TaskService.class);

	private TaskRepository taskRepository;
	private TaskOfficeRepository taskOfficeRepository;
	private TaskOfficeRelationRepository taskOfficeRelationRepository;
	private ICompanyService companyService;
	private ITopicService topicService;

	@Autowired
	public void setTaskRepository(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Autowired
	public void setCompanyService(ICompanyService companyService) {
		this.companyService = companyService;
	}

	@Autowired
	public void setTopicService(ITopicService topicService) {
		this.topicService = topicService;
	}

	@Autowired
	public void setTaskOfficeRepository(TaskOfficeRepository taskOfficeRepository) {
		this.taskOfficeRepository = taskOfficeRepository;
	}

	@Autowired
	public void setTaskOfficeRelationRepository(TaskOfficeRelationRepository taskOfficeRelationRepository) {
		this.taskOfficeRelationRepository = taskOfficeRelationRepository;
	}

	@Override
	public List<Task> getTasksByDescriptionOrCompaniesOrTopics(String description, List<Company> companies,
			List<Topic> topics) {

		return null;
	}

	@Override
	public List<Task> getTasks() {

		User userLoggedIn = getTokenUserDetails().getUser();

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			return taskRepository.getTasks();
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			List<Company> companies = companyService.getCompaniesByRole();
			List<Topic> topics = topicService.getTopicsByRole();
			// FIXME
			return new ArrayList<>();
		}
		return new ArrayList<>();
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public Task saveUpdateTask(Task task) {
		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		Task taskStored = null;

		// New Task template
		if(isEmpty(task.getIdTask())) {
			task.setCreationDate(new Date());
			task.setCreatedBy(username);
			task.setEnabled(true);
			taskStored = task;
		} else { // Existing Task template
			taskStored = taskRepository.findById(task.getIdTask()).get();
			//			taskStored = getTaskById(task.getIdTask());
		}

		taskStored.setDay(task.getDay());
		taskStored.setDaysBeforeShowExpiration(task.getDaysBeforeShowExpiration());
		taskStored.setDaysOfNotice(task.getDaysOfNotice());
		taskStored.setExpirationType(task.getExpirationType());
		taskStored.setFrequenceOfNotice(task.getFrequenceOfNotice());
		taskStored.setRecurrence(task.getRecurrence());
		taskStored.setModificationDate(new Date());
		taskStored.setModifiedBy(username);

		taskStored = taskRepository.save(taskStored);

		taskStored.setTaskOffices(mergeTaskOffice(taskStored, task.getTaskOffices()));

		if(!isEmpty(task.getTaskOffices())) {
			List<TaskOffice> taskOffices = new ArrayList<>();
			for (TaskOffice taskOfficeLoop : task.getTaskOffices()) {

				TaskOffice taskOffice = saveUpdateTaskOffice(taskStored, taskOfficeLoop);
				taskOffices.add(taskOffice);
			}
			taskStored.setTaskOffices(new HashSet<TaskOffice>(taskOffices));
		}

		//	updateTaskCache(taskStored, false);

		return taskStored;
	}

	@Override
	public TaskOffice saveUpdateTaskOffice(Task task, TaskOffice taskOffice) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TaskOffice taskOfficeStored = null;

		// New Task office
		if(isEmpty(taskOffice.getIdTaskOffice())) {
			taskOffice.setCreationDate(new Date());
			taskOffice.setCreatedBy(username);

			taskOfficeStored = taskOffice;
		} else { // Existing Task template
			taskOfficeStored = taskOfficeRepository.findById(taskOffice.getIdTaskOffice()).get();

			//			taskStored = getTaskById(task.getIdTask());
		}

		taskOfficeStored.setTask(task);
		taskOfficeStored.setStartDate(new Date());
		taskOfficeStored.setEndDate(new Date());
		taskOfficeStored.setCreatedBy(username);
		taskOfficeStored.setCreationDate(new Date());
		taskOfficeStored.setModifiedBy(username);
		taskOfficeStored.setModificationDate(new Date());

		taskOfficeStored = taskOfficeRepository.save(taskOfficeStored);
		taskOfficeStored.setTaskOfficeRelations(taskOffice.getTaskOfficeRelations());

		if(!isEmpty(taskOfficeStored.getTaskOfficeRelations())) {
			for (TaskOfficeRelations taskOfficeRelationLoop : taskOfficeStored.getTaskOfficeRelations()) {

				saveUpdateTaskOfficeRelation(taskOfficeRelationLoop, taskOfficeStored);
			}
		}

		return taskOffice;
	}

	@Override
	public TaskOfficeRelations saveUpdateTaskOfficeRelation(TaskOfficeRelations taskOfficeRelation, TaskOffice taskOffice) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TaskOfficeRelations taskOfficeRelationStored = null;

		// New Task office relation
		if(isEmpty(taskOfficeRelation.getIdTaskOfficeRelation())) {
			taskOfficeRelation.setCreationDate(new Date());
			taskOfficeRelation.setCreatedBy(username);
			taskOfficeRelation.setEnabled(true);

			taskOfficeRelationStored = taskOfficeRelation;
		} else { // Existing Task template
			taskOfficeRelationStored = taskOfficeRelationRepository.findById(taskOfficeRelation.getIdTaskOfficeRelation()).get();
			//			taskStored = getTaskById(task.getIdTask());
		}

		taskOfficeRelationStored.setUsername(taskOfficeRelation.getUsername());
		taskOfficeRelationStored.setTaskOffice(taskOffice);
		taskOfficeRelationStored.setRelationType(taskOfficeRelation.getRelationType());
		taskOfficeRelationStored.setModifiedBy(username);
		taskOfficeRelationStored.setModificationDate(new Date());

		taskOfficeRelationStored = taskOfficeRelationRepository.save(taskOfficeRelationStored);

		return taskOfficeRelationStored;
	}

	public Set<TaskOffice> mergeTaskOffice(Task taskStored, Set<TaskOffice> taskOffices) {

		Set<TaskOffice> taskOfficesToSave = new HashSet<>();
		for (TaskOffice taskOffice : taskOffices) {
			for (TaskOffice taskOfficeStored : taskStored.getTaskOffices()) {
				if(isEmpty(taskOffice.getIdTaskOffice()) || 
						(!isEmpty(taskOffice.getIdTaskOffice()) && 
								taskOffice.getIdTaskOffice().compareTo(taskOfficeStored.getIdTaskOffice()) == 0)) {

					if(!isEmpty(taskOfficeStored.getTaskOfficeRelations())) {
						for (TaskOfficeRelations taskOfficeRelationToDelete : taskOfficeStored.getTaskOfficeRelations()) {
							Optional<TaskOfficeRelations> checkIfExist = taskOfficeRelationRepository.findById(taskOfficeRelationToDelete.getIdTaskOfficeRelation());
							if(checkIfExist.isPresent()) {
								taskOfficeRelationRepository.delete(checkIfExist.get());	
							}
						}
					}
					taskOfficesToSave.add(taskOffice);
				}
			}
		}

		taskOfficesToSave.addAll(taskOffices);

		Set<TaskOffice> taskOfficesToRemove = taskStored.getTaskOffices();
		taskOfficesToRemove.removeAll(taskOfficesToSave);
		if(!isEmpty(taskOfficesToRemove)) {
			for (TaskOffice taskOffice : taskOfficesToRemove) {
				taskOfficeRelationRepository.deleteAll(taskOffice.getTaskOfficeRelations());
				taskOffice.setTaskOfficeRelations(new HashSet<>());
			}
			taskOfficeRepository.deleteAll(taskOfficesToRemove);
			taskStored.setTaskOffices(new HashSet<>());
		}


		taskStored.getTaskOffices().addAll(taskOfficesToSave);

		return taskStored.getTaskOffices();
	}
}
