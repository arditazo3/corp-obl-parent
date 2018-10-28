package com.tx.co.back_office.company.service;

import java.util.List;

import com.tx.co.back_office.company.domain.CompanyTopic;

public interface ICompanyTopicService {

	/**
	 * @param idCompany
	 * @return
	 */
	List<CompanyTopic> getCompanyTopicByIdCompany(String idCompany);
	
	void deleteCompanyTopic(Long idCompanyTopic);
}
