import {Injectable} from '@angular/core';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {AppConfig} from '../../../shared/common/api/app-config';
import {Observable} from '../../../../../node_modules/rxjs/Rx';

@Injectable()
export class OfficeTaskService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {}

    searchOffice(objectSearchDescrTaskTempOfficies): Observable<any> {
        console.log('OfficeTaskService - searchOffice');

        return this.apiRequest.post(this.appConfig.searchOfficeTask, objectSearchDescrTaskTempOfficies);
    }
}