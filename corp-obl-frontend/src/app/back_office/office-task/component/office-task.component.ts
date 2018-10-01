import {Component, OnInit, ViewChild} from '@angular/core';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {OfficeTaskService} from '../service/office-task.service';
import {OfficeService} from '../../office/service/office.service';
import {TaskTempOfficies} from '../model/tasktemp-officies';
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

    officiesObservable: Observable<any[]>;

    constructor(
        private router: Router,
        private officeTaskService: OfficeTaskService,
        private officeService: OfficeService
    ) {
    }

    ngOnInit() {
        console.log('OfficeTaskComponent - ngOnInit');

        this.getOfficies();
    }

    getOfficies() {
        console.log('OfficeTaskComponent - getOfficies');

        const me = this;
        me.officiesObservable = me.officeService.getOfficesByRole();
    }

    searchOfficeTasks() {
        console.log('OfficeTaskComponent - searchOffice');

        const me = this;
        const taskTempOfficies = new TaskTempOfficies();
        taskTempOfficies.descriptionTaskTemplate = this.descriptionTaskTemplate;
        taskTempOfficies.officies = this.offices;

        this.officeTaskService.searchOfficeTasks(taskTempOfficies).subscribe(
            (data) => {
                me.officeTaksCollapse.getOfficeTaskTemplatesArray(data);
                console.log('OfficeTaskComponent - searchOffice - next');
            }
        );
    }

}
