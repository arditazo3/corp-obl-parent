import {Component, Input, OnInit} from '@angular/core';
import {TaskOfficeExpirations} from '../../model/task-office-expirations';
import {ExpirationService} from '../../service/expiration.service';
import {StatusExpirationEnum} from '../../../shared/common/api/enum/status.expiration.enum';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-task-template-expirations',
    templateUrl: './task-template-expirations.component.html',
    styleUrls: ['./task-template-expirations.component.css']
})
export class TaskTemplateExpirationsComponent implements OnInit {

    isMobile = false;

    @Input() taskExpirations: TaskOfficeExpirations[];

    itemIndexUpdatedOpenPanel;
    activeIds: string[] = [];

    constructor(
        private expirationService: ExpirationService,
        private deviceService: DeviceDetectorService
    ) {

        this.isMobile = this.deviceService.isMobile();
    }

    ngOnInit() {
    }

    restoreAll(taskExpiration) {
        console.log('TaskTemplateExpirationsComponent - restoreAll');

        taskExpiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.RESTORE];
        this.saveStatusAllExpirationsOnChange(taskExpiration);
    }

    archiveAll(taskExpiration) {
        console.log('TaskTemplateExpirationsComponent - archiveAll');

        taskExpiration.statusExpirationOnChange = StatusExpirationEnum[StatusExpirationEnum.ARCHIVED];

        this.saveStatusAllExpirationsOnChange(taskExpiration);
    }

    saveStatusAllExpirationsOnChange(taskExpiration) {
        console.log('TaskTemplateExpirationsComponent - saveStatusAllExpirationsOnChange');

        const me = this;

        me.expirationService.saveStatusAllExpirationsOnChange(taskExpiration).subscribe(
            data => {
                me.updateTaskExpirationsList(data);
                console.log('TaskTemplateExpirationsComponent - saveStatusAllExpirationsOnChange - next');
            },
            error => {

            }
        );
    }

    updateTaskExpirationsList(taskExpiration) {

        this.itemIndexUpdatedOpenPanel = this.taskExpirations.findIndex(taskExpirationLoop =>
            (this.containItemToUpdateIndex(taskExpirationLoop, taskExpiration)));

        this.activeIds = [];
        this.activeIds.push('panel-' + this.itemIndexUpdatedOpenPanel);
        this.taskExpirations[this.itemIndexUpdatedOpenPanel] = taskExpiration;
    }

    containItemToUpdateIndex(taskExpirationLoop, taskExpiration): boolean {
        let containItemIndex = false;

        taskExpirationLoop.expirations.forEach(expiration => {

            if (expiration.idExpiration === taskExpiration.expirations[0].idExpiration) {
                containItemIndex = true;
            }
        });

        return containItemIndex;
    }

    updateTaskExpirationOnChange($event, expiration, taskExpiration) {
        console.log('TaskTemplateExpirationsComponent - updateTaskExpirationOnChange');

        if ($event) {
            const me = this;
            this.expirationService.updateTaskExpiration(expiration).subscribe(
                data => {
                    me.updateTaskExpirationsList(data);
                    console.log('TaskTemplateExpirationsComponent - updateTaskExpirationOnChange - next');
                },
                error => {
                    console.error('TaskTemplateExpirationsComponent - updateTaskExpirationOnChange - error \n', error);
                }
            );
        }
    }
}
