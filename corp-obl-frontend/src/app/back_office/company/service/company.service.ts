import {Injectable} from '@angular/core';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {AppConfig} from '../../../shared/common/api/app-config';
import {Observable} from 'rxjs';

@Injectable()
export class CompanyService {

  constructor(
    private apiRequest: ApiRequestService,
    private appConfig: AppConfig
  ) {}

  getCompanies(): Observable<any> {
    console.log('CompanyService - getCompanies');

    return this.apiRequest.get(this.appConfig.getCompanies);
  }

    getCompaniesByRole(): Observable<any> {
        console.log('CompanyService - getCompaniesByRole');

        return this.apiRequest.get(this.appConfig.getCompaniesByRole);
    }

  saveUpdateCompany(company): Observable<any> {
    return this.apiRequest.post(this.appConfig.createUpdateCompany, company);
  }

  deleteCompany(company): Observable<any> {
    console.log('CompanyService - deleteCopany');

    return this.apiRequest.put(this.appConfig.deleteCompany, company);
  }

  saveAssociationCompanyUsers(usersCompany): Observable<any> {
      console.log('CompanyService - saveAssociationCompanyUsers');

      return this.apiRequest.post(this.appConfig.assocCompanyUsers, usersCompany);
  }
}
