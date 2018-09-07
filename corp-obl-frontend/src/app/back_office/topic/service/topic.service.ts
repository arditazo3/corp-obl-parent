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

    deleteTopic(topic): Observable<any> {
        console.log('TopicService - deleteTopic');

        return this.apiRequest.put(this.appConfig.deleteTopic, topic);
    }
}