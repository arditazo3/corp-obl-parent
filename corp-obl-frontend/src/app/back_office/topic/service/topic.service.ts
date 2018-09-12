import {Injectable} from '@angular/core';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {AppConfig} from '../../../shared/common/api/app-config';
import {Observable} from '../../../../../node_modules/rxjs/Rx';

@Injectable()
export class TopicService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {
    }

    getTopics(): Observable<any> {
        console.log('TopicService - getTopics');

        return this.apiRequest.get(this.appConfig.getTopics);
    }

    saveUpdateTopic(topic): Observable<any> {
        console.log('TopicService - saveUpdateTopic');

        return this.apiRequest.post(this.appConfig.createUpdateTopic, topic);
    }

    deleteTopic(topic): Observable<any> {
        console.log('TopicService - deleteTopic');

        return this.apiRequest.put(this.appConfig.deleteTopic, topic);
    }

    saveUpdateTopicConsultant(topicConsultant): Observable<any> {
        console.log('ConsultantService - saveUpdateTopicConsultant');

        return this.apiRequest.post(this.appConfig.createUpdateTopicConsultant, topicConsultant);
    }

    deleteTopicConsultant(topicConsultant): Observable<any> {
        console.log('ConsultantService - deleteTopicConsultant');

        return this.apiRequest.put(this.appConfig.deleteTopicConsultant, topicConsultant);
    }

    deleteTopicConsultants(topicConsultant): Observable<any> {
        console.log('ConsultantService - deleteTopicConsultants');

        return this.apiRequest.put(this.appConfig.deleteTopicConsultants, topicConsultant);
    }
}