import {ChangeDetectorRef, Component, NgModule, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {UserInfoService} from '../../../../user/service/user-info.service';
import {User} from '../../../../user/model/user';
import {UserService} from '../../../../user/service/user.service';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Company} from '../../model/Company';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {CompanyService} from "../../service/company.service";
import {ApiErrorDetails} from "../../../../shared/common/api/model/api-error-details";
import {SwalComponent} from "@toverux/ngx-sweetalert2";

@Component({
  selector: 'app-company-create-edit',
  templateUrl: './company-create-edit.component.html',
  styleUrls: ['./company-create-edit.component.css']
})
export class CompanyCreateEditComponent implements OnInit {

  isNewForm;
  users: User[];
  company: Company = new Company();
  submitted = false;
  errorDetails: ApiErrorDetails;

  @ViewChild('cancelBtn') cancelBtn;
  @ViewChild('submitBtn') submitBtn;
  @ViewChild('errorDescriptionSwal') private errorDescriptionSwal: SwalComponent;
  createEditCompany: FormGroup;

  constructor(
    private router: Router,
    private userService: UserService,
    private transferService: TransferDataService,
    private formBuilder: FormBuilder,
    private companyService: CompanyService
  ) { }

  ngOnInit() {
    console.log('CompanyCreateEditComponent - ngOnInit');

//    this.cdRef.detectChanges();

    this.getUsers();

    this.company = this.transferService.objectParam;
    if (this.company === undefined) {
      this.isNewForm = true;
      this.company = new Company();
    } else {
      this.submitBtn.nativeElement.innerText = 'Update Company';
    }

    this.createEditCompany = this.formBuilder.group({
      description: new FormControl({value: this.company.description, disabled: false}, Validators.required),
      createdBy: new FormControl({value: this.company.createdBy, disabled: true}),
      modifiedBy: new FormControl({value: this.company.modifiedBy, disabled: true})
    });

  }

  // convenience getter for easy access to form fields
  get f() { return this.createEditCompany.controls; }

  getUsers() {
    console.log('CompanyCreateEditComponent - getUsers');

    const me = this;
    me.userService.getAllUsers().subscribe(
      (data) => {
        me.users = data;
      }
    );
  }

  createEditCompanySubmit() {
    console.log('CompanyCreateEditComponent - createEditCompanySubmit');

    this.submitted = true;

    if (this.createEditCompany.invalid) {
      return;
    }

    this.company.description = this.createEditCompany.get('description').value;

    this.companyService.saveUpdateCompany(this.company).subscribe(
      (data) => {
        this.errorDetails = undefined;
        console.log('CompanyCreateEditComponent - createEditCompanySubmit - next');
      }, error => {
        this.errorDetails = error.error;
        this.showErrorDescriptionSwal();
        console.log('CompanyCreateEditComponent - createEditCompanySubmit - error');
      }
    );
  }

  showErrorDescriptionSwal() {
    console.log('CompanyCreateEditComponent - showErrorDescriptionSwal');

    if (this.errorDetails !== undefined) {
      this.errorDescriptionSwal.title = 'Seems that ' + this.company.description + ' already exist!';
      this.errorDescriptionSwal.show();
    }
  }

}
