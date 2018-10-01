import {Component, OnInit, ViewChild} from '@angular/core';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {OfficeService} from '../../service/office.service';
import {Office} from '../../model/office';

@Component({
    selector: 'app-office-table',
    templateUrl: './office-table.component.html',
    styleUrls: ['./office-table.component.css']
})
export class OfficeTableComponent implements OnInit {

    @ViewChild('myTable') table: any;
    @ViewChild('deleteOfficeSwal') deleteOfficeSwal: SwalComponent;

    columns: any[];
    rows: Office[];
    data: any;
    temp = [];
    rowSelected: Office;
    errorDetails: ApiErrorDetails;

    constructor(
        private router: Router,
        private officeService: OfficeService,
        private transferService: TransferDataService
    ) {
    }

    async ngOnInit() {
        console.log('OfficeTableComponent - ngOnInit');

        const me = this;
        me.getOffices();

        me.columns = [
            {prop: 'description', name: 'Description'}
        ];
    }

    getOffices() {
        console.log('OfficeTableComponent - getOffices');

        const me = this;
        this.officeService.getOffices().subscribe(
            (data) => {
                me.rows = data;
                me.temp = [...data];
            }
        );
    }

    updateFilter(event) {
        console.log('OfficeTableComponent - updateFilter');

        const val = event.target.value.toLowerCase();

        // filter our data
        const temp = this.temp.filter(function (d) {
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

    createNewOffice() {
        console.log('OfficeTableComponent - createNewOffice');

        this.router.navigate(['/back-office/office/create']);
    }

    editOffice(office: Office) {
        console.log('OfficeTableComponent - editOffice');

        this.transferService.objectParam = office;

        this.router.navigate(['/back-office/office/edit']);
    }

    deleteOfficeAlert(row) {
        console.log('OfficeTableComponent - deleteOfficeAlert');

        this.rowSelected = row;

        this.deleteOfficeSwal.title = 'Delete: ' + row.description + '?';

        this.deleteOfficeSwal.show();
    }

    deleteOfficeCofirm() {
        console.log('OfficeTableComponent - deleteOfficeCofirm' + this.rowSelected.idOffice);

        const officeSelected = this.rowSelected;

        this.officeService.deleteOffice(officeSelected).subscribe(
            (data) => {
                this.errorDetails = undefined;
                this.getOffices();
                console.log('OfficeTableComponent - deleteOfficeCofirm - next');
            }, error => {
                this.errorDetails = error.error;
                console.log('OfficeTableComponent - deleteOfficeCofirm - error');
            }
        );
    }

    toggleExpandRow(row) {
        console.log('OfficeTableComponent - Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
    }
}
