import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {OfficeService} from '../../service/office.service';
import {Office} from '../../model/office';
import {DataFilter} from '../../../../shared/common/api/model/data-filter';
import {PageEnum} from '../../../../shared/common/api/enum/page.enum';
import {TranslateService} from '@ngx-translate/core';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-office-table',
    templateUrl: './office-table.component.html',
    styleUrls: ['./office-table.component.css']
})
export class OfficeTableComponent implements OnInit {

    @ViewChild('descriptionOffice') descriptionOffice: ElementRef;
    @ViewChild('myTable') table: any;
    @ViewChild('deleteOfficeSwal') deleteOfficeSwal: SwalComponent;

    isMobile = false;

    columns: any[];
    rows: Office[];
    data: any;
    temp = [];
    rowSelected: Office;
    errorDetails: ApiErrorDetails;
    dataFilter: DataFilter = new DataFilter(PageEnum.BO_OFFICE);

    constructor(
        private router: Router,
        private officeService: OfficeService,
        private transferService: TransferDataService,
        private deviceService: DeviceDetectorService
    ) {

        this.isMobile = this.deviceService.isMobile();
    }

    async ngOnInit() {
        console.log('OfficeTableComponent - ngOnInit');

        const me = this;
        me.getOffices();

        me.columns = [
            {prop: 'description', name: 'Description'}
        ];

        const dataFilterTemp: DataFilter = this.transferService.dataFilter;
        if (dataFilterTemp && dataFilterTemp.page === PageEnum.BO_OFFICE) {
            this.dataFilter = dataFilterTemp;
            if (this.dataFilter.description) {
                this.descriptionOffice.nativeElement.value = this.dataFilter.description;
            }
        }
    }

    getOffices() {
        console.log('OfficeTableComponent - getOffices');

        const me = this;
        this.officeService.getOffices().subscribe(
            (data) => {
                me.rows = data;
                me.temp = [...data];

                me.updateDataFilter(this.dataFilter.description);
            }
        );
    }

    updateDataFilter(val) {
        console.log('OfficeTableComponent - updateDataFilter');

        if (val) {
            this.filterData(val);
        }
    }

    updateFilter(event) {
        console.log('OfficeTableComponent - updateFilter');

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

    createNewOffice() {
        console.log('OfficeTableComponent - createNewOffice');

        this.transferService.dataFilter = this.dataFilter;

        this.router.navigate(['/back-office/office/create']);
    }

    editOffice(office: Office) {
        console.log('OfficeTableComponent - editOffice');

        this.transferService.dataFilter = this.dataFilter;
        this.transferService.objectParam = office;

        this.router.navigate(['/back-office/office/edit']);
    }

    deleteOfficeAlert(row) {
        console.log('OfficeTableComponent - deleteOfficeAlert');

        this.rowSelected = row;

        this.deleteOfficeSwal.title = 'Delete: ' + row.description + '?';

        this.deleteOfficeSwal.show();
    }

    deleteOfficeConfirm() {
        console.log('OfficeTableComponent - deleteOfficeConfirm' + this.rowSelected.idOffice);

        const officeSelected = this.rowSelected;

        this.officeService.deleteOffice(officeSelected).subscribe(
            (data) => {
                this.errorDetails = undefined;
                this.getOffices();
                console.log('OfficeTableComponent - deleteOfficeConfirm - next');
            }, error => {
                this.errorDetails = error.error;
                console.error('OfficeTableComponent - deleteOfficeConfirm - error \n', error);
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
