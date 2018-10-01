import {AppConfig} from '../../../shared/common/api/app-config';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpParams} from '@angular/common/http';

@Injectable()
export class ConsultantService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {}

    getCompanyConsultant(company): Observable<any> {

        const httpParms: HttpParams = new HttpParams().set('idCompany', company.idCompany.toString());

        return this.apiRequest.get(this.appConfig.getConsultants, httpParms);
    }

    saveUpdateConsultant(consultant): Observable<any> {
        console.log('ConsultantService - saveUpdateConsultant');

        return this.apiRequest.post(this.appConfig.createUpdateConsultant, consultant);
    }

    deleteCompanyConsultant(companyConsultant): Observable<any> {
        console.log('ConsultantService - deleteConsultant');

        return this.apiRequest.put(this.appConfig.deleteConsultant, companyConsultant);
    }

    getCompanyTopic(company): Observable<any> {
        console.log('ConsultantService - getCompanyTopic');

        const httpParms: HttpParams = new HttpParams().set('idCompany', company.idCompany.toString());

        return this.apiRequest.get(this.appConfig.getTopicConsultant, httpParms);
    }
}
