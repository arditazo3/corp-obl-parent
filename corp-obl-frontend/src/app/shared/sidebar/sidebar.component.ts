import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {ROUTES} from './menu-items';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FullComponent} from '../../layouts/full/full.component';
import {UserInfoService, UserTokenInStorage} from '../../user/service/user-info.service';
import {TranslateService} from '@ngx-translate/core';
import {UserService} from '../../user/service/user.service';
import {ApiErrorDetails} from '../common/api/model/api-error-details';
import {Title} from '@angular/platform-browser';
import {filter, map, mergeMap} from 'rxjs/operators';
import {SwalComponent} from '@toverux/ngx-sweetalert2';

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
    currentPage = 'Current page';
    pageInfo;
    public logoutVariable = '/authentication/logout';

    errorDetails: ApiErrorDetails;
    flagLanguage = 'flag-icon-us';

    public fullComponent: FullComponent = new FullComponent();
    @ViewChild('refreshCacheSwal') private refreshCacheSwal: SwalComponent;

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
        private activatedRoute: ActivatedRoute,
        private titleService: Title,
        private route: ActivatedRoute,
        private userInfoService: UserInfoService,
        private cdr: ChangeDetectorRef,
        private userService: UserService,
        private translate: TranslateService
    ) {

        this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .pipe(map(() => this.activatedRoute))
            .pipe(
                map(route => {
                    while (route.firstChild) {
                        route = route.firstChild;
                    }
                    return route;
                })
            )
            .pipe(filter(route => route.outlet === 'primary'))
            .pipe(mergeMap(route => route.data))
            .subscribe(event => {
                this.titleService.setTitle(event['title']);
                this.pageInfo = event;
            });

        const userLang = this.userInfoService.getUserLang().toUpperCase();

        // this language will be used as a fallback when a translation isn't found in the current language
        translate.setDefaultLang('EN');

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

        this.translate.use(language.toUpperCase());

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

        this.getUserLangFlag(language.toUpperCase());
    }

    getUserLangFlag(language) {

        if (language === 'EN') {
            this.flagLanguage = 'flag-icon-us';
        } else if (language === 'IT') {
            this.flagLanguage = 'flag-icon-it';
        }
    }

    refreshCache() {
        console.info('SidebarComponent - refreshCache');

        const me = this;
        this.userService.refreshCache().subscribe(
            success => {
                me.refreshCacheSwal.show();
            },
            error => {
                console.error('SidebarComponent - refreshCache - error \n', error);
            }
        );
    }

    isAdmin() {
        return this.userInfoService.isRoleAdmin();
    }
}
