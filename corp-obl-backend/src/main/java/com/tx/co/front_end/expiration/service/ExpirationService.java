package com.tx.co.front_end.expiration.service;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.common.utils.UtilStatic;
import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchived;
import com.tx.co.front_end.expiration.api.model.TaskExpirations;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.domain.ExpirationActivity;
import com.tx.co.front_end.expiration.repository.ExpirationRepository;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.exception.GeneralException;
import com.tx.co.user.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

import static com.tx.co.common.constants.AppConstants.ADMIN;
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
	private IExpirationActivityService expirationActivityService;
	private EntityManager em;

	@Autowired
	public void setExpirationRepository(ExpirationRepository expirationRepository) {
		this.expirationRepository = expirationRepository;
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
	public List<TaskExpirations> searchDateExpirationOffices(
			DateExpirationOfficesHasArchived dateExpirationOfficesHasArchived) {

		logger.info("Searching Task Templates Expirations");

		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();

		Date dateStart = dateExpirationOfficesHasArchived.getDateStart();
		Date endDate = dateExpirationOfficesHasArchived.getDateEnd();
		List<Office> offices = dateExpirationOfficesHasArchived.getOffices();
		Boolean hasArchived = dateExpirationOfficesHasArchived.getHasArchived();

		String querySql = "select t " +
				"from Expiration e " +
				"left join e.task t " +
				"left join t.taskTemplate tt " +
				"left join t.taskOffices to " +
				"left join to.office o ";

		Query query;

		if ((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER) ||
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER)) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {

			querySql += "left join to.office o " +
					"left join o.company c " +
					"left join c.companyUsers cu " +
					"left join cu.user u on cu.username = u.username ";
		}

		querySql += " where tt.enabled <> 0 and o.enabled <> 0 and e.enabled <> 0 ";

		if (!isEmpty(dateStart) && !isEmpty(endDate)) {

			querySql += "and e.expirationDate between :dateStart and :endDate ";
		}
		if (!isEmpty(offices)) {
			querySql += "and e.office in :officeList ";
		}
		if (hasArchived) {
			querySql += "or e.registered < :dateNow ";
		}
		if ((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER) ||
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER)) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			querySql += "and cu.username = :username ";
		}

		querySql += "group by tt.idTaskTemplate, e.expirationDate " +
				"order by e.expirationDate desc";

		query = em.createQuery(querySql);

		if ((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER) ||
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER)) &&
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			query.setParameter("username", username);
		}
		if (!isEmpty(dateStart) && !isEmpty(endDate)) {

			query.setParameter("dateStart", dateStart);
			query.setParameter("endDate", endDate);
		}
		if (!isEmpty(offices)) {
			query.setParameter("officeList", offices);
		}
		if (hasArchived) {
			query.setParameter("dateNow", new Date());
		}

		return convertToTaskTemplateExpirations(query.getResultList());
	}

	public List<TaskExpirations> convertToTaskTemplateExpirations(List<Task> tasks) {

		List<TaskExpirations> taskExpirations = new ArrayList<>();

		/*
		 * Split the list based on those fields 
		 * */
		List<Date> expirationDates = new ArrayList<>();
		Map<Date, List<Expiration>> dateExpirationMap = new HashMap<>();

		if (!isEmpty(tasks)) {
			for (Task taskLoop : tasks) {
				
				Task taskClone = new Task();
				BeanUtils.copyProperties(taskLoop, taskClone);
				
				TaskExpirations taskExpiration = new TaskExpirations();
				TaskTemplate taskTemplate = taskClone.getTaskTemplate();
				taskExpiration.setIdTaskTemplate(taskTemplate.getIdTaskTemplate());
				taskExpiration.setDescription(taskTemplate.getDescription());

				int countTaskExpiration = 0;
				int countTaskExpirationCompleted = 0;
				Date expirationDate = null;

				Set<Expiration> expirations = taskClone.getExpirationsFilterEnabled();
				if (!isEmpty(expirations)) {

					dateExpirationMap = expirations.stream()
							.collect(Collectors.groupingBy( exp -> exp.getExpirationDate() ));

					for (Expiration expirationLoop : expirations) {
						expirationDate = expirationLoop.getExpirationDate();
						if(isEmpty(expirationDates) || !expirationDates.contains(expirationDate)) {

							expirationDates.add(expirationDate);
							for (Expiration expirationGrouped : dateExpirationMap.get(expirationDate)) {
								Date completedDate = expirationGrouped.getCompleted();
								if (!isEmpty(completedDate) && completedDate.compareTo(new Date()) < 0) {
									countTaskExpirationCompleted++;
								}
								countTaskExpiration++;
							}
							taskExpiration.setExpirations(new HashSet<>(dateExpirationMap.get(expirationDate)));
							taskClone.setExpirations(new HashSet<>());
							break;
						}
					}
				}
				// TODO To ask Roberto
				taskExpiration.setTotalExpirations(countTaskExpiration);
				taskExpiration.setTotalCompleted(countTaskExpirationCompleted);
				taskExpiration.setTask(taskClone);
				taskExpiration.setExpirationDate(UtilStatic.formatDateToString(expirationDate));
				
				taskExpirations.add(taskExpiration);
			}
		}

		// sort the list by expiration date desc
		if(!isEmpty(taskExpirations)) {
			Collections.sort(taskExpirations, new Comparator<TaskExpirations>() {
				@Override
				public int compare(final TaskExpirations object1, final TaskExpirations object2) {
					if(isEmpty(object1.getExpirationDate())) return 1;
					if(isEmpty(object2.getExpirationDate())) return -1;

					return UtilStatic.formatStringToDate(object2.getExpirationDate()).compareTo(UtilStatic.formatStringToDate(object1.getExpirationDate()));
				}
			});
		}

		return taskExpirations;
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

}