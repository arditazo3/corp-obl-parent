import {ChangeDetectionStrategy, Component, OnInit, ViewChild} from '@angular/core';
import {TaskTemplate} from '../../model/tasktemplate';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Router} from '@angular/router';
import {UserInfoService} from '../../../../user/service/user-info.service';
import {TaskTemplateService} from '../../service/tasktemplate.service';
import {TopicService} from '../../../topic/service/topic.service';
import {Observable, Subject} from 'rxjs';
import {Topic} from '../../../topic/model/topic';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {TranslationService} from '../../../../shared/common/translation/translation.service';
import {Translation} from '../../../../shared/common/translation/model/translation';
import {FileItem, FileLikeObject, ParsedResponseHeaders} from 'ng2-file-upload';
import {saveAs as importedSaveAs} from 'file-saver';
import {Task} from '../../../task/model/task';
import {UploadService} from '../../../../shared/common/service/upload.service';
import {TaskTemplateAttachment} from '../../../tasktemplateattachment/tasktemplateattachment';
import {TaskService} from '../../../task/service/task.service';
import {AssociationOfficeComponent} from '../../../task/component/association-office/association-office.component';
import {TaskTemplateOffice} from '../../../office-task/model/tasktemplate-office';
import {IMyOptions} from 'mydatepicker';
import {AppGlobals} from '../../../../shared/common/api/app-globals';
import {NgSelectComponent} from '@ng-select/ng-select';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import {DeviceDetectorService} from 'ngx-device-detector';
import {TaskOffice} from '../../../task/model/taskoffice';
import {ngxLoadingAnimationTypes, NgxLoadingComponent} from 'ngx-loading';

