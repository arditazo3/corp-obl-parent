import {ChangeDetectorRef, Component, NgModule, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Router} from '@angular/router';
import {UserInfoService} from '../../../../user/service/user-info.service';
import {User} from '../../../../user/model/user';
import {UserService} from '../../../../user/service/user.service';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Company} from '../../model/company';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {CompanyService} from '../../service/company.service';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {Observable} from 'rxjs';
import {CompanyAssociateUsersComponent} from '../company-associate-users/company-associate-users.component';

@Component({
    encapsulation: ViewEncapsulation.None,
    selector: 'app-company-create-edit',
    templateUrl: './company-create-edit.component.html',
    styleUrls: ['./company-create-edit.component.css']
})
export class CompanyCreateEditComponent implements OnInit {

    isNewForm;
    company: Company = new Company();
    submitted = false;
    errorDetails: ApiErrorDetails;

    @ViewChild('cancelBtn') cancelBtn;
    @ViewChild('submitBtn') submitBtn;
    @ViewChild('errorDescriptionSwal') private errorDescriptionSwal: SwalComponent;
    @ViewChild('confirmationCompanySwal') private confirmationCompanySwal: SwalComponent;
    @ViewChild('appCompanyAssociateUserComponent') private appCompanyAssociateUserComponent: CompanyAssociateUsersComponent;
    createEditCompany: FormGroup;

    constructor(
        private router: Router,
        private transferService: TransferDataService,
        private formBuilder: FormBuilder,
        private companyService: CompanyService) {
    }

    ngOnInit() {
        console.log('CompanyCreateEditComponent - ngOnInit');

        this.company = this.transferService.objectParam;
        if (this.company === undefined) {
            this.isNewForm = true;
            this.company = new Company();
        } else {
            this.submitBtn.nativeElement.innerText = 'Update Company';
        }

        this.createEditCompany = this.formBuilder.group({
            description: new FormControl({value: this.company.description, disabled: false}, Validators.required)
        });

    }

    // convenience getter for easy access to form fields
    get f() {
        return this.createEditCompany.controls;
    }

    createEditCompanySubmit() {
        console.log('CompanyCreateEditComponent - createEditCompanySubmit');

        let me = this;
        this.submitted = true;

        if (this.createEditCompany.invalid) {
            return;
        }


        this.company.description = this.createEditCompany.get('description').value;

        this.confirmationCompanySwal.title = 'Do you want to save: ' + this.company.description + '?';
        this.confirmationCompanySwal.show()
            .then(function (result) {
                // handle confirm, result is needed for modals with input
                me.companyService.saveUpdateCompany(me.company).subscribe(
                    (data) => {
                        me.errorDetails = undefined;
                        console.log('CompanyCreateEditComponent - createEditCompanySubmit - next');

                        const company: Company = data;
                        me.appCompanyAssociateUserComponent.company = company;
                        me.appCompanyAssociateUserComponent.associateUsersToCompanySubmit(me.isNewForm);

                        // If it is new form the route back should be from the child
                        // after it is done all the process
                        if (!me.isNewForm ||
                            me.appCompanyAssociateUserComponent.selectedUsers.length === 0) {

                            me.router.navigate(['/back-office/company']);
                        }
                    },
                    (error) => {
                        me.errorDetails = error.error;
                        me.showErrorDescriptionSwal();
                        console.log('CompanyCreateEditComponent - createEditCompanySubmit - error');
                    }
                );
            }, function (dismiss) {
                // dismiss can be "cancel" | "close" | "outside"
            });
    }

    showErrorDescriptionSwal() {
        console.log('CompanyCreateEditComponent - showErrorDescriptionSwal');

        if (this.errorDetails !== undefined) {
            this.errorDescriptionSwal.title = 'Seems that ' + this.company.description + ' already exist!';
            this.errorDescriptionSwal.show();
        }
    }

}
