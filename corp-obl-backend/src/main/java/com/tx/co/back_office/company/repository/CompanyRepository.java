package com.tx.co.back_office.company.repository;

import com.tx.co.back_office.company.domain.Company;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    @Query("select c from Company c where c.description = ?1 and c.enabled <> 0")
    List<Company> findCompaniesByDescription(String description);

    @Query("select c from Company c where c.enabled <> 0 order by c.description asc ")
    List<Company> findAllByOrderByDescriptionAsc();
}
