import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {OfficeService} from '../../../office/service/office.service';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {UserService} from '../../../../user/service/user.service';
import {IHash} from '../../../../shared/common/interface/ihash';
import {TaskOffice} from '../../model/taskoffice';
import {Task} from '../../model/task';

@Component({
    selector: 'app-association-office',
    templateUrl: './association-office.component.html',
    styleUrls: ['./association-office.component.css']
})
export class AssociationOfficeComponent implements OnInit {

    taskOfficesArray = [];
    selectedOffices = [];

    officesObservable: Observable<any[]>;
    usersObservable: Observable<any[]>;

    @Output() checkAssociationOffice = new EventEmitter<boolean>();
    @Input() isTaskTemplateForm = false;
    @Input() task: Task;

    errorDetails: ApiErrorDetails = new ApiErrorDetails();

    constructor(
        private officeService: OfficeService,
        private userService: UserService
    ) {
    }

    ngOnInit() {
        console.log('AssociationOfficeComponent - ngOnInit');
        const me = this;

        //     officesFiltredByTask = me.task.taskOffices.map(taskOffice => taskOffice.office);

        const officesFiltredByTask = me.task.taskTemplate.topic.companyList[0].offices;

        me.officesObservable = Observable.of(officesFiltredByTask);

        me.usersObservable = me.userService.getAllUsersExceptAdminRole();

        me.getTaskOfficesArray(null);

        //     me.populateAvailableUsersOnOffices();
    }

    getTaskOfficesArray(taskOffices) {

        const me = this;
        if (!taskOffices) {
            return;
        }

        me.taskOfficesArray = taskOffices;

        if (taskOffices && taskOffices[0]) {
            me.taskOfficesArray.forEach((taskOffice) => {

                // avoid infinite cicle json
                taskOffice.task = undefined;

                me.selectedOffices.push(taskOffice.office);
            });
        } else {
            me.taskOfficesArray = [];
        }
    }

    onAddOfficeRealation($event) {
        console.log('AssociationOfficeComponent - onAddOfficeRealation');

        const taskOffice: TaskOffice = new TaskOffice();
        taskOffice.office = $event;
        taskOffice.office.company = this.task.taskTemplate.topic.companyList[0];

        this.taskOfficesArray.push(taskOffice);

        //       this.populateAvailableUsersOnOffices();
    }


    onRemoveOfficeRelation($event) {
        console.log('AssociationOfficeComponent - onRemoveOfficeRelation');

        const officeToRemove = $event.value;

        this.removeOffice(officeToRemove);
    }

    removeOffice(office) {
        const index = this.taskOfficesArray.findIndex(taskOffice => taskOffice.office.idOffice === office.idOffice);
        if (index > -1) {
            this.taskOfficesArray.splice(index, 1);
        }

        const indexRemoveOfficeCB = this.selectedOffices.findIndex(officeLoop => officeLoop.idOffice === office.idOffice);
        if (indexRemoveOfficeCB > -1) {
            this.selectedOffices.splice(index, 1);
        }

        this.selectedOffices = [...this.selectedOffices];

        //      this.populateAvailableUsersOnOffices();
    }

    clearAll() {
        console.log('AssociationOfficeComponent - clearAll');

        this.taskOfficesArray = [];

        //    this.populateAvailableUsersOnOffices();
    }

}
