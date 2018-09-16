import {Injectable} from '@angular/core';
import {AppConfig} from '../../../shared/common/api/app-config';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {Observable} from '../../../../../node_modules/rxjs/Rx';

@Injectable()
export class TaskTemplateService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {}

    saveUpdateTaskTemplate(taskTemplate): Observable<any> {
        console.log('TaskTemplateService - saveUpdateTaskTemplate');

        return this.apiRequest.post(this.appConfig.createUpdateTaskTemplate, taskTemplate);
    }
}
