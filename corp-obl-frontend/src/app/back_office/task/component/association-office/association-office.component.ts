import {Component, Input, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {OfficeService} from '../../../office/service/office.service';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {AuthorityEnum} from '../../../../shared/common/api/enum/authority.enum';
import {UserInfoService} from '../../../../user/service/user-info.service';
import {UserService} from '../../../../user/service/user.service';
import {IHash} from '../../../../shared/common/interface/ihash';
import {User} from '../../../../user/model/user';
import {Company} from '../../../company/model/company';
import {TaskOffice} from '../../model/taskoffice';

@Component({
    selector: 'app-association-office',
    templateUrl: './association-office.component.html',
    styleUrls: ['./association-office.component.css']
})
export class AssociationOfficeComponent implements OnInit {

    officesArray = [];
    taskOfficesArray = [];
    selectedTaskOffices = [];
    selectedOffices = [];
    officeUserProviders: IHash = {};
    officeUserBeneficiaries: IHash = {};
    officesObservable: Observable<any[]>;
    usersObservable: Observable<any[]>;

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

        me.officesObservable = me.officeService.getOffices();
        me.usersObservable = me.userService.getAllUsersExceptAdminRole();

        me.getTaskOfficesArray(null);
    }

    getTaskOfficesArray(taskOffices) {

        const me = this;
        if (!taskOffices) {
            return;
        }

        me.taskOfficesArray = taskOffices;

        me.taskOfficesArray.forEach((taskOffice) => {
            me.selectedOffices.push(taskOffice.office);
        });
    }

    onAddOfficeRealation($event) {
        console.log('AssociationOfficeComponent - onAddOfficeRealation');

        const taskOffice: TaskOffice = new TaskOffice();
        taskOffice.office = $event;

        this.taskOfficesArray.push(taskOffice);
    }

    onRemoveOfficeRealation($event) {
        console.log('AssociationOfficeComponent - onChangeSelectOffices');

        const officeToRemove = $event.value;
        const index = this.taskOfficesArray.findIndex(taskOffice => taskOffice.office.idOffice === officeToRemove.idOffice);
        if (index > -1) {
            this.taskOfficesArray.splice(index, 1);
        }
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
    }
}
