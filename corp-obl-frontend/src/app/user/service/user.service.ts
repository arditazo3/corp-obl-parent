import {Observable} from 'rxjs/Rx';
import {AppConfig} from '../../shared/common/api/app-config';
import {ApiRequestService} from '../../shared/common/service/api-request.service';

export class UserService {

  constructor(
    private apiRequest: ApiRequestService,
    private appConfig: AppConfig
  ) {}

  getAllUsers(): Observable<any> {

    return this.apiRequest.get(this.appConfig.userList);
  }
}
