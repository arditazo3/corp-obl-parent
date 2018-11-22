import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TaskOffice} from '../../model/taskoffice';
import {Office} from '../../../office/model/office';
import {User} from '../../../../user/model/user';
import {Company} from '../../../company/model/company';
import {TranslateService} from '@ngx-translate/core';
import {DeviceDetectorService} from 'ngx-device-detector';
import {Task} from '../../model/task';

@Component({
    selector: 'app-association-office-users',
    templateUrl: './association-office-users.component.html',
    styleUrls: ['./association-office-users.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class AssociationOfficeUsersComponent implements OnInit {

    @Output() removeOfficeEvent = new EventEmitter<Office>();

    @Input() taskOffice: TaskOffice;

    isMobile = false;

    office: Office;
    company: Company;

    userAvailableArray: User[];
    userProvidersArray: User[];
    userBeneficiariesArray: User[];

    constructor(private deviceService: DeviceDetectorService) {

        this.isMobile = this.deviceService.isMobile();
    }

    ngOnInit() {
        console.log('AssociationOfficeUsersComponent - ngOnInit');

        this.office = this.taskOffice.office;
        this.company = this.office.company;

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
        console.log('AssociationOfficeUsersComponent - removeOfficeBtn');

        office.userProviders = [];
        office.userProviders = [];
        office.userBeneficiaries = [];

        this.removeOffice(office);
    }

    removeOffice(office) {

        this.removeOfficeEvent.emit(office);
    }

    onAddProvidersOffice($event) {

        const userSelected = $event;

        this.userProvidersArray.push(userSelected);


        this.populateAvailableUsersOnOffices();
    }

    onRemoveProvidersOffice($event) {

        const me = this;
        const userSelected = $event.value;

        me.userAvailableArray.push(userSelected);
        me.userAvailableArray = [...me.userAvailableArray];

        this.populateAvailableUsersOnOffices();
    }

    onAddBeneficiariesOffice($event) {

        const userSelected = $event;

        this.userBeneficiariesArray.push(userSelected);

        this.populateAvailableUsersOnOffices();
    }

    onRemoveBeneficiariesOffice($event, office) {

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

                        const indexBeneficiary = me.userBeneficiariesArray
                            .findIndex(userAvailable => userAvailable.username === user.username);

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
