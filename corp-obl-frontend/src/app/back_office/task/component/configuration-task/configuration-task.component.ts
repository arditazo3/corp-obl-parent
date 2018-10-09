import {Component, OnInit, ViewChild} from '@angular/core';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {TaskTemplateService} from '../../../tasktemplate/service/tasktemplate.service';
import {CompanyService} from '../../../company/service/company.service';
import {TopicService} from '../../../topic/service/topic.service';
import {Task} from '../../model/task';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {ObjectSearchTaskTemplate} from '../../../tasktemplate/model/object-search-tasktemplate';
import {Observable} from 'rxjs';

@Component({
    selector: 'app-configuration-task',
    templateUrl: './configuration-task.component.html',
    styleUrls: ['./configuration-task.component.css'],
})
export class ConfigurationTaskComponent implements OnInit {

    @ViewChild('myTable') table: any;
    expansionDefault = false;

    descriptionTaskTemplate: string;

    columns: any[];
    rows: Task[];
    data: any;
    errorDetails: ApiErrorDetails;
    idGroup = 0;
    index = 1;

    companiesObservable: Observable<any[]>;
    topicsObservable: Observable<any[]>;

    selectedCompanies = [];
    selectedTopics = [];

    constructor(
        private router: Router,
        private taskTemplateService: TaskTemplateService,
        private topicService: TopicService,
        private companyService: CompanyService,
        private transferService: TransferDataService
    ) {}

    async ngOnInit() {
        console.log('ConfigurationTaskComponent - ngOnInit');

        this.getCompanies();
        this.getTopics();
        this.getTaskTemplates();
    }

    getCompanies() {
        console.log('ConfigurationTaskComponent - getCompanies');

        const me = this;
        me.companiesObservable = me.companyService.getCompaniesByRole();
    }

    getTopics() {
        console.log('ConfigurationTaskComponent - getTopics');

        const me = this;
        me.topicsObservable = me.topicService.getTopicsByRole();
    }

    getTaskTemplates() {
        console.log('ConfigurationTaskComponent - getTaskTemplates');

        const me = this;
        this.taskTemplateService.getTaskTemplatesForTable().subscribe(
            (data) => {
                me.rows = data;
           //     console.log(JSON.stringify(data));
            }
        );
    }

    searchTaskTemplate() {
        console.log('ConfigurationTaskComponent - createNewTaskTemplate');

        const me = this;
        const objectSearchTaskTemplate = new ObjectSearchTaskTemplate(this.descriptionTaskTemplate,
            this.selectedCompanies, this.selectedTopics);

        this.taskTemplateService.searchTaskTemplate(objectSearchTaskTemplate).subscribe(
            (data) => {
                me.rows = data;
                //     console.log(JSON.stringify(data));
            }
        );

    }

    createNewTaskTemplate() {
        console.log('ConfigurationTaskComponent - createNewTaskTemplate');

        this.transferService.objectParam = {
            isTaskTemplateForm: true,
            task: undefined,
            hasOfficeAssociated: true
        };

        this.router.navigate(['/back-office/task-template/create']);
    }

    modifyTaskTemplate(group) {
        console.log('modifyTaskTemplate - modifyTaskTemplate');

        this.transferService.objectParam = {
            isTaskTemplateForm: true,
            task: group.value[0],
            hasOfficeAssociated: true
        };

        this.router.navigate(['/back-office/task-template/edit']);
    }

    createTask(group) {
        console.log('modifyTaskTemplate - createTask');

        const taskTemp = group.value[0];
        const newTaskTemp = new Task();
        newTaskTemp.taskTemplate = taskTemp.taskTemplate;

        this.transferService.objectParam = {
            isTaskTemplateForm: false,
            task: newTaskTemp,
            newTask: true
        };

        this.router.navigate(['/back-office/task/create-edit']);
    }

    displayConfigurationText(row, group, rowIndex): string {

        if (row && row.idTask && row.idTaskTemplate) {

            let configText = '';

            if (this.idGroup !== row.idTaskTemplate) {
                this.index = 1;
                this.idGroup = row.idTaskTemplate;
            } else {
                this.index++;
            }

            configText += 'Configuration ' + rowIndex + ': ' + row.expirationType + ' - ' + row.daysBeforeShowExpiration;
            return configText;
        }
        return;
    }

    modifyTask(row) {
        console.log('modifyTaskTemplate - modifyTask');

        this.transferService.objectParam = {
            isTaskTemplateForm: false,
            task: row
        };

        this.router.navigate(['/back-office/task/create-edit']);
    }

    toggleExpandCollapse(collapse) {
        if (collapse) {
            this.expansionDefault = collapse;
        } else {
            this.expansionDefault = collapse;
            this.getTaskTemplates();
        }
    }

    getGroupRowHeight(group, rowHeight) {
        let style = {};

        style = {
            height: (group.length * 40) + 'px',
            width: '100%'
        };

        return style;
    }

    toggleExpandGroup(group) {
        console.log('ConfigurationTaskComponent - toggled expand group', group);
        this.table.groupHeader.toggleExpandGroup(group);
    }

    onDetailToggle(event) {
        console.log('ConfigurationTaskComponent - detail toggled', event);
    }
}
