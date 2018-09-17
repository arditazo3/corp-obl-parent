package com.tx.co.back_office.topic.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyTopic;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;
import com.tx.co.back_office.topic.repository.TopicConsultantRepository;
import com.tx.co.back_office.topic.repository.TopicRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.common.translation.domain.Translation;
import com.tx.co.common.translation.repository.TranslationRepository;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.exception.GeneralException;
import com.tx.co.user.domain.User;

/**
 * Service for {@link com.tx.co.back_topic.topic.domain.Topic}s.
 *
 * @author aazo
 */
@Service
public class TopicService extends UpdateCacheData implements ITopicService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(TopicService.class);

	private TopicRepository topicRepository;
	private TopicConsultantRepository topicConsultantRepository;
	private TranslationRepository translationRepository;

	@Autowired
	public void setTopicRepository(TopicRepository topicRepository) {
		this.topicRepository = topicRepository;
	}

	@Autowired
	public void setTranslationRepository(TranslationRepository translationRepository) {
		this.translationRepository = translationRepository;
	}

	@Autowired
	public void setTopicConsultantRepository(TopicConsultantRepository topicConsultantRepository) {
		this.topicConsultantRepository = topicConsultantRepository;
	}

	@Override
	public List<Topic> findAllTopic() {
		List<Topic> topicList = new ArrayList<>();

		List<Topic> topicListFromCache = getTopicsFromCache();
		if (!isEmpty(getTopicsFromCache())) {
			topicList = topicListFromCache;
		} else {
			topicRepository.findAllByOrderByDescriptionAsc().forEach(topicList::add);
		}

		logger.info("The number of the topics: " + topicList.size());

		return topicList;
	}

	@Override
	public Topic saveUpdateTopic(Topic topic) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		Topic topicStored = null;

		// New Topic
		if (isEmpty(topic.getIdTopic())) {
			topic.setCreationDate(new Date());
			topic.setCreatedBy(username);
			topic.setEnabled(true);
			topicStored = topic;
		} else { // Existing Topic
			topicStored = getTopicById(topic.getIdTopic(), null);
			topicStored.setDescription(topic.getDescription());
			topicStored.setCompanyTopic(new HashSet<>());
		}

		topicStored.setTranslationList(topic.getTranslationList());

		List<Company> companyListIncluded = new ArrayList<>();
		for (CompanyTopic companyTopic : topic.getCompanyTopic()) {

			if (!isEmpty(topicStored.getIdTopic())) {
				Optional<CompanyTopic> companyTopicCheckIfExist = topicRepository
						.getCompanyTopicByCompanyAndTopic(companyTopic.getCompany(), topicStored);
				if (companyTopicCheckIfExist.isPresent()) {
					companyTopic = companyTopicCheckIfExist.get();
				}
				topicStored.getCompanyTopic().add(companyTopic);
			}

			// New Company Topic
			if (isEmpty(companyTopic.getIdCompanyTopic())) {
				companyTopic.setCreationDate(new Date());
				companyTopic.setCreatedBy(username);
			}
			companyTopic.setModificationDate(new Date());
			companyTopic.setModifiedBy(username);
			companyTopic.setTopic(topicStored);
			companyTopic.setEnabled(true);

			companyListIncluded.add(companyTopic.getCompany());
		}

		topicStored.setModificationDate(new Date());
		topicStored.setModifiedBy(username);

		topicStored = topicRepository.save(topicStored);

		for (Translation translation : topic.getTranslationList()) {
			if (isEmpty(translation.getIdTranslation())) {
				translation.setEntityId(topicStored.getIdTopic());
				translation.setTablename("co_topic");
			}
			translationRepository.save(translation);
		}

		topicRepository.updateCompanyTopicNotEnable(topic, companyListIncluded);

		updateTopicsCache(topicStored, true);

		return topicStored;
	}

	@Override
	public Optional<Topic> findByIdTopic(Long idTopic) {
		return topicRepository.findById(idTopic);
	}

	@Override
	public void deleteTopic(Long idTopic) {
		try {
			Optional<Topic> topicOptional = findByIdTopic(idTopic);

			if (!topicOptional.isPresent()) {
				throw new NotFoundException();
			}

			// The modification of User
			String username = getTokenUserDetails().getUser().getUsername();

			Topic topic = topicOptional.get();
			// disable the topic
			topic.setEnabled(false);

			for (CompanyTopic companyTopic : topic.getCompanyTopic()) {
				companyTopic.setEnabled(false);
			}

			topic.setModificationDate(new Date());
			topic.setModifiedBy(username);

			topicRepository.save(topic);

			updateTopicsCache(topic, false);
		} catch (Exception e) {
			throw new GeneralException("Company not found");
		}

	}


	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public List<Topic> findAllOrderByDescriptionAsc() {

		List<Topic> topicList = topicRepository.findAllByOrderByDescriptionAsc();

		for (Topic topic : topicList) {
			List<Translation> translationList = translationRepository
					.getTranslationByEntityIdAndTablename(topic.getIdTopic(), "co_topic");
			topic.setTranslationList(translationList);
		}
		return topicList;
	}

	@Override
	public TopicConsultant saveUpdateTopicConsultant(TopicConsultant topicConsultant) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TopicConsultant topicConsultantStored = null;

		// New TopicConsultant
		Optional<TopicConsultant> ifExistTopicConsultant = findByIdTopicConsultant(topicConsultant);
		if(!ifExistTopicConsultant.isPresent()) {
			topicConsultant.setCreationDate(new Date());
			topicConsultant.setCreatedBy(username);
			topicConsultantStored = topicConsultant;
		} else { // Existing TopicConsultant
			topicConsultantStored = ifExistTopicConsultant.get();
		}

		if(!isEmpty(topicConsultant.getCompanyConsultant())) {
			topicConsultantStored.setCompanyConsultant(topicConsultant.getCompanyConsultant());
		}
		if(!isEmpty(topicConsultant.getTopic())) {
			topicConsultantStored.setTopic(topicConsultant.getTopic());
		}

		topicConsultantStored.setEnabled(true);
		topicConsultantStored.setModificationDate(new Date());
		topicConsultantStored.setModifiedBy(username);

		topicConsultantStored = topicConsultantRepository.save(topicConsultantStored);

		updateTopicConsultantsCache(topicConsultantStored, false);

		return topicConsultantStored;
	}

	@Override
	public Optional<TopicConsultant> findByIdTopicConsultant(TopicConsultant topicConsultant) {
		return topicConsultantRepository.findTopicConsultantByIds(topicConsultant.getCompanyConsultant(), topicConsultant.getTopic());
	}

	@Override
	public void deleteTopicConsultant(TopicConsultant topicConsultantToDelete) {
		try {
			Optional<TopicConsultant> topicConsultantOptional = findByIdTopicConsultant(topicConsultantToDelete);

			if (!topicConsultantOptional.isPresent()) {
				throw new NotFoundException();
			}

			// The modification of User
			String username = getTokenUserDetails().getUser().getUsername();

			TopicConsultant topicConsultant = topicConsultantOptional.get();
			// disable the topic
			topicConsultant.setEnabled(false);

			topicConsultant.setModificationDate(new Date());
			topicConsultant.setModifiedBy(username);

			topicConsultantRepository.save(topicConsultant);

			updateTopicConsultantsCache(topicConsultant, false);
		} catch (Exception e) {
			logger.error("Error deleting Topic Consultant", e);
			throw new GeneralException("TopicConsultant not found");
		}

	}

	@Override
	public List<TopicConsultant> findAllOrderByTopicDescription() {
		return topicConsultantRepository.findAllOrderByTopicDescription();
	}

	@Override
	public void deleteTopicConsultants(Company company, Topic topic) {
		List<TopicConsultant> topicConsultants = topicConsultantRepository.findTopicConsultantsByIds(company, topic);
		if(!isEmpty(topicConsultants)) {
			for (TopicConsultant topicConsultant : topicConsultants) {
				deleteTopicConsultant(topicConsultant);
			}
		}
	}

	@Override
	public List<Topic> getTopicsByRole() {
		
		User userLoggedIn = getTokenUserDetails().getUser();
		
		if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			return topicRepository.getTopicsByRoleAdmin();
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			return topicRepository.getTopicsByRoleForeign(userLoggedIn.getUsername());
		} else if(userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_INLAND)) {
			Pageable topOne = PageRequest.of(0, 1);
			return topicRepository.getTopicsByRoleInland(userLoggedIn.getUsername(), topOne);
		}
		return new ArrayList<>();
	}
}
