import {Component, Input, OnInit} from '@angular/core';
import {TaskOfficeExpirations} from '../../model/task-office-expirations';
import {ExpirationService} from '../../service/expiration.service';
import {TaskService} from '../../../back_office/task/service/task.service';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-task-template-expirations-controlled',
    templateUrl: './task-template-expirations-controlled.component.html',
    styleUrls: ['./task-template-expirations-controlled.component.css']
})
export class TaskTemplateExpirationsControlledComponent implements OnInit {

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

    updateTaskExpirationOnChange($event, expiration, taskExpiration) {
        console.log('TaskTemplateExpirationsControlledComponent - updateTaskExpirationOnChange');

        if ($event) {
            const me = this;
            this.expirationService.updateTaskExpiration(expiration).subscribe(
                data => {
                    me.updateTaskExpirationsList(data);
                    console.log('OfficeExpirationControlledComponent - updateTaskExpirationOnChange - next');
                },
                error => {
                    console.error('OfficeExpirationControlledComponent - updateTaskExpirationOnChange - error \n', error);
                }
            );
        }
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
}
