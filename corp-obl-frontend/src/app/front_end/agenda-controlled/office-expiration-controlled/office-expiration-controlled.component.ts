import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {TaskOfficeExpirations} from '../../model/task-office-expirations';
import {Expiration} from '../../model/expiration';
import {ApiErrorDetails} from '../../../shared/common/api/model/api-error-details';
import {ExpirationService} from '../../service/expiration.service';
import {StatusExpirationEnum} from '../../../shared/common/api/enum/status.expiration.enum';

@Component({
  selector: 'app-office-expiration-controlled',
  templateUrl: './office-expiration-controlled.component.html',
  styleUrls: ['./office-expiration-controlled.component.css']
})
export class OfficeExpirationControlledComponent implements OnInit {

    @Output() updateTaskExpirationOnChange = new EventEmitter<boolean>();
    @Input() taskExpiration: TaskOfficeExpirations;
    @Input() expiration: Expiration;

    completedActivityBtn = false;
    notCompletedActivityBtn = false;

    errorDetails: ApiErrorDetails;

    constructor(
        private expirationService: ExpirationService
    ) {
    }

    ngOnInit() {
        console.log('OfficeExpirationControlledComponent - ngOnInit');

        this.setStatusExpiration();
    }

    setStatusExpiration() {
        const statusExp = this.expiration.expirationDetail.statusExpiration;

        this.completedActivityBtn = false;
        this.notCompletedActivityBtn = false;

        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.BASE]) {
            this.completedActivityBtn = true;
        }
        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.COMPLETED]) {
            this.notCompletedActivityBtn = true;
        }
        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.APPROVED]) {
        }
        if (statusExp === StatusExpirationEnum[StatusExpirationEnum.ARCHIVED]) {
        }
    }

    completedExp() {
        console.log('OfficeExpirationControlledComponent - completedExp');

        this.expiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.COMPLETED];
        this.saveStatusExpirationOnChange();
    }

    notCompletedExp() {
        console.log('OfficeExpirationControlledComponent - archivedExp');

        this.expiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.NOT_COMPLETED];
        this.saveStatusExpirationOnChange();
    }

    saveStatusExpirationOnChange() {
        console.log('OfficeExpirationControlledComponent - saveStatusExpirationOnChange');

        const me = this;
        me.expirationService.statusExpirationOnChange(this.expiration).subscribe(
            data => {
                me.errorDetails = undefined;
                me.expiration = data;
                me.setStatusExpiration();
                me.updateTaskExpirationOnChange.next(true);
                console.log('OfficeExpirationControlledComponent - saveStatusExpirationOnChange - next');
            },
            error => {
                console.error('OfficeExpirationControlledComponent - saveStatusExpirationOnChange - error \n', error);
            }
        );
    }
}
