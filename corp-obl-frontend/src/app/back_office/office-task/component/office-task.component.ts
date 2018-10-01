import {Component, OnInit, ViewChild} from '@angular/core';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {OfficeTaskService} from '../service/office-task.service';
import {OfficeService} from '../../office/service/office.service';
import {TaskTempOffices} from '../model/tasktemp-offices';
import {OfficeTaksCollapseComponent} from './office-taks-collapse/office-taks-collapse.component';

@Component({
    selector: 'app-office-task',
    templateUrl: './office-task.component.html',
    styleUrls: ['./office-task.component.css']
})
export class OfficeTaskComponent implements OnInit {

    @ViewChild(OfficeTaksCollapseComponent) officeTaksCollapse: OfficeTaksCollapseComponent;

    descriptionTaskTemplate: string;
    offices = [];

    officesObservable: Observable<any[]>;

    constructor(
        private router: Router,
        private officeTaskService: OfficeTaskService,
        private officeService: OfficeService
    ) {
    }

    ngOnInit() {
        console.log('OfficeTaskComponent - ngOnInit');

        this.getOffices();
    }

    getOffices() {
        console.log('OfficeTaskComponent - getOffices');

        const me = this;
        me.officesObservable = me.officeService.getOfficesByRole();
    }

    searchOfficeTasks() {
        console.log('OfficeTaskComponent - searchOffice');

        const me = this;
        const taskTempOffices = new TaskTempOffices();
        taskTempOffices.descriptionTaskTemplate = this.descriptionTaskTemplate;
        taskTempOffices.offices = this.offices;

        this.officeTaskService.searchOfficeTasks(taskTempOffices).subscribe(
            (data) => {
                me.officeTaksCollapse.getOfficeTaskTemplatesArray(data);
                console.log('OfficeTaskComponent - searchOffice - next');
            }
        );
    }

}
