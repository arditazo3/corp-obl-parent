import {Injectable} from '@angular/core';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {AppConfig} from '../../../shared/common/api/app-config';
import {Observable} from '../../../../../node_modules/rxjs/Rx';

@Injectable()
export class OfficeService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {}

    getOffices(): Observable<any> {
        console.log('OfficeService - getOffices');

        return this.apiRequest.get(this.appConfig.getOffices);
    }

    getOfficesByRole(): Observable<any> {
        console.log('OfficeService - getOffices');

        return this.apiRequest.get(this.appConfig.getOffices);
    }

    saveUpdateOffice(office): Observable<any> {
        console.log('OfficeService - saveUpdateOffice');

        return this.apiRequest.post(this.appConfig.createUpdateOffice, office);
    }

    deleteOffice(office): Observable<any> {
        console.log('OfficeService - deleteOffice');

        return this.apiRequest.put(this.appConfig.deleteOffice, office);
    }
}