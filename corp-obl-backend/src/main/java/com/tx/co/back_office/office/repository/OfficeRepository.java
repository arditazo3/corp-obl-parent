package com.tx.co.back_office.office.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tx.co.back_office.office.domain.Office;

public interface OfficeRepository extends CrudRepository<Office, Long> {

	@Query("select o from Office o where o.enabled <> 0 order by o.description asc ")
	List<Office> findAllByOrderByDescriptionAsc();

	@Query("select o from Office o where o.description = ?1 and o.enabled <> 0")
	List<Office> findOfficesByDescription(String description);

}
