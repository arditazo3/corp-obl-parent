import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs';
import {OfficeService} from '../../../office/service/office.service';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {UserService} from '../../../../user/service/user.service';
import {IHash} from '../../../../shared/common/interface/ihash';
import {TaskOffice} from '../../model/taskoffice';

@Component({
    selector: 'app-association-office',
    templateUrl: './association-office.component.html',
    styleUrls: ['./association-office.component.css']
})
export class AssociationOfficeComponent implements OnInit {

    taskOfficesArray = [];
    selectedOffices = [];
    officeUserProviders: IHash = {};
    officeUserBeneficiaries: IHash = {};
    officesObservable: Observable<any[]>;
    usersObservable: Observable<any[]>;

    @Output() checkAssociationOffice = new EventEmitter<boolean>();
    @Input() isTaskTemplateForm = false;

    errorDetails: ApiErrorDetails = new ApiErrorDetails();

    constructor(
        private officeService: OfficeService,
        private userService: UserService
    ) {
    }

    ngOnInit() {
        console.log('AssociationOfficeComponent - ngOnInit');

        const me = this;

        me.officesObservable = me.officeService.getOfficesByRole();
        me.usersObservable = me.userService.getAllUsersExceptAdminRole();

        me.getTaskOfficesArray(null);

        me.populateAvailableUsersOnOffices();
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

        this.taskOfficesArray.push(taskOffice);

        this.populateAvailableUsersOnOffices();
    }

    removeOfficeBtn(office) {
        console.log('AssociationOfficeComponent - removeOfficeBtn');

        this.removeOffice(office);
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

        this.populateAvailableUsersOnOffices();
    }

    clearAll() {
        console.log('AssociationOfficeComponent - clearAll');

        this.taskOfficesArray = [];

        this.populateAvailableUsersOnOffices();
    }

    onAddProvidersOffice($event, office) {
        console.log('AssociationOfficeComponent - onAddProvidersOffice');

        let arrayUsers = this.officeUserProviders[office.idOffice];
        if (arrayUsers) {
            arrayUsers.push($event);
        } else {
            arrayUsers = [];
            arrayUsers.push($event);
        }
        this.officeUserProviders[office.idOffice] = arrayUsers;

        this.populateAvailableUsersOnOffices();
    }

    onRemoveProvidersOffice($event, office) {
        console.log('AssociationOfficeComponent - onRemoveProvidersOffice');

        const arrayUsers = this.officeUserProviders[office.idOffice];

        if (arrayUsers) {
            const userToRemove = $event.value;
            const index = arrayUsers.findIndex(user => user.username === userToRemove.username);
            if (index > -1) {
                arrayUsers.splice(index, 1);
            }
        }

        this.populateAvailableUsersOnOffices();
    }

    clearAllUsers(office) {
        console.log('AssociationOfficeComponent - clearAllUsers');

        let arrayUsers = this.officeUserProviders[office.idOffice];

        arrayUsers = [];

        this.populateAvailableUsersOnOffices();
    }

    onAddBeneficiariesOffice($event, office) {
        console.log('AssociationOfficeComponent - onAddBeneficiariesOffice');

        let arrayUsers = this.officeUserBeneficiaries[office.idOffice];
        if (arrayUsers) {
            arrayUsers.push($event);
        } else {
            arrayUsers = [];
            arrayUsers.push($event);
        }
        this.officeUserBeneficiaries[office.idOffice] = arrayUsers;

        this.populateAvailableUsersOnOffices();
    }

    onRemoveBeneficiariesOffice($event, office) {
        console.log('AssociationOfficeComponent - onRemoveBeneficiariesOffice');

        const arrayUsers = this.officeUserBeneficiaries[office.idOffice];

        if (arrayUsers) {
            const userToRemove = $event.value;
            const index = arrayUsers.findIndex(user => user.username === userToRemove.username);
            if (index > -1) {
                arrayUsers.splice(index, 1);
            }
        }

        this.populateAvailableUsersOnOffices();
    }

    /*
    * Render of task office on change
    * */
    populateAvailableUsersOnOffices() {
        console.log('AssociationOfficeComponent - populateAvailableUsersOnOffices');

        const me = this;
        me.usersObservable.subscribe(
            (data) => {

                const listUsersAvailableFinal = data;

                if (listUsersAvailableFinal && listUsersAvailableFinal.length > 0 &&
                    me.taskOfficesArray && me.taskOfficesArray.length > 0) {

                    me.taskOfficesArray.forEach(
                        (taskOffice) => {

                            const listUsersAvailable = [];
                            listUsersAvailableFinal.forEach(user => listUsersAvailable.push(user));

                            if (taskOffice.office.userProviders && taskOffice.office.userProviders.length > 0) {
                                taskOffice.office.userProviders.forEach(
                                    (user) => {

                                        const index = listUsersAvailable.
                                            findIndex(userAvailable => userAvailable.username === user.username);
                                        if (index > -1) {
                                            listUsersAvailable.splice(index, 1);
                                        }
                                    }
                                );
                            }
                            if (taskOffice.office.userBeneficiaries && taskOffice.office.userBeneficiaries.length > 0) {
                                taskOffice.office.userBeneficiaries.forEach(
                                    (user) => {

                                        const index = listUsersAvailable.
                                            findIndex(userAvailable => userAvailable.username === user.username);
                                        if (index > -1) {
                                            listUsersAvailable.splice(index, 1);
                                        }
                                    }
                                );
                            }
                            taskOffice.office.userAvailable = Observable.of(listUsersAvailable);
                        }
                    );
                }
            },
            (error) => {
                console.error('AssociationOfficeComponent - populateAvailableUsersOnOffices - error \n', error);
            }
        );
        this.checkAssociationOffice.next(true);
    }
}
