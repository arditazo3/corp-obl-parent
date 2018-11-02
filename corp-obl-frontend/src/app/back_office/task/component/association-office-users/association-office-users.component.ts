import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Task} from '../../model/task';
import {TaskOffice} from '../../model/taskoffice';
import {Observable} from 'rxjs';
import {Office} from '../../../office/model/office';
import {IHash} from '../../../../shared/common/interface/ihash';
import {User} from '../../../../user/model/user';
import {Company} from '../../../company/model/company';

@Component({
    selector: 'app-association-office-users',
    templateUrl: './association-office-users.component.html',
    styleUrls: ['./association-office-users.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AssociationOfficeUsersComponent implements OnInit {

    @Output() removeOfficeEvent = new EventEmitter<Office>();

    @Input() taskOffice: TaskOffice;

    office: Office;
    company: Company;

    userAvailableArray: User[];
    userProvidersArray: User[];
    userBeneficiariesArray: User[];

    constructor() {
    }

    ngOnInit() {

        this.office = this.taskOffice.office;
        this.company = this.office.company;

        if (this.company) {
            this.office.company.offices = [];
        }

        if (!this.office.userProviders) {
            this.office.userProviders = [];
        }
        if (!this.office.userBeneficiaries) {
            this.office.userBeneficiaries = [];
        }

        this.userProvidersArray = this.office.userProviders;
        this.userBeneficiariesArray = this.office.userBeneficiaries;

        this.userAvailableArray = this.company.usersAssociated.map(companyUsers => companyUsers.user);

        this.populateAvailableUsersOnOffices();
    }

    removeOfficeBtn(office) {
        console.log('AssociationOfficeComponent - removeOfficeBtn');

        office.userProviders = [];
        office.userProviders = [];
        office.userBeneficiaries = [];

        this.removeOffice(office);
    }

    removeOffice(office) {

        this.removeOfficeEvent.emit(office);
    }

    onAddProvidersOffice($event) {
        console.log('AssociationOfficeComponent - onAddProvidersOffice');

        const me = this;
        const userSelected = $event;

        this.userProvidersArray.push(userSelected);


        this.populateAvailableUsersOnOffices();
    }

    onRemoveProvidersOffice($event) {
        console.log('AssociationOfficeComponent - onRemoveProvidersOffice');

        const me = this;
        const userSelected = $event.value;

        me.userAvailableArray.push(userSelected);
        me.userAvailableArray = [...me.userAvailableArray];

        this.populateAvailableUsersOnOffices();
    }

    onAddBeneficiariesOffice($event) {
        console.log('AssociationOfficeComponent - onAddBeneficiariesOffice');

        const me = this;
        const userSelected = $event;

        this.userBeneficiariesArray.push(userSelected);

        this.populateAvailableUsersOnOffices();
    }

    onRemoveBeneficiariesOffice($event, office) {
        console.log('AssociationOfficeComponent - onRemoveBeneficiariesOffice');

        const me = this;
        const userSelected = $event.value;

        me.userAvailableArray.push(userSelected);
        me.userAvailableArray = [...me.userAvailableArray];

        this.populateAvailableUsersOnOffices();
    }

    /*
    * Render of task office on change
    * */
    populateAvailableUsersOnOffices() {
        console.log('AssociationOfficeComponent - populateAvailableUsersOnOffices');

        const me = this;

        if (this.userAvailableArray && this.userAvailableArray.length) {

            if (this.userProvidersArray && this.userProvidersArray.length > 0) {
                this.userProvidersArray.forEach(
                    (user) => {

                        const index = me.userAvailableArray.findIndex(userAvailable => userAvailable.username === user.username);
                        if (index > -1) {
                            me.userAvailableArray.splice(index, 1);
                            me.userAvailableArray = [...me.userAvailableArray];
                        }
                    }
                );
            }
            if (this.userBeneficiariesArray && this.userBeneficiariesArray.length > 0) {
                this.userBeneficiariesArray.forEach(
                    (user) => {

                        const index = me.userAvailableArray.findIndex(userAvailable => userAvailable.username === user.username);
                        if (index > -1) {
                            me.userAvailableArray.splice(index, 1);
                            me.userAvailableArray = [...me.userAvailableArray];
                        }
                    }
                );
            }
            if (this.userAvailableArray && this.userAvailableArray.length > 0) {
                this.userAvailableArray.forEach(
                    (user) => {

                        const indexProvider = me.userProvidersArray.findIndex(userAvailable => userAvailable.username === user.username);
                        if (indexProvider > -1) {
                            me.userProvidersArray.splice(indexProvider, 1);
                        }

                        const indexBeneficiary = me.userBeneficiariesArray.findIndex(userAvailable => userAvailable.username === user.username);
                        if (indexBeneficiary > -1) {
                            me.userBeneficiariesArray.splice(indexBeneficiary, 1);
                        }
                    }
                );
            }
        }

        this.office.userProviders = this.userProvidersArray;
        this.office.userBeneficiaries = this.userBeneficiariesArray;
    }
}
