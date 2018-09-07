import {Injectable} from '@angular/core';

@Injectable()
export class AppConfig {

    // Provide all the Application Configs here
    public version = '1.0.0';
    public locale = 'en-US';
    public dateFormat = {day: 'numeric', month: 'short', year: 'numeric'};

    // API Related configs
    public apiPort = '8100';
    public apiProtocol: string;
    public apiHostName: string;
    public apiBasePath = 'admin-rest';
    public baseApiPath: string;

    // User
    public userPath = 'user';
    public userList = this.userPath + '/list';
    public userListExceptRole = this.userPath + '/user-except';

    // Back office
    public backOfficePath = 'back-office';

    // Company URL's
    public companyPath = '/company';
    public getCompanies = this.backOfficePath + this.companyPath + '/list';                 // GET
    public getCompanyById = this.backOfficePath + this.companyPath + '/{idCompany}';        // GET
    public createUpdateCompany = this.backOfficePath + this.companyPath + '/create-update'; // POST
    public deleteCompany = this.backOfficePath + this.companyPath + '/delete';              // PUT
    public assocCompanyUsers = this.backOfficePath + this.companyPath + '/assoc-user-company';  // POST

    // Office URL's
    public officePath = '/office';
    public getOffices = this.backOfficePath + this.officePath + '/list';                      // GET
    public createUpdateOffice = this.backOfficePath + this.officePath + '/create-update';     // POST
    public deleteOffice = this.backOfficePath + this.officePath + '/delete';                  // PUT

    // Topic URL's
    public topicPath = '/topic';
    public getTopics = this.backOfficePath + this.topicPath + '/list';                      // GET
    public deleteTopic = this.backOfficePath + this.topicPath + '/delete';                  // PUT
    public createUpdateTopic = this.backOfficePath + this.topicPath + '/create-update';     // POST

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
