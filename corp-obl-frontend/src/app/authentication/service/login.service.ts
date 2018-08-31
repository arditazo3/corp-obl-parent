import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {LoginInfoInStorage, UserInfoService} from '../../user/service/user-info.service';
import {ApiRequestService} from '../../shared/common/service/api-request.service';
import {BehaviorSubject, Observable} from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

export interface LoginRequestParam {
  username: string;
}

@Injectable()
export class LoginService {

  public dashboardPage = '/dashboard';

  constructor(
    private router: Router,
    private userInfoService: UserInfoService,
    private apiRequest: ApiRequestService
  ) {
  }

  logIn(username: string): Observable<any> {
    console.log('LoginService - login');

    const bodyData: LoginRequestParam = {
      'username': username
    };

    /*
      Using BehaviorSubject instead of Subject
      In Angular services are initialized before the components, if any component is
      subscribing, it will only receive events which are executed after subscription.
      therefore if you put a syncronize next() in the service, the component wont get it.

      A BehaviourSubject will always provide the values wheather the subscription happened after or before event
    */

    // Will use this BehaviorSubject to emit data that we want after ajax login attempt
    const loginDataSubject: BehaviorSubject<any> = new BehaviorSubject<any>([]);
    // Object that we want to send back to Login Page
    let loginInfoReturn: LoginInfoInStorage;

    this.apiRequest.post('auth', bodyData)
      .subscribe(
        jsonResp => {
          if (jsonResp !== undefined && jsonResp !== null) {
            loginInfoReturn = {
              'message': jsonResp.operationMessage,
              'landingPage': '/dashboard/trendy',
              'userAndToken': {
                'user': {
                  'username': jsonResp.user.username,
                  'fullName': jsonResp.user.fullName,
                  'email': jsonResp.user.email,
                  'lang': jsonResp.user.lang,
                  'enabled': jsonResp.user.enabled,
                  'authorities': jsonResp.user.authorities
                },
                'token': jsonResp.token
              }
            };
            // Store the user and jwt token in session storaget to keep the user llogged in between page refreshes
            this.userInfoService.storeUserTokenInfo(JSON.stringify(loginInfoReturn.userAndToken));
          } else {
            // Create a failure object that we want to send back to login page
            loginInfoReturn = {
              'message': jsonResp.msgDesc,
              'landingPage': '/login',
            };
          }
          loginDataSubject.next(loginInfoReturn);
        },
        error => {
          loginInfoReturn = {
            'message': error.error.message,
            'landingPage': '/login'
          };
          loginDataSubject.next(loginInfoReturn);
        }
      );

    return loginDataSubject;
  }

  logout(navigatetoLogout = true): void {
    // clear token remove user from local storage to log user out
    this.userInfoService.removeUserTokenInfo();
    if (navigatetoLogout) {
      this.router.navigate(['logout']);
    }
  }
}
