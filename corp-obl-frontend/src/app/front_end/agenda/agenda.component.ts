import {Component, OnInit} from '@angular/core';
import {IMyOptions} from 'mydatepicker';
import {AppGlobals} from '../../shared/common/api/app-globals';
import * as moment from 'moment';
import {Observable} from 'rxjs';
import {OfficeService} from '../../back_office/office/service/office.service';
import {DateExpirationOfficesHasArchived} from '../model/date-expiration-offices-hasarchived';
import {ExpirationService} from '../service/expiration.service';
import {TaskOfficeExpirations} from '../model/task-office-expirations';

@Component({
    selector: 'app-agenda',
    templateUrl: './agenda.component.html',
    styleUrls: ['./agenda.component.css']
})
export class AgendaComponent implements OnInit {

    officesObservable: Observable<any[]>;
    offices = [];

    taskExpirations: TaskOfficeExpirations[] = [];

    myDatePickerOptions: IMyOptions = AppGlobals.myDatePickerOptions;

    dateStart: any;
    dateEnd: any;

    hideArchivedTasks = false;

    constructor(
        private officeService: OfficeService,
        private expirationService: ExpirationService
    ) {
    }

    ngOnInit() {
        console.log('AgendaComponent - ngOnInit');

        const dateNow: Date = new Date();
        this.dateStart = AppGlobals.convertDateToDatePicker(dateNow);

        this.searchTaskTemplatesDefault();

        const dateAfter3Months = moment(dateNow).add(3, 'months').toDate();
        this.dateEnd = AppGlobals.convertDateToDatePicker(dateAfter3Months);

        this.getOffices();
    }

    getOffices() {
        console.log('AgendaComponent - getOffices');

        const me = this;
        me.officesObservable = me.officeService.getOfficesByRole();
    }

    searchTaskTemplatesDefault() {
        console.log('AgendaComponent - searchTaskTemplatesDefault');

        const dateNow: Date = new Date();
        const dateAfter6Months = moment(dateNow).add(6, 'months').toDate();
        const dateEnd = AppGlobals.convertDateToDatePicker(dateAfter6Months);

        const me = this;
        const dateExpirationOfficesArchived: DateExpirationOfficesHasArchived = new DateExpirationOfficesHasArchived();
        dateExpirationOfficesArchived.dateStart = AppGlobals.convertDatePickerToDate(this.dateStart.date);
        dateExpirationOfficesArchived.dateEnd = AppGlobals.convertDatePickerToDate(dateEnd.date);
        dateExpirationOfficesArchived.offices = this.offices;
        dateExpirationOfficesArchived.hideArchived = this.hideArchivedTasks;

        this.expirationService
            .searchDateExpirationOffices(dateExpirationOfficesArchived)
            .subscribe(
                data => {
                    me.taskExpirations = data;
                    console.log('AgendaComponent - searchTaskTemplates - next');
                },
                error => {
                    console.error('AgendaComponent - searchTaskTemplates - error \n', error);
                }
            );
    }

    searchTaskTemplates() {
        console.log('AgendaComponent - searchTaskTemplates');

        const me = this;
        const dateExpirationOfficesArchived: DateExpirationOfficesHasArchived = new DateExpirationOfficesHasArchived();
        dateExpirationOfficesArchived.dateStart = AppGlobals.convertDatePickerToDate(this.dateStart.date);
        dateExpirationOfficesArchived.dateEnd = AppGlobals.convertDatePickerToDate(this.dateEnd.date);
        dateExpirationOfficesArchived.offices = this.offices;
        dateExpirationOfficesArchived.hideArchived = this.hideArchivedTasks;

        this.expirationService
            .searchDateExpirationOffices(dateExpirationOfficesArchived)
            .subscribe(
                data => {
                    me.taskExpirations = data;
                    console.log('AgendaComponent - searchTaskTemplates - next');
                },
                error => {
                    console.error('AgendaComponent - searchTaskTemplates - error \n', error);
                }
            );
    }
}
