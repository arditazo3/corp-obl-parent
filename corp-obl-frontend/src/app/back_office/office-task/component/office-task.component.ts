import {Component, OnInit, ViewChild} from '@angular/core';
import {Observable} from 'rxjs';
import {Router} from '@angular/router';
import {OfficeTaskService} from '../service/office-task.service';
import {OfficeService} from '../../office/service/office.service';
import {TaskTempOffices} from '../model/tasktemp-offices';
import {OfficeTaksCollapseComponent} from './office-taks-collapse/office-taks-collapse.component';
import {TaskTemplateService} from '../../tasktemplate/service/tasktemplate.service';
import {DataFilter} from '../../../shared/common/api/model/data-filter';
import {PageEnum} from '../../../shared/common/api/enum/page.enum';
import {TransferDataService} from '../../../shared/common/service/transfer-data.service';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import {TaskTemplate} from '../../tasktemplate/model/tasktemplate';

@Component({
    selector: 'app-office-task',
    templateUrl: './office-task.component.html',
    styleUrls: ['./office-task.component.css']
})
export class OfficeTaskComponent implements OnInit {

    @ViewChild(OfficeTaksCollapseComponent) officeTaksCollapse: OfficeTaksCollapseComponent;

    langOnChange = '';
    temp = [];

    descriptionTaskTemplate: string;
    offices = [];

    officesObservable: Observable<any[]>;

    taskTemplatesArray = [];
    dataFilter: DataFilter = new DataFilter(PageEnum.BO_TASK_OFFICE);

    constructor(
        private router: Router,
        private officeTaskService: OfficeTaskService,
        private officeService: OfficeService,
        private taskTemplateService: TaskTemplateService,
        private transferService: TransferDataService,
        private translateService: TranslateService
    ) {
        const me = this;

        me.langOnChange = me.translateService.currentLang;

        me.translateService.onLangChange
            .subscribe((event: LangChangeEvent) => {
                if (event.lang) {
                    me.langOnChange = event.lang;
                    me.descriptionTaskTemplateLangOnChange(me.taskTemplatesArray);
                    me.descriptionTaskTemplateLangOnChange(me.temp);
                }
            });
    }

    ngOnInit() {
        console.log('OfficeTaskComponent - ngOnInit');

        this.getOffices();

        const dataFilterTemp: DataFilter = this.transferService.dataFilter;
        if (dataFilterTemp && dataFilterTemp.page === PageEnum.BO_TASK_OFFICE) {
            this.dataFilter = dataFilterTemp;
            this.descriptionTaskTemplate = this.dataFilter.description;
            this.offices = this.dataFilter.offices;

            this.searchOfficeTasks();
            this.searchTaskTemplates();
            this.storeDataFilter();
        } else {
            this.searchOfficeTasks();
            this.searchTaskTemplates();
        }
    }

    getOffices() {
        console.log('OfficeTaskComponent - getOffices');

        const me = this;
        me.officesObservable = me.officeService.getOfficesByRole();
    }

    searchOfficeTasks() {
        console.log('OfficeTaskComponent - searchOfficeTasks');

        const me = this;
        const taskTempOffices = new TaskTempOffices();
        taskTempOffices.descriptionTaskTemplate = this.descriptionTaskTemplate;
        taskTempOffices.offices = this.offices;

        this.officeTaskService.searchOfficeTaskTemplates(taskTempOffices).subscribe(
            (data) => {
                me.temp = data;
                me.descriptionTaskTemplateLangOnChange(data);
                me.officeTaksCollapse.getOfficeTaskTemplatesArray(data);
                console.log('OfficeTaskComponent - searchOffice - next');
            }
        );
    }

    searchTaskTemplates() {
        console.log('OfficeTaskComponent - searchTaskTemplates');

        const me = this;

        this.taskTemplateService.searchTaskTemplateByDescr({result: this.descriptionTaskTemplate}).subscribe(
            (data) => {
                me.descriptionTaskTemplateLangOnChange(data);
                me.taskTemplatesArray = data;
            }
        );
    }

    descriptionTaskTemplateLangOnChange(data) {

        const me = this;

        if (data && data.length > 0) {
            data.forEach(object => {
                if (object.hasOwnProperty('idTaskTemplate')) {
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
                    me.officeTaksCollapse.getOfficeTaskTemplatesArray(data);
                }
            });
        }
    }

    changeTextDescription() {
        this.storeDataFilter();
    }

    onChangeCompanies() {
        this.storeDataFilter();
    }

    storeDataFilter() {

        this.dataFilter.description = this.descriptionTaskTemplate;
        this.dataFilter.offices = this.offices;

        this.transferService.dataFilter = this.dataFilter;
    }

}
