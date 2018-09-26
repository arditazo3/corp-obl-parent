import {Component, OnInit, ViewChild} from '@angular/core';
import {Office} from '../../../office/model/office';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {TaskTemplateService} from '../../../tasktemplate/service/tasktemplate.service';
import {Observable} from '../../../../../../node_modules/rxjs/Rx';
import {CompanyService} from '../../../company/service/company.service';
import {TopicService} from '../../../topic/service/topic.service';
import {TaskService} from '../../service/task.service';
import {Task} from '../../model/task';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {TaskTemplate} from '../../../tasktemplate/model/tasktemplate';
import {ObjectSearchTaskTemplate} from '../../../tasktemplate/model/object-search-tasktemplate';

@Component({
    selector: 'app-configuration-task',
    templateUrl: './configuration-task.component.html',
    styleUrls: ['./configuration-task.component.css']
})
export class ConfigurationTaskComponent implements OnInit {

    @ViewChild('myTable') table: any;

    descriptionTaskTemplate: string;

    columns: any[];
    rows: Task[];
    data: any;
    temp = [];
    errorDetails: ApiErrorDetails;

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

        const objectSearchTaskTemplate = new ObjectSearchTaskTemplate(this.descriptionTaskTemplate, this.selectedCompanies, this.selectedTopics);

        this.taskTemplateService.searchTaskTemplate(objectSearchTaskTemplate);

    }

    createNewTaskTemplate() {
        console.log('ConfigurationTaskComponent - createNewTaskTemplate');

        this.router.navigate(['/back-office/task-template/create']);
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

    modifyTaskTemplate(group) {
        console.log('modifyTaskTemplate - modifyTaskTemplate');

        this.transferService.objectParam = group.value[0];

        this.router.navigate(['/back-office/task-template/edit']);
    }

    createTask(group) {
        console.log('modifyTaskTemplate - createTask');
    }
}
