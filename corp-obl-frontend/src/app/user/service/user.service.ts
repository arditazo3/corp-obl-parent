import {Observable} from 'rxjs/Rx';
import {AppConfig} from '../../shared/common/api/app-config';
import {ApiRequestService} from '../../shared/common/service/api-request.service';
import {Injectable} from '@angular/core';
import {HttpParams} from '@angular/common/http';
import {AuthorityEnum} from '../../shared/common/api/enum/authority.enum';

@Injectable()
export class UserService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {
    }

    getAllUsersExceptAdminRole(): Observable<any> {

        let httpParms: HttpParams = new HttpParams().set('role', AuthorityEnum[AuthorityEnum.CORPOBLIG_ADMIN]);

        return this.apiRequest.get(this.appConfig.userListExceptRole, httpParms);
    }

    getAllUsers(): Observable<any> {

        return this.apiRequest.get(this.appConfig.userList);
    }
}
