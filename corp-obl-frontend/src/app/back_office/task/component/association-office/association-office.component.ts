import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {OfficeService} from '../../../office/service/office.service';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {UserService} from '../../../../user/service/user.service';
import {TaskOffice} from '../../model/taskoffice';
import {Task} from '../../model/task';
import {Company} from '../../../company/model/company';
import {Office} from '../../../office/model/office';

@Component({
    selector: 'app-association-office',
    templateUrl: './association-office.component.html',
    styleUrls: ['./association-office.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class AssociationOfficeComponent implements OnInit {

    taskOfficesArray = [];
    selectedOffices = [];
    companyTemp: Company;

    officesAvailable: Office[] = [];
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

        if (!me.task) {

            if (me.officesAvailable.length === 0) {
                me.officeService.getOfficesByRole().subscribe(
                    data => {
                        const offices: Office[] = data;
                        me.officesAvailable = [...offices];
                    }
                );
            }
            return;
        }

        if (me.task.taskTemplate &&
            me.task.taskTemplate.topic &&
            me.task.taskTemplate.topic.companyList) {

            const officesFiltredByTask = [];

            me.task.taskTemplate.topic.companyList.forEach(
                company => {
                    if (company.offices) {
                        officesFiltredByTask.push(...company.offices);
                    }
                }
            );
            me.officesAvailable = [...officesFiltredByTask];
        }

        me.usersObservable = me.userService.getAllUsersExceptAdminRole();

        me.getTaskOfficesArray(null);
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

                me.companyTemp = taskOffice.office.company;
                if (me.companyTemp.offices) {
                    me.officesAvailable = [...taskOffice.office.company.offices];
                }
            });
        } else {
            me.taskOfficesArray = [];
        }
    }

    onAddOfficeRealation($event) {
        console.log('AssociationOfficeComponent - onAddOfficeRealation');

        const taskOffice: TaskOffice = new TaskOffice();
        taskOffice.office = $event;

        if (this.task) {
            if (this.task.taskTemplate.topic.companyList) {
                this.task.taskTemplate.topic.companyList.forEach(
                    companyLoop => {
                        if (companyLoop.offices) {
                            companyLoop.offices.forEach(
                                officeLoop => {
                                    if (officeLoop.idOffice === taskOffice.office.idOffice) {
                                        taskOffice.office.company = companyLoop;
                                    }
                                }
                            );
                        }
                    }
                );
            }
        } else if (this.companyTemp) {
            taskOffice.office.company = this.companyTemp;
        }

        this.taskOfficesArray.push(taskOffice);
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
    }

}
