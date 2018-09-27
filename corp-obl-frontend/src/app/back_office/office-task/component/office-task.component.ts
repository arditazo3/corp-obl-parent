import {Component, OnInit} from '@angular/core';
import {Observable} from '../../../../../node_modules/rxjs/Rx';
import {ObjectSearchTaskTemplate} from '../../tasktemplate/model/object-search-tasktemplate';
import {Router} from '@angular/router';
import {OfficeTaskService} from '../service/office-task.service';
import {OfficeService} from '../../office/service/office.service';

@Component({
    selector: 'app-office-task',
    templateUrl: './office-task.component.html',
    styleUrls: ['./office-task.component.css']
})
export class OfficeTaskComponent implements OnInit {

    descriptionTaskTemplate: string;
    selectedOfficies = [];
    officesTasks = [];

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

    searchOffice() {
        console.log('OfficeTaskComponent - searchOffice');

        const me = this;
        const objectSearchDescrTaskTempOfficies = {
            description: this.descriptionTaskTemplate,
            officies: this.selectedOfficies
        };

        this.officeTaskService.searchOffice(objectSearchDescrTaskTempOfficies).subscribe(
            (data) => {
                me.officesTasks = data;
            }
        );
    }

}
