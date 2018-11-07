package com.tx.co.common.utils;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.company.domain.CompanyConsultant;
import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplateattachment.model.TaskTemplateAttachment;
import com.tx.co.back_office.topic.domain.Topic;
import com.tx.co.back_office.topic.domain.TopicConsultant;
import com.tx.co.front_end.expiration.api.model.ExpirationDetailResult;
import com.tx.co.front_end.expiration.api.model.TaskOfficeExpirationsResult;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.domain.ExpirationActivity;
import com.tx.co.front_end.expiration.enums.StatusExpirationEnum;
import com.tx.co.user.domain.User;

import static org.springframework.util.ObjectUtils.isEmpty;
import static com.tx.co.common.constants.AppConstants.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UtilStatic {

	private static final Logger logger = LogManager.getLogger(UtilStatic.class);
	
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

	public static int getIndexByPropertyTopicLConsultantist(Long idTopicConsultant, List<TopicConsultant> comparableList) {

		if(!isEmpty(comparableList)) {
			for (int i = 0; i < comparableList.size(); i++) {
				TopicConsultant topicConsultant = comparableList.get(i);

				if (topicConsultant !=null && topicConsultant.getIdTopicConsultant().compareTo(idTopicConsultant) == 0) {
					return i;
				}
			}
		}
		return -1;// not there is list
	}

	public static int getIndexByPropertyCompanyConsultantList(Long idCompanyConsultant, List<CompanyConsultant> comparableList) {

		if(!isEmpty(comparableList)) {
			for (int i = 0; i < comparableList.size(); i++) {
				CompanyConsultant companyConsultant = comparableList.get(i);

				if (companyConsultant !=null && companyConsultant.getIdCompanyConsultant().compareTo(idCompanyConsultant) == 0) {
					return i;
				}
			}
		}
		return -1;// not there is list
	}

	public static int getIndexByPropertyTaskTemplateList(Long idTaskTemplate, List<TaskTemplate> comparableList) {

		if(!isEmpty(comparableList)) {
			for (int i = 0; i < comparableList.size(); i++) {
				TaskTemplate taskTemplate = comparableList.get(i);

				if (taskTemplate !=null && taskTemplate.getIdTaskTemplate().compareTo(idTaskTemplate) == 0) {
					return i;
				}
			}
		}
		return -1;// not there is list
	}

	public static int getIndexByPropertyTaskTemplateListAttachment(Long idTaskTemplateAttachment, List<TaskTemplateAttachment> comparableList) {

		if(!isEmpty(comparableList)) {
			for (int i = 0; i < comparableList.size(); i++) {
				TaskTemplateAttachment taskTemplateAttachment = comparableList.get(i);

				if (taskTemplateAttachment !=null && taskTemplateAttachment.getIdTaskTemplateAttachment().compareTo(idTaskTemplateAttachment) == 0) {
					return i;
				}
			}
		}
		return -1;// not there is list
	}
	
	public static int getIndexByPropertyUserList(String username, List<User> comparableList) {

		if(!isEmpty(comparableList)) {
			for (int i = 0; i < comparableList.size(); i++) {
				User user = comparableList.get(i);

				if (user !=null && user.getUsername().equalsIgnoreCase(username)) {
					return i;
				}
			}
		}
		return -1;// not there is list
	}


	public static String formatDateToString(Date date) {

		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date (day/month/year)
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		// representation of a date with the defined format.
		return df.format(date);
	}
	
	public static String formatHoursMinutesToString(Date date) {

		// Create an instance of SimpleDateFormat used for formatting 
		// the string representation of date (hour:nminute)
		DateFormat df = new SimpleDateFormat("HH:mm");

		// representation of a date with the defined format.
		return df.format(date);
	}

	public static Date formatStringToDate(String dateString) {

		if(isEmpty(dateString)) {
			return null;
		}
		
		SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		try {
			return simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
			logger.error(e);
			return null;
		}
	}
	
	public static String buildColor(TaskOfficeExpirationsResult taskExpiration) {

		Integer totalExpirations = taskExpiration.getTotalExpirations();
		Integer totalCompleted = taskExpiration.getTotalCompleted();
		Integer totalArchived = taskExpiration.getTotalArchived();

		String colorDefined = "";
		if(totalExpirations > 0) {
			if(totalExpirations.compareTo(totalArchived) == 0) {
				colorDefined = ALERT_PRIMARY;
			} else if(totalExpirations.compareTo(totalCompleted) == 0) {
				colorDefined = "alert alert-success";
			} else if(totalCompleted == 0) {
				colorDefined = "";
			} else if(totalExpirations.compareTo(totalCompleted) > 0) {
				colorDefined = "alert alert-warning";
			}
		} else {
			colorDefined = "";
		}

		return colorDefined;
	}

	public static ExpirationDetailResult buildExpirationDetail(Expiration expiration) {

		ExpirationDetailResult expirationDetail = new ExpirationDetailResult();
		String colorDefined = "";
		String descriptionDate = "";
		StatusExpirationEnum statusExpiration = StatusExpirationEnum.BASE;
		
		Date dateNow = new Date();
		Date completed = expiration.getCompleted();
		Date approved = expiration.getApproved();
		Date registred = expiration.getRegistered();
		Date expirationDate = expiration.getExpirationDate();
		
		// base
		if(isEmpty(completed) && isEmpty(approved) && isEmpty(registred)) {
			colorDefined = "";
			statusExpiration = StatusExpirationEnum.BASE;
		// archived	
		} else if(!isEmpty(registred)) {
			colorDefined = ALERT_PRIMARY;
			descriptionDate = "Archived at " + formatDateToString(registred);
			statusExpiration = StatusExpirationEnum.ARCHIVED;
			// completed	
		} else if(!isEmpty(completed) && isEmpty(approved) && isEmpty(registred)) {
			colorDefined = "success";
			descriptionDate = "Completed at " + formatDateToString(completed);
			statusExpiration = StatusExpirationEnum.COMPLETED;
			// approved 	
		} else if(!isEmpty(completed) && !isEmpty(approved) && isEmpty(registred)) {
			colorDefined = ALERT_PRIMARY;
			descriptionDate = "Approved at " + formatDateToString(approved);
			statusExpiration = StatusExpirationEnum.APPROVED;
			// expired and non completed  	
		} else if(!isEmpty(expirationDate) && expirationDate.before(dateNow) &&
				isEmpty(completed)) {
			colorDefined = "warning";
			// not completed but not expired	
		} else {
			colorDefined = "";
		}

		expirationDetail.setColorDefined(colorDefined);
		expirationDetail.setExpirationDescriptionDate(descriptionDate);
		expirationDetail.setStatusExpiration(statusExpiration);
		expirationDetail.setStatusExpiration(statusExpiration);
		
		return expirationDetail;
	}
	
	public static String buildDescriptionLastActivity(ExpirationActivity expirationActivity) {
		
		String descriptionLastActivity = "";
		String userModify = expirationActivity.getModifiedBy();
		Date lastModify = expirationActivity.getModificationDate();
		
		if(!isEmpty(userModify) && !isEmpty(lastModify)) {
			descriptionLastActivity += userModify + ", " + formatDateToString(lastModify) + ", at " + formatHoursMinutesToString(lastModify) + " wrote";	
		}
		
		return descriptionLastActivity;
	}
	
}
