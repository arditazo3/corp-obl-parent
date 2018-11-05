import {Component, Input, OnInit} from '@angular/core';
import {TaskOfficeExpirations} from '../../model/task-office-expirations';
import {Expiration} from '../../model/expiration';
import {StatusExpirationEnum} from '../../../shared/common/api/enum/status.expiration.enum';
import {ExpirationService} from '../../service/expiration.service';
import {ApiErrorDetails} from '../../../shared/common/api/model/api-error-details';
import {ExpirationActivity} from '../../model/expiration-activity';

@Component({
    selector: 'app-office-expiration',
    templateUrl: './office-expiration.component.html',
    styleUrls: ['./office-expiration.component.css']
})
export class OfficeExpirationComponent implements OnInit {

    @Input() taskExpiration: TaskOfficeExpirations;
    @Input() expiration: Expiration;

    restoreBtn = false;
    rejectBtn = false;
    archivedBtn = false;
    approvedBtn = false;
    notApprovedBtn = false;

    hideDetailsBtn = false;
    expirationActivitiesTemp: ExpirationActivity[];

    errorDetails: ApiErrorDetails;

    constructor(
        private expirationService: ExpirationService
    ) {
    }

    ngOnInit() {
        console.log('OfficeExpirationComponent - ngOnInit');

        this.setStatusExpiration();

        this.expirationActivitiesTemp = this.expiration.expirationActivities;

        this.showHideActivitiesOnChange();
    }

    setStatusExpiration() {
        const statusExp = this.expiration.expirationDetail.statusExpiration;

        this.restoreBtn = false;
        this.rejectBtn = false;
        this.archivedBtn = false;
        this.approvedBtn = false;
        this.notApprovedBtn = false;

        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.ARCHIVED]) {
            this.restoreBtn = true;
        }
        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.BASE] ||
            statusExp === StatusExpirationEnum[StatusExpirationEnum.COMPLETED] ||
            statusExp === StatusExpirationEnum[StatusExpirationEnum.APPROVED]) {
            this.archivedBtn = true;
        }
        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.COMPLETED]) {
            this.approvedBtn = true;
            this.rejectBtn = true;
        }
        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.APPROVED]) {
            this.notApprovedBtn = true;
        }

        this.showHideActivitiesOnChange();
    }

    restoreExp() {
        console.log('OfficeExpirationComponent - restoreExp');

        this.expiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.RESTORE];
        this.saveStatusExpirationOnChange();
    }

    rejectExp() {
        console.log('OfficeExpirationComponent - rejectExp');

        this.expiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.REJECT];
        this.saveStatusExpirationOnChange();
    }

    archivedExp() {
        console.log('OfficeExpirationComponent - archivedExp');

        this.expiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.ARCHIVED];
        this.saveStatusExpirationOnChange();
    }

    approvedExp() {
        console.log('OfficeExpirationComponent - approvedExp');

        this.expiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.APPROVED];
        this.saveStatusExpirationOnChange();
    }

    notApprovedExp() {
        console.log('OfficeExpirationComponent - archivedExp');

        this.expiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.NOT_APPROVED];
        this.saveStatusExpirationOnChange();
    }

    saveStatusExpirationOnChange() {
        console.log('OfficeExpirationComponent - saveStatusExpirationOnChange');

        const me = this;
        me.expirationService.statusExpirationOnChange(this.expiration).subscribe(
            data => {
                me.errorDetails = undefined;
                me.expiration = data;
                me.setStatusExpiration();

                console.log('OfficeExpirationComponent - saveStatusExpirationOnChange - next');
            },
            error => {
                console.error('OfficeExpirationComponent - saveStatusExpirationOnChange - error \n', error);
            }
        );
    }

    showHideDetailsMsg() {
        this.hideDetailsBtn = !this.hideDetailsBtn;

        this.showHideActivitiesOnChange();
    }

    showHideActivitiesOnChange() {

        if (this.expirationActivitiesTemp) {
            this.expiration.expirationActivities = this.includeExcludeEmptyActivity();
        }
    }

    includeExcludeEmptyActivity(): any {

        const expirationActivitiesLocal = this.expirationActivitiesTemp.slice(0);

        let index = 0;
        const showSingleActivity: ExpirationActivity[] = [];

        expirationActivitiesLocal.forEach((expirationActivityLoop) => {

            if (!expirationActivityLoop.idExpirationActivity &&
                !this.restoreBtn) {
                showSingleActivity.push(expirationActivityLoop);
            } else if (expirationActivityLoop.idExpirationActivity && index === 0) {
                showSingleActivity.push(expirationActivityLoop);

                if (!this.hideDetailsBtn) {
                    index++;
                }

            }
        });

        return showSingleActivity;
    }

    updateExpirationActivities($event) {
        if ($event && this.expiration.expirationActivities && this.expiration.expirationActivities.length > 1) {

            this.expirationActivitiesTemp.splice(1, 0, this.expiration.expirationActivities[1]);
            this.showHideActivitiesOnChange();
        }
    }
}
