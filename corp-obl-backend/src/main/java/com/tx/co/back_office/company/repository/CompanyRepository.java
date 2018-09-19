package com.tx.co.back_office.company.repository;

import com.tx.co.back_office.company.domain.Company;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    @Query("select c from Company c where c.description = ?1 and c.enabled <> 0")
    List<Company> findCompaniesByDescription(String description);

    @Query("select c from Company c where c.enabled <> 0 order by c.description asc ")
    List<Company> findAllByOrderByDescriptionAsc();
    
    @Query("select c from Company c "
			+ "left join c.companyUsers cu "
			+ "where c.enabled <> 0 and cu.enabled <> 0 "
			+ "and cu.username = :username "
			+ "group by c.idCompany "
			+ "order by c.description asc")
    List<Company> getCompaniesByRoleForeign(@Param("username") String username);
}
