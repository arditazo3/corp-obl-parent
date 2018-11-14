import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {CommonModule, HashLocationStrategy, LocationStrategy} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {AgmCoreModule} from '@agm/core';

import {FullComponent} from './layouts/full/full.component';
import {BlankComponent} from './layouts/blank/blank.component';

import {NavigationComponent} from './shared/header-navigation/navigation.component';
import {SidebarComponent} from './shared/sidebar/sidebar.component';
import {BreadcrumbComponent} from './shared/breadcrumb/breadcrumb.component';

import {Approutes} from './app-routing.module';
import {AppComponent} from './app.component';
import {SpinnerComponent} from './shared/spinner.component';

import {PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface, PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {UserInfoService} from './user/service/user-info.service';
import {ApiRequestService} from './shared/common/service/api-request.service';
import {LoginService} from './authentication/service/login.service';
import {AppConfig} from './shared/common/api/app-config';
import {AuthGuard} from './authentication/service/auth_guard.service';
import {TransferDataService} from './shared/common/service/transfer-data.service';
import {SweetAlert2Module} from '@toverux/ngx-sweetalert2';
import {NgSelectModule} from '@ng-select/ng-select';
import {OfficeService} from './back_office/office/service/office.service';
import {UploadService} from './shared/common/service/upload.service';
import {UserService} from './user/service/user.service';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {DeviceDetectorModule} from 'ngx-device-detector';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
    suppressScrollX: true,
    wheelSpeed: 1,
    wheelPropagation: true,
    minScrollbarLength: 20
};

@NgModule({
    declarations: [
        AppComponent,
        SpinnerComponent,
        FullComponent,
        BlankComponent,
        NavigationComponent,
        BreadcrumbComponent,
        SidebarComponent,
        BlankComponent,
    ],
    imports: [
        CommonModule,
        BrowserModule,
        BrowserAnimationsModule,
        NgSelectModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        NgbModule,
        RouterModule.forRoot(Approutes, {useHash: false}),
        PerfectScrollbarModule,
        AgmCoreModule.forRoot({apiKey: 'AIzaSyBUb3jDWJQ28vDJhuQZxkC0NXr_zycm8D0'}),
        SweetAlert2Module.forRoot({
            buttonsStyling: false,
            customClass: 'modal-content',
            confirmButtonClass: 'btn btn-primary',
            cancelButtonClass: 'btn'
        }),
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
        DeviceDetectorModule.forRoot()
    ],
    providers: [
        UserInfoService,
        ApiRequestService,
        LoginService,
        AppConfig,
        AuthGuard,
        TransferDataService,
        UploadService,
        UserService,
        {
            provide: PERFECT_SCROLLBAR_CONFIG,
            useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
        },
        {
            provide: LocationStrategy,
            useClass: HashLocationStrategy
        },
        OfficeService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

// required for AOT compilation
export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}
