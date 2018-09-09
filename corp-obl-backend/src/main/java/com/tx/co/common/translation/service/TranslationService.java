package com.tx.co.common.translation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tx.co.common.translation.domain.Translation;
import com.tx.co.common.translation.repository.TranslationRepository;

/**
 * Service for {@link Translation}s.
 *
 * @author Ardit Azo
 */
@Service
public class TranslationService implements ITranslationService {

	private TranslationRepository translationRepository;
	
	@Autowired
	public void setTranslationRepository(TranslationRepository translationRepository) {
		this.translationRepository = translationRepository;
	}

	@Override
	public List<Translation> getTranslationByEntityIdAndTablename(Long entityId, String tablename) {
		return translationRepository.getTranslationByEntityIdAndTablename(entityId, tablename);
	}

}
