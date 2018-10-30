import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TaskOfficeExpirations} from '../../model/task-office-expirations';
import {Expiration} from '../../model/expiration';
import {StatusExpirationEnum} from '../../../shared/common/api/enum/status.expiration.enum';
import {ExpirationService} from '../../service/expiration.service';
import {ApiErrorDetails} from '../../../shared/common/api/model/api-error-details';

@Component({
    selector: 'app-office-expiration',
    templateUrl: './office-expiration.component.html',
    styleUrls: ['./office-expiration.component.css']
})
export class OfficeExpirationComponent implements OnInit {

    @Input() taskExpiration: TaskOfficeExpirations;
    @Input() expiration: Expiration;

    restoreBtn = false;
    archivedBtn = false;
    approvedBtn = false;
    notApprovedBtn = false;

    errorDetails: ApiErrorDetails;

    constructor(
        private expirationService: ExpirationService
    ) {
    }

    ngOnInit() {
        console.log('OfficeExpirationComponent - ngOnInit');

        this.setStatusExpiration();
    }

    setStatusExpiration() {
        const statusExp = this.expiration.expirationDetail.statusExpiration;

        this.restoreBtn = false;
        this.archivedBtn = false;
        this.approvedBtn = false;
        this.notApprovedBtn = false;

        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.COMPLETED] ||
            statusExp === StatusExpirationEnum[StatusExpirationEnum.ARCHIVED]) {
            this.restoreBtn = true;
        }
        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.BASE] ||
            statusExp === StatusExpirationEnum[StatusExpirationEnum.COMPLETED] ||
            statusExp === StatusExpirationEnum[StatusExpirationEnum.APPROVED]) {
            this.archivedBtn = true;
        }
        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.COMPLETED]) {
            this.approvedBtn = true;
        }
        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.APPROVED]) {
            this.notApprovedBtn = true;
        }
    }

    restoreExp() {
        console.log('OfficeExpirationComponent - restoreExp');

        this.expiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.RESTORE];
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
}
