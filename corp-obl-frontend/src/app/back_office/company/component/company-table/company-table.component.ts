import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {CompanyService} from '../../service/company.service';
import {Router} from '@angular/router';
import {Company} from '../../model/Company';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {ApiErrorDetails} from "../../../../shared/common/api/model/api-error-details";

@Component({
  selector: 'app-company-table',
  templateUrl: './company-table.component.html',
  styleUrls: ['./company-table.component.css']
})
export class CompanyTableComponent implements OnInit {

  @ViewChild('myTable') table: any;
  @ViewChild('deleteCompanySwal') private deleteCompanySwal: SwalComponent;

  columns: any[];
  rows: Company[];
  data: any;
  temp = [];
  expanded: any = {};
  rowSelected: Company;
  errorDetails: ApiErrorDetails;

  loadingIndicator = true;
  reorderable = true;

  constructor(
    private router: Router,
    private companyService: CompanyService,
    private transferService: TransferDataService
  ) {}

  async ngOnInit() {
    console.log('CompanyTableComponent - ngOnInit');

    const me = this;
    me.getCompanies();

    me.columns = [
      { prop: 'description', name: 'Description' },
      { prop: 'createdBy', name: 'Created By' },
      { prop: 'modifiedBy', name: 'Modified By' }
    ];

  }

  getCompanies() {
    console.log('CompanyTableComponent - getCompanies');

    const me = this;
    this.companyService.getCompanies().subscribe(
      (data) => {
        me.rows = data;
        me.temp = [...data];
      }
    );
  }

  updateFilter(event) {
    console.log('CompanyTableComponent - updateFilter');

    const val = event.target.value.toLowerCase();

    // filter our data
    const temp = this.temp.filter(function(d) {
      return d.description.toLowerCase().indexOf(val) !== -1 ||
             d.createdBy.toLowerCase().indexOf(val) !== -1 ||
             d.modifiedBy.toLowerCase().indexOf(val) !== -1 ||
             !val;
    });

    // update the rows
    this.rows = temp;
    // Whenever the filter changes, always go back to the first page
    this.table = this.data;
  }

  toggleExpandRow(row) {
    console.log('CompanyTableComponent - Toggled Expand Row!', row);
    this.table.rowDetail.toggleExpandRow(row);
  }

  onDetailToggle(event) {
  }

  createNewCompany() {
    console.log('CompanyTableComponent - createNewCompany');

    this.router.navigate(['/back-office/company/create']);
  }

  editCompany(company: Company) {
    console.log('CompanyTableComponent - createNewCompany');

    this.transferService.objectParam = company;

    this.router.navigate(['/back-office/company/edit']);

  }

  deleteCompanyAlert(row) {
    console.log('CompanyTableComponent - deleteCompany');

    this.rowSelected = row;

    this.deleteCompanySwal.title = 'Delete: ' + row.description + '?';

    this.deleteCompanySwal.show();
  }

  deleteCompanyCofirm() {
    console.log('CompanyTableComponent - deleteCompanySwal' + this.rowSelected.idCompany);

    const companySelected = this.rowSelected;

    this.companyService.deleteCompany(companySelected).subscribe(
      (data) => {
        this.errorDetails = undefined;
        this.getCompanies();
        console.log('CompanyTableComponent - deleteCompanyCofirm - next');
      }, error => {
        this.errorDetails = error.error;
        console.log('CompanyTableComponent - deleteCompanyCofirm - error');
      }
    );


  }

}
