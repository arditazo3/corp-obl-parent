import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Task} from '../../../task/model/task';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';

@Component({
    selector: 'app-office-taks-collapse',
    templateUrl: './office-taks-collapse.component.html',
    styleUrls: ['./office-taks-collapse.component.css']
})
export class OfficeTaksCollapseComponent implements OnInit {

    officeTaskTemplatesArray = [];

    constructor(
        private router: Router,
        private transferService: TransferDataService
    ) {
    }

    ngOnInit() {
        console.log('OfficeTaksCollapseComponent - ngOnInit');

        const me = this;
        me.getOfficeTaskTemplatesArray(null);
    }

    getOfficeTaskTemplatesArray(officeTaskTemplatessFromParent) {
        console.log('OfficeTaksCollapseComponent - getOfficeTaskTemplatesArray');

        if (!officeTaskTemplatessFromParent) {
            return;
        }
        this.officeTaskTemplatesArray = officeTaskTemplatessFromParent;
    }

    modifyTaskTemplate(taskTemplate) {
        console.log('OfficeTaksCollapseComponent - ngOnInit');

        const task: Task = new Task();
        task.taskTemplate = taskTemplate;
        this.transferService.objectParam = {
            isTaskTemplateForm: true,
            task: task
        };

        this.router.navigate(['/back-office/quick-configuration']);
    }

    createTaskTemplate(officeTaskTemplates) {
        console.log('OfficeTaksCollapseComponent - createTaskTemplate');

        this.transferService.objectParam = {
            isTaskTemplateForm: true,
            task: undefined
        };

        this.router.navigate(['/back-office/quick-configuration']);
    }
}
