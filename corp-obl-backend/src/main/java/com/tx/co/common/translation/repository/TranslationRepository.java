package com.tx.co.common.translation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tx.co.common.translation.domain.Translation;

public interface TranslationRepository extends CrudRepository<Translation, Long>{

	@Query("select t from Translation t where t.entityId = ?1 and t.tablename = ?2")
	List<Translation> getTranslationByEntityIdAndTablename(Long entityId, String tablename);
	
	@Query("select t from Translation t where t.tablename like ?1 and t.lang = ?2 order by t.entityId asc")
	List<Translation> getTranslationLikeTablename(String tablename, String lang);
}
