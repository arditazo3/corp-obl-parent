import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Task} from '../../../task/model/task';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {TaskService} from '../../../task/service/task.service';

@Component({
    selector: 'app-office-taks-collapse',
    templateUrl: './office-taks-collapse.component.html',
    styleUrls: ['./office-taks-collapse.component.css']
})
export class OfficeTaksCollapseComponent implements OnInit {

    officeTaskTemplatesArray = [];

    constructor(
        private router: Router,
        private transferService: TransferDataService,
        private taskService: TaskService
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

    modifyTaskTemplate(taskTemplate, office) {
        console.log('OfficeTaksCollapseComponent - ngOnInit');

        const me = this;
        const tempObject = {
            isNewForm: false,
            task: null,
            office: office,
            taskOffice: null
        };

        this.taskService.getTaskOfficeByTaskTemplateAndOffice(taskTemplate, office).subscribe(
            data => {

                const task = data.task;
                task.taskTemplate = data.taskTemplate;
                tempObject.task = task;
                tempObject.taskOffice = data;
                me.transferService.objectParam = tempObject;

                this.router.navigate(['/back-office/quick-configuration/edit']);

                console.log('OfficeTaksCollapseComponent - modifyTaskTemplate - next');
            },
            error => {
                console.error('OfficeTaksCollapseComponent - modifyTaskTemplate - error');
            }
        );
    }

    createTaskTemplate(officeTaskTemplates) {
        console.log('OfficeTaksCollapseComponent - createTaskTemplate');

        this.transferService.objectParam = {
            isNewForm: true,
            task: undefined,
            office: officeTaskTemplates.office
        };

        this.router.navigate(['/back-office/quick-configuration/create']);
    }
}
