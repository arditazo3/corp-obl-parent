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
    public static final String COMPANY_LIST = COMPANY + "/list";
    public static final String COMPANY_CREATE_UPDATE = COMPANY + "/create-update";
    public static final String COMPANY_EDIT = COMPANY + "/edit";
    public static final String COMPANY_DELETE = COMPANY + "/delete";
    public static final String ASSOC_USER_COMPANY = COMPANY + "/assoc-user-company";
    
    // Office URL
    /* End list of Routes */
    public static final String OFFICE = "office";
    public static final String OFFICE_LIST = OFFICE + "/list";
    public static final String OFFICE_EDIT = OFFICE + "/edit";
    public static final String OFFICE_DELETE = OFFICE + "/delete";
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
    public static final String LANGUAGE_LIST_CACHE = "LANGUAGE_LIST_CACHE";
    public static final String STORAGE_DATA_CACHE = "StorageDataCache";
    /* End of the cache key */
}
