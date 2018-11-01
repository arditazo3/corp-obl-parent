import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Task} from '../../model/task';
import {TaskOffice} from '../../model/taskoffice';
import {Observable} from 'rxjs';
import {Office} from '../../../office/model/office';
import {IHash} from '../../../../shared/common/interface/ihash';

@Component({
    selector: 'app-association-office-users',
    templateUrl: './association-office-users.component.html',
    styleUrls: ['./association-office-users.component.css']
})
export class AssociationOfficeUsersComponent implements OnInit {

    @Output() removeOfficeEvent = new EventEmitter<Office>();

    @Input() taskOffice: TaskOffice;

    officeUserProviders: IHash = {};
    officeUserBeneficiaries: IHash = {};

    constructor() {
    }

    ngOnInit() {

        this.taskOffice.office.userAvailable = this.taskOffice.office.company.usersAssociated.map(companyUsers => companyUsers.user);
    }

    removeOfficeBtn(office) {
        console.log('AssociationOfficeComponent - removeOfficeBtn');

        this.removeOffice(office);
    }

    removeOffice(office) {

        this.removeOfficeEvent.emit(office);
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

        const listUsersAvailableFinal = this.taskOffice.office.userAvailable;

        if (listUsersAvailableFinal && listUsersAvailableFinal.length) {

            const listUsersAvailable = [];
            listUsersAvailableFinal.forEach(user => listUsersAvailable.push(user));

            if (this.taskOffice.office.userProviders && this.taskOffice.office.userProviders.length > 0) {
                this.taskOffice.office.userProviders.forEach(
                    (user) => {

                        const index = listUsersAvailable.findIndex(userAvailable => userAvailable.username === user.username);
                        if (index > -1) {
                            listUsersAvailable.splice(index, 1);
                        }
                    }
                );
            }
            if (this.taskOffice.office.userBeneficiaries && this.taskOffice.office.userBeneficiaries.length > 0) {
                this.taskOffice.office.userBeneficiaries.forEach(
                    (user) => {

                        const index = listUsersAvailable.findIndex(userAvailable => userAvailable.username === user.username);
                        if (index > -1) {
                            listUsersAvailable.splice(index, 1);
                        }
                    }
                );
            }
            this.taskOffice.office.userAvailable = listUsersAvailable;
        }
        //      this.checkAssociationOffice.next(true);
    }
}