@Component({
    selector: 'app-tasktemplate-create-update',
    templateUrl: './task-template-create-update.component.html',
    styleUrls: ['./task-template-create-update.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    providers: [UploadService]
})
export class TaskTemplateCreateUpdateComponent implements OnInit {

    isNewForm;
    isForeign = true;
    isTaskTemplateForm = true;
    taskTemplate: TaskTemplate = new TaskTemplate();
    task: Task = new Task();
    submitted = false;
    counterUpload = 0;
    counterCallback = 0;
    errorDetails: ApiErrorDetails = new ApiErrorDetails();

    isMobile = false;
    langOnChange = '';
    filesArray = [];

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
    @ViewChild('deleteTaskTemplateSwal') private deleteTaskTemplateSwal: SwalComponent;
    @ViewChild('errorTaskTemplateSwal') private errorTaskTemplateSwal: SwalComponent;
    @ViewChild('errorEmptyProvidersBeneficiaries') private errorEmptyProvidersBeneficiaries: SwalComponent;
    @ViewChild(AssociationOfficeComponent) associationOffice: AssociationOfficeComponent;
    createEditTaskTemplate: FormGroup;

    @ViewChild('ngxLoading') ngxLoadingComponent: NgxLoadingComponent;
    public primaryColour = AppGlobals.PrimaryWhite;
    public secondaryColour = AppGlobals.SecondaryGrey;
    public ngxLoadingAnimationTypes = ngxLoadingAnimationTypes;
    public config = { animationType: ngxLoadingAnimationTypes.none, primaryColour: this.primaryColour, secondaryColour: this.secondaryColour, tertiaryColour: this.primaryColour, backdropBorderRadius: '3px' };

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
        private translateService: TranslateService,
        private deviceService: DeviceDetectorService
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

        this.isMobile = this.deviceService.isMobile();
    }

    ngOnInit() {
        console.log('TaskTemplateCreateUpdateComponent - ngOnInit');

        this.isForeign = this.userInfoService.isRoleForeign();

        const me = this;
        const objectParam = this.transferService.objectParam;

        // return to the list if the user refresh the browser
        if (!objectParam) {
            this.createEditTaskTemplate = this.formBuilder.group({
                description: new FormControl(),
                daysOfNotice: new FormControl(),
                frequenceOfNotice: new FormControl(),
                daysBeforeShowExpiration: new FormControl()
            });

            me.router.navigate(['/back-office/task']);
            return;
        }
        this.isTaskTemplateForm = objectParam.isTaskTemplateForm;
        this.task = objectParam.task;

        this.getTopics();
        this.periodicityObservable = this.getTranslationLikeTablename('tasktemplate#periodicity');
        this.expirationTypeOnlyFixedDayObservable = this.getTranslationLikeTablename('tasktemplate#expirationtype#fix_day');
        this.expirationTypeObservableMonthly = this.getTranslationLikeTablename('tasktemplate#expirationtype');
        this.expirationTypeObservable = this.getTranslationLikeTablename('tasktemplate#expirationtype');
        this.periodWeeklyExpFixedDay = this.getTranslationLikeTablename('tasktemplate#period_weekly_exp_fixed_day');

        if (this.isTaskTemplateForm && this.task === undefined) {
            this.isNewForm = true;
            this.taskTemplate = new TaskTemplate();
            this.submitBtn.nativeElement.innerText = 'Create Task Template';
        } else if (!this.isTaskTemplateForm && this.task && objectParam.newTask) {
            this.isNewForm = true;
            this.submitBtn.nativeElement.innerText = 'Create Task';
            this.taskTemplate = this.task.taskTemplate;

            this.periodicityObservable.subscribe(
                (data) => {
                    data.forEach((item) => {
                        if (item && item.tablename.indexOf(me.task.taskTemplate.recurrence) >= 0) {
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
                        if (item && item.tablename.indexOf(me.task.taskTemplate.expirationType) >= 0) {
                            me.selectedExpirationType = item;
                            me.onChangePeriodExpiration(null);
                        }
                    });
                }
            );
        } else if (!this.isTaskTemplateForm && !objectParam.newTask) {
            this.submitBtn.nativeElement.innerText = 'Update Task';

            this.periodicityObservable.subscribe(
                (data) => {
                    data.forEach((item) => {
                        if (item && item.tablename.indexOf(me.task.recurrence) >= 0) {
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
                        if (item && item.tablename.indexOf(me.task.expirationType) >= 0) {
                            me.selectedExpirationType = item;
                            me.onChangePeriodExpiration(null);
                        }
                    });
                }
            );
        } else if (this.isTaskTemplateForm && this.task && this.task.taskTemplate) {

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

        if (this.isTaskTemplateForm || (!this.isTaskTemplateForm && objectParam.newTask)) {
            this.createEditTaskTemplate = this.formBuilder.group({
                description: new FormControl({value: this.taskTemplate.description, disabled: false}, Validators.required),
                expirationRadio: new FormControl(this.taskTemplate.expirationClosableBy, Validators.required),
                daysOfNotice: new FormControl({value: this.taskTemplate.daysOfNotice, disabled: false}, Validators.required),
                frequenceOfNotice: new FormControl({value: this.taskTemplate.frequenceOfNotice, disabled: false}, Validators.required),
                daysBeforeShowExpiration: new FormControl({value: this.taskTemplate.daysBeforeShowExpiration, disabled: false},
                    Validators.required)
            });

            this.dayValue = this.taskTemplate.day;
        } else {
            this.createEditTaskTemplate = this.formBuilder.group({
                description: new FormControl({value: this.task.taskTemplate.description, disabled: true}),
                daysOfNotice: new FormControl({value: this.task.daysOfNotice, disabled: false}, Validators.required),
                frequenceOfNotice: new FormControl({value: this.task.frequenceOfNotice, disabled: false}, Validators.required),
                daysBeforeShowExpiration: new FormControl({value: this.task.daysBeforeShowExpiration, disabled: false}, Validators.required)
            });

            this.dayValue = this.task.day;
            this.associationOffice.getTaskOfficesArray(this.task.taskOffices);
        }

        this.uploader = this.uploadService.uploader;
        this.uploadService.uploadFileWithAuth();
        this.uploader.onBeforeUploadItem = (item) => {
            item.withCredentials = false;
        };
        this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);
        this.onLoadFilesUploaded();
    }

    getTopics() {
        console.log('TaskTemplateCreateUpdateComponent - getTopics');

        const me = this;
        me.topicService.getTopicsByRole().subscribe(
            data => {
                me.tempTopicsArray = data;
                me.descriptionTopicOnChange();
            });
    }

    getTranslationLikeTablename(tablename): Observable<any[]> {
        return this.translationService.getTranslationsLikeTablename(tablename);
    }

    // convenience getter for easy access to form fields
    get f() {
        return this.createEditTaskTemplate.controls;
    }

    createEditTaskTemplateSubmit() {
        console.log('TaskTemplateCreateUpdateComponent - createEditTaskTemplateSubmit');

        const me = this;
        this.submitted = true;

        if (this.isForeign && this.isTaskTemplateForm) {
            this.createEditTaskTemplate.get('expirationRadio').setValue('1');
        }

        if (this.createEditTaskTemplate.invalid) {
            return;
        }

        //     this.dayValue = this.dayDateDP;

        if (this.isTaskTemplateForm) {
            if ((this.selectedTopic === undefined ||
                this.selectedPeriodicity === undefined ||
                this.selectedExpirationType === undefined) ||
                !this.hasValueDayComp()) {
                return;
            }
            this.taskTemplate.description = this.createEditTaskTemplate.get('description').value;
            this.taskTemplate.isRapidConfiguration = false;
            this.taskTemplate.topic = this.selectedTopic;
            this.taskTemplate.recurrence = this.selectedPeriodicity.tablename.split('#')[2];
            this.taskTemplate.expirationType = this.selectedExpirationType.tablename.split('#')[2];
            this.taskTemplate.expirationClosableBy = this.createEditTaskTemplate.get('expirationRadio').value;
            this.taskTemplate.day = this.getDayValueOnDynamicComp();
            this.taskTemplate.daysOfNotice = this.createEditTaskTemplate.get('daysOfNotice').value;
            this.taskTemplate.daysBeforeShowExpiration = this.createEditTaskTemplate.get('daysBeforeShowExpiration').value;
            this.taskTemplate.frequenceOfNotice = this.createEditTaskTemplate.get('frequenceOfNotice').value;
        } else {
            if ((this.selectedPeriodicity === undefined ||
                this.selectedExpirationType === undefined) ||
                !this.hasValueDayComp() ||
                this.checkEmptyProvidersBeneficiaries()) {
                return;
            }
            this.task.recurrence = this.selectedPeriodicity.tablename.split('#')[2];
            this.task.expirationType = this.selectedExpirationType.tablename.split('#')[2];
            this.task.day = this.getDayValueOnDynamicComp();
            this.task.daysOfNotice = this.createEditTaskTemplate.get('daysOfNotice').value;
            this.task.daysBeforeShowExpiration = this.createEditTaskTemplate.get('daysBeforeShowExpiration').value;
            this.task.frequenceOfNotice = this.createEditTaskTemplate.get('frequenceOfNotice').value;
        }

        let msgSwal = '';
        this.translateService.get('GENERAL.DO_WANT_SAVE').subscribe(
            data => {
                msgSwal = data;
            });

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

                    me.ngxLoadingComponent.show = true;

                    if (me.isTaskTemplateForm) {

                        const taskTemplateOffice = new TaskTemplateOffice();
                        taskTemplateOffice.taskTemplate = me.taskTemplate;

                        me.taskTemplateService.saveUpdateTaskTemplate(taskTemplateOffice).subscribe(
                            (data) => {
                                const taskTemplate: TaskTemplate = data;
                                me.errorDetails = undefined;
                                console.log('TaskTemplateCreateUpdateComponent - createEditTaskTemplateSubmit - next');

                                me.removeFiles();

                                me.uploader.onBuildItemForm = (fileItem: any, form: any) => {
                                    form.append('idTaskTemplate', taskTemplate.idTaskTemplate);
                                };

                                if (me.uploader.queue.length === 0) {
                                    me.router.navigate(['/back-office/task']);
                                } else {
                                    let noFileUpload = true;
                                    me.uploader.queue.forEach((item) => {
                                        if (!item.formData || item.formData.length === 0) {
                                            item.upload();
                                            noFileUpload = false;
                                            me.counterUpload++;
                                        }
                                    });
                                    if (noFileUpload) {
                                        me.router.navigate(['/back-office/task']);
                                    }
                                    me.uploader.onErrorItem = (item, response, status, headers) =>
                                        me.onErrorItem(item, response, status, headers);
                                    me.uploader.onSuccessItem = (item, response, status, headers) =>
                                        me.onSuccessItem(item, response, status, headers);
                                }
                            }, error => {
                                me.errorDetails = error.error;
                                //    me.showErrorDescriptionSwal();
                                console.error('TaskTemplateCreateUpdateComponent - createEditTaskTemplateSubmit - error \n', error);
                            }
                        );
                    } else {

                        me.task.taskOffices = me.associationOffice.taskOfficesArray;
                        // Avoid infinite loop
                        me.task.taskOffices.forEach(taskOffice => {
                            taskOffice.office.company.offices = [];
                        });

                        me.taskService.saveUpdateTask(me.task).subscribe(
                            (data) => {
                                const taskTemplate: TaskTemplate = data;
                                me.errorDetails = undefined;
                                console.log('TaskTemplateCreateUpdateComponent - createEditTaskSubmit - next');

                                me.removeFiles();

                                me.router.navigate(['/back-office/task']);
                            }, error => {
                                me.errorDetails = error.error;
                                //    me.showErrorDescriptionSwal();
                                console.error('TaskTemplateCreateUpdateComponent - createEditTaskSubmit - error \n', error);
                            }
                        );
                    }
                }
            }, function (dismiss) {
                // dismiss can be "cancel" | "close" | "outside"
                me.ngxLoadingComponent.show = false;
            });
    }

    deleteTaskTemplate() {
        console.log('TaskTemplateCreateUpdateComponent - deleteTaskTemplate');
        const me = this;

        if (this.taskTemplate) {
            let msgSwal = '';
            this.translateService.get('GENERAL.DO_WANT_DELETE').subscribe(
                data => {
                    msgSwal = data;
                });
            this.deleteTaskTemplateSwal.title = msgSwal + this.taskTemplate.description + '?';
            this.deleteTaskTemplateSwal.show()
                .then(function (result) {
                    if (result.value === true) {
                        // handle confirm, result is needed for modals with input
                        me.taskTemplateService.deleteTaskTemplate(me.taskTemplate).subscribe(
                            next => {
                                me.router.navigate(['/back-office/task']);
                                console.log('TaskTemplateCreateUpdateComponent - deleteTaskTemplate - next');
                            },
                            error => {
                                console.error('TaskTemplateCreateUpdateComponent - deleteTaskTemplate - error \n', error);
                            }
                        );
                    }
                }, function (dismiss) {
                    // dismiss can be "cancel" | "close" | "outside"
                });
        }
    }

    deleteTask() {
        console.log('TaskTemplateCreateUpdateComponent - deleteTask');
        const me = this;

        if (this.taskTemplate) {
            let msgSwal = '';
            this.translateService.get('GENERAL.DO_WANT_DELETE').subscribe(
                data => {
                    msgSwal = data;
                });
            if (this.taskTemplate && this.taskTemplate.description) {
                msgSwal += this.taskTemplate.description;
            } else {
                msgSwal += this.task.taskTemplate.description;
            }
            msgSwal += '?';
            this.deleteTaskTemplateSwal.title = msgSwal;
            this.deleteTaskTemplateSwal.show()
                .then(function (result) {
                    if (result.value === true) {
                        // handle confirm, result is needed for modals with input

                        me.taskService.deleteTask(me.task).subscribe(
                            next => {
                                me.router.navigate(['/back-office/task']);
                                console.log('TaskTemplateCreateUpdateComponent - deleteTask - next');
                            },
                            error => {
                                console.error('TaskTemplateCreateUpdateComponent - deleteTask - error \n', error);
                            }
                        );
                    }
                }, function (dismiss) {
                    // dismiss can be "cancel" | "close" | "outside"
                });
        }
    }

    onSuccessItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
        this.counterCallback++;
        if (this.counterUpload === this.counterCallback) {
            this.router.navigate(['/back-office/task']);
        }
    }

    onErrorItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
        this.counterCallback++;
        if (this.counterUpload === this.counterCallback) {
            this.router.navigate(['/back-office/task']);
        }
    }

    downloadFile(item) {
        console.log('TaskTemplateCreateUpdateComponent - downloadFile');

        const me = this;
        if (item.formData && item.formData.fileName) {
            const taskTempAttach: TaskTemplateAttachment = item.formData;
            this.uploadService.downloadFile(item.formData).subscribe(
                (data) => {
                    importedSaveAs(data, taskTempAttach.fileName);
                    console.log('TaskTemplateCreateUpdateComponent - downloadFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.error('TaskTemplateCreateUpdateComponent - downloadFile - error \n', error);
                });
        } else {
            importedSaveAs(item.file.rawFile);
        }
    }

    removeFileToRemove(item) {
        console.log('TaskTemplateCreateUpdateComponent - removeFileToRemove');

        this.filesArray.push(item);
        item.remove();
    }

    removeFiles() {
        console.log('TaskTemplateCreateUpdateComponent - removeFile');

        const me = this;

        if (this.filesArray && this.filesArray.length > 0) {
            this.filesArray.forEach(item => {
                if (item.formData && item.formData.length === undefined) {
                    const taskTempAttach: TaskTemplateAttachment = item.formData;

                    this.uploadService.removeFile(taskTempAttach).subscribe(
                        data => {
                            console.log('TaskTemplateCreateUpdateComponent - removeFile - next');
                        },
                        error => {
                            me.errorDetails = error.error;
                            console.error('TaskTemplateCreateUpdateComponent - removeFile - error \n', error);
                        }
                    );
                }
            });
        }
    }

    onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
        console.log('TaskTemplateCreateUpdateComponent - onWhenAddingFileFailed');

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
        console.log('TaskTemplateCreateUpdateComponent - onLoadFilesUploaded');

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

    checkAssociationOffice($event) {
        if ($event && this.task && this.task.taskOffices) {
            this.task.taskOffices = this.associationOffice.taskOfficesArray;
        }
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

    checkEmptyProvidersBeneficiaries(): boolean {
        let hasEmptyProvidersBeneficiaries = false;
        let emptyEmptyProvidersBeneficiariesMsg = '';

        if (this.associationOffice && this.associationOffice.taskOfficesArray) {

            let providerSectionMsg = '';
            let beneficiarySectionMsg = '';

            this.translateService.get('GENERAL.PROVIDER_SECTION').subscribe(
                data => {
                    providerSectionMsg = data;
                });
            this.translateService.get('GENERAL.BENEFICIARY_SECTION').subscribe(
                data => {
                    beneficiarySectionMsg = data;
                });

            const taskOfficesArray = this.associationOffice.taskOfficesArray;

            taskOfficesArray.forEach(taskOffice => {
                if (taskOffice.office) {

                    let hasEmptyProviders = true;
                    let hasEmptyBeneficiaries = true;

                    const office = taskOffice.office;
                    if (office.userProviders && office.userProviders.length > 0) {
                        hasEmptyProviders = false;
                    }
                    if (office.userBeneficiaries && office.userBeneficiaries.length > 0) {
                        hasEmptyBeneficiaries = false;
                    }
                    if (hasEmptyProviders || hasEmptyBeneficiaries) {
                        hasEmptyProvidersBeneficiaries = true;

                        if (hasEmptyProviders) {
                            emptyEmptyProvidersBeneficiariesMsg += taskOffice.office.description;
                            emptyEmptyProvidersBeneficiariesMsg += ' ';
                            emptyEmptyProvidersBeneficiariesMsg += providerSectionMsg;
                            if (hasEmptyBeneficiaries) {
                                emptyEmptyProvidersBeneficiariesMsg += ' <br>';
                            }
                        }
                        if (hasEmptyBeneficiaries) {
                            emptyEmptyProvidersBeneficiariesMsg += taskOffice.office.description;
                            emptyEmptyProvidersBeneficiariesMsg += ' ';
                            emptyEmptyProvidersBeneficiariesMsg += beneficiarySectionMsg;
                        }
                    }
                    emptyEmptyProvidersBeneficiariesMsg += ' <br>';
                } else {
                    emptyEmptyProvidersBeneficiariesMsg += taskOffice.office.description;
                    emptyEmptyProvidersBeneficiariesMsg += ' ';
                    emptyEmptyProvidersBeneficiariesMsg += providerSectionMsg;
                    emptyEmptyProvidersBeneficiariesMsg += ' <br>';

                    emptyEmptyProvidersBeneficiariesMsg += taskOffice.office.description;
                    emptyEmptyProvidersBeneficiariesMsg += ' ';
                    emptyEmptyProvidersBeneficiariesMsg += beneficiarySectionMsg;
                    emptyEmptyProvidersBeneficiariesMsg += ' <br> ';
                }
            });

            let pleaseSelectLeastOneUser = '';
            this.translateService.get('GENERAL.PLEASE_SELECT_LEAST_ONE_USER').subscribe(
                data => {
                    pleaseSelectLeastOneUser = data;
                });

            this.errorEmptyProvidersBeneficiaries.title = pleaseSelectLeastOneUser;
            this.errorEmptyProvidersBeneficiaries.html = '<div class="text-left margin-left-55">' + emptyEmptyProvidersBeneficiariesMsg + '</div>';

            this.errorEmptyProvidersBeneficiaries.show();

        } else {
            hasEmptyProvidersBeneficiaries = false;
        }


        return hasEmptyProvidersBeneficiaries;
    }
}
