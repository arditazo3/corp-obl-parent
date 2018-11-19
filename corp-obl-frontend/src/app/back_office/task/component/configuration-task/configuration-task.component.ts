import {
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    DoCheck,
    ElementRef,
    IterableDiffers, OnDestroy,
    OnInit,
    ViewChild
} from '@angular/core';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {TaskTemplateService} from '../../../tasktemplate/service/tasktemplate.service';
import {CompanyService} from '../../../company/service/company.service';
import {TopicService} from '../../../topic/service/topic.service';
import {Task} from '../../model/task';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {ObjectSearchTaskTemplate} from '../../../tasktemplate/model/object-search-tasktemplate';
import {Observable} from 'rxjs';
import {DataFilter} from '../../../../shared/common/api/model/data-filter';
import {PageEnum} from '../../../../shared/common/api/enum/page.enum';
import {Topic} from '../../../topic/model/topic';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-configuration-task',
    templateUrl: './configuration-task.component.html',
    styleUrls: ['./configuration-task.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class ConfigurationTaskComponent implements OnInit {

    @ViewChild('descriptionTask') descriptionTask: ElementRef;
    @ViewChild('myTable') table: any;
    expansionDefault = false;

    isMobile = false;
    langOnChange = '';
    descriptionTaskTemplate: string;

    columns: any[];
    rows: Task[] = []; 
    data: any;
    errorDetails: ApiErrorDetails;
    idGroup = 0;
    index = 1;

    companiesObservable: Observable<any[]>;
    tempTopicsArray: Topic[];
    topicsArray: Topic[];

    selectedCompanies = [];
    selectedTopics = [];
    dataFilter: DataFilter = new DataFilter(PageEnum.BO_TASK);

    constructor(
        private router: Router,
        private taskTemplateService: TaskTemplateService,
        private topicService: TopicService,
        private companyService: CompanyService,
        private transferService: TransferDataService,
        private translateService: TranslateService,
        private cdr: ChangeDetectorRef,
        private deviceService: DeviceDetectorService
    ) {
        const me = this;

        me.langOnChange = me.translateService.currentLang;

        me.translateService.onLangChange
            .subscribe((event: LangChangeEvent) => {
                if (event.lang) {
                    me.langOnChange = event.lang;
                    me.descriptionTopicOnChange();
                    me.descriptionTopicTableOnChange();
                    me.descriptionTopicSelectedOnChange();
                    me.descriptionTaskTemplateLangOnChange(me.rows);
                }
            });

        this.isMobile = this.deviceService.isMobile();
    }

    async ngOnInit() {
        console.log('ConfigurationTaskComponent - ngOnInit');

        const me = this;

        this.getCompanies();
        this.getTopics();
    //    this.getTaskTemplates();

        const dataFilterTemp: DataFilter = this.transferService.dataFilter;
        if (dataFilterTemp && dataFilterTemp.page === PageEnum.BO_TASK) {
            this.dataFilter = dataFilterTemp;
            this.descriptionTaskTemplate = this.dataFilter.description;
            this.selectedCompanies = this.dataFilter.companies;
            this.selectedTopics = this.dataFilter.topics;
        }

        this.searchTaskTemplate();
    }



    getCompanies() {
        console.log('ConfigurationTaskComponent - getCompanies');

        const me = this;
        me.companiesObservable = me.companyService.getCompaniesByRole();
    }

    getTopics() {
        console.log('ConfigurationTaskComponent - getTopics');

        const me = this;
        me.topicService.getTopicsByRole().subscribe(
            data => {
                me.tempTopicsArray = data;
                me.descriptionTopicOnChange();
            }
        );
    }

    getTaskTemplates() {
        console.log('ConfigurationTaskComponent - getTaskTemplates');

        const me = this;
        this.taskTemplateService.getTaskTemplatesForTable().subscribe(
            (data) => {
                me.descriptionTaskTemplateLangOnChange(data);
                me.rows = data;
            }
        );
    }

    descriptionTaskTemplateLangOnChange(data) {

        const me = this;

        if (data && data.length > 0) {
            data.forEach(object => {
                if (object.hasOwnProperty('idTask')) {
                    object.descriptionLangList.forEach(descriptionLang => {

                        if (descriptionLang.lang === me.langOnChange) {
                            object.descriptionTask = descriptionLang.description;
                        }
                    });
                } else if (object.hasOwnProperty('idTaskTemplate')) {
                    object.descriptionLangList.forEach(descriptionLang => {

                        if (descriptionLang.lang === me.langOnChange) {
                            object.descriptionTaskTemplate = descriptionLang.description;
                        }
                    });
                } else {
                    object.taskTemplates.forEach(taskTemplate => {
                        taskTemplate.descriptionLangList.forEach(descriptionLang => {

                            if (descriptionLang.lang === me.langOnChange) {
                                taskTemplate.descriptionTaskTemplate = descriptionLang.description;
                            }
                        });
                    });
                }
            });
        }
    }

    searchTaskTemplate() {
        console.log('ConfigurationTaskComponent - searchTaskTemplate');

        const me = this;
        const objectSearchTaskTemplate = new ObjectSearchTaskTemplate(this.descriptionTaskTemplate,
            this.selectedCompanies, this.selectedTopics);

        this.taskTemplateService.searchTaskTemplate(objectSearchTaskTemplate).subscribe(
            (data) => {

                const dataUpdated: Task[] = data.map(x => Object.assign({}, x));
                me.descriptionTaskTemplateLangOnChange(dataUpdated)
                me.rows = [...dataUpdated];
                me.descriptionTopicTableOnChange();
                me.cdr.detectChanges();
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

        this.collectDataFilterAndTransfer();

        this.router.navigate(['/back-office/task-template/create']);
    }

    modifyTaskTemplate(group) {
        console.log('modifyTaskTemplate - modifyTaskTemplate');

        this.collectDataFilterAndTransfer();
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
        const taskOfficesArray = taskTemp.taskOffices;
        const newTaskTemp = new Task();
        newTaskTemp.taskOffices = taskOfficesArray;
        newTaskTemp.taskTemplate = taskTemp.taskTemplate;

        this.collectDataFilterAndTransfer();
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

        this.collectDataFilterAndTransfer();
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
            // this.getTaskTemplates();
            this.rows = [...this.rows];
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

    collectDataFilterAndTransfer() {

        this.dataFilter.description = this.descriptionTaskTemplate;
        this.dataFilter.companies = this.selectedCompanies;
        this.dataFilter.topics = this.selectedTopics;

        this.transferService.dataFilter = this.dataFilter;
    }

    descriptionTopicOnChange() {
        const me = this;

        if (me.tempTopicsArray && me.tempTopicsArray.length > 0) {

            me.tempTopicsArray.forEach(topic => {
                if (topic.translationList && topic.translationList.length > 1) {
                    topic.translationList.forEach(translation => {
                        if (translation.lang === me.langOnChange) {
                            topic.description = translation.description;
                        }
                    });
                }
            });

            me.topicsArray = [...me.tempTopicsArray];
        } else {
            me.topicsArray = [...[]];
        }
    }

    descriptionTopicTableOnChange() {
        const me = this;

        if (me.rows && me.rows.length > 0) {

            me.rows.forEach(task => {
                if (task.taskTemplate && task.taskTemplate.topic &&
                    task.taskTemplate.topic.translationList &&
                    task.taskTemplate.topic.translationList.length > 1) {

                    task.taskTemplate.topic.translationList.forEach(translation => {
                        if (translation.lang === me.langOnChange) {
                            task.taskTemplate.topic.description = translation.description;
                        }
                    });
                }
            });
        }
    }

    descriptionTopicSelectedOnChange() {
        const me = this;

        if (me.selectedTopics && me.selectedTopics.length > 0) {

            me.selectedTopics.forEach(topic => {
                if (topic.translationList && topic.translationList.length > 1) {
                    topic.translationList.forEach(translation => {
                        if (translation.lang === me.langOnChange) {
                            topic.description = translation.description;
                        }
                    });
                }
            });
            const selectedTopicsTemp = me.selectedTopics.map(x => Object.assign({}, x));
            me.selectedTopics = selectedTopicsTemp;
        }
    }



}
