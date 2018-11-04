import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ROUTES} from './menu-items';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FullComponent} from '../../layouts/full/full.component';
import {UserInfoService, UserTokenInStorage} from '../../user/service/user-info.service';
import {TranslateService} from '@ngx-translate/core';
import {UserService} from '../../user/service/user.service';
import {ApiErrorDetails} from '../common/api/model/api-error-details';

declare var $: any;

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidebarComponent implements OnInit {
    showMenu = '';
    showSubMenu = '';
    public sidebarnavItems: any[];
    fullname = '';
    public logoutVariable = '/authentication/logout';

    errorDetails: ApiErrorDetails;
    flagLanguage = 'flag-icon-us';

    public fullComponent: FullComponent = new FullComponent();

    // this is for the open close
    addExpandClass(element: any) {
        if (element === this.showMenu) {
            this.showMenu = '0';
        } else {
            this.showMenu = element;
        }
    }

    addActiveClass(element: any) {
        if (element === this.showSubMenu) {
            this.showSubMenu = '0';
        } else {
            this.showSubMenu = element;
        }
    }

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private route: ActivatedRoute,
        private userInfoService: UserInfoService,
        private cdr: ChangeDetectorRef,
        private userService: UserService,
        private translate: TranslateService
    ) {
        const userLang = this.userInfoService.getUserLang();

        // this language will be used as a fallback when a translation isn't found in the current language
        translate.setDefaultLang('en');

        // the lang to use, if the lang isn't available, it will use the current loader to get them
        translate.use(userLang);

        this.getUserLangFlag(userLang);
    }

    // End open close
    ngOnInit() {
        this.sidebarnavItems = ROUTES.filter(sidebarnavItem => sidebarnavItem);
        this.fullname = this.userInfoService.getUsername();

        this.cdr.detectChanges();
    }

    containAuth(authoritiesAllowed) {
        const me = this;

        let hasAuthority = false;

        authoritiesAllowed.forEach(authorityAllowed => {
            me.userInfoService.getAuthorities().forEach(authority => {
                if (authorityAllowed === authority) {
                    hasAuthority = true;
                }
            });
        });

        return hasAuthority;
    }

    changeLanguage(language) {
        console.log('CompanyTableComponent - changeLanguage');

        const me = this;

        this.translate.use(language);

        const userInStorage: UserTokenInStorage = this.userInfoService.getUserInfo();
        userInStorage.user.lang = language;

        this.userService.userLanguangeOnChange(userInStorage.user).subscribe(
            data => {

                me.userInfoService.storeUserTokenInfo(JSON.stringify(userInStorage));
            },
            error => {
                me.errorDetails = error.error;
                console.error('SidebarComponent - changeLanguage - error \n', error);
            }
        );

        this.getUserLangFlag(language);
    }

    getUserLangFlag(language) {
        const userLang = this.userInfoService.getUserLang();

        if (language === 'EN') {
            this.flagLanguage = 'flag-icon-us';
        } else if (language === 'IT') {
            this.flagLanguage = 'flag-icon-it';
        }
    }
}
