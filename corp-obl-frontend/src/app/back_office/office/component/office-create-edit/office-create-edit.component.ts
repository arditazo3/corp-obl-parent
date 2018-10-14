import {Component, OnInit, ViewChild} from '@angular/core';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Office} from '../../model/office';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {Router} from '@angular/router';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {OfficeService} from '../../service/office.service';
import {Observable} from 'rxjs';
import {Company} from '../../../company/model/company';
import {CompanyService} from '../../../company/service/company.service';

@Component({
  selector: 'app-office-create-edit',
  templateUrl: './office-create-edit.component.html',
  styleUrls: ['./office-create-edit.component.css']
})
export class OfficeCreateEditComponent implements OnInit {

    isNewForm;
    office: Office = new Office();
    submitted = false;
    errorDetails: ApiErrorDetails;

    companiesObservable: Observable<any[]>;
    selectedCompany: Company;

    @ViewChild('cancelBtn') cancelBtn;
    @ViewChild('submitBtn') submitBtn;
    @ViewChild('errorDescriptionSwal') private errorDescriptionSwal: SwalComponent;
    @ViewChild('confirmationOfficeSwal') private confirmationOfficeSwal: SwalComponent;
    createEditOffice: FormGroup;

  constructor(
      private router: Router,
      private transferService: TransferDataService,
      private formBuilder: FormBuilder,
      private officeService: OfficeService,
      private companyService: CompanyService
  ) { }

  ngOnInit() {
      console.log('OfficeCreateEditComponent - ngOnInit');

      this.office = this.transferService.objectParam;

      if (this.office === undefined) {
          this.isNewForm = true;
          this.office = new Office();
      } else {
          this.submitBtn.nativeElement.innerText = 'Update office';
          this.selectedCompany = this.office.company;
      }

      this.createEditOffice = this.formBuilder.group({
          description: new FormControl({value: this.office.description, disabled: false}, Validators.required)
      });

      this.getCompanies();
  }

    getCompanies() {
        console.log('OfficeCreateEditComponent - getCompanies');

        const me = this;
        me.companiesObservable = me.companyService.getCompanies();
    }

    // convenience getter for easy access to form fields
    get f() {
        return this.createEditOffice.controls;
    }

    createEditOfficeSubmit() {
        console.log('OfficeCreateEditComponent - createEditOfficeSubmit');

        const me = this;
        this.submitted = true;

        if (this.createEditOffice.invalid || this.selectedCompany === undefined) {
            return;
        }

        this.office.description = this.createEditOffice.get('description').value;
        this.office.company = this.selectedCompany;

        me.officeService.saveUpdateOffice(me.office).subscribe(
            (data) => {
                me.errorDetails = undefined;
                console.log('OfficeCreateEditComponent - createEditOfficeSubmit - next');
                me.router.navigate(['/back-office/office']);
            }, error => {
                me.errorDetails = error.error;
                me.showErrorDescriptionSwal();
                console.error('OfficeCreateEditComponent - createEditOfficeSubmit - error \n', error);
            }
        );
    }

    showErrorDescriptionSwal() {
        console.log('OfficeCreateEditComponent - showErrorDescriptionSwal');

        if (this.errorDetails !== undefined) {
            this.errorDescriptionSwal.title = this.errorDetails.message;
            this.errorDescriptionSwal.show();
        }
    }
}
