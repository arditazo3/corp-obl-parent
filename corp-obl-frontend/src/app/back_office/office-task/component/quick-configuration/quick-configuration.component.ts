import {ChangeDetectionStrategy, Component, OnInit, ViewChild} from '@angular/core';
import {TaskTemplate} from '../../../tasktemplate/model/tasktemplate';
import {Task} from '../../../task/model/task';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Observable, Subject} from 'rxjs';
import {Topic} from '../../../topic/model/topic';
import {Translation} from '../../../../shared/common/translation/model/translation';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {AssociationOfficeComponent} from '../../../task/component/association-office/association-office.component';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {TopicService} from '../../../topic/service/topic.service';
import {TaskTemplateService} from '../../../tasktemplate/service/tasktemplate.service';
import {TaskService} from '../../../task/service/task.service';
import {UserInfoService} from '../../../../user/service/user-info.service';
import {TranslationService} from '../../../../shared/common/translation/translation.service';
import {UploadService} from '../../../../shared/common/service/upload.service';
import {FileItem, FileLikeObject, ParsedResponseHeaders} from 'ng2-file-upload';
import {TaskTemplateAttachment} from '../../../tasktemplateattachment/tasktemplateattachment';
import {saveAs as importedSaveAs} from 'file-saver';
import {Office} from '../../../office/model/office';
import {TaskOffice} from '../../../task/model/taskoffice';
import {TaskTemplateOffice} from '../../model/tasktemplate-office';
import {IMyOptions} from 'mydatepicker';
import {AppGlobals} from '../../../../shared/common/api/app-globals';
import {NgSelectComponent} from '@ng-select/ng-select';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'app-quick-configuration',
    templateUrl: './quick-configuration.component.html',
    styleUrls: ['./quick-configuration.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    providers: [UploadService]
})
export class QuickConfigurationComponent implements OnInit {

    isNewForm = true;
    isForeign = true;
    taskTemplate: TaskTemplate = new TaskTemplate();
    task: Task = new Task();
    office: Office = new Office();
    taskOffice: TaskOffice = new TaskOffice();
    submitted = false;
    counterUpload = 0;
    counterCallback = 0;
    errorDetails: ApiErrorDetails = new ApiErrorDetails();

    langOnChange = '';

    tempTopicsArray: Topic[];
    topicsArray: Topic[];
    periodicityObservable: Observable<any[]>;
    expirationTypeOnlyFixedDayObservable: Observable<any[]>;
    expirationTypeObservableMonthly: Observable<any[]>;
    periodWeeklyExpFixedDay: Observable<any[]>;
    expirationTypeObservable: Observable<any[]>;
    selectedTopic: Topic;
    selectedPeriodicity: Translation;
    selectedPeriodicityTypeChange: Subject<Translation> = new Subject<Translation>();
    selectedExpirationType: Translation;

    dayNumberTI: any;
    dayDateDP: any;
    selectedPeriodWeeklyExpFixedDay: Translation;

    dayValue: any;

    isFixedDay = false;
    isWeekly = false;
    isMonthly = false;
    isYearly = false;

    myDatePickerOptionsTaskTempl: IMyOptions = {
        // other options...
        dateFormat: AppGlobals.dateFormat,
        editableDateField: false
    };

    uploader;

    @ViewChild('selectExpiration') selectExpiration: NgSelectComponent;
    @ViewChild('cancelBtn') cancelBtn;
    @ViewChild('submitBtn') submitBtn;
    @ViewChild('confirmationTaskTemplateSwal') private confirmationTaskTemplateSwal: SwalComponent;
    @ViewChild('errorTaskTemplateSwal') private errorTaskTemplateSwal: SwalComponent;
    @ViewChild(AssociationOfficeComponent) associationOffice: AssociationOfficeComponent;
    createEditTaskTemplate: FormGroup;

