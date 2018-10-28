package com.tx.co.back_office.task.service;

import static org.springframework.util.ObjectUtils.isEmpty;
import static com.tx.co.common.constants.AppConstants.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.model.TaskOfficeRelations;
import com.tx.co.back_office.task.repository.TaskOfficeRelationRepository;
import com.tx.co.back_office.task.repository.TaskOfficeRepository;
import com.tx.co.back_office.task.repository.TaskRepository;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.exception.GeneralException;
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

	@Autowired
	public void setTaskRepository(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Autowired
	public void setTaskOfficeRepository(TaskOfficeRepository taskOfficeRepository) {
		this.taskOfficeRepository = taskOfficeRepository;
	}

	@Autowired
	public void setTaskOfficeRelationRepository(TaskOfficeRelationRepository taskOfficeRelationRepository) {
		this.taskOfficeRelationRepository = taskOfficeRelationRepository;
	}

	/**
	 * @author rfratti
	 * @return return list of enabled task for scheduler
	 */
	@Override
	public List<Task> getAllTasksForScheduler() {
		return taskRepository.getTasks();
	}

	@Override
	public List<Task> getTasks() {

		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();

		List<Task> tasks = new ArrayList<>();
		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			tasks = taskRepository.getTasks();
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			tasks = taskRepository.getTasksByRole(username);
		}

		logger.info("The number of the tasks: " + tasks.size());

		return tasks;
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

			logger.info("Creating the new task");
		} else { // Existing Task template
			Optional<Task> taskOptional = taskRepository.findById(task.getIdTask());

			if(taskOptional.isPresent()) {
				taskStored = taskOptional.get();
			} else {
				throw new GeneralException("Task not found, id: " + task.getIdTask());
			}

			logger.info("Updating the task with id: " + taskStored.getIdTask());
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

		return taskTaskOfficeSaveUpdate(taskStored, task);
	}

	public Task taskTaskOfficeSaveUpdate(Task taskStored, Task task) {

		taskStored.setOffice(task.getOffice());
		taskStored.setExcludeOffice(task.getExcludeOffice());

		taskStored.setTaskOffices(mergeTaskOffice(taskStored, task.getTaskOfficesFilterEnabled()));

		if(!isEmpty(task.getTaskOffices())) {
			List<TaskOffice> taskOffices = new ArrayList<>();
			for (TaskOffice taskOfficeLoop : task.getTaskOffices()) {

				TaskOffice taskOffice = saveUpdateTaskOffice(taskStored, taskOfficeLoop);
				taskOffices.add(taskOffice);
			}
			taskStored.setTaskOffices(new HashSet<TaskOffice>(taskOffices));
		}

		logger.info("Stored the task with id: " + taskStored.getIdTask());

		return taskStored;
	}

	@Override
	public TaskOffice saveUpdateTaskOffice(Task task, TaskOffice taskOffice) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TaskOffice taskOfficeStored = null;

		// New Task office
		if(isEmpty(taskOffice.getIdTaskOffice())) {

			if(!isEmpty(task.getTaskTemplate()) && !isEmpty(task.getTaskTemplate().getIdTaskTemplate()) &&
					!isEmpty(taskOffice.getTask()) && !isEmpty(taskOffice.getTask().getIdTask()) &&
					!isEmpty(taskOffice.getOffice()) && !isEmpty(taskOffice.getOffice().getIdOffice())) {
				taskOfficeStored = taskOfficeRepository.getAllTaskOfficeByTaskTemplateAndTaskAndOffice(task.getTaskTemplate(), taskOffice.getTask(), taskOffice.getOffice());	
			}

			if(isEmpty(taskOfficeStored)) {
				taskOffice.setCreationDate(new Date());
				taskOffice.setCreatedBy(username);

				taskOfficeStored = taskOffice;

				logger.info("Creating the new taskOffice");
			}

			Date date = null;
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
			try {
				date = sdf.parse(OFFICE_TASK_END_DATE);
			} catch (ParseException e) {
				logger.error("Error parsing date", e);
			}
			taskOfficeStored.setEndDate(date);
		} else { // Existing Task template
			Optional<TaskOffice> taskOfficeOptional = taskOfficeRepository.findById(taskOffice.getIdTaskOffice());

			if (taskOfficeOptional.isPresent()) {
				taskOfficeStored = taskOfficeOptional.get();
			} else {
				throw new GeneralException("Task office not found, id: " + taskOffice.getIdTaskOffice());
			}

			logger.info("Updating the taskOffice with id: " + taskOfficeStored.getIdTaskOffice());
		}

		taskOfficeStored.setTask(task);
		taskOfficeStored.setStartDate(new Date());
		taskOfficeStored.setEnabled(true);
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

		logger.info("Stored the taskOffice with id: " + taskOfficeStored.getIdTaskOffice());

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

			logger.info("Creating the new taskOfficeRelation");
		} else { // Existing Task template
			Optional<TaskOfficeRelations> taskOfficeRelationOptional = taskOfficeRelationRepository.findById(taskOfficeRelation.getIdTaskOfficeRelation());

			if(taskOfficeRelationOptional.isPresent()) {
				taskOfficeRelationStored = taskOfficeRelationOptional.get();
			} else {
				throw new GeneralException("Task office relation not found, id: " + taskOfficeRelation.getIdTaskOfficeRelation());
			}

			logger.info("Updating the taskOfficeRelation with id: " + taskOfficeRelationStored.getIdTaskOfficeRelation());
		}

		taskOfficeRelationStored.setUsername(taskOfficeRelation.getUsername());
		taskOfficeRelationStored.setTaskOffice(taskOffice);
		taskOfficeRelationStored.setRelationType(taskOfficeRelation.getRelationType());
		taskOfficeRelationStored.setModifiedBy(username);
		taskOfficeRelationStored.setModificationDate(new Date());

		taskOfficeRelationStored = taskOfficeRelationRepository.save(taskOfficeRelationStored);

		logger.info("Stored the taskOfficeRelation with id: " + taskOfficeRelationStored.getIdTaskOfficeRelation());

		return taskOfficeRelationStored;
	}

	public Set<TaskOffice> mergeTaskOffice(Task taskStored, Set<TaskOffice> taskOffices) {

		logger.info("Merging Task - Office");

		Set<TaskOffice> taskOfficesToSave = new HashSet<>();
		for (TaskOffice taskOffice : taskOffices) {

			for (TaskOffice taskOfficeStored : taskStored.getTaskOfficesFilterEnabled()) {
				if(isEmpty(taskOffice.getIdTaskOffice()) || 
						(!isEmpty(taskOffice.getIdTaskOffice()) && !isEmpty(taskOfficeStored.getIdTaskOffice()) && 
								taskOffice.getIdTaskOffice().compareTo(taskOfficeStored.getIdTaskOffice()) == 0)) {

					if(!isEmpty(taskOfficeStored.getTaskOfficeRelations())) {
						for (TaskOfficeRelations taskOfficeRelationToDelete : taskOfficeStored.getTaskOfficeRelations()) {
							if(!isEmpty(taskOfficeRelationToDelete.getIdTaskOfficeRelation())) {
								Optional<TaskOfficeRelations> checkIfExist = taskOfficeRelationRepository.findById(taskOfficeRelationToDelete.getIdTaskOfficeRelation());
								if(checkIfExist.isPresent()) {
									taskOfficeRelationRepository.delete(checkIfExist.get());	
								}
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
			logger.info("Number of task offices to remove: " + taskOfficesToRemove.size());

			for (TaskOffice taskOffice : taskOfficesToRemove) {
				if( taskStored.getExcludeOffice()  ||
						(!isEmpty(taskOffice.getOffice()) && 
								!isEmpty(taskStored.getOffice()) &&
								taskOffice.getOffice().getIdOffice().compareTo(taskStored.getOffice().getIdOffice()) == 0)) {

					taskOffice.setEnabled(false);
					taskOffice.setEndDate(new Date());

					if(taskOfficeRepository.findById(taskOffice.getIdTaskOffice()).isPresent()) {
						taskOfficeRepository.save(taskOffice);	
					}
				}
			}
			taskStored.setTaskOffices(new HashSet<>());
		}

		logger.info("Number of task offices to save: " + taskOfficesToSave.size());
		taskStored.getTaskOffices().addAll(taskOfficesToSave);

		return taskStored.getTaskOffices();
	}

	@Override
	public TaskOffice getTaskOfficeByTaskTemplateAndOffice(TaskTemplate taskTemplate, Office office) {
		return taskOfficeRepository.getTaskOfficeByTaskTemplateAndOffice(taskTemplate, office);
	}

	@Override
	public void deleteTask(Task task) {

		if(!isEmpty(task)) {

			// The modification of User
			String username = getTokenUserDetails().getUser().getUsername();

			Task taskStored;
			Optional<Task> taskOptional = taskRepository.findById(task.getIdTask());
			if (taskOptional.isPresent()) {
				taskStored = taskOptional.get();
			} else {
				throw new GeneralException("Task not found, id: " + task.getIdTask());
			}

			taskStored.setEnabled(false);
			taskStored.setModificationDate(new Date());
			taskStored.setModifiedBy(username);

			taskStored = taskRepository.save(taskStored);

			taskTaskOfficeSaveUpdate(taskStored, task);
		}

	}

	@Override
	public Task getTasksByTaskTemplate(TaskTemplate taskTemplate) {

		List<Task> tasks = taskRepository.getTasksByTaskTemplate(taskTemplate);

		Optional<Task> firstTask = tasks.stream().findFirst();

		if(firstTask.isPresent()) {
			return firstTask.get();
		}
		return null;
	}
}
