import {Injectable} from '@angular/core';

@Injectable()
export class AppConfig {

  // Provide all the Application Configs here
  public version = '1.0.0';
  public locale = 'en-US';
  public dateFormat = {day: 'numeric', month: 'short', year: 'numeric'};

  // API Related configs
  public apiPort = '8060';
  public apiProtocol: string;
  public apiHostName: string;
  public apiBasePath = 'admin-rest';
  public baseApiPath: string;

  // User
  public userPath = 'user';
  public userList = this.userPath + '/user';

  // Back office
  public backOfficePath = 'back-office';

  // Company URL's
  public companyPath = '/company';
  public getCompanies = this.backOfficePath + this.companyPath + '/list';                 // GET
  public getCompanyById = this.backOfficePath + this.companyPath + '/{idCompany}';        // GET
  public createCompany = this.backOfficePath + this.companyPath + '/create';              // POST
  public editCompany = this.backOfficePath + this.companyPath + '/edit';                  // PUT
  public deleteCompany = this.backOfficePath + this.companyPath + '/delete';  // PUT


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
