import {Injectable} from '@angular/core';
import {AppConfig} from '../../../shared/common/api/app-config';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {Observable} from 'rxjs';

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

    getTaskOfficeByTaskTemplateAndOffice(taskTemplate, office): Observable<any> {

        const taskTemplateOffice = {taskTemplate: taskTemplate, office: office};

        return this.apiRequest.post(this.appConfig.taskTemplateOffice, taskTemplateOffice);
    }

    deleteTask(task): Observable<any> {

        return this.apiRequest.put(this.appConfig.deleteTask, task);
    }

    getSingleTaskByTaskTemplate(taskTemplate): Observable<any> {

        return this.apiRequest.post(this.appConfig.getSingleTaskByTaskTemplate, taskTemplate);
    }
}
