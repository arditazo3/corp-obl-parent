import {Component, OnInit, ViewChild} from '@angular/core';
import {User} from '../../../../user/model/user';
import { Observable } from 'rxjs/Rx';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {UserService} from '../../../../user/service/user.service';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Company} from '../../model/company';
import {CompanyService} from '../../service/company.service';
import {CompanyUser} from '../../model/company_user';

@Component({
    selector: 'app-company-associate-users',
    templateUrl: './company-associate-users.component.html',
    styleUrls: ['./company-associate-users.component.css']
})
export class CompanyAssociateUsersComponent implements OnInit {

    usersObservable: Observable<any[]>;
    selectedUsers = [];
    usersAsAdminObservable: Observable<any[]>;
    selectedUsersAsAdmin = [];
    submitted = false;
    errorDetails: ApiErrorDetails;
    company: Company = new Company();
    companyUsersArray: CompanyUser[];
    me = this;

    associateUsersToCompany: FormGroup;

    @ViewChild('headerAssociateUsers') headerAssociateUsers;

    constructor(
        private router: Router,
        private userService: UserService,
        private transferService: TransferDataService,
        private formBuilder: FormBuilder,
        private companyService: CompanyService
    ) {
    }

    // convenience getter for easy access to form fields
    get f() {
        return this.associateUsersToCompany.controls;
    }

    ngOnInit() {

        this.company = this.transferService.objectParam;

        this.associateUsersToCompany = this.formBuilder.group({});

        if (this.company === undefined) {
            this.router.navigate(['back-office/company']);
            return;
        }

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
        return this.usersObservable.map(users => {
            const inside = this;
            return users.filter(
                (user: User) => {
                    let isIncluded = false;
                    if (inside.company.usersAssociated !== undefined) {
                        for (const usersAssociatedLoop of inside.company.usersAssociated) {
                            if (user.username === usersAssociatedLoop.username && usersAssociatedLoop.companyAdmin) {
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
                            if (user.username === usersAssociatedLoop.username) {
                                isIncluded = true;
                                break;
                            }
                        }
                    }
                    return isIncluded;
                });
        });
    }


    associateUsersToCompanySubmit() {
        console.log('CompanyAssociateUsersComponent - associateUsersToCompanySubmit');

        const me = this;
        this.submitted = true;

        if (this.selectedUsers === undefined || this.selectedUsers.length === 0) {
            return;
        }

        const companyUsers: CompanyUser[] = [];
        this.selectedUsers.forEach( (user) => {
            const companyUser: CompanyUser = new CompanyUser();

            companyUser.idCompanyUser = user.idCompanyUser;
            companyUser.username = user.username;
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
                console.log('CompanyAssociateUsersComponent - associateUsersToCompanySubmit - next');
                me.router.navigate(['/back-office/company']);
            }, error => {
                me.errorDetails = error.error;
           //     me.showErrorDescriptionSwal();
                console.log('CompanyAssociateUsersComponent - associateUsersToCompanySubmit - error');
            }
        );
        console.log(companyUsers);
    }

    onChangeSelectUsers($event) {
        console.log('CompanyAssociateUsersComponent - onChangeSelectUsers');

        this.usersAsAdminObservable = Observable.of(this.selectedUsers);
    }

    onAddAsAdmin($event) {
        console.log('CompanyAssociateUsersComponent - onAddAsAdmin');
    }

    onRemoveAsAdmin($event) {
        console.log('CompanyAssociateUsersComponent - onRemoveAsAdmin');
    }

    mapIdCompanyUser(companyUsers): CompanyUser[] {
        const me = this;
        const companyUsersNewArray: CompanyUser[] = [];
        companyUsers.forEach( (companyUser) => {

            if (me.company.usersAssociated !== undefined) {
                for (const userAssociated of me.company.usersAssociated) {
                    if (companyUser.username === userAssociated.username) {
                        companyUser.idCompanyUser = userAssociated.idCompanyUser;
                    }
                }
            }
            companyUsersNewArray.push(companyUser);
        });
        return companyUsersNewArray;
    }
}
