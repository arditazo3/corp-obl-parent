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

    getTaskTemplates(): Observable<any> {

        return this.apiRequest.get(this.appConfig.getTaskTemplates);
    }

    getTaskTemplatesForTable(): Observable<any> {

        return this.apiRequest.get(this.appConfig.getTaskTemplatesForTable);
    }

    saveUpdateTaskTemplate(taskTemplate): Observable<any> {
        console.log('TaskTemplateService - saveUpdateTaskTemplate');

        return this.apiRequest.post(this.appConfig.createUpdateTaskTemplate, taskTemplate);
    }
}
