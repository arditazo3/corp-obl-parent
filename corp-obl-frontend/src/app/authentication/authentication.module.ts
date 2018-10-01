import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import {NotfoundComponent} from './404/not-found.component';
import {LockComponent} from './lock/lock.component';
import {LoginComponent} from './login/login.component';

import {AuthenticationRoutes} from './authentication.routing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {UserInfoService} from '../user/service/user-info.service';
import {ApiRequestService} from '../shared/common/service/api-request.service';
import {LoginService} from './service/login.service';
import {AppConfig} from '../shared/common/api/app-config';
import {AuthGuard} from './service/auth_guard.service';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AuthenticationRoutes),
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  declarations: [
    NotfoundComponent,
    LoginComponent,
    LockComponent,
  ],
  providers: [
    UserInfoService,
    ApiRequestService,
    LoginService,
    AppConfig,
    AuthGuard
  ]
})
export class AuthenticationModule {}
