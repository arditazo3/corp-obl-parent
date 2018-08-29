import {CompanyComponent} from './company/component/company.component';
import {PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {BackOfficeRoutes} from './back_office.routing';
import {RouterModule} from '@angular/router';
import {DragulaModule} from 'ng2-dragula';
import {QuillModule} from 'ngx-quill';
import {NgbModalModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {CalendarModule} from 'angular-calendar';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgbModule,
    NgbModalModule.forRoot(),
    CalendarModule.forRoot(),
    QuillModule,
    DragulaModule,
    RouterModule.forChild(BackOfficeRoutes),
    PerfectScrollbarModule
  ],
  declarations: [
    CompanyComponent
  ]
})
export class BackOfficeModule {}
