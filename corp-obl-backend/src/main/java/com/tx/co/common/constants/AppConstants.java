package com.tx.co.common.constants;

/**
 * The class contains the static values
 *
 * @author Ardit Azo
 */
public final class AppConstants {

    private AppConstants() {}

    /**
     * List of Routes
     * */
    public static final String APP_PATH = "admin-rest";

    public static final String AUTH = "auth";
    public static final String REFRESH = "refresh";
    
    public static final String LIST = "/list";
    public static final String CREATE_UPDATE = "/create-update";
    public static final String DELETE = "/delete";
    
    // User URL
    public static final String USER = "user";
    public static final String USER_LIST = "list";
    public static final String USER_LIST_EXCEPT = "user-except";
    public static final String USER_BY_USERNAME = "{username}";
    public static final String ME = "me";


    public static final String BACK_OFFICE = "back-office";

    // Company URL
    public static final String COMPANY = "company";
    public static final String COMPANY_GET_BY_ID = COMPANY + "/{idCompany}";
    public static final String COMPANY_LIST = COMPANY + LIST;
    public static final String COMPANY_CREATE_UPDATE = COMPANY + CREATE_UPDATE;
    public static final String COMPANY_DELETE = COMPANY + DELETE;
    public static final String ASSOC_USER_COMPANY = COMPANY + "/assoc-user-company";
    
    // Office URL
    public static final String OFFICE = "office";
    public static final String OFFICE_LIST = OFFICE + LIST;
    public static final String OFFICE_CREATE_UPDATE = OFFICE + CREATE_UPDATE;
    public static final String OFFICE_DELETE = OFFICE + DELETE;
    
    // Topic URL
    public static final String TOPIC = "topic";
    public static final String TOPIC_LIST = TOPIC + LIST;
    public static final String TOPIC_CREATE_UPDATE = TOPIC + CREATE_UPDATE;
    public static final String TOPIC_DELETE = TOPIC + DELETE;
    
    // Consultant URL
    public static final String CONSULTANT = "consultant";
    public static final String CONSULTANT_LIST = CONSULTANT + LIST;
    public static final String CONSULTANT_CREATE_UPDATE = CONSULTANT + CREATE_UPDATE;
    public static final String CONSULTANT_DELETE = CONSULTANT + DELETE;
    
    /**
     * List of authorization Role
     * */
    public static final String ADMIN_ROLE = "CORPOBLIG_ADMIN";
    public static final String USER_ROLE = "CORPOBLIG_USER";
    /* End of authorization Role list */

    /**
     * The key of the cache
     * */
    public static final String USER_LIST_CACHE = "USER_LIST_CACHE";
    public static final String COMPANY_LIST_CACHE = "COMPANY_LIST_CACHE";
    public static final String OFFICE_LIST_CACHE = "OFFICE_LIST_CACHE";
    public static final String TOPIC_LIST_CACHE = "TOPIC_LIST_CACHE";
    public static final String LANGUAGE_LIST_CACHE = "LANGUAGE_LIST_CACHE";
    public static final String STORAGE_DATA_CACHE = "StorageDataCache";
    /* End of the cache key */
}
