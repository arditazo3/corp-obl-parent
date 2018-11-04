import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgSelectModule} from '@ng-select/ng-select';
import {CommonModule} from '@angular/common';
import {NgbModalModule, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {CalendarModule} from 'angular-calendar';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {DragulaModule} from 'ng2-dragula';
import {RouterModule} from '@angular/router';
import {FileUploadModule} from 'ng2-file-upload';
import {SweetAlert2Module} from '@toverux/ngx-sweetalert2';
import {PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {FrontEndRoutes} from './front_end.routing';
import {AgendaComponent} from './agenda/agenda.component';
import {MyDatePickerModule} from 'mydatepicker';
import {MomentModule} from 'ngx-moment';
import {ExpirationService} from './service/expiration.service';
import {TaskTemplateExpirationsComponent} from './agenda/task-template-expirations/task-template-expirations.component';
import {OfficeExpirationComponent} from './agenda/office-expiration/office-expiration.component';
import {ExpirationActivityComponent} from './agenda/expiration-activity/expiration-activity.component';
import { AgendaControlledComponent } from './agenda-controlled/agenda-controlled.component';
import { ExpirationActivityControlledComponent } from './agenda-controlled/expiration-activity-controlled/expiration-activity-controlled.component';
import { OfficeExpirationControlledComponent } from './agenda-controlled/office-expiration-controlled/office-expiration-controlled.component';
import { TaskTemplateExpirationsControlledComponent } from './agenda-controlled/task-template-expirations-controlled/task-template-expirations-controlled.component';
import {TranslateModule} from '@ngx-translate/core';

@NgModule({
    imports: [
        FormsModule,
        ReactiveFormsModule,
        NgSelectModule,
        CommonModule,
        NgbModule,
        NgbModalModule.forRoot(),
        CalendarModule.forRoot(),
        NgxDatatableModule,
        MyDatePickerModule,
        MomentModule,
        DragulaModule,
        RouterModule.forChild(FrontEndRoutes),
        PerfectScrollbarModule,
        FileUploadModule,
        SweetAlert2Module.forRoot({
            buttonsStyling: false,
            customClass: 'modal-content',
            confirmButtonClass: 'btn btn-primary',
            cancelButtonClass: 'btn'
        }),
        TranslateModule
    ],
    declarations: [
        AgendaComponent,
        TaskTemplateExpirationsComponent,
        OfficeExpirationComponent,
        ExpirationActivityComponent,
        AgendaControlledComponent,
        ExpirationActivityControlledComponent,
        OfficeExpirationControlledComponent,
        TaskTemplateExpirationsControlledComponent
    ],
    providers: [
        ExpirationService
    ]
})

export class FrontEndModule {
}