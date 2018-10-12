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
    ],
    declarations: [
    ],
    providers: [
    ]
})

export class FrontEndModule {
}