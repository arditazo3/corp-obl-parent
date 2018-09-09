import {AppConfig} from '../../../shared/common/api/app-config';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {Injectable} from '@angular/core';
import {Observable} from '../../../../../node_modules/rxjs/Rx';
import {AuthorityEnum} from '../../../shared/common/api/enum/authority.enum';
import {HttpParams} from '@angular/common/http';

@Injectable()
export class CompanyConsultantService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {}

    getCompanyConsultant(company): Observable<any> {

        let httpParms: HttpParams = new HttpParams().set('idCompany', company.idCompany.toString());

        return this.apiRequest.get(this.appConfig.getCompanyConsultant, httpParms);
    }

    deleteCompanyConsultant(companyConsultant): Observable<any> {
        console.log('CompanyService - deleteCopany');

        return this.apiRequest.put(this.appConfig.deleteCompanyConsultant, companyConsultant);
    }
}