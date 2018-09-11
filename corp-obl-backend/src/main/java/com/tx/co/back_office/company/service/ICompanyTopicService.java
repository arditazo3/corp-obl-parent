package com.tx.co.back_office.company.service;

import java.util.List;

import com.tx.co.back_office.company.domain.CompanyTopic;

public interface ICompanyTopicService {

	List<CompanyTopic> getCompanyTopicByIdCompany(String idCompany);
	
	CompanyTopic saveUpdateCompanyTopic(CompanyTopic companyTopic);
	
	void deleteCompanyTopic(Long idCompanyTopic);
}
