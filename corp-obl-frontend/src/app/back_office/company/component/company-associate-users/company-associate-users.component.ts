import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {User} from '../../../../user/model/user';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {UserService} from '../../../../user/service/user.service';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Company} from '../../model/company';
import {CompanyService} from '../../service/company.service';
import {CompanyUser} from '../../model/company_user';
import {AuthorityEnum} from '../../../../shared/common/api/enum/authority.enum';
import {Observable} from 'rxjs/Rx';
import {ConsultantService} from '../../../consultant/service/consultant.service';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-company-associate-users',
    templateUrl: './company-associate-users.component.html',
    styleUrls: ['./company-associate-users.component.css']
})
export class CompanyAssociateUsersComponent implements OnInit {

    isMobile = false;

    usersObservable: Observable<any[]>;
    selectedUsers = [];
    usersAsAdminObservable: Observable<any[]>;
    selectedUsersAsAdmin = [];
    submitted = false;
    errorDetails: ApiErrorDetails;
    @Input() company: Company = new Company();
    me = this;

    associateUsersToCompany: FormGroup;

    @ViewChild('headerAssociateUsers') headerAssociateUsers;

    constructor(
        private router: Router,
        private userService: UserService,
        private transferService: TransferDataService,
        private formBuilder: FormBuilder,
        private companyService: CompanyService,
        private deviceService: DeviceDetectorService
    ) {

        this.isMobile = this.deviceService.isMobile();
    }

    // convenience getter for easy access to form fields
    get f() {
        return this.associateUsersToCompany.controls;
    }

    ngOnInit() {
        console.log('CompanyAssociateUsersComponent - ngOnInit');

        this.associateUsersToCompany = this.formBuilder.group({});

        this.getUsers();
    }

    getUsers() {
        console.log('CompanyAssociateUsersComponent - getUsers');

        const me = this;
        me.usersObservable = me.userService.getAllUsersExceptAdminRole();

        this.setUserSelected().subscribe(
            data => {
                this.selectedUsers = data;
                this.usersAsAdminObservable = Observable.of(this.selectedUsers);
            }
        );

        this.setUserSelectedAsAdmin().subscribe(
            data => this.selectedUsersAsAdmin = data
        );
    }

    setUserSelectedAsAdmin(): Observable<any[]> {
        console.log('CompanyAssociateUsersComponent - setUserSelectedAsAdmin');

        return this.usersObservable.map(users => {
            const inside = this;
            return users.filter(
                (user: User) => {
                    let isIncluded = false;
                    if (inside.company.usersAssociated !== undefined) {
                        const userAssociated = inside.company.usersAssociated;
                        for (const usersAssociatedLoop of userAssociated) {
                            if (user.username === usersAssociatedLoop.user.username && usersAssociatedLoop.companyAdmin) {
                                isIncluded = true;
                                break;
                            }
                        }
                    }
                    return isIncluded;
                });
        });
    }

    setUserSelected(): Observable<any[]> {
        return this.usersObservable.map(users => {
            const inside = this;
            return users.filter(
                (user: User) => {
                    let isIncluded = false;
                    if (inside.company.usersAssociated !== undefined) {
                        for (const usersAssociatedLoop of inside.company.usersAssociated) {
                            if (user.username === usersAssociatedLoop.user.username) {
                                isIncluded = true;
                                break;
                            }
                        }
                    }
                    return isIncluded;
                });
        });
    }

    associateUsersToCompanySubmit(isNewForm) {
        console.log('CompanyAssociateUsersComponent - associateUsersToCompanySubmit');

        const me = this;
        this.submitted = true;

        if (this.selectedUsers === undefined) {
            return;
        }

        const companyUsers: CompanyUser[] = [];
        this.selectedUsers.forEach( (user) => {
            const companyUser: CompanyUser = new CompanyUser();

            companyUser.idCompanyUser = user.idCompanyUser;
            companyUser.user = user;
            me.selectedUsersAsAdmin.forEach( (userAdmin) => {


               if (user.username === userAdmin.username) {
                   companyUser.companyAdmin = true;
               }
            });

            companyUsers.push(companyUser);
        });

        this.company.usersAssociated = this.mapIdCompanyUser(companyUsers);

        this.companyService.saveAssociationCompanyUsers(this.company).subscribe(
            (data) => {
                me.errorDetails = undefined;

                me.router.navigate(['/back-office/company']);

                console.log('CompanyAssociateUsersComponent - associateUsersToCompanySubmit - next');
            }, error => {
                me.errorDetails = error.error;
                console.error('CompanyAssociateUsersComponent - associateUsersToCompanySubmit - error \n', error);
            }
        );


        console.log(companyUsers);
    }

    onChangeSelectUsers($event) {
        console.log('CompanyAssociateUsersComponent - onChangeSelectUsers');

        const selectedUsersToAdminAlternetive = [];
        this.selectedUsers.forEach((userSelectedFromCB) => {

            if (userSelectedFromCB.authorities !== undefined &&
                userSelectedFromCB.authorities.length !== 0 &&
                userSelectedFromCB.authorities.some(authority => authority === AuthorityEnum[AuthorityEnum.CORPOBLIG_USER])) {

                selectedUsersToAdminAlternetive.push(userSelectedFromCB);
            }

        });

        this.usersAsAdminObservable = Observable.of(selectedUsersToAdminAlternetive);

        // if (this.company.idCompany !== undefined) {
        //     this.associateUsersToCompanySubmit(false);
        // }
    }

    mapIdCompanyUser(companyUsers): CompanyUser[] {
        const me = this;
        const companyUsersNewArray: CompanyUser[] = [];
        companyUsers.forEach( (companyUser) => {

            if (me.company.usersAssociated !== undefined) {
                for (const userAssociated of me.company.usersAssociated) {
                    if (companyUser.username === userAssociated.user.username) {
                        companyUser.idCompanyUser = userAssociated.idCompanyUser;
                    }
                }
            }
            companyUsersNewArray.push(companyUser);
        });
        return companyUsersNewArray;
    }
}
