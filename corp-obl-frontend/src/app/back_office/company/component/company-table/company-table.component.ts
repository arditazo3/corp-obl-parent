import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {CompanyService} from '../../service/company.service';
import {Router} from '@angular/router';
import {Company} from '../../model/company';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {DataFilter} from '../../../../shared/common/api/model/data-filter';
import {PageEnum} from '../../../../shared/common/api/enum/page.enum';

@Component({
    selector: 'app-company-table',
    templateUrl: './company-table.component.html',
    styleUrls: ['./company-table.component.css']
})
export class CompanyTableComponent implements OnInit {

    @ViewChild('descriptionCompany') descriptionCompany: ElementRef;
    @ViewChild('myTable') table: any;
    @ViewChild('deleteCompanySwal') private deleteCompanySwal: SwalComponent;

    columns: any[];
    rows: Company[];
    data: any;
    temp = [];
    rowSelected: Company;
    errorDetails: ApiErrorDetails;
    dataFilter: DataFilter = new DataFilter(PageEnum.BO_COMPANY);

    constructor(
        private router: Router,
        private companyService: CompanyService,
        private transferService: TransferDataService
    ) {
    }

    async ngOnInit() {
        console.log('CompanyTableComponent - ngOnInit');

        const me = this;
        me.getCompaniesByRole();

        me.columns = [
            {prop: 'description', name: 'Description'}
        ];

        const dataFilterTemp: DataFilter = this.transferService.dataFilter;
        if (dataFilterTemp && dataFilterTemp.page === PageEnum.BO_COMPANY) {
            this.dataFilter = dataFilterTemp;
            if (this.dataFilter.description) {
                this.descriptionCompany.nativeElement.value = this.dataFilter.description;
            }
        }
    }

    getCompaniesByRole() {
        console.log('CompanyTableComponent - getCompanies');

        const me = this;
        this.companyService.getCompaniesByRole().subscribe(
            (data) => {
                me.rows = data;
                me.temp = [...data];

                me.updateDataFilter(this.dataFilter.description);
            }
        );
    }

    updateDataFilter(val) {
        console.log('CompanyTableComponent - updateDataFilter');

        if (val) {
            this.filterData(val);
        }
    }

    updateFilter(event) {
        console.log('CompanyTableComponent - updateFilter');

        const val = event.target.value.toLowerCase();

        this.filterData(val);
    }

    filterData(val) {
        // filter our data
        const temp = this.temp.filter(function (d) {
            return d.description.toLowerCase().indexOf(val) !== -1 || !val;
        });

        // update the rows
        this.rows = temp;
        // Whenever the filter changes, always go back to the first page
        this.table = this.data;

        this.dataFilter.description = val;
    }

    createNewCompany() {
        console.log('CompanyTableComponent - createNewCompany');

        this.transferService.dataFilter = this.dataFilter;

        this.router.navigate(['/back-office/company/create']);
    }

    editCompany(company: Company) {
        console.log('CompanyTableComponent - editCompany');

        this.transferService.dataFilter = this.dataFilter;
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
        const me = this;

        this.companyService.deleteCompany(companySelected).subscribe(
            (data) => {
                me.errorDetails = undefined;
                me.getCompaniesByRole();
                console.log('CompanyTableComponent - deleteCompanyCofirm - next');
            }, error => {
                this.errorDetails = error.error;
                console.error('CompanyTableComponent - deleteCompanyCofirm - error \n', error);
            }
        );
    }

    toggleExpandRow(row) {
        console.log('CompanyTableComponent - Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
    }

}
