import {Component, HostListener, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';

declare var $: any;
import {PerfectScrollbarConfigInterface} from 'ngx-perfect-scrollbar';
import {TaskService} from '../../back_office/task/service/task.service';
import {DeviceDetectorService} from 'ngx-device-detector';
import {UserService} from '../../user/service/user.service';
import {filter, map, mergeMap} from 'rxjs/operators';
import {Title} from '@angular/platform-browser';

@Component({
    selector: 'app-full-layout',
    templateUrl: './full.component.html',
    styleUrls: ['./full.component.scss']
})
export class FullComponent implements OnInit {

    isMobile = false;

    public config: PerfectScrollbarConfigInterface = {};

    tabStatus = 'justified';

    public isCollapsed = false;

    public innerWidth: any;
    public defaultSidebar: any;
    public showSettings = false;
    showMobileMenu = false;

    currentPage = 'Current page';
    pageInfo;

    options = {
        theme: 'light', // two possible values: light, dark
        dir: 'ltr', // two possible values: ltr, rtl
        layout: 'horizontal', // fixed value. shouldn't be changed.
        sidebartype: 'full', // fixed value. shouldn't be changed.
        sidebarpos: 'absolute', // two possible values: fixed, absolute
        headerpos: 'absolute', // two possible values: fixed, absolute
        boxed: 'full', // two possible values: full, boxed
        navbarbg: 'skin1', // six possible values: skin(1/2/3/4/5/6)
        sidebarbg: 'skin6', // six possible values: skin(1/2/3/4/5/6)
        logobg: 'skin1' // six possible values: skin(1/2/3/4/5/6)
    };

    constructor(
        public router?: Router,
        private deviceService?: DeviceDetectorService,
        private userService?: UserService,
        private titleService?: Title,
        private activatedRoute?: ActivatedRoute,
        private route?: ActivatedRoute
    ) {

        if (this.deviceService) {
            this.isMobile = this.deviceService.isMobile();
        }

        if (this.userService) {
            userService.showMobileMenu.subscribe(value => {
                this.showMobileMenu = value;
            });
        }

        if (this.router) {
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
        }
    }

    ngOnInit() {
        if (this.router.url === '/') {
            this.router.navigate(['/dashboard']);
        }
        this.defaultSidebar = this.options.sidebartype;
        this.handleSidebar();
    }

    @HostListener('window:resize', ['$event'])
    onResize(event) {
        this.handleSidebar();
    }

    handleSidebar() {
        this.innerWidth = window.innerWidth;
        switch (this.defaultSidebar) {
            case 'full':
                if (this.innerWidth < 1170) {
                    this.options.sidebartype = 'mini-sidebar';
                } else {
                    this.options.sidebartype = this.defaultSidebar;
                }
                break;
            default:
        }
    }
}
