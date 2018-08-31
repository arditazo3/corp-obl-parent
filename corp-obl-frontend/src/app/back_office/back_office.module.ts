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
import { CompanyTableComponent } from './company/component/company-table/company-table.component';
import {CompanyService} from './company/service/company.service';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {SweetAlert2Module} from '@toverux/ngx-sweetalert2';
import { CompanyCreateEditComponent } from './company/component/company-create-edit/company-create-edit.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NgbModule,
    NgbModalModule.forRoot(),
    CalendarModule.forRoot(),
    NgxDatatableModule,
    QuillModule,
    DragulaModule,
    RouterModule.forChild(BackOfficeRoutes),
    PerfectScrollbarModule,
    SweetAlert2Module.forRoot({
      buttonsStyling: false,
      customClass: 'modal-content',
      confirmButtonClass: 'btn btn-primary',
      cancelButtonClass: 'btn'
    }),
  ],
  declarations: [
    CompanyComponent,
    CompanyTableComponent,
    CompanyCreateEditComponent
  ],
  providers: [
    CompanyService
  ]
})
export class BackOfficeModule {}
