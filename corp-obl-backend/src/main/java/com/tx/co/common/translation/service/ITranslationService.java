package com.tx.co.common.translation.service;

import java.util.List;

import com.tx.co.common.translation.domain.Translation;

public interface ITranslationService {

	List<Translation> getTranslationByEntityIdAndTablename(Long entityId, String tablename);
	
	List<Translation> getTranslationLikeTablename(String tablename, String lang);
}
