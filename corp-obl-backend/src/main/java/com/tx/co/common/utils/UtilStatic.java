package com.tx.co.common.utils;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.topic.domain.Topic;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.List;

public class UtilStatic {

	private UtilStatic() {}

	public static int getIndexByPropertyCompanyList(Long idCompany, List<Company> comparableList) {

		if(!isEmpty(comparableList)) {
			for (int i = 0; i < comparableList.size(); i++) {
				Company company = comparableList.get(i);

				if (company !=null && company.getIdCompany().compareTo(idCompany) == 0) {
					return i;
				}
			}
		}
		return -1;// not there is list
	}

	public static int getIndexByPropertyOfficeList(Long idOffice, List<Office> comparableList) {

		if(!isEmpty(comparableList)) {
			for (int i = 0; i < comparableList.size(); i++) {
				Office office = comparableList.get(i);

				if (office !=null && office.getIdOffice().compareTo(idOffice) == 0) {
					return i;
				}
			}
		}
		return -1;// not there is list
	}

	public static int getIndexByPropertyTopicList(Long idTopic, List<Topic> comparableList) {

		if(!isEmpty(comparableList)) {
			for (int i = 0; i < comparableList.size(); i++) {
				Topic topic = comparableList.get(i);

				if (topic !=null && topic.getIdTopic().compareTo(idTopic) == 0) {
					return i;
				}
			}
		}
		return -1;// not there is list
	}

}
