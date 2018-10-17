package com.tx.co.front_end.expiration.service;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.front_end.expiration.api.model.DateExpirationOfficesHasArchived;
import com.tx.co.front_end.expiration.api.model.TaskTemplateExpirations;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.repository.ExpirationRepository;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.user.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

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
    private EntityManager em;


    @Autowired
    public void setExpirationRepository(ExpirationRepository expirationRepository) {
        this.expirationRepository = expirationRepository;
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
        if ((userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER) ||
                userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER)) &&
                !userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
            querySql += "and cu.username = :username ";
        }

        querySql += "group by tt.idTaskTemplate, t.idTask, o.idOffice " +
                "order by e.expirationDate desc ";

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

    public List<TaskTemplateExpirations> convertToTaskTemplateExpirations(List<TaskTemplate> taskTemplates) {

        List<TaskTemplateExpirations> taskTemplateExpirations = new ArrayList<>();

        for (TaskTemplate taskTemplateLoop : taskTemplates) {

            TaskTemplateExpirations taskTemplateExpiration = new TaskTemplateExpirations();
            taskTemplateExpiration.setIdTaskTemplate(taskTemplateLoop.getIdTaskTemplate());
            taskTemplateExpiration.setDescription(taskTemplateLoop.getDescription());

            if (!isEmpty(taskTemplateLoop.getTasks())) {

                int countTaskExpiration = 0;
                int countTaskExpirationCompleted = 0;
                for (Task taskLoop : taskTemplateLoop.getTasks()) {

                    if (!isEmpty(taskLoop.getExpirations())) {
                        for (Expiration expirationLoop : taskLoop.getExpirations()) {

                            if (expirationLoop.getCompleted().compareTo(new Date()) < 0) {
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

    /**
     * author rfratti
     *
     * @param taskId
     * @return list of removable expirations
     */
    @Override
    public List<Expiration> getSchedulerRemovableExpirationList(long taskId) {

        final List<Expiration> result = new LinkedList<>();
        String querySql = " select e from Task t " +
                " inner join t.taskOffices tof " +
                " inner join tof.taskOfficeRelations tor " +
                " inner join t.expirations e " +
                " left join e.expirationActivities ea " +
                " where t.id = " + taskId + " and e.expirationClosableBy = 2  " +
                " and tor.enabled = 0 and tor.relationType = 2 " +
                " and tor.username = e.username " +
                " and e.expirationDate > DATE(CONCAT(CURDATE(), ' 23:59:59')) " +
                " having count(ea.id) = 0 "
                ;
        Query query = em.createQuery(querySql);
        result.addAll(query.getResultList());

        querySql = " select e from corporate_obligations.co_task t " +
                " inner join corporate_obligations.co_taskoffice tof on t.id = tof.task_id " +
                " inner join corporate_obligations.co_taskofficerelations tor on tof.id = tor.taskoffice_id " +
                " inner join corporate_obligations.co_expiration e on t.id = e.task_id " +
                " left join corporate_obligations.co_expirationactivity ea on e.id = ea.expiration_id " +
                " where t.id = " + taskId + " and e.expirationclosableby = 1  " +
                " and tor.enabled = 1 and tor.relationtype = 2 " +
                " and e.expirationdate > DATE(CONCAT(CURDATE(), ' 23:59:59')) " +
                " having count(ea.id) = 0 and count(tor.id) = 0 ";

        query = em.createQuery(querySql);
        result.addAll(query.getResultList());

        return result;
    }

    /**
     * @param expirationList
     * @return true if update is successfully
     * @author rfratti
     */
    @Override
    public boolean updateExpirationList(List<Expiration> expirationList) {
        try {
            final Iterable<Expiration> result = expirationRepository.saveAll(expirationList);
            return ((Collection<?>) result).size() > 0;
        } catch (Exception e) {
            logger.error("Exception on updating list of expiration. Exception: " + e, e);
        }
        return false;
    }
}