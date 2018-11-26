import {Component, AfterViewInit} from '@angular/core';
import {UserInfoService} from '../../user/service/user-info.service';
import {Router} from '@angular/router';

@Component({
    templateUrl: './dashboard-home.component.html',
    styleUrls: ['./dashboard-home.component.css']
})
export class DashboardHomeComponent implements AfterViewInit {

    constructor(
        private router: Router,
        private userInfoService: UserInfoService
    ) {
    }

    ngAfterViewInit() {

        if (this.userInfoService &&
            !this.userInfoService.isRoleAdmin() &&
            !this.userInfoService.isRoleForeign() &&
            !this.userInfoService.isRoleInland()) {

            if (this.userInfoService.isRoleController()) {
                this.router.navigate(['/front-end/agenda']);
            } else if (this.userInfoService.isRoleControlled()) {
                this.router.navigate(['/front-end/agenda-controlled']);
            }
        }
    }
}
