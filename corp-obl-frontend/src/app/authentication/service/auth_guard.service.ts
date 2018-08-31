import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {LoginService} from './login.service';
import {UserInfoService} from '../../user/service/user-info.service';

@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild {

  constructor(
    private router: Router,
    private loginService: LoginService,
    private userInfoService: UserInfoService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    console.log('AuthGuard - canActivate');
    const url: string = state.url;
    return this.checkLogin(url);
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    console.log('AuthGuard - canActivateChild');
    return this.canActivate(childRoute, state);
  }

  checkLogin(url: string): boolean {
    console.log('AuthGuard - checkLogin');
    if (this.userInfoService.isLoggedIn()) {
      return true;
    }
    console.log('AuthGuard - User is not logged - This routing guard prvents redirection to any routes that needs logging.');
    /*
     * Store the original url in login
     * service and then redirect to login page
     */
    this.loginService.dashboardPage = url;
    this.router.navigate(['authentication/login', ]);
    return false;
  }

}