    constructor(
        private router: Router,
        private transferService: TransferDataService,
        private formBuilder: FormBuilder,
        private topicService: TopicService,
        private taskTemplateService: TaskTemplateService,
        private taskService: TaskService,
        private userInfoService: UserInfoService,
        private translationService: TranslationService,
        private uploadService: UploadService,
        private translateService: TranslateService
    ) {
        const me = this;
        this.selectedPeriodicityTypeChange.subscribe(
            value => {
                me.swtichItemsExpirationSelect(value);
            }
        );

        me.langOnChange = me.translateService.currentLang;

        me.translateService.onLangChange
            .subscribe((event: LangChangeEvent) => {
                if (event.lang) {
                    me.langOnChange = event.lang;
                    me.descriptionTopicOnChange();
                    me.descriptionTopicSelectedOnChange();
                }
            });
    }

    ngOnInit() {
        console.log('QuickConfigurationComponent - ngOnInit');

        const me = this;
        const objectParam = this.transferService.objectParam;
        if (!objectParam) {
            this.router.navigate(['/back-office/office-task']);
        } else {
            this.isNewForm = objectParam.isNewForm;
            this.task = objectParam.task;
            this.office = objectParam.office;
            this.taskOffice = objectParam.taskOffice;

            if (objectParam.isNewForm) {
                this.task = new Task();

                if (this.office) {
                    this.taskOffice = new TaskOffice();
                    this.taskOffice.office = this.office;
                }

                this.submitBtn.nativeElement.innerText = 'Create Task Template';
            }
            this.associationOffice.getTaskOfficesArray([this.taskOffice]);
        }

        this.getTopics();
        this.periodicityObservable = this.getTranslationLikeTablename('tasktemplate#periodicity');
        this.expirationTypeOnlyFixedDayObservable = this.getTranslationLikeTablename('tasktemplate#expirationtype#fix_day');
        this.expirationTypeObservableMonthly = this.getTranslationLikeTablename('tasktemplate#expirationtype');
        this.expirationTypeObservable = this.getTranslationLikeTablename('tasktemplate#expirationtype');
        this.periodWeeklyExpFixedDay = this.getTranslationLikeTablename('tasktemplate#period_weekly_exp_fixed_day');

        if (!this.isNewForm) {
            this.submitBtn.nativeElement.innerText = 'Update Task Template';
            this.taskTemplate = this.task.taskTemplate;
            this.selectedTopic = this.taskTemplate.topic;

            this.periodicityObservable.subscribe(
                (data) => {
                    data.forEach((item) => {
                        if (item && item.tablename.indexOf(me.taskTemplate.recurrence) >= 0) {
                            me.selectedPeriodicityTypeChange.next(item);
                            me.selectedPeriodicity = item;
                            me.onChangePeriodExpiration(null);
                        }
                    });
                }
            );
            this.expirationTypeObservable.subscribe(
                (data) => {
                    data.forEach((item) => {
                        if (item && item.tablename.indexOf(me.taskTemplate.expirationType) >= 0) {
                            me.selectedExpirationType = item;
                            me.onChangePeriodExpiration(null);
                        }
                    });
                }
            );
        }

        this.createEditTaskTemplate = this.formBuilder.group({
            description: new FormControl({value: this.taskTemplate.description, disabled: false}, Validators.required),
            expirationRadio: new FormControl(this.taskTemplate.expirationClosableBy, Validators.required),
            daysOfNotice: new FormControl({value: this.taskTemplate.daysOfNotice, disabled: false}, Validators.required),
            frequenceOfNotice: new FormControl({value: this.taskTemplate.frequenceOfNotice, disabled: false}, Validators.required),
            daysBeforeShowExpiration: new FormControl({value: this.taskTemplate.daysBeforeShowExpiration, disabled: false},
                Validators.required)
        });

        this.dayValue = this.task.day;
        this.associationOffice.getTaskOfficesArray(this.task.taskOffices);

        this.uploader = this.uploadService.uploader;
        this.uploadService.uploadFileWithAuth();
        this.uploader.onBeforeUploadItem = (item) => {
            item.withCredentials = false;
        };
        this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);
        this.onLoadFilesUploaded();

