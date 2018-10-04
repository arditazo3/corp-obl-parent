import {Component, OnInit, ViewChild} from '@angular/core';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Router} from '@angular/router';
import {Consultant} from '../../model/consultant';
import {Company} from '../../../company/model/company';
import {ConsultantService} from '../../service/consultant.service';
import {AppGlobals} from '../../../../shared/common/api/app-globals';

@Component({
  selector: 'app-consultant-create-update',
  templateUrl: './consultant-create-update.component.html',
  styleUrls: ['./consultant-create-update.component.css']
})
export class ConsultantCreateUpdateComponent implements OnInit {

    isNewForm;
    consultant: Consultant;
    submitted = false;
    errorDetails: ApiErrorDetails;
    company: Company;

    @ViewChild('errorDescriptionSwal') errorDescriptionSwal: SwalComponent;
    @ViewChild('errorCompanyEmptySwal') errorCompanyEmptySwal: SwalComponent;
    @ViewChild('cancelBtn') cancelBtn;
    @ViewChild('submitBtn') submitBtn;
    createEditConsultant: FormGroup;

  constructor(
      private router: Router,
      private transferService: TransferDataService,
      private formBuilder: FormBuilder,
      private consultantService: ConsultantService
  ) { }

  ngOnInit() {
      console.log('ConsultantCreateUpdateComponent - ngOnInit');

      const arrayCompanyConsultant = this.transferService.arrayParam;

      if (arrayCompanyConsultant) {
          this.company = arrayCompanyConsultant[0];
          this.consultant = arrayCompanyConsultant[1];
      }

      if (!this.consultant) {
          this.isNewForm = true;
          this.consultant = new Consultant();
      } else {
          this.submitBtn.nativeElement.innerText = 'Update Consultant';
      }

      this.createEditConsultant = this.formBuilder.group({
          name: new FormControl({value: this.consultant.name, disabled: false}, Validators.required),
          email: new FormControl({value: this.consultant.email, disabled: false}, Validators.compose(
              [ Validators.required, Validators.email, Validators.pattern(AppGlobals.emailPattern) ])),
          phone1: new FormControl({value: this.consultant.phone1, disabled: false}),
          phone2: new FormControl({value: this.consultant.phone2, disabled: false})
      });

      if (!this.company) {
          this.errorCompanyEmptySwal.text = 'Select a company';
          this.errorCompanyEmptySwal.title = 'Select a company';
          this.errorCompanyEmptySwal.show();

          this.router.navigate(['/back-office/consultant']);
          return;
      }
  }

    // convenience getter for easy access to form fields
    get f() {
        return this.createEditConsultant.controls;
    }

    createEditConsultantSubmit() {
        console.log('ConsultantCreateEditComponent - createEditConsultantSubmit');

        const me = this;
        this.submitted = true;

        if (this.createEditConsultant.invalid) {
            return;
        }

        this.consultant.name = this.createEditConsultant.get('name').value;
        this.consultant.email = this.createEditConsultant.get('email').value;
        this.consultant.phone1 = this.createEditConsultant.get('phone1').value;
        this.consultant.phone2 = this.createEditConsultant.get('phone2').value;
        this.consultant.company = this.company;

        me.consultantService.saveUpdateConsultant(me.consultant).subscribe(
            (data) => {
                me.errorDetails = undefined;
                console.log('ConsultantCreateEditComponent - createEditConsultantSubmit - next');
                this.transferService.objectParam = this.company;
                me.router.navigate(['/back-office/consultant']);
            }, error => {
                me.errorDetails = error.error;
                me.showErrorDescriptionSwal();
                console.error('ConsultantCreateEditComponent - createEditConsultantSubmit - error');
            }
        );
    }

    showErrorDescriptionSwal() {
        console.log('ConsultantCreateEditComponent - showErrorDescriptionSwal');

        if (this.errorDetails !== undefined) {
            this.errorDescriptionSwal.title = this.errorDetails.message;
            this.errorDescriptionSwal.show();
        }
    }

    backToConsultant() {
        console.log('ConsultantCreateEditComponent - backToConsultant');

        this.transferService.objectParam = this.company;
        this.router.navigate(['/back-office/consultant']);
    }

}
