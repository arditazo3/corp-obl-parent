package com.tx.co.front_end.expiration.service;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.common.utils.UtilStatic;
import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchived;
import com.tx.co.front_end.expiration.api.model.TaskOfficeExpirations;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.domain.ExpirationActivity;
import com.tx.co.front_end.expiration.enums.StatusExpirationEnum;
import com.tx.co.front_end.expiration.repository.ExpirationActivityRepository;
import com.tx.co.front_end.expiration.repository.ExpirationRepository;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.exception.GeneralException;
import com.tx.co.user.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;

import java.util.*;

import static com.tx.co.common.constants.AppConstants.*;
import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * Service for {@link com.tx.co.front_end.expiration.domain.Expiration}s.
 *
 * @author aazo
 */
@Service
public class ExpirationService extends UpdateCacheData implements IExpirationService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(ExpirationService.class);

	private ExpirationRepository expirationRepository;
	private ExpirationActivityRepository expirationActivityRepository;
	private IExpirationActivityService expirationActivityService;
	private EntityManager em;

	@Autowired
	public void setExpirationRepository(ExpirationRepository expirationRepository) {
		this.expirationRepository = expirationRepository;
	}

	@Autowired
	public void setExpirationActivityRepository(ExpirationActivityRepository expirationActivityRepository) {
		this.expirationActivityRepository = expirationActivityRepository;
	}

	@Autowired
	public void setExpirationActivityService(IExpirationActivityService expirationActivityService) {
		this.expirationActivityService = expirationActivityService;
	}

	@Autowired
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskOfficeExpirations> searchDateExpirationOffices(
			DateExpirationOfficesHasArchived dateExpirationOfficesHasArchived) {

		logger.info("Searching Task Templates Expirations");

		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();
		Integer userRelationType = dateExpirationOfficesHasArchived.getUserRelationType();

		Date dateStart = dateExpirationOfficesHasArchived.getDateStart();
		Date endDate = dateExpirationOfficesHasArchived.getDateEnd();
		List<Office> offices = dateExpirationOfficesHasArchived.getOffices();
		Boolean hideArchived = dateExpirationOfficesHasArchived.getHideArchived();

		String querySql = "select e " +
				"from Expiration e " +
				"left join e.task t " +
				"left join t.taskTemplate tt " +
				"left join t.taskOffices to " + 
				"left join e.office o ";

		Query query;

		if ((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER) || 
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLED)) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {

			querySql += "left join to.taskOfficeRelations tof ";
		}

		querySql += " where t.enabled <> 0 and o.enabled <> 0 and e.enabled <> 0 ";

		if (!isEmpty(dateStart) && !isEmpty(endDate)) {

			querySql += "and e.expirationDate between :dateStart and :endDate ";
		}
		if (!isEmpty(offices)) {
			querySql += "and e.office in :officeList ";
		}
		if (hideArchived) {
			querySql += "and e.registered is null ";
		}
		if ((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER) || 
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLED)) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			querySql += "and tof.username = :username " + 
						"and tof.relationType = :relationType " ;
			
		}

		querySql += "group by e.idExpiration " + 
				"order by e.expirationDate desc, o.idOffice asc, tt.description asc ";

		query = em.createQuery(querySql);

		if ((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER) || 
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLED)) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			query.setParameter("username", username);
			query.setParameter("relationType", userRelationType);
		}
		if (!isEmpty(dateStart) && !isEmpty(endDate)) {

			query.setParameter("dateStart", dateStart);
			query.setParameter("endDate", endDate);
		}
		if (!isEmpty(offices)) {
			query.setParameter("officeList", offices);
		}

		return convertToTaskTemplateExpirations(query.getResultList());
	}

	public List<TaskOfficeExpirations> convertToTaskTemplateExpirations(List<Expiration> expirationsResult) {

		/*
		 * Split the list based on those fields 
		 * */
		Map<TaskOfficeExpirationDateKey, TaskOfficeExpirations> taskOfficeExpDateMap = new HashMap<>();

		if (!isEmpty(expirationsResult)) {
			for (Expiration expiration : expirationsResult) {

				TaskOfficeExpirations taskOfficeExpiration = new TaskOfficeExpirations();

				TaskTemplate taskTemplate = expiration.getTaskTemplate();
				taskOfficeExpiration.setIdTaskTemplate(taskTemplate.getIdTaskTemplate());
				taskOfficeExpiration.setDescription(taskTemplate.getDescription());

				Office office = expiration.getOffice();
				taskOfficeExpiration.setOffice(office);

				Task task = expiration.getTask();
				taskOfficeExpiration.setTask(task);

				TaskOfficeExpirationDateKey taskOfficeExpirationDateKey = new TaskOfficeExpirationDateKey();
				taskOfficeExpirationDateKey.idTask = task.getIdTask();
				//		taskOfficeExpirationDateKey.idOffice = office.getIdOffice();
				taskOfficeExpirationDateKey.expirationDate = expiration.getExpirationDate();

				addTaskOfficeExpMap(taskOfficeExpirationDateKey, taskOfficeExpDateMap, taskOfficeExpiration, expiration);

			}
		}

		buildCounterExpirations(taskOfficeExpDateMap);

		return new ArrayList<>(taskOfficeExpDateMap.values());
	}

	public synchronized void addTaskOfficeExpMap(TaskOfficeExpirationDateKey key, 
			Map<TaskOfficeExpirationDateKey, TaskOfficeExpirations> taskOfficeExpDateMap,
			TaskOfficeExpirations taskOfficeExpiration, Expiration expiration) {

		TaskOfficeExpirations taskOfficeExpirations = taskOfficeExpDateMap.get(key);
		if (taskOfficeExpirations == null) {
			taskOfficeExpirations = taskOfficeExpiration;
			taskOfficeExpDateMap.put(key, taskOfficeExpiration);
		}
		taskOfficeExpirations.getExpirations().add(expiration);
	}

	public void buildCounterExpirations(Map<TaskOfficeExpirationDateKey, TaskOfficeExpirations> taskOfficeExpDateMap) {

		for (Map.Entry<TaskOfficeExpirationDateKey, TaskOfficeExpirations> entry : 
			taskOfficeExpDateMap.entrySet()) {

			int countTaskExpiration = 0;
			int countTaskExpirationCompleted = 0;
			TaskOfficeExpirations taskOfficeExpirations = entry.getValue();
			for (Expiration expirationLoop : taskOfficeExpirations.getExpirations()) {
				if(isProvider() && 
						expirationLoop.getExpirationActivities().stream().allMatch(ea -> ea.getIdExpirationActivity() != null)) {
					expirationLoop.getExpirationActivities().add(new ExpirationActivity());
				}

				Date completedDate = expirationLoop.getCompleted();
				if (!isEmpty(completedDate) && completedDate.compareTo(new Date()) < 0) {
					countTaskExpirationCompleted++;
				}
				countTaskExpiration++;
			}

			taskOfficeExpirations.setTotalExpirations(countTaskExpiration);
			taskOfficeExpirations.setTotalCompleted(countTaskExpirationCompleted);
			taskOfficeExpirations.setExpirationDate(UtilStatic.formatDateToString(taskOfficeExpirations.getExpirations().iterator().next().getExpirationDate()));
		}
	}

	@Override
	public Expiration saveUpdateExpiration(Expiration expiration) {

		// The modification of User
		String username = ADMIN;
		try {
			username = getTokenUserDetails().getUser().getUsername();
		} catch (Exception e) {
			logger.info("Job process of the Expiration");
		}

		Expiration expirationStored = null;

		// New Expiration
		if (isEmpty(expiration.getIdExpiration())) {
			expiration.setCreationDate(new Date());
			expiration.setModificationDate(new Date());
			expiration.setCreatedBy(username);
			expiration.setModifiedBy(username);
			expiration.setEnabled(true);
			expirationStored = expiration;

			logger.info("Creating the new expiration");
		} else { // Existing Expiration
			Optional<Expiration> expirationOptional = expirationRepository.findById(expiration.getIdExpiration());

			if (expirationOptional.isPresent()) {
				expirationStored = expirationOptional.get();
			} else {
				throw new GeneralException("Expiration not found, id: " + expiration.getIdExpiration());
			}

			logger.info("Updating the expiration with id: " + expiration.getIdExpiration());
		}

		expirationStored.setTaskTemplate(expiration.getTaskTemplate());
		expirationStored.setTask(expiration.getTask());
		expirationStored.setOffice(expiration.getOffice());
		expirationStored.setExpirationClosableBy(expiration.getExpirationClosableBy());
		expirationStored.setEnabled(expiration.getEnabled());
		expirationStored.setModifiedBy(expiration.getModifiedBy());
		expirationStored.setModificationDate(expiration.getModificationDate());
		expirationStored.setExpirationDate(expiration.getExpirationDate());
		if (!isEmpty(expiration.getCompleted())) {
			expirationStored.setCompleted(expiration.getCompleted());
		}
		if (!isEmpty(expiration.getApproved())) {
			expirationStored.setApproved(expiration.getApproved());
		}
		if (!isEmpty(expiration.getRegistered())) {
			expirationStored.setRegistered(expiration.getRegistered());
		}

		expirationStored = expirationRepository.save(expirationStored);

		if (!isEmpty(expiration.getExpirationActivities())) {
			List<ExpirationActivity> expirationActivities = new ArrayList<>();
			for (ExpirationActivity expirationActivityLoop : expiration.getExpirationActivities()) {

				ExpirationActivity expirationActivity = expirationActivityService.
						saveUpdateExpirationActivity(expirationStored, expirationActivityLoop);

				expirationActivities.add(expirationActivity);
			}
			expirationStored.setExpirationActivities(new HashSet<ExpirationActivity>(expirationActivities));
		}
		return expirationStored;
	}

	@Override
	public Expiration statusExpirationOnChange(Expiration expiration) {

		try {
			String statusExpirationOnChange = expiration.getStatusExpirationOnChange(); 
			if(isEmpty(statusExpirationOnChange)) {
				return expiration;
			}
			
			Long idExpiration = expiration.getIdExpiration();
			logger.info("Archiving the expiration with id: " + idExpiration);

			Optional<Expiration> expirationOptional = expirationRepository.findById(idExpiration);
			
			if(!expirationOptional.isPresent()) {
    			throw new NotFoundException();
    		}
			
			Expiration expirationStored = expirationOptional.get();
			expirationStored.setStatusExpirationOnChange(statusExpirationOnChange);
			
			changeStatusExpiration(expirationStored);
			
    		expiration = expirationRepository.save(expirationStored);

    		if(isProvider() && 
    				expiration.getExpirationActivities().stream().allMatch(ea -> ea.getIdExpirationActivity() != null)) {
    			expiration.getExpirationActivities().add(new ExpirationActivity());
			}
		} catch (Exception e) {
			logger.error(e);
			throw new GeneralException("Expiration not found");
		}

		return expiration;
	}

	private void changeStatusExpiration(Expiration expiration) {

		String statusExpirationOnChange = expiration.getStatusExpirationOnChange(); 
		
		if(statusExpirationOnChange.equalsIgnoreCase(StatusExpirationEnum.ARCHIVED.name())) {
			expiration.setRegistered(new Date());
		} else if(statusExpirationOnChange.equalsIgnoreCase(StatusExpirationEnum.RESTORE.name())) {
			expiration.setCompleted(null);
			expiration.setRegistered(null);
		} else if(statusExpirationOnChange.equalsIgnoreCase(StatusExpirationEnum.APPROVED.name())) {
			expiration.setApproved(new Date());
		} else if(statusExpirationOnChange.equalsIgnoreCase(StatusExpirationEnum.NOT_APPROVED.name())) {
			expiration.setApproved(null);
		}
		
		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();
		expiration.setModificationDate(new Date());
		expiration.setModifiedBy(username);
	}

	@Override
	public void deleteExpiration(Long idExpiration) {

		try {
			logger.info("Deleting the Expiration with id: " + idExpiration);

			Optional<Expiration> expirationOptional = expirationRepository.findById(idExpiration);

			if(!expirationOptional.isPresent()) {
				throw new NotFoundException();
			}

			// The modification of User
			String username = getTokenUserDetails().getUser().getUsername();

			Expiration expiration = expirationOptional.get();
			// disable the expiration
			expiration.setEnabled(false);
			expiration.setModificationDate(new Date());
			expiration.setModifiedBy(username);

			expirationRepository.save(expiration);

			logger.info("Deleting the Expiration with id: " + idExpiration );
		} catch (Exception e) {
			throw new GeneralException("Expiration not found");
		}
	}

	@Override
	public ExpirationActivity saveUpdateExpirationActivity(ExpirationActivity expirationActivity) {

		// The modification of User
		String username = ADMIN;
		try {
			username = getTokenUserDetails().getUser().getUsername();
		} catch (Exception e) {
			logger.info("Job process of the Expiration Activity");
		}

		ExpirationActivity expirationActivityStored = null;

		// New Expiration Activity 
		if (isEmpty(expirationActivity.getIdExpirationActivity())) {
			expirationActivity.setCreationDate(new Date());
			expirationActivity.setCreatedBy(username);
			expirationActivity.setDeleted(false);
			expirationActivityStored = expirationActivity;

			logger.info("Creating the new expiration activity");
		} else { // Existing Expiration
			Optional<ExpirationActivity> expirationActivityOptional = expirationActivityRepository.findById(expirationActivity.getIdExpirationActivity());

			if (expirationActivityOptional.isPresent()) {
				expirationActivityStored = expirationActivityOptional.get();
			} else {
				throw new GeneralException("Expiration activity not found, id: " + expirationActivity.getIdExpirationActivity());
			}

			logger.info("Updating the expiration activity with id: " + expirationActivity.getIdExpirationActivity());
		}

		expirationActivityStored.setBody(expirationActivity.getBody());

		expirationActivityStored.setExpiration(expirationActivity.getExpiration());
		expirationActivityStored.setModificationDate(new Date());
		expirationActivityStored.setModifiedBy(username);

		expirationActivityStored = expirationActivityRepository.save(expirationActivityStored);

		return expirationActivityStored;

	}


	/**
	 * author rfratti
	 *
	 * @param taskId
	 * @return list of removable expirations
	 */
	@Override
	public Iterable<Expiration> getSchedulerRemovableExpirationList(long taskId) {
		final List<Long> idList = new LinkedList<>();
		String querySql = " select e.id from Task t " +
				" inner join t.taskOffices tof " +
				" inner join tof.taskOfficeRelations tor " +
				" inner join t.expirations e " +
				" left join e.expirationActivities ea " +
				" where t.id = " + taskId + " and e.expirationClosableBy = 2  " +
				" and tor.enabled = 0 and tor.relationType = 2 " +
				" and tor.username = e.username " +
				" and e.expirationDate > DATE(CONCAT(CURDATE(), ' 23:59:59')) " +
				" group by e.id " +
				" having count(ea.id) = 0 ";
		Query query = em.createQuery(querySql);
		idList.addAll(query.getResultList());

		querySql = " select e.id from Task t " +
				" inner join t.taskOffices tof " +
				" inner join tof.taskOfficeRelations tor " +
				" inner join t.expirations e " +
				" left join e.expirationActivities ea " +
				" where t.id = " + taskId + " and e.expirationClosableBy = 1  " +
				" and tor.enabled = 1 and tor.relationType = 2 " +
				" and e.expirationDate > DATE(CONCAT(CURDATE(), ' 23:59:59')) " +
				" group by e.id " +
				" having count(ea.id) = 0 and count(tor.id) = 0 ";

		query = em.createQuery(querySql);
		idList.addAll(query.getResultList());

		final Iterable<Expiration> result = expirationRepository.findAllById(idList);

		return result;
	}


	/**
	 * @param expirationList
	 * @return true if update is successfully
	 * @author rfratti
	 */
	@Override
	public boolean updateExpirationList(Iterable<Expiration> expirationList) {
		try {
			final Iterable<Expiration> result = expirationRepository.saveAll(expirationList);
			return ((Collection<?>) result).size() > 0;
		} catch (Exception e) {
			logger.error("Exception on updating list of expiration. Exception: " + e, e);
		}
		return false;
	}

	public Boolean isProvider() {
		Boolean isProvider = false;
		User userLoggedIn = getTokenUserDetails().getUser();

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER)) {
			isProvider = true;
		}
		return isProvider;
	}

}
class TaskOfficeExpirationDateKey {

	Long idTask;
	Long idOffice;
	Date expirationDate;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((idOffice == null) ? 0 : idOffice.hashCode());
		result = prime * result + ((idTask == null) ? 0 : idTask.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskOfficeExpirationDateKey other = (TaskOfficeExpirationDateKey) obj;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (idOffice == null) {
			if (other.idOffice != null)
				return false;
		} else if (!idOffice.equals(other.idOffice))
			return false;
		if (idTask == null) {
			if (other.idTask != null)
				return false;
		} else if (!idTask.equals(other.idTask))
			return false;
		return true;
	}
}