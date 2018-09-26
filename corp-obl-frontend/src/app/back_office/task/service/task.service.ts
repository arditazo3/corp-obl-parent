import {Injectable} from '@angular/core';
import {AppConfig} from '../../../shared/common/api/app-config';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {Observable} from '../../../../../node_modules/rxjs/Rx';
import {HttpParams} from '@angular/common/http';
import {TaskObjectTable} from '../model/taskobjecttable';

@Injectable()
export class TaskService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {}

    saveUpdateTask(task): Observable<any> {
        console.log('TaskTemplateService - saveUpdateTask');

        return this.apiRequest.post(this.appConfig.createUpdateTask, task);
    }

    getTasks(): Observable<any> {

        return this.apiRequest.get(this.appConfig.getTasks);
    }

    getTasksByDescriptionOrCompaniesOrTopics(taskObjectTable): Observable<any> {

        return this.apiRequest.post(this.appConfig.getTaskByDescrOrCompOrTopic, taskObjectTable);
    }
}
