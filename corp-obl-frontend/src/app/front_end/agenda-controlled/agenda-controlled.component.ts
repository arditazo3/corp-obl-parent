import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import {TaskOfficeExpirations} from '../model/task-office-expirations';
import {IMyOptions} from 'mydatepicker';
import {AppGlobals} from '../../shared/common/api/app-globals';
import {OfficeService} from '../../back_office/office/service/office.service';
import {ExpirationService} from '../service/expiration.service';
import * as moment from 'moment';
import {DateExpirationOfficesHasArchived} from '../model/date-expiration-offices-hasarchived';
import {TranslateService} from '@ngx-translate/core';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
  selector: 'app-agenda-controlled',
  templateUrl: './agenda-controlled.component.html',
  styleUrls: ['./agenda-controlled.component.css']
})
export class AgendaControlledComponent implements OnInit {

    isMobile = false;

    officesObservable: Observable<any[]>;

    taskExpirations: TaskOfficeExpirations[] = [];

    myDatePickerOptions: IMyOptions = AppGlobals.myDatePickerOptions;

    dateStart: any;
    dateEnd: any;

    archivedTasks = true;

    constructor(
        private officeService: OfficeService,
        private expirationService: ExpirationService,
        private deviceService: DeviceDetectorService
    ) {

        this.isMobile = this.deviceService.isMobile();
    }

    ngOnInit() {
        console.log('AgendaControlledComponent - ngOnInit');

        let dateNow: Date = new Date();

        const dateAMonthAgo = moment(dateNow).add(-1, 'months').toDate();
        this.dateStart = AppGlobals.convertDateToDatePicker(dateAMonthAgo);

        this.searchTaskTemplatesDefault();

        dateNow = new Date();
        const dateAfter3Months = moment(dateNow).add(3, 'months').toDate();
        this.dateEnd = AppGlobals.convertDateToDatePicker(dateAfter3Months);

        this.getOffices();
    }

    getOffices() {
        console.log('AgendaControlledComponent - getOffices');

        const me = this;
        me.officesObservable = me.officeService.getOfficesByRole();
    }

    searchTaskTemplatesDefault() {
        console.log('AgendaControlledComponent - searchTaskTemplatesDefault');

        const dateNow: Date = new Date();
        const dateAfter6Months = moment(dateNow).add(6, 'months').toDate();
        const dateEnd = AppGlobals.convertDateToDatePicker(dateAfter6Months);

        const me = this;
        const dateExpirationOfficesArchived: DateExpirationOfficesHasArchived = new DateExpirationOfficesHasArchived();
        if (this.dateStart) {
            dateExpirationOfficesArchived.dateStart = AppGlobals.convertDatePickerToDate(this.dateStart.date);
        }
        if (this.dateEnd) {
            dateExpirationOfficesArchived.dateEnd = AppGlobals.convertDatePickerToDate(dateEnd.date);
        }
        dateExpirationOfficesArchived.showArchived = this.archivedTasks;
        dateExpirationOfficesArchived.userRelationType = AppGlobals.CONTROLLED;

        this.expirationService
            .searchDateExpirationOffices(dateExpirationOfficesArchived)
            .subscribe(
                data => {
                    me.taskExpirations = data;
                    console.log('AgendaControlledComponent - searchTaskTemplates - next');
                },
                error => {
                    console.error('AgendaControlledComponent - searchTaskTemplates - error \n', error);
                }
            );
    }

    searchTaskTemplates() {
        console.log('AgendaControlledComponent - searchTaskTemplates');

        const me = this;
        const dateExpirationOfficesArchived: DateExpirationOfficesHasArchived = new DateExpirationOfficesHasArchived();
        if (this.dateStart) {
            dateExpirationOfficesArchived.dateStart = AppGlobals.convertDatePickerToDate(this.dateStart.date);
        }
        if (this.dateEnd) {
            dateExpirationOfficesArchived.dateEnd = AppGlobals.convertDatePickerToDate(this.dateEnd.date);
        }
        dateExpirationOfficesArchived.showArchived = this.archivedTasks;
        dateExpirationOfficesArchived.userRelationType = AppGlobals.CONTROLLED;

        this.expirationService
            .searchDateExpirationOffices(dateExpirationOfficesArchived)
            .subscribe(
                data => {
                    me.taskExpirations = data;
                    console.log('AgendaControlledComponent - searchTaskTemplates - next');
                },
                error => {
                    console.error('AgendaControlledComponent - searchTaskTemplates - error \n', error);
                }
            );
    }
}
