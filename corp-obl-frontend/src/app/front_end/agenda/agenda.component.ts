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

    archivedTasks = true;

    constructor(
        private officeService: OfficeService,
        private expirationService: ExpirationService
    ) {
    }

    ngOnInit() {
        console.log('AgendaComponent - ngOnInit');

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
        dateExpirationOfficesArchived.showArchived = this.archivedTasks;
        dateExpirationOfficesArchived.userRelationType = AppGlobals.CONTROLLER;

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
        if (this.dateStart) {
            dateExpirationOfficesArchived.dateStart = AppGlobals.convertDatePickerToDate(this.dateStart.date);
        }
        if (this.dateEnd) {
            dateExpirationOfficesArchived.dateEnd = AppGlobals.convertDatePickerToDate(this.dateEnd.date);
        }
        dateExpirationOfficesArchived.offices = this.offices;
        dateExpirationOfficesArchived.showArchived = this.archivedTasks;
        dateExpirationOfficesArchived.userRelationType = AppGlobals.CONTROLLER;

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
