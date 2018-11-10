package com.tx.co.common.constants;

/**
 * The class contains the static values
 *
 * @author aazo
 */
public final class ApiConstants {

	private ApiConstants() {}

	/**
	 * List of Routes
	 * */
	public static final String APP_PATH = "admin-rest";

	public static final String AUTH = "auth";
	public static final String REFRESH = "refresh";

	public static final String LIST = "/list";
	public static final String CREATE_UPDATE = "/create-update";
	public static final String DELETE = "/delete";
	public static final String BY_ROLE = "/by-role";

	// User URL
	public static final String USER = "user";
	public static final String USER_LIST = "list";
	public static final String USER_LIST_EXCEPT = "user-except";
	public static final String USER_LANGUAGE_ON_CHANGE = "user-language-change";
	public static final String USER_BY_USERNAME = "{username}";
	public static final String ME = "me";
	// Refresh cache URL
	public static final String REFRESH_CACHE = "refresh-cache";

	/**
	 * Back office Resources
	 * */
	public static final String BACK_OFFICE = "back-office";

	// Company URL
	public static final String COMPANY = "company";
	public static final String COMPANY_GET_BY_ID = COMPANY + "/{idCompany}";
	public static final String COMPANY_LIST = COMPANY + LIST;
	public static final String COMPANY_LIST_ROLE = COMPANY + LIST + BY_ROLE;
	public static final String COMPANY_CREATE_UPDATE = COMPANY + CREATE_UPDATE;
	public static final String COMPANY_DELETE = COMPANY + DELETE;
	public static final String ASSOC_USER_COMPANY = COMPANY + "/assoc-user-company";

	// Office URL
	public static final String OFFICE = "office";
	public static final String OFFICE_LIST = OFFICE + LIST;
	public static final String OFFICE_LIST_ROLE = OFFICE + LIST + BY_ROLE;
	public static final String OFFICE_CREATE_UPDATE = OFFICE + CREATE_UPDATE;
	public static final String OFFICE_DELETE = OFFICE + DELETE;

	// Topic URL
	public static final String TOPIC = "topic";
	public static final String TOPIC_LIST = TOPIC + LIST;
	public static final String TOPIC_LIST_ROLE = TOPIC + LIST + BY_ROLE;
	public static final String TOPIC_CREATE_UPDATE = TOPIC + CREATE_UPDATE;
	public static final String TOPIC_DELETE = TOPIC + DELETE;

	// Consultant URL
	public static final String CONSULTANT = "consultant";
	public static final String CONSULTANT_LIST = CONSULTANT + LIST;
	public static final String CONSULTANT_CREATE_UPDATE = CONSULTANT + CREATE_UPDATE;
	public static final String CONSULTANT_DELETE = CONSULTANT + DELETE;

	// Company topic URL
	public static final String TOPIC_CONSULTANT = "topic-consultant";
	public static final String TOPIC_CONSULTANT_LIST = TOPIC_CONSULTANT + LIST;
	public static final String TOPIC_CONSULTANT_CREATE_UPDATE = TOPIC_CONSULTANT + CREATE_UPDATE;
	public static final String TOPIC_CONSULTANT_DELETE = TOPIC_CONSULTANT + DELETE;
	public static final String TOPIC_CONSULTANTS_DELETE = TOPIC_CONSULTANT + DELETE + "/all";

	// Task Template URL
	public static final String TASK_TEMPLATE = "task-template";
	public static final String TASK_TEMPLATE_LIST = TASK_TEMPLATE + LIST;
	public static final String TASK_TEMPLATE_LIST_FOR_TABLE = TASK_TEMPLATE + LIST + "/for-table";
	public static final String TASK_TEMPLATE_CREATE_UPDATE = TASK_TEMPLATE + CREATE_UPDATE;
	public static final String TASK_TEMPLATE_DELETE = TASK_TEMPLATE + DELETE;
	public static final String TASK_TEMPLATE_SEARCH = TASK_TEMPLATE + "/search-task-template";
	public static final String TASK_TEMPLATE_SEARCH_BY_DESCR = TASK_TEMPLATE + "/search-task-template-by-descr";


	// Translate URL
	public static final String TRANSLATION = "translation";
	public static final String TRANSLATION_LIKE_TABLENAME = TRANSLATION + "/like-tablename";

	// Upload files URL
	public static final String UPLOAD_FILES = "upload-files";
	public static final String DOWNLOAD_FILES = "download-files";
	public static final String REMOVE_FILES = "remove-files";

