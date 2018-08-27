package com.tx.co.back_office.company.repository;

import com.tx.co.back_office.company.domain.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {

}
