import {Component, Input, OnInit} from '@angular/core';
import {TaskOfficeExpirations} from '../../model/task-office-expirations';
import {ExpirationService} from '../../service/expiration.service';
import {StatusExpirationEnum} from '../../../shared/common/api/enum/status.expiration.enum';

@Component({
    selector: 'app-task-template-expirations',
    templateUrl: './task-template-expirations.component.html',
    styleUrls: ['./task-template-expirations.component.css']
})
export class TaskTemplateExpirationsComponent implements OnInit {

    @Input() taskExpirations: TaskOfficeExpirations[];

    itemIndexUpdatedOpenPanel;
    activeIds: string[] = [];

    constructor(
        private expirationService: ExpirationService
    ) {
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
            (taskExpirationLoop.idTaskTemplate === taskExpiration.idTaskTemplate &&
                taskExpirationLoop.office.idOffice === taskExpiration.office.idOffice &&
                taskExpirationLoop.task.idTask === taskExpiration.task.idTask));

        this.activeIds = [];
        this.activeIds.push('panel-' + this.itemIndexUpdatedOpenPanel);
        this.taskExpirations[this.itemIndexUpdatedOpenPanel] = taskExpiration;
    }
}
