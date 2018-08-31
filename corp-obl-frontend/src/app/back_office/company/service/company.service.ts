import {Injectable} from "@angular/core";
import {ApiRequestService} from "../../../shared/common/service/api-request.service";
import {Observable, Subject} from "rxjs/Rx";
import {AppConfig} from "../../../shared/common/api/app-config";

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

  deleteCompany(idCompany): void {
    console.log('CompanyService - deleteCopany');

    let body = { idCompany: idCompany};

    this.apiRequest.put(this.appConfig.deleteCompany, body);
  }
}
