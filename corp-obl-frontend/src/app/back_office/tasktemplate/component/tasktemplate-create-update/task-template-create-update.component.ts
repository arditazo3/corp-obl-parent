import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {TaskTemplate} from '../../model/tasktemplate';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Router} from '@angular/router';
import {UserInfoService} from '../../../../user/service/user-info.service';
import {TaskTemplateService} from '../../service/tasktemplate.service';
import {TopicService} from '../../../topic/service/topic.service';
import {Observable} from '../../../../../../node_modules/rxjs/Rx';
import {Topic} from '../../../topic/model/topic';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {TranslationService} from '../../../../shared/common/translation/translation.service';
import {Translation} from '../../../../shared/common/translation/model/translation';
import {FileItem, FileLikeObject, FileUploader, ParsedResponseHeaders} from 'ng2-file-upload';
import {AppConfig} from '../../../../shared/common/api/app-config';
import {ApiRequestService} from '../../../../shared/common/service/api-request.service';
import {saveAs as importedSaveAs} from 'file-saver';
import {Task} from '../../../task/model/task';
import {UploadService} from '../../../../shared/common/service/upload.service';
import {TaskTemplateAttachment} from '../../../tasktemplateattachment/tasktemplateattachment';

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
    taskTemplate: TaskTemplate = new TaskTemplate();
    submitted = false;
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
    createEditTaskTemplate: FormGroup;


    constructor(
        private router: Router,
        private transferService: TransferDataService,
        private formBuilder: FormBuilder,
        private topicService: TopicService,
        private taskTemplateService: TaskTemplateService,
        private userInfoService: UserInfoService,
        private translationService: TranslationService,
        private uploadService: UploadService
    ) {
    }

    ngOnInit() {
        console.log('TaskTemplateCreateUpdateComponent - ngOnInit');

        const me = this;
        const task: Task = this.transferService.objectParam;

        this.getTopics();
        this.periodicityObservable = this.getTranslationLikeTablename('tasktemplate#periodicity');
        this.expirationTypeObservable = this.getTranslationLikeTablename('tasktemplate#expirationtype');

        if (task === undefined) {
            this.isNewForm = true;
            this.taskTemplate = new TaskTemplate();
        } else {
            this.submitBtn.nativeElement.innerText = 'Update Task Template';
            this.taskTemplate = task.taskTemplate;
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
                        }
                    });
                }
            );
        }

        this.createEditTaskTemplate = this.formBuilder.group({
            description: new FormControl({value: this.taskTemplate.description, disabled: false}, Validators.required),
            //    expirationClosableBy: new FormControl({value: this.taskTemplate.expirationClosableBy}, Validators.required),
            expirationRadio: new FormControl(this.taskTemplate.expirationClosableBy, Validators.required),
            day: new FormControl({value: this.taskTemplate.day, disabled: false}, Validators.required),
            daysOfNotice: new FormControl({value: this.taskTemplate.daysOfNotice, disabled: false}, Validators.required),
            frequenceOfNotice: new FormControl({value: this.taskTemplate.frequenceOfNotice, disabled: false}, Validators.required),
            daysBeforeShowExpiration: new FormControl({
                value: this.taskTemplate.daysBeforeShowExpiration,
                disabled: false
            }, Validators.required)
        });

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
        console.log('TaskTemplateCreateUpdateComponent - getTopics');

        const me = this;
        me.topicsObservable = me.topicService.getTopicsByRole();
    }

    getTranslationLikeTablename(tablename): Observable<any[]> {
        console.log('TaskTemplateCreateUpdateComponent - getTranslationLikeTablename');
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

        if (this.createEditTaskTemplate.invalid) {
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

        this.confirmationTaskTemplateSwal.title = 'Do you want to save: ' + this.taskTemplate.description + '?';
        this.confirmationTaskTemplateSwal.show()
            .then(function (result) {
                if (result.value === true) {
                    // handle confirm, result is needed for modals with input
                    me.taskTemplateService.saveUpdateTaskTemplate(me.taskTemplate).subscribe(
                        (data) => {
                            const taskTemplate: TaskTemplate = data;
                            me.errorDetails = undefined;
                            console.log('TaskTemplateCreateUpdateComponent - createEditTaskTemplateSubmit - next');

                            me.uploader.onBuildItemForm = (fileItem: any, form: any) => {
                                form.append('idTaskTemplate', taskTemplate.idTaskTemplate);
                            };

                            me.uploader.queue.forEach((item) => {
                                if (!item.formData || item.formData.length === 0) {
                                    item.upload();
                                }
                            });

                            me.uploader.onErrorItem = (item, response, status, headers) => me.onErrorItem(item, response, status, headers);
                            me.uploader.onSuccessItem = (item, response, status, headers) => me.onSuccessItem(item, response, status, headers);

                        }, error => {
                            me.errorDetails = error.error;
                            //    me.showErrorDescriptionSwal();
                            console.log('TaskTemplateCreateUpdateComponent - createEditTaskTemplateSubmit - error');
                        }
                    );
                }
            }, function (dismiss) {
                // dismiss can be "cancel" | "close" | "outside"
            });
    }

    onSuccessItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
        this.router.navigate(['/back-office/task']);
    }

    onErrorItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
        this.router.navigate(['/back-office/task']);
    }

    downloadFile(item) {
        console.log('TaskTemplateCreateUpdateComponent - downloadFile');

        const me = this;
        if (item.formData) {
            const taskTempAttach: TaskTemplateAttachment = item.formData;
            this.uploadService.downloadFile(item.formData).subscribe(
                (data) => {
                    importedSaveAs(data, taskTempAttach.fileName);
                    console.log('TaskTemplateCreateUpdateComponent - downloadFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.log('TaskTemplateCreateUpdateComponent - downloadFile - error');
                });
        } else {
            importedSaveAs(item.file.rawFile);
        }
    }

    removeFile(item) {
        console.log('TaskTemplateCreateUpdateComponent - removeFile');

        const me = this;
        if (item.formData && item.formData.length === undefined) {
            const taskTempAttach: TaskTemplateAttachment = item.formData;

            this.uploadService.removeFile(taskTempAttach).subscribe(
                data => {
                    console.log('TaskTemplateCreateUpdateComponent - removeFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.log('TaskTemplateCreateUpdateComponent - removeFile - error');
                }
            );
        }
         item.remove();
    }

    onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
        console.log('TaskTemplateCreateUpdateComponent - onWhenAddingFileFailed');

        switch (filter.name) {
            case 'fileSize':
                this.errorDetails.message = `Maximum upload size exceeded (${item.size} of ${this.uploader.options.maxFileSize} allowed)`;
                break;
            case 'mimeType':
                const allowedTypes = this.uploader.options.allowedMimeType.join();
                this.errorDetails.message = `Type "${item.type} is not allowed. Allowed types: "${allowedTypes}"`;
                break;
            default:
                this.errorDetails.message = `Unknown error (filter is ${filter.name})`;
        }
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
}