        this.isForeign = this.userInfoService.isRoleForeign();
    }

    getTopics() {
        console.log('QuickConfigurationComponent - getTopics');

        const me = this;
        me.topicService.getTopicsByRole().subscribe(
            data => {
                me.tempTopicsArray = data;
                me.descriptionTopicOnChange();
            });
    }

    getTranslationLikeTablename(tablename): Observable<any[]> {
        console.log('QuickConfigurationComponent - getTranslationLikeTablename');
        return this.translationService.getTranslationsLikeTablename(tablename);
    }

    // convenience getter for easy access to form fields
    get f() {
        return this.createEditTaskTemplate.controls;
    }

    createEditTaskTemplateSubmit() {
        console.log('QuickConfigurationComponent - createEditTaskTemplateSubmit');

        const me = this;
        this.submitted = true;

        if (this.createEditTaskTemplate.invalid) {
            return;
        }

        if (this.selectedTopic === undefined ||
            this.selectedPeriodicity === undefined ||
            this.selectedExpirationType === undefined ||
            !this.hasValueDayComp()) {
            return;
        }

        this.taskTemplate.description = this.createEditTaskTemplate.get('description').value;
        this.taskTemplate.topic = this.selectedTopic;
        this.taskTemplate.recurrence = this.selectedPeriodicity.tablename.split('#')[2];
        this.taskTemplate.expirationType = this.selectedExpirationType.tablename.split('#')[2];
        this.taskTemplate.expirationClosableBy = this.createEditTaskTemplate.get('expirationRadio').value;
        this.taskTemplate.day = this.getDayValueOnDynamicComp();
        this.taskTemplate.daysOfNotice = this.createEditTaskTemplate.get('daysOfNotice').value;
        this.taskTemplate.daysBeforeShowExpiration = this.createEditTaskTemplate.get('daysBeforeShowExpiration').value;
        this.taskTemplate.frequenceOfNotice = this.createEditTaskTemplate.get('frequenceOfNotice').value;

        this.task.recurrence = this.taskTemplate.recurrence;
        this.task.expirationType = this.taskTemplate.expirationType;
        this.task.day = this.taskTemplate.day;
        this.task.daysOfNotice = this.taskTemplate.daysOfNotice;
        this.task.daysBeforeShowExpiration = this.taskTemplate.daysBeforeShowExpiration;
        this.task.frequenceOfNotice = this.taskTemplate.frequenceOfNotice;

        this.confirmationTaskTemplateSwal.title = 'Do you want to save: ' + this.taskTemplate.description + '?';
        this.confirmationTaskTemplateSwal.show()
            .then(function (result) {
                if (result.value === true) {

                    const taskTemplateOffice = new TaskTemplateOffice();
                    taskTemplateOffice.taskTemplate = me.taskTemplate;
                    taskTemplateOffice.office = me.office;

                    me.taskTemplateService.saveUpdateTaskTemplate(taskTemplateOffice).subscribe(
                        (data) => {
                            const taskTemplate: TaskTemplate = data;

                            if (taskTemplate.taskResults && taskTemplate.taskResults.length === 1) {
                                me.task.idTask = taskTemplate.taskResults[0].idTask;
                            }

                            me.errorDetails = undefined;
                            console.log('QuickConfigurationComponent - createEditTaskTemplateSubmit - next');

                            me.uploader.onBuildItemForm = (fileItem: any, form: any) => {
                                form.append('idTaskTemplate', taskTemplate.idTaskTemplate);
                            };

                            let noFileUpload = true;
                            me.uploader.queue.forEach((item) => {
                                if (!item.formData || item.formData.length === 0) {
                                    item.upload();
                                    noFileUpload = false;
                                    me.counterUpload++;
                                }
                            });
                            me.uploader.onErrorItem = (item, response, status, headers) =>
                                me.onErrorItem(item, response, status, headers);
                            me.uploader.onSuccessItem = (item, response, status, headers) =>
                                me.onSuccessItem(item, response, status, headers);

                            me.task.taskTemplate = taskTemplate;
                            me.task.taskOffices = me.associationOffice.taskOfficesArray;
                            me.task.office = me.office;
                            me.task.excludeOffice = true;

                            me.taskService.saveUpdateTask(me.task).subscribe(
                                dataTask => {
                                    console.log('QuickConfigurationComponent - createEditTaskSubmit - next');

                                    me.router.navigate(['/back-office/office-task']);
                                },
                                errorTask => {
                                    me.errorDetails = errorTask.error;
                                    //    me.showErrorDescriptionSwal();
                                    console.log('QuickConfigurationComponent - createEditTaskSubmit - error \n', errorTask);
                                }
                            );
                        }, error => {
                            me.errorDetails = error.error;
                            //    me.showErrorDescriptionSwal();
                            console.error('QuickConfigurationComponent - createEditTaskTemplateSubmit - error \n', error);
                        }
                    );
                }
            }, function (dismiss) {
                // dismiss can be "cancel" | "close" | "outside"
            });
    }

    onSuccessItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
        this.counterCallback++;
        if (this.counterUpload === this.counterCallback && !this.isNewForm) {
            this.router.navigate(['/back-office/office-task']);
        }
    }

    onErrorItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
        this.counterCallback++;
        if (this.counterUpload === this.counterCallback && !this.isNewForm) {
            this.router.navigate(['/back-office/office-task']);
        }
    }

    downloadFile(item) {
        console.log('QuickConfigurationComponent - downloadFile');

        const me = this;
        if (item.formData) {
            const taskTempAttach: TaskTemplateAttachment = item.formData;
            this.uploadService.downloadFile(item.formData).subscribe(
                (data) => {
                    importedSaveAs(data, taskTempAttach.fileName);
                    console.log('QuickConfigurationComponent - downloadFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.error('QuickConfigurationComponent - downloadFile - error \n', error);
                });
        } else {
            importedSaveAs(item.file.rawFile);
        }
    }

    removeFile(item) {
        console.log('QuickConfigurationComponent - removeFile');

        const me = this;
        if (item.formData && item.formData.length === undefined) {
            const taskTempAttach: TaskTemplateAttachment = item.formData;

            this.uploadService.removeFile(taskTempAttach).subscribe(
                data => {
                    console.log('QuickConfigurationComponent - removeFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.error('QuickConfigurationComponent - removeFile - error \n', error);
                }
            );
        }
        item.remove();
    }

    onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
        console.log('QuickConfigurationComponent - onWhenAddingFileFailed');

        switch (filter.name) {
            case 'fileSize':
                this.errorDetails.message = 'Maximum upload size exceeded (' + (item.size / 1024 / 1024).toFixed(2) +
                    ' MB of ' + (this.uploader.options.maxFileSize / 1024 / 1024).toFixed(2) + ' MB allowed)';
                break;
            case 'mimeType':
                const allowedTypes = this.uploader.options.allowedMimeType.join();
                this.errorDetails.message = `Type ${item.type} is not allowed. Allowed types: document, zip, image`;
                break;
            default:
                this.errorDetails.message = `Unknown error (filter is ${filter.name})`;
        }

        this.errorTaskTemplateSwal.title = this.errorDetails.message;
        this.errorTaskTemplateSwal.show();
    }

    onLoadFilesUploaded() {
        console.log('QuickConfigurationComponent - onLoadFilesUploaded');

        const me = this;
        if (this.taskTemplate.taskTemplateAttachmentResults && this.taskTemplate.taskTemplateAttachmentResults.length > 0) {
            this.taskTemplate.taskTemplateAttachmentResults.forEach((attachment) => {
                const file: File = new File(['#'.repeat(attachment.fileSize)], attachment.fileName);
                const fileItem: FileItem = new FileItem(me.uploader, file, null);
                fileItem.isUploaded = true;
                fileItem.formData = attachment;

                me.uploader.queue.push(fileItem);
            });
        }
    }

    swtichItemsExpirationSelect(fromComp) {

        const me = this;
        if (fromComp && fromComp.tablename) {

            if (fromComp.tablename.indexOf('weekly') >= 0) {
                this.expirationTypeOnlyFixedDayObservable.subscribe(
                    data => {
                        me.expirationTypeObservable = Observable.of(data);
                        me.selectedExpirationType = data[0];
                        me.isFixedDay = true;
                        me.isWeekly = true;
                    }
                );
            } else if (fromComp.tablename.indexOf('monthly') >= 0) {
                this.expirationTypeObservableMonthly.subscribe(
                    data => {
                        me.expirationTypeObservable = Observable.of(data);
                        if (this.isFixedDay) {
                            me.selectedExpirationType = data[0];
                            me.isFixedDay = true;
                        }
                        me.isMonthly = true;
                    }
                );
            } else if (fromComp.tablename.indexOf('yearly') >= 0) {
                this.expirationTypeOnlyFixedDayObservable.subscribe(
                    data => {
                        me.expirationTypeObservable = Observable.of(data);
                        me.selectedExpirationType = data[0];
                        me.isFixedDay = true;
                        me.isYearly = true;
                    }
                );
            }
        } else {
            me.expirationTypeObservable = this.getTranslationLikeTablename('tasktemplate#expirationtype');
        }
    }

    onChangePeriod(fromComp) {

        this.swtichItemsExpirationSelect(fromComp);

        this.onChangePeriodExpiration(fromComp);
    }

    onChangeExpiration(fromComp) {
        this.onChangePeriodExpiration(fromComp);
    }

    onChangePeriodExpiration(fromComp) {

        this.isFixedDay = false;
        this.isWeekly = false;
        this.isMonthly = false;
        this.isYearly = false;

        if (this.selectedPeriodicity && this.selectedExpirationType) {
            // dont reset value on modify
            if (!this.selectedExpirationType) {
                this.dayValue = undefined;
            }
            const periodicityType = this.selectedPeriodicity.tablename.split('#')[2];
            const expirationType = this.selectedExpirationType.tablename.split('#')[2];

            if (expirationType === 'fix_day') {
                this.isFixedDay = true;
                if (periodicityType === 'weekly') {
                    this.isWeekly = true;
                } else if (periodicityType === 'monthly') {
                    this.isMonthly = true;
                    if (this.dayValue < 1 || this.dayValue > 31) {
                        this.dayValue = '';
                    }
                } else if (periodicityType === 'yearly') {
                    this.isYearly = true;
                    this.myDatePickerOptionsTaskTempl = {dateFormat: 'dd/mm', editableDateField: false};
                }
            }
        }

        this.setDayValueOnDynamicComp(fromComp);
    }

    setDayValueOnDynamicComp(fromComp) {

        if (!this.dayValue) {
            return;
        }

        const day = this.dayValue;
        if (this.isFixedDay) {
            if (this.isWeekly) {
                this.dayNumberTI = '';
                this.dayDateDP = undefined;

                this.setDayValueOnWeekly(day);
            } else if (this.isMonthly) {
                this.selectedPeriodWeeklyExpFixedDay = undefined;
                this.dayDateDP = undefined;

                this.dayNumberTI = day;
            } else if (this.isYearly && day.toString().length === 8) {
                this.selectedPeriodWeeklyExpFixedDay = undefined;
                this.dayNumberTI = '';

                const dateDay = new Date();
                const dayString = day.toString();
                const yearMonthDayArray = [dayString.substring(0, 4), dayString.substring(4, 6), dayString.substring(6)];

                dateDay.setFullYear(yearMonthDayArray[0], yearMonthDayArray[1] - 1, yearMonthDayArray[2]);
                this.dayDateDP = AppGlobals.convertDateToDatePicker(dateDay);
            }
        } else if (fromComp) {
            this.dayNumberTI = '';
            this.selectedPeriodWeeklyExpFixedDay = undefined;
            this.dayDateDP = undefined;
            this.dayValue = undefined;
        }
    }

    setDayValueOnWeekly(day) {

        const me = this;
        this.periodWeeklyExpFixedDay.subscribe(
            (data) => {
                data.forEach((item) => {
                    if (item && item.tablename.indexOf(day) >= 0) {
                        me.selectedPeriodWeeklyExpFixedDay = item;
                    }
                });
            }
        );
    }

    getDayValueOnDynamicComp(): number {

        let dayValueTemp;

        if (this.isFixedDay) {
            if (this.isWeekly) {
                dayValueTemp = +this.selectedPeriodWeeklyExpFixedDay.tablename.split('#')[2];
            } else if (this.isYearly) {
                const getDateFromDatePicker = this.dayDateDP.date;

                const yearString = getDateFromDatePicker.year.toString();
                let monthString = getDateFromDatePicker.month.toString();
                let dayString = getDateFromDatePicker.day.toString();
                if (monthString && monthString.length === 1) {
                    monthString = '0' + monthString;
                }
                if (dayString && dayString.length === 1) {
                    dayString = '0' + dayString;
                }

                dayValueTemp = (yearString + monthString + dayString);
            } else if (this.isMonthly) {
                dayValueTemp = this.dayNumberTI;
            }
        }
        this.dayValue = dayValueTemp;
        return this.dayValue;
    }

    hasValueDayComp() {
        let hasValue = true;
        if (this.isFixedDay) {
            if (this.isWeekly && !this.selectedPeriodWeeklyExpFixedDay) {
                hasValue = false;
            } else if (this.isMonthly && !this.dayNumberTI) {
                hasValue = false;
            } else if (this.isYearly && !this.dayDateDP) {
                hasValue = false;
            }
        }
        return hasValue;
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

    descriptionTopicSelectedOnChange() {
        const me = this;

        if (me.selectedTopic) {

            if (me.selectedTopic.translationList && me.selectedTopic.translationList.length > 1) {
                me.selectedTopic.translationList.forEach(translation => {
                    if (translation.lang === me.langOnChange) {
                        me.selectedTopic.description = translation.description;
                    }
                });
            }
        }
    }

    deleteTaskOffice() {
        console.log('QuickConfigurationComponent - deleteTaskOffice');
        const me = this;

        if (this.taskTemplate) {
            let msgSwal = 'Do you want to delete: ';
            if (this.taskTemplate && this.taskTemplate.description) {
                msgSwal += this.taskTemplate.description;
            } else {
                msgSwal += this.task.taskTemplate.description;
            }
            msgSwal += '?';
            this.confirmationTaskTemplateSwal.title = msgSwal;
            this.confirmationTaskTemplateSwal.show()
                .then(function (result) {
                    if (result.value === true) {
                        // handle confirm, result is needed for modals with input

                        if (!me.taskOffice) {
                            me.taskOffice = new TaskOffice();
                            me.taskOffice.task = me.task;
                            me.taskOffice.task.excludeOffice = true;
                            me.taskOffice.taskTemplate = me.taskTemplate;
                        }

                        me.taskService.deleteTaskOffice(me.taskOffice).subscribe(
                            next => {
                                me.router.navigate(['/back-office/office-task']);
                                console.log('QuickConfigurationComponent - deleteTask - next');
                            },
                            error => {
                                console.error('QuickConfigurationComponent - deleteTask - error \n', error);
                            }
                        );
                    }
                }, function (dismiss) {
                    // dismiss can be "cancel" | "close" | "outside"
                });
        }
    }

}
