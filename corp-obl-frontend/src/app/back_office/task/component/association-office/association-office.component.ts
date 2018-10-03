import {ChangeDetectionStrategy, Component, Input, OnInit, ViewChild} from '@angular/core';
import {Observable} from 'rxjs';
import {OfficeService} from '../../../office/service/office.service';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {UserService} from '../../../../user/service/user.service';
import {IHash} from '../../../../shared/common/interface/ihash';
import {TaskOffice} from '../../model/taskoffice';
import {NgSelectComponent} from '@ng-select/ng-select';

@Component({
    selector: 'app-association-office',
    templateUrl: './association-office.component.html',
    styleUrls: ['./association-office.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AssociationOfficeComponent implements OnInit {

    taskOfficesArray = [];
    selectedOffices = [];
    selectedOfficesEmpty = [];
    officeUserProviders: IHash = {};
    officeUserBeneficiaries: IHash = {};
    officesObservable: Observable<any[]>;
    officesObservableStoredOnComp: Observable<any[]>;
    officesAvailable = [];
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

        me.usersObservable = me.userService.getAllUsersExceptAdminRole();

        me.getTaskOfficesArray(null);
    }

    getTaskOfficesArray(taskOffices) {
        console.log('AssociationOfficeComponent - getTaskOfficesArray');

        const me = this;
        if (!taskOffices) {
            return;
        }

        me.taskOfficesArray = taskOffices;

        me.taskOfficesArray.forEach((taskOffice) => {
            me.selectedOffices.push(taskOffice.office);
        });

        me.officesObservableStoredOnComp = this.officeService.getOffices();
        me.officesObservable = this.officeService.getOffices();
        this.removeAddDisplayedOffices(taskOffices, null);
    }

    onAddOfficeRealation($event) {
        console.log('AssociationOfficeComponent - onAddOfficeRealation');

        const taskOffice: TaskOffice = new TaskOffice();
        taskOffice.office = $event;

        this.taskOfficesArray.push(taskOffice);

        this.removeAddDisplayedOffices([taskOffice], null);
        this.selectedOfficesEmpty = [];
    }

    onRemoveOfficeRealation($event) {
        console.log('AssociationOfficeComponent - onChangeSelectOffices');

        const officeToRemove = $event.value;
        const index = this.taskOfficesArray.findIndex(taskOfficeLoop => taskOfficeLoop.office.idOffice === officeToRemove.idOffice);
        if (index > -1) {
            this.taskOfficesArray.splice(index, 1);
        }

        const taskOffice = new TaskOffice();
        taskOffice.office = officeToRemove;

        this.removeAddDisplayedOffices(null, [taskOffice]);
        this.selectedOfficesEmpty = [];
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

        this.removeAddDisplayedOffices(null, office);
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

    removeAddDisplayedOffices(officesToRemove, officesToAdd) {
        console.log('AssociationOfficeComponent - removeAddDisplayedOffices');

        const me = this;
        if (!me.officesObservableStoredOnComp) {
            return;
        }
        me.officesObservable.subscribe(
            data => {

                const allOffices = data;
                if (officesToRemove && officesToRemove.length > 0) {

                    officesToRemove.forEach(officeToRemove => {
                        const index = allOffices.findIndex(officeLoop => officeLoop.idOffice === officeToRemove.office.idOffice);
                        if (index > -1) {
                            allOffices.splice(index, 1);
                        }
                    });
                }
                if (officesToAdd && officesToAdd.length > 0) {
                    allOffices.push(officesToAdd);
                }

                me.officesAvailable = allOffices;
               // me.ngSelectOffices.refres
                me.officesObservable = Observable.of(allOffices);
            },
            error => {

            }
        );
    }
}
