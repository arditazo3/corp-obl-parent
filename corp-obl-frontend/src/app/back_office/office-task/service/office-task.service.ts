import {Injectable} from '@angular/core';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {AppConfig} from '../../../shared/common/api/app-config';
import {Observable} from 'rxjs';

@Injectable()
export class OfficeTaskService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {}

    searchOfficeTaskTemplates(objectSearchDescrTaskTempOffices): Observable<any> {
        return this.apiRequest.post(this.appConfig.searchOfficeTask, objectSearchDescrTaskTempOffices);
    }
}
