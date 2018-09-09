import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Companyconsultant} from '../../model/CompanyConsultant';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Router} from '@angular/router';
import {Company} from '../../../company/model/company';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {CompanyConsultantService} from '../../service/companyconsultant.service';

@Component({
  selector: 'app-consultant-table',
  templateUrl: './consultant-table.component.html',
  styleUrls: ['./consultant-table.component.css']
})
export class ConsultantTableComponent implements OnInit {

    @ViewChild('myTable') table: any;
    @ViewChild('deleteCompanyConsultantSwal') private deleteCompanyConsultantSwal: SwalComponent;

    columns: any[];
    rows: Companyconsultant[];
    data: any;
    temp = [];
    rowSelected: Companyconsultant;
    errorDetails: ApiErrorDetails;
    company: Company;

  constructor(
      private router: Router,
      private companyConsultantService: CompanyConsultantService,
      private transferService: TransferDataService
  ) { }

  ngOnInit() {
      console.log('ConsultantTableComponent - ngOnInit');

      const me = this;
      me.getCompanyConsultant(null);

      me.columns = [
          {prop: 'name', name: 'Nominative / Business name'},
          {prop: 'email', name: 'Email'},
          {prop: 'phone1', name: 'Telephone no.1'},
          {prop: 'phone2', name: 'Telephone no.2'},
      ];
  }

    getCompanyConsultant(selectedCompany) {
        console.log('ConsultantTableComponent - getCompanyConsultant');

        if (!selectedCompany) {
          return;
        }

        const me = this;
        me.companyConsultantService.getCompanyConsultant(selectedCompany).subscribe(
            (data) => {
                me.rows = data;
                me.temp = [...data];
            }
        );
    }

    updateFilter(event) {
        console.log('ConsultantTableComponent - updateFilter');

        const val = event.target.value.toLowerCase();

        // filter our data
        const temp = this.temp.filter(function (d) {
            return d.name.toLowerCase().indexOf(val) !== -1 ||
                d.email.toLowerCase().indexOf(val) !== -1 ||
                d.phone1.toLowerCase().indexOf(val) !== -1 ||
                d.phone2.toLowerCase().indexOf(val) !== -1 ||
                !val;
        });

        // update the rows
        this.rows = temp;
        // Whenever the filter changes, always go back to the first page
        this.table = this.data;
    }

    toggleExpandRow(row) {
        console.log('ConsultantTableComponent - Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
    }

    createNewCompanyConsultant() {
        console.log('ConsultantTableComponent - createNewCompanyConsultant');

        this.router.navigate(['/back-office/consultant/create']);
    }

    editCompanyConsultant(companyConsultant: Companyconsultant) {
        console.log('CompanyConsultantTableComponent - editCompanyConsultant');

        this.transferService.objectParam = companyConsultant;

        this.router.navigate(['/back-office/companyConsultant/edit']);
    }

    deleteCompanyConsultantAlert(row) {
        console.log('CompanyConsultantTableComponent - deleteCompanyConsultantAlert');

        this.rowSelected = row;

        this.deleteCompanyConsultantSwal.title = 'Delete: ' + row.description + '?';

        this.deleteCompanyConsultantSwal.show();
    }

    deleteCompanyConsultantCofirm() {
        console.log('CompanyConsultantTableComponent - deleteCompanyConsultantCofirm' + this.rowSelected.idCompanyConsultant);

        const companyConsultantSelected = this.rowSelected;

        this.companyConsultantService.deleteCompanyConsultant(companyConsultantSelected).subscribe(
            (data) => {
                this.errorDetails = undefined;
                this.getCompanyConsultant(this.company);
                console.log('CompanyConsultantTableComponent - deleteCompanyConsultantCofirm - next');
            }, error => {
                this.errorDetails = error.error;
                console.log('CompanyConsultantTableComponent - deleteCompanyConsultantCofirm - error');
            }
        );
    }

}