	// Task URL
	public static final String TASK = "task";
	public static final String TASK_LIST = TASK + LIST;
	public static final String TASK_CREATE_UPDATE = TASK + CREATE_UPDATE;
	public static final String SINGLE_TASK_BY_TASKTEMPLATE = TASK + "/by-tasktemplate";
	public static final String TASK_DELETE = TASK + DELETE;
	public static final String TASK_OFFICE_DELETE = TASK + DELETE + "/task-office";

	// Office Task URL
	public static final String OFFICE_TASKS = "office-task";
	public static final String SEARCH_OFFICE_TASKS = OFFICE_TASKS + "/search-office";

	// Task template office URL
	public static final String TASK_TEMPLATE_OFFICE = "tasktemplate-office";

	/** End Back office Resources */

	/**
	 * Front end Resources
	 * */
	public static final String FRONT_END = "front-end";

	public static final String AGENDA = "agenda";
	public static final String SEARCH_AGENDA_TASK = AGENDA + "/search-task";

	// Upload files URL
	public static final String UPLOAD_FILES_EXP = "upload-files-exp";
	public static final String DOWNLOAD_FILES_EXP = "download-files-exp";
	public static final String REMOVE_FILES_EXP = "remove-files-exp";

	public static final String EXP_ACT_SAVE_UPDATE = AGENDA + "/save-update-exp-activ";
	public static final String STATUS_EXPIRATION_ON_CHANGE = AGENDA + "/status-expiration-on-change";
	public static final String STATUS_ALL_EXPIRATION_ON_CHANGE = AGENDA + "/status-all-expiration-on-change";
	public static final String UPDATE_TASK_EXPIRATION = AGENDA + "/update-task-expiration";

	/** End Front end Resources */

	/**
	 * List of authorization Role
	 * */
	public static final String ADMIN_ROLE = "CORPOBLIG_ADMIN";
	public static final String FOREIGN_ROLE = "CORPOBLIG_BACKOFFICE_FOREIGN";
	public static final String INLAND_ROLE = "CORPOBLIG_BACKOFFICE_INLAND";
	public static final String USER_ADMIN = "CORPOBLIG_USER_ADMIN_COMPANY";
	public static final String USER_ROLE = "CORPOBLIG_USER";

	public static final String hasAuthority = "hasAuthority";
	public static final String orHasAuthority = "or " + hasAuthority;
	public static final String AUTH_ADMIN_FOREIGN_INLAND = hasAuthority + "('"+ ADMIN_ROLE + "') " + orHasAuthority + "('" + FOREIGN_ROLE + "') " + orHasAuthority + "('" + INLAND_ROLE + "')";
	public static final String AUTH_ADMIN_FOREIGN_INLAND_USER_ADMIN = hasAuthority + "('"+ ADMIN_ROLE + "') " + orHasAuthority + "('" + FOREIGN_ROLE + "') " + orHasAuthority + "('" + INLAND_ROLE + "') " + orHasAuthority + "('" + USER_ADMIN + "')";
	public static final String AUTH_ADMIN_USER = hasAuthority + "('"+ ADMIN_ROLE + "') " + orHasAuthority + "('" + USER_ROLE + "')";
	public static final String AUTH_ADMIN = hasAuthority + "('"+ ADMIN_ROLE + "')";
	/* End of authorization Role list */

	/**
	 * The key of the cache
	 * */
	public static final String USER_LIST_CACHE = "USER_LIST_CACHE";
	public static final String COMPANY_LIST_CACHE = "COMPANY_LIST_CACHE";
	public static final String OFFICE_LIST_CACHE = "OFFICE_LIST_CACHE";
	public static final String TOPIC_LIST_CACHE = "TOPIC_LIST_CACHE";
	public static final String TOPIC_CONSULTANT_LIST_CACHE = "TOPIC_CONSULTANT_LIST_CACHE";
	public static final String COMPANY_CONSULTANT_LIST_CACHE = "COMPANY_CONSULTANT_LIST_CACHE";
	public static final String LANGUAGE_LIST_CACHE = "LANGUAGE_LIST_CACHE";
	public static final String LANGUAGE_NOT_AVAILABLE_LIST_CACHE = "LANGUAGE_NOT_AVAILABLE_LIST_CACHE";
	public static final String TASK_TEMPLATE_LIST_CACHE = "TASK_TEMPLATE_LIST_CACHE";
	public static final String TASK_LIST_CACHE = "TASK_LIST_CACHE";
	public static final String TASK_TEMPLATE_ATTACHMENT_LIST_CACHE = "TASK_TEMPLATE_ATTACHMENT_LIST_CACHE";
	public static final String TRANSLATION_LIST_CACHE = "TRANSLATION_LIST_CACHE";

	public static final String STORAGE_DATA_CACHE = "StorageDataCache";
	/* End of the cache key */


	/* File static values */
	public static final String FILE_MAX_SIZE = "20";

}
