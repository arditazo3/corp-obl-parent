package com.tx.co.back_office.office.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.company.domain.OfficeTaskTemplate;
import com.tx.co.back_office.office.api.model.OfficeTaskTemplates;
import com.tx.co.back_office.office.api.model.TaskTempOffices;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.office.repository.OfficeRepository;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.common.translation.api.model.TranslationPairKey;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.exception.GeneralException;
import com.tx.co.user.domain.User;

/**
 * Service for {@link com.tx.co.back_office.office.domain.Office}s.
 *
 * @author aazo
 */
@Service
public class OfficeService extends UpdateCacheData implements IOfficeService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(OfficeService.class);

	private OfficeRepository officeRepository;
	private EntityManager em;

	@Autowired
	public void setOfficeRepository(OfficeRepository officeRepository) {
		this.officeRepository = officeRepository;
	}

	@Autowired
	public void setEm(EntityManager em) {
		this.em = em;
	}

	/**
	 * @return get all the Office
	 */
	@Override
	public List<Office> findAllOffice() {
		List<Office> officeList = new ArrayList<>();

		List<Office> officeListFromCache = getOfficesFromCache();
		if (!isEmpty(getOfficesFromCache())) {
			officeList = officeListFromCache;
		} else {
			officeRepository.findAllByOrderByDescriptionAsc().forEach(officeList::add);
		}

		logger.info("The number of the offices: " + officeList.size());

		return officeList;
	}

	@Override
	public Optional<Office> findByIdOffice(Long idOffice) {
		return officeRepository.findById(idOffice);
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public Office saveUpdateOffice(Office office) {

		// Check if exist other office with same description
		if (checkIfExistOtherOfficeSameDescription(office)) {
			throw new GeneralException("This company already exist");
		}

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		Office officeStored = null;

		// New Office
		if (isEmpty(office.getIdOffice())) {
			office.setCreationDate(new Date());
			office.setCreatedBy(username);
			office.setEnabled(true);
			officeStored = office;
			
			logger.info("Creating the new office");
		} else { // Existing Company
			officeStored = getOfficeById(office.getIdOffice());
			officeStored.setDescription(office.getDescription());
			
			logger.info("Updating the office with id: " + officeStored.getIdOffice());
		}

		officeStored.setCompany(office.getCompany());
		officeStored.setModificationDate(new Date());
		officeStored.setModifiedBy(username);

		officeStored = officeRepository.save(officeStored);

		updateOfficesCache(officeStored, false);

		logger.info("Stored the office with id: " + officeStored.getIdOffice());
		
		return officeStored;
	}

	/**
	 * @param company
	 * @return true if the Company already exist
	 */
	private boolean checkIfExistOtherOfficeSameDescription(Office office) {

		List<Office> officeListByDescription = officeRepository.findOfficesByDescription(office.getDescription());
		if (isEmpty(officeListByDescription)) {
			return false;
			// Check if i'm modifing the exist one
		} else {
			int counter = 0;
			for (Office officeLoop : officeListByDescription) {
				if ((!isEmpty(office.getIdOffice()) && officeLoop.getIdOffice().compareTo(office.getIdOffice()) != 0
						&& officeLoop.getDescription().trim().equalsIgnoreCase(office.getDescription().trim()))
						|| (isEmpty(office.getIdOffice()) && officeLoop.getDescription().trim()
								.equalsIgnoreCase(office.getDescription().trim()))) {
					counter++;
				}
			}
			return counter > 0;
		}
	}

	@Override
	public void deleteOffice(Long idOffice) {

		try {
			logger.info("Deleting the Office with id: " + idOffice);
			
			Optional<Office> officeOptional = findByIdOffice(idOffice);

			if (!officeOptional.isPresent()) {
				throw new NotFoundException();
			}

			// The modification of User
			String username = getTokenUserDetails().getUser().getUsername();

			Office office = officeOptional.get();
			// disable the office
			office.setEnabled(false);
			office.setModificationDate(new Date());
			office.setModifiedBy(username);

			officeRepository.save(office);

			updateOfficesCache(office, false);
			
			logger.info("Deleted the Office with id: " + idOffice);
		} catch (Exception e) {
			throw new GeneralException("Office not found");
		}

	}

	@Override
	public List<OfficeTaskTemplates> searchOfficeTaskTemplates(TaskTempOffices taskTempOffices) {

		logger.info("Searching Office Task Templates");
		
		String querySql = "select to.office, tt " + 
				"from TaskOffice to " + 
				"left join to.taskTemplate tt ";
		Query query;

		if (isEmpty(taskTempOffices.getOffices())) {
			querySql += "where to.enabled <> 0 and tt.description like :description " + "group by to.idTaskOffice "
					+ "order by to.taskTemplate.description asc ";
			query = em.createQuery(querySql);

			query.setParameter("description", "%" + taskTempOffices.getDescriptionTaskTemplate() + "%");
		} else {
			querySql += "where to.enabled <> 0 and tt.description like :description " + "and to.office in :officeList "
					+ "group by to.idTaskOffice " + "order by to.taskTemplate.description asc ";
			query = em.createQuery(querySql);

			query.setParameter("description", "%" + taskTempOffices.getDescriptionTaskTemplate() + "%");
			query.setParameter("officeList", taskTempOffices.getOffices());
		}

		List<OfficeTaskTemplates> officeTaskTemplatesList = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Object[]> officeTaskTemplateList = query.getResultList();
		if (!isEmpty(officeTaskTemplateList)) {
			List<OfficeTaskTemplate> officeTaskTemplates = new ArrayList<>();

			for (Object[] officeTaskTemplateObject : officeTaskTemplateList) {
				OfficeTaskTemplate officeTaskTemplate = new OfficeTaskTemplate();
				Office officeToSet = (Office) officeTaskTemplateObject[0];
				TaskTemplate taskTemplateToSet = (TaskTemplate) officeTaskTemplateObject[1];

				officeTaskTemplate.setOffice(officeToSet);
				officeTaskTemplate.setTaskTemplate(taskTemplateToSet);
				officeTaskTemplates.add(officeTaskTemplate);
			}
			officeTaskTemplatesList = convertToOfficeTasks(officeTaskTemplates);
		}

		if (!isEmpty(taskTempOffices.getOffices())
				&& taskTempOffices.getOffices().size() > officeTaskTemplatesList.size()) {

			List<OfficeTaskTemplates> officeTasksListTemp = new ArrayList<>();
			for (Office officeFirstLoop : taskTempOffices.getOffices()) {
				if (!isEmpty(officeTaskTemplatesList)) {
					boolean hasOffice = false;
					for (OfficeTaskTemplates officeTasksLoop : officeTaskTemplatesList) {
						if (officeFirstLoop.getIdOffice().compareTo(officeTasksLoop.getOffice().getIdOffice()) == 0) {
							hasOffice = true;
						}
					}
					if (!hasOffice) {
						OfficeTaskTemplates officeTasks = new OfficeTaskTemplates();
						officeTasks.setOffice(officeFirstLoop);
						officeTasksListTemp.add(officeTasks);
					}
				} else {
					OfficeTaskTemplates officeTasks = new OfficeTaskTemplates();
					officeTasks.setOffice(officeFirstLoop);
					officeTasksListTemp.add(officeTasks);
				}
			}
			officeTaskTemplatesList.addAll(officeTasksListTemp);
		}

		logger.info("Number of items: " + officeTaskTemplatesList.size());
		
		return officeTaskTemplatesList;
	}

	public List<OfficeTaskTemplates> convertToOfficeTasks(List<OfficeTaskTemplate> officeTaskList) {

		User userLoggedIn = getTokenUserDetails().getUser();
		String lang = userLoggedIn.getLang();
		
		HashMap<Office, List<TaskTemplate>> officeTaskTemplatesMap = new HashMap<>();
		for (OfficeTaskTemplate officeTask : officeTaskList) {
			Office office = officeTask.getOffice();
			TaskTemplate taskTemplate = officeTask.getTaskTemplate();
			if (!officeTaskTemplatesMap.containsKey(office)) {
				List<TaskTemplate> taskTemplates = new ArrayList<>();
				taskTemplates.add(taskTemplate);
				officeTaskTemplatesMap.put(office, taskTemplates);
			} else {
				officeTaskTemplatesMap.get(office).add(taskTemplate);
			}
		}

		List<OfficeTaskTemplates> officeTasks = new ArrayList<>();
		for (Office officeLoop : officeTaskTemplatesMap.keySet()) {
			
			List<TaskTemplate> taskTemplates = officeTaskTemplatesMap.get(officeLoop);
			if(!isEmpty(taskTemplates)) {
				for (TaskTemplate taskTemplate : taskTemplates) {

					String descriptionTaskTemplate = taskTemplate.getDescription() + " - ";

					descriptionTaskTemplate += getTranslationByLangLikeTablename(new TranslationPairKey(taskTemplate.getExpirationType(), lang)).getDescription() + " - " + taskTemplate.getDay();

					taskTemplate.setDescriptionTaskTemplate(descriptionTaskTemplate);
				}
			}
			
			OfficeTaskTemplates officeTaskFinal = new OfficeTaskTemplates();
			officeTaskFinal.setOffice(officeLoop);
			officeTaskFinal.setTaskTemplates(officeTaskTemplatesMap.get(officeLoop));
			officeTasks.add(officeTaskFinal);
		}

		return officeTasks;
	}

	@Override
	public List<Office> getOfficesByRole() {

		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();

		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			return officeRepository.findAllByOrderByDescriptionAsc();
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN) ||
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_INLAND) ||
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_CONTROLLER) ||
				userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_USER)) {
			return officeRepository.getOfficesByRole(username);
		}

		return new ArrayList<>();
	}
}
