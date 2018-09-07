package com.tx.co.back_office.topic.service;


import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.repository.TopicRepository;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.exception.GeneralException;

/**
 * Service for {@link com.tx.co.back_topic.topic.domain.Topic}s.
 *
 * @author Ardit Azo
 */
@Service
public class TopicService extends UpdateCacheData implements ITopicService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(TopicService.class);

	private TopicRepository topicRepository;
	
	@Autowired
	public void setTopicRepository(TopicRepository topicRepository) {
		this.topicRepository = topicRepository;
	}

	@Override
	public List<Topic> findAllTopic() {
		List<Topic> topicList = new ArrayList<>();

		List<Topic> topicListFromCache = getTopicsFromCache();
		if(!isEmpty(getTopicsFromCache())) {
			topicList = topicListFromCache;
		} else {
			topicRepository.findAllByOrderByDescriptionAsc().forEach(topicList::add);
		}

		logger.info("The number of the topics: " + topicList.size());

		return topicList;
	}

	@Override
	public Topic saveUpdateTopic(Topic topic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Topic> findByIdTopic(Long idTopic) {
		return topicRepository.findById(idTopic);
	}

	@Override
	public void deleteTopic(Long idTopic) {
		try {
            Optional<Topic> topicOptional = findByIdTopic(idTopic);

            if(!topicOptional.isPresent()) {
                throw new NotFoundException();
            }

            Topic topic = topicOptional.get();
            // disable the company
            topic.setEnabled(false);

            topicRepository.save(topic);

            updateOffficesCache(topic, false);
        } catch (Exception e) {
            throw new GeneralException("Company not found");
        }
		
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
	}
	
}
