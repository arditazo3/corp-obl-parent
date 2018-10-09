import {ChangeDetectionStrategy, Component, OnInit, ViewChild} from '@angular/core';
import {TaskTemplate} from '../../../tasktemplate/model/tasktemplate';
import {Task} from '../../../task/model/task';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Observable} from 'rxjs';
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
import {TaskTemplateOffice} from '../../model/tasktemplate-office';
import {FileItem, FileLikeObject, ParsedResponseHeaders} from 'ng2-file-upload';
import {TaskTemplateAttachment} from '../../../tasktemplateattachment/tasktemplateattachment';
import {saveAs as importedSaveAs} from 'file-saver';

@Component({
  selector: 'app-single-task-create-update',
  templateUrl: './single-task-create-update.component.html',
  styleUrls: ['./single-task-create-update.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush,
    providers: [UploadService]
})
export class SingleTaskCreateUpdateComponent implements OnInit {

    isNewForm;
    isForeign = true;
    isTaskTemplateForm = true;
    taskTemplate: TaskTemplate = new TaskTemplate();
    task: Task = new Task();
    submitted = false;
    counterUpload = 0;
    counterCallback = 0;
    errorDetails: ApiErrorDetails = new ApiErrorDetails();

    topicsObservable: Observable<any[]>;
    periodicityObservable: Observable<any[]>;
    expirationTypeObservable: Observable<any[]>;
    selectedTopic: Topic;
    selectedPeriodicity: Translation;
    selectedExpirationType: Translation;

    uploader;

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
        private uploadService: UploadService
    ) {
    }

    ngOnInit() {
        console.log('SingleTaskCreateUpdateComponent - ngOnInit');

        const me = this;
        let objectParam = this.transferService.objectParam;
        if (!objectParam) {
            const taskTemplateTemp = new TaskTemplate();
            const taskTemp = new Task();
            taskTemp.taskTemplate = taskTemplateTemp;
            objectParam = {isTaskTemplateForm: true, task: taskTemp};
        }
        this.isTaskTemplateForm = objectParam.isTaskTemplateForm;
        this.task = objectParam.task;

        this.getTopics();
        this.periodicityObservable = this.getTranslationLikeTablename('tasktemplate#periodicity');
        this.expirationTypeObservable = this.getTranslationLikeTablename('tasktemplate#expirationtype');

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
                            me.selectedPeriodicity = item;
                        }
                    });
                }
            );
            this.expirationTypeObservable.subscribe(
                (data) => {
                    data.forEach((item) => {
                        if (item && item.tablename.indexOf(me.task.taskTemplate.expirationType) >= 0) {
                            me.selectedExpirationType = item;
                            me.onChangeExpiration();
                        }
                    });
                }
            );
        } else if (this.isTaskTemplateForm) {
            this.submitBtn.nativeElement.innerText = 'Update Task Template';
            this.taskTemplate = this.task.taskTemplate;
            this.selectedTopic = this.taskTemplate.topic;

            this.periodicityObservable.subscribe(
                (data) => {
                    data.forEach((item) => {
                        if (item && item.tablename.indexOf(me.taskTemplate.recurrence) >= 0) {
                            me.selectedPeriodicity = item;
                        }
                    });
                }
            );
            this.expirationTypeObservable.subscribe(
                (data) => {
                    data.forEach((item) => {
                        if (item && item.tablename.indexOf(me.taskTemplate.expirationType) >= 0) {
                            me.selectedExpirationType = item;
                            me.onChangeExpiration();
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
                            me.selectedPeriodicity = item;
                        }
                    });
                }
            );
            this.expirationTypeObservable.subscribe(
                (data) => {
                    data.forEach((item) => {
                        if (item && item.tablename.indexOf(me.task.expirationType) >= 0) {
                            me.selectedExpirationType = item;
                            me.onChangeExpiration();
                        }
                    });
                }
            );
        }

        if (this.isTaskTemplateForm || (!this.isTaskTemplateForm && objectParam.newTask)) {
            this.createEditTaskTemplate = this.formBuilder.group({
                description: new FormControl({value: this.taskTemplate.description, disabled: false}, Validators.required),
                expirationRadio: new FormControl(this.taskTemplate.expirationClosableBy, Validators.required),
                day: new FormControl({value: this.taskTemplate.day, disabled: false}, Validators.required),
                daysOfNotice: new FormControl({value: this.taskTemplate.daysOfNotice, disabled: false}, Validators.required),
                frequenceOfNotice: new FormControl({value: this.taskTemplate.frequenceOfNotice, disabled: false}, Validators.required),
                daysBeforeShowExpiration: new FormControl({value: this.taskTemplate.daysBeforeShowExpiration, disabled: false},
                    Validators.required)
            });
        } else {
            this.createEditTaskTemplate = this.formBuilder.group({
                description: new FormControl({value: this.task.taskTemplate.description, disabled: true}),
                day: new FormControl({value: this.task.day, disabled: false}, Validators.required),
                daysOfNotice: new FormControl({value: this.task.daysOfNotice, disabled: false}, Validators.required),
                frequenceOfNotice: new FormControl({value: this.task.frequenceOfNotice, disabled: false}, Validators.required),
                daysBeforeShowExpiration: new FormControl({value: this.task.daysBeforeShowExpiration, disabled: false}, Validators.required)
            });
        }

        me.onChangeExpiration();

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
        console.log('SingleTaskCreateUpdateComponent - getTopics');

        const me = this;
        me.topicsObservable = me.topicService.getTopicsByRole();
    }

    getTranslationLikeTablename(tablename): Observable<any[]> {
        console.log('SingleTaskCreateUpdateComponent - getTranslationLikeTablename');
        return this.translationService.getTranslationsLikeTablename(tablename);
    }

    // convenience getter for easy access to form fields
    get f() {
        return this.createEditTaskTemplate.controls;
    }

    createEditTaskTemplateSubmit() {
        console.log('SingleTaskCreateUpdateComponent - createEditTaskTemplateSubmit');

        const me = this;
        this.submitted = true;

        if (this.createEditTaskTemplate.invalid) {
            return;
        }

        if (this.isTaskTemplateForm) {
            if (this.selectedTopic === undefined || this.selectedPeriodicity === undefined || this.selectedExpirationType === undefined) {
                return;
            }
            this.taskTemplate.description = this.createEditTaskTemplate.get('description').value;
            this.taskTemplate.topic = this.selectedTopic;
            this.taskTemplate.recurrence = this.selectedPeriodicity.tablename.split('#')[2];
            this.taskTemplate.expirationType = this.selectedExpirationType.tablename.split('#')[2];
            this.taskTemplate.expirationClosableBy = this.createEditTaskTemplate.get('expirationRadio').value;
            this.taskTemplate.day = this.createEditTaskTemplate.get('day').value;
            this.taskTemplate.daysOfNotice = this.createEditTaskTemplate.get('daysOfNotice').value;
            this.taskTemplate.daysBeforeShowExpiration = this.createEditTaskTemplate.get('daysBeforeShowExpiration').value;
            this.taskTemplate.frequenceOfNotice = this.createEditTaskTemplate.get('frequenceOfNotice').value;
        } else {
            if (this.selectedPeriodicity === undefined || this.selectedExpirationType === undefined) {
                return;
            }
            this.task.recurrence = this.selectedPeriodicity.tablename.split('#')[2];
            this.task.expirationType = this.selectedExpirationType.tablename.split('#')[2];
            this.task.day = this.createEditTaskTemplate.get('day').value;
            this.task.daysOfNotice = this.createEditTaskTemplate.get('daysOfNotice').value;
            this.task.daysBeforeShowExpiration = this.createEditTaskTemplate.get('daysBeforeShowExpiration').value;
            this.task.frequenceOfNotice = this.createEditTaskTemplate.get('frequenceOfNotice').value;
        }

        this.confirmationTaskTemplateSwal.title = 'Do you want to save: ' + this.task.taskTemplate.description + '?';
        this.confirmationTaskTemplateSwal.show()
            .then(function (result) {
                if (result.value === true) {
                    // handle confirm, result is needed for modals with input

                    if (me.isTaskTemplateForm) {

                        const taskTemplateOffice = new TaskTemplateOffice();
                        taskTemplateOffice.taskTemplate = me.taskTemplate;

                        me.taskTemplateService.saveUpdateTaskTemplate(taskTemplateOffice).subscribe(
                            (data) => {
                                const taskTemplate: TaskTemplate = data;
                                me.errorDetails = undefined;
                                console.log('SingleTaskCreateUpdateComponent - createEditTaskTemplateSubmit - next');

                                me.uploader.onBuildItemForm = (fileItem: any, form: any) => {
                                    form.append('idTaskTemplate', taskTemplate.idTaskTemplate);
                                };

                                if (me.uploader.queue.length === 0) {
                                    me.router.navigate(['/back-office/office-task']);
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
                                        me.router.navigate(['/back-office/office-task']);
                                    }
                                    me.uploader.onErrorItem = (item, response, status, headers) =>
                                        me.onErrorItem(item, response, status, headers);
                                    me.uploader.onSuccessItem = (item, response, status, headers) =>
                                        me.onSuccessItem(item, response, status, headers);
                                }
                            }, error => {
                                me.errorDetails = error.error;
                                //    me.showErrorDescriptionSwal();
                                console.error('SingleTaskCreateUpdateComponent - createEditTaskTemplateSubmit - error');
                            }
                        );
                    } else {
                        me.task.excludeOffice = true;

                        me.taskService.saveUpdateTask(me.task).subscribe(
                            (data) => {
                                const taskTemplate: TaskTemplate = data;
                                me.errorDetails = undefined;
                                console.log('SingleTaskCreateUpdateComponent - createEditTaskSubmit - next');

                                me.router.navigate(['/back-office/office-task']);
                            }, error => {
                                me.errorDetails = error.error;
                                //    me.showErrorDescriptionSwal();
                                console.error('SingleTaskCreateUpdateComponent - createEditTaskSubmit - error');
                            }
                        );
                    }
                }
            }, function (dismiss) {
                // dismiss can be "cancel" | "close" | "outside"
            });
    }

    deleteTaskTemplate() {
        console.log('SingleTaskCreateUpdateComponent - deleteTaskTemplate');
        const me = this;

        if (this.taskTemplate) {
            this.confirmationTaskTemplateSwal.title = 'Do you want to delete: ' + this.taskTemplate.description + '?';
            this.confirmationTaskTemplateSwal.show()
                .then(function (result) {
                    if (result.value === true) {
                        // handle confirm, result is needed for modals with input
                        me.taskTemplateService.deleteTaskTemplate(me.taskTemplate).subscribe(
                            next => {
                                me.router.navigate(['/back-office/office-task']);
                                console.log('SingleTaskCreateUpdateComponent - deleteTaskTemplate - next');
                            },
                            error => {
                                console.error('SingleTaskCreateUpdateComponent - deleteTaskTemplate - error');
                            }
                        );
                    }
                }, function (dismiss) {
                    // dismiss can be "cancel" | "close" | "outside"
                });
        }
    }

    deleteTask() {
        console.log('SingleTaskCreateUpdateComponent - deleteTask');
        const me = this;

        if (this.taskTemplate) {
            this.confirmationTaskTemplateSwal.title = 'Do you want to delete: ' + this.taskTemplate.description + '?';
            this.confirmationTaskTemplateSwal.show()
                .then(function (result) {
                    if (result.value === true) {
                        // handle confirm, result is needed for modals with input
                        me.taskService.deleteTask(me.task).subscribe(
                            next => {
                                me.router.navigate(['/back-office/office-task']);
                                console.log('SingleTaskCreateUpdateComponent - deleteTask - next');
                            },
                            error => {
                                console.error('SingleTaskCreateUpdateComponent - deleteTask - error');
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
            this.router.navigate(['/back-office/office-task']);
        }
    }

    onErrorItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
        this.counterCallback++;
        if (this.counterUpload === this.counterCallback) {
            this.router.navigate(['/back-office/office-task']);
        }
    }

    downloadFile(item) {
        console.log('SingleTaskCreateUpdateComponent - downloadFile');

        const me = this;
        if (item.formData) {
            const taskTempAttach: TaskTemplateAttachment = item.formData;
            this.uploadService.downloadFile(item.formData).subscribe(
                (data) => {
                    importedSaveAs(data, taskTempAttach.fileName);
                    console.log('SingleTaskCreateUpdateComponent - downloadFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.error('SingleTaskCreateUpdateComponent - downloadFile - error');
                });
        } else {
            importedSaveAs(item.file.rawFile);
        }
    }

    removeFile(item) {
        console.log('SingleTaskCreateUpdateComponent - removeFile');

        const me = this;
        if (item.formData && item.formData.length === undefined) {
            const taskTempAttach: TaskTemplateAttachment = item.formData;

            this.uploadService.removeFile(taskTempAttach).subscribe(
                data => {
                    console.log('SingleTaskCreateUpdateComponent - removeFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.error('SingleTaskCreateUpdateComponent - removeFile - error');
                }
            );
        }
        item.remove();
    }

    onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
        console.log('SingleTaskCreateUpdateComponent - onWhenAddingFileFailed');

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
        console.log('SingleTaskCreateUpdateComponent - onLoadFilesUploaded');

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

    onChangeExpiration() {

        if (!this.selectedExpirationType) {
            this.createEditTaskTemplate.get('day').disable();
        } else {
            const expirationType = this.selectedExpirationType.tablename.split('#')[2];
            if (expirationType === 'fix_day') {
                this.createEditTaskTemplate.get('day').disable();
                this.createEditTaskTemplate.get('day').setValue(0);
            } else {
                this.createEditTaskTemplate.get('day').enable();
            }
        }
    }

}
