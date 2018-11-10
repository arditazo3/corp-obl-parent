import {Injectable} from '@angular/core';

@Injectable()
export class AppConfig {

    // Provide all the Application Configs here
    public version = '1.0.0';
    public locale = 'en-US';

    // API Related configs
    public apiPort = '8100';
    public apiProtocol: string;
    public apiHostName: string;
    public apiBasePath = 'admin-rest';
    public baseApiPath: string;

    public list = '/list';
    public delete = '/delete';
    public createupdate = '/create-update';

    // User
    public userPath = 'user';
    public userList = this.userPath + this.list;
    public userListExceptRole = this.userPath + '/user-except';
    public userLanguangeOnChange = this.userPath + '/user-language-change';

    public refreshCache = this.userPath + '/refresh-cache';

    /**
     * Back office
     * */
    public backOfficePath = 'back-office';

    // Company URL's
    public companyPath = '/company';
    public getCompanies = this.backOfficePath + this.companyPath + this.list;                       // GET
    public getCompaniesByRole = this.backOfficePath + this.companyPath + this.list + '/by-role';    // GET
    public createUpdateCompany = this.backOfficePath + this.companyPath + this.createupdate;        // POST
    public deleteCompany = this.backOfficePath + this.companyPath + this.delete;                    // PUT
    public assocCompanyUsers = this.backOfficePath + this.companyPath + '/assoc-user-company';      // POST

    // Office URL's
    public officePath = '/office';
    public getOffices = this.backOfficePath + this.officePath + this.list;                      // GET
    public getOfficesByRole = this.backOfficePath + this.officePath + this.list + '/by-role';    // GET
    public createUpdateOffice = this.backOfficePath + this.officePath + this.createupdate;      // POST
    public deleteOffice = this.backOfficePath + this.officePath + this.delete;                  // PUT

    // Topic URL's
    public topicPath = '/topic';
    public getTopics = this.backOfficePath + this.topicPath + this.list;                        // GET
    public getTopicsByRole = this.backOfficePath + this.topicPath + this.list + '/by-role';     // GET
    public deleteTopic = this.backOfficePath + this.topicPath + this.delete;                    // PUT
    public createUpdateTopic = this.backOfficePath + this.topicPath + this.createupdate;        // POST

    // Company Consultant URL's
    public consultantPath = '/consultant';
    public getConsultants = this.backOfficePath + this.consultantPath + this.list;                          // GET
    public deleteConsultant = this.backOfficePath + this.consultantPath + this.delete;                      // PUT
    public createUpdateConsultant = this.backOfficePath + this.consultantPath + this.createupdate;          // POST

    // Company Topic URL's
    public topicConsutantPath = '/topic-consultant';
    public getTopicConsultant = this.backOfficePath + this.topicConsutantPath + this.list;                           // GET
    public deleteTopicConsultant = this.backOfficePath + this.topicConsutantPath + this.delete;                      // PUT
    public deleteTopicConsultants = this.backOfficePath + this.topicConsutantPath + this.delete + '/all';            // PUT
    public createUpdateTopicConsultant = this.backOfficePath + this.topicConsutantPath + this.createupdate;          // POST

    // Task template URL's
    public taskTemplatePath = '/task-template';
    public getTaskTemplates = this.backOfficePath + this.taskTemplatePath + this.list;                                  // GET
    public getTaskTemplatesForTable = this.backOfficePath + this.taskTemplatePath + this.list + '/for-table';           // GET
    public deleteTaskTemplate = this.backOfficePath + this.taskTemplatePath + this.delete;                              // PUT
    public createUpdateTaskTemplate = this.backOfficePath + this.taskTemplatePath + this.createupdate;                  // POST
    public searchTaskTemplate = this.backOfficePath + this.taskTemplatePath + '/search-task-template';                  // POST
    public searchTaskTemplateByDescr = this.backOfficePath + this.taskTemplatePath + '/search-task-template-by-descr';  // POST

    // Translation URL's
    public translationPath = '/translation';
    public getTranslationsLikeTablename = this.backOfficePath + this.translationPath + '/like-tablename';     // GET

    // File upload URL's
    public uploadFile = this.backOfficePath + '/upload-files';              // POST
    public downloadFile = this.backOfficePath + '/download-files';          // POST
    public removeFile = this.backOfficePath + '/remove-files';              // PUT

    // Task URL's
    public taskPath = '/task';
    public getSingleTaskByTaskTemplate = this.backOfficePath +
        this.taskPath + '/by-tasktemplate';                                      // GET

    public getTasks = this.backOfficePath + this.taskPath + this.list;                                      // GET
    public createUpdateTask = this.backOfficePath + this.taskPath + this.createupdate;                      // POST
    public deleteTask = this.backOfficePath + this.taskPath + this.delete;                                  // PUT
    public deleteTaskOffice = this.backOfficePath + this.taskPath + this.delete + '/task-office';           // PUT

    // Task template office URL
    public taskTemplateOffice = this.backOfficePath + '/tasktemplate-office';

    // Office task URL's
    public officeTaskPath = '/office-task';
    public searchOfficeTask = this.backOfficePath + this.officeTaskPath + '/search-office';
    /** End Back Office */

    /**
     * Front end
     * */
    public frontEndPath = 'front-end';

    // File upload Exp
    public uploadFileExp = this.frontEndPath + '/upload-files-exp';             // POST
    public downloadFileExp = this.frontEndPath + '/download-files-exp';         // POST
    public removeFileExp = this.frontEndPath + '/remove-files-exp';             // POST

    // Expiration
    public agenda = '/agenda';
    public expirationSearchTaskTemplateOfficeArchived = this.frontEndPath + this.agenda + '/search-task';
    public saveUpdateExpirationActivity = this.frontEndPath + this.agenda + '/save-update-exp-activ';
    public updateTaskExpiration = this.frontEndPath + this.agenda + '/update-task-expiration';
    public statusExpirationOnChange = this.frontEndPath + this.agenda + '/status-expiration-on-change';
    public statusAllExpirationsOnChange = this.frontEndPath + this.agenda + '/status-all-expiration-on-change';


    constructor() {
        console.log('AppConfig - constructor');

        if (this.apiProtocol === undefined) {
            this.apiProtocol = window.location.protocol;
        }
        if (this.apiHostName === undefined) {
            this.apiHostName = window.location.hostname;
        }
        if (this.apiPort === undefined) {
            this.apiPort = window.location.port;
        } else {
            this.baseApiPath = this.apiProtocol + '//' + this.apiHostName + ':' + this.apiPort + '/' + this.apiBasePath + '/';
        }
        if (this.locale === undefined) {
            this.locale = navigator.language;
        }
    }
}
