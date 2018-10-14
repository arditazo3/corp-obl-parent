package com.tx.co.front_end.expiration.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchived;
import com.tx.co.front_end.expiration.api.model.TaskTemplateExpirations;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.user.domain.User;

/**
 * Service for {@link com.tx.co.front_end.expiration.domain.Expiration}s.
 *
 * @author aazo
 */
@Service
public class ExpirationService extends UpdateCacheData implements IExpirationService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(ExpirationService.class);

	private EntityManager em;

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
	public List<TaskTemplateExpirations> searchDateExpirationOffices(
			DateExpirationOfficesHasArchived dateExpirationOfficesHasArchived) {

		logger.info("Searching Task Templates Expirations");

		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();

		Date dateStart = dateExpirationOfficesHasArchived.getDateStart();
		Date endDate = dateExpirationOfficesHasArchived.getDateEnd();
		List<Office> offices = dateExpirationOfficesHasArchived.getOffices();
		Boolean hasArchived = dateExpirationOfficesHasArchived.getHasArchived();

		String querySql = "select tt " + 
				"from Expiration e " + 
				"left join e.taskTemplate tt " +
				"left join tt.tasks t " +
				"left join t.taskOffices to ";

		Query query;

		if((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER) || 
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER)) && 
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {

			querySql += "left join to.office o " +
					"left join o.company c " + 
					"left join c.companyUsers cu " + 
					"left join cu.user u on cu.username = u.username ";
		}

		querySql += " where e.enabled <> 0 ";

		if (!isEmpty(dateStart) && !isEmpty(endDate)) {

			querySql += "and e.expirationDate between :dateStart and :endDate ";
		}
		if (!isEmpty(offices)) {
			querySql += "and to.office in :officeList ";
		}
		if (hasArchived) {
			querySql += "and e.registered < :dateNow ";
		}
		if((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER) || 
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER)) && 
				!userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			querySql += "and cu.username = :username ";
		}

		querySql += "group by tt.idTaskTemplate, t.idTask, o.idOffice " + 
				"order by tt.description asc ";

		query = em.createQuery(querySql);

		if((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER) || 
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

	public List<TaskTemplateExpirations> convertToTaskTemplateExpirations(List<TaskTemplate> taskTemplates) {

		List<TaskTemplateExpirations> taskTemplateExpirations = new ArrayList<>();

		for (TaskTemplate taskTemplateLoop : taskTemplates) {

			TaskTemplateExpirations taskTemplateExpiration = new TaskTemplateExpirations();
			taskTemplateExpiration.setIdTaskTemplate(taskTemplateLoop.getIdTaskTemplate());
			taskTemplateExpiration.setDescription(taskTemplateLoop.getDescription());

			if(!isEmpty(taskTemplateLoop.getTasks())) {

				int countTaskExpiration = 0;
				int countTaskExpirationCompleted = 0; 
				for (Task taskLoop : taskTemplateLoop.getTasks()) {

					if(!isEmpty(taskLoop.getExpirations())) {
						for (Expiration expirationLoop : taskLoop.getExpirations()) {

							if(expirationLoop.getCompleted().compareTo(new Date()) < 0) {
								countTaskExpirationCompleted++;
							}
							
							countTaskExpiration++;
						}
					}
				}

				// TODO To ask Roberto
				taskTemplateExpiration.setTotalExpirations(countTaskExpiration);
				taskTemplateExpiration.setTotalCompleted(countTaskExpirationCompleted);

				taskTemplateExpiration.setTasks(taskTemplateLoop.getTasks());
			}
			taskTemplateExpirations.add(taskTemplateExpiration);
		}

		return taskTemplateExpirations;
	}
}