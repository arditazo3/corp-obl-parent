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
import {FileLikeObject, FileUploader} from 'ng2-file-upload';
import {AppConfig} from '../../../../shared/common/api/app-config';
import {ApiRequestService} from '../../../../shared/common/service/api-request.service';
import {saveAs as importedSaveAs} from 'file-saver';

@Component({
    selector: 'app-tasktemplate-create-update',
    templateUrl: './task-template-create-update.component.html',
    styleUrls: ['./task-template-create-update.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush
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
        private appConfig: AppConfig,
        private apiRequestServie: ApiRequestService
    ) {
    }

    ngOnInit() {
        console.log('TaskTemplateCreateUpdateComponent - ngOnInit');

        this.taskTemplate = this.transferService.objectParam;

        if (this.taskTemplate === undefined) {
            this.isNewForm = true;
            this.taskTemplate = new TaskTemplate();
        } else {
            this.submitBtn.nativeElement.innerText = 'Update Task Template';
        }

        this.createEditTaskTemplate = this.formBuilder.group({
            description: new FormControl({value: this.taskTemplate.description, disabled: false}, Validators.required),
            expirationclosableby: new FormControl({value: this.taskTemplate.expirationClosableBy, disabled: false}, Validators.required),
            day: new FormControl({value: this.taskTemplate.day, disabled: false}),
            daysOfNotice: new FormControl({value: this.taskTemplate.daysOfNotice, disabled: false}, Validators.required),
            daysBeforeShowExpiration: new FormControl({value: this.taskTemplate.daysBeforeShowExpiration, disabled: false}, Validators.required),
            expirationClosableBy: new FormControl({value: this.taskTemplate.expirationClosableBy, disabled: false}, Validators.required)
        });

        this.getTopics();
        this.periodicityObservable = this.getTranslationLikeTablename('tasktemplate#periodicity');
        this.expirationTypeObservable = this.getTranslationLikeTablename('tasktemplate#expirationtype');

        this.uploader = this.apiRequestServie.uploader;
        this.apiRequestServie.uploadFileWithAuth();
        this.uploader.onBeforeUploadItem = (item) => {
            item.withCredentials = false;
        };
        this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);

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
        this.taskTemplate.day = this.createEditTaskTemplate.get('day').value;
        this.taskTemplate.daysOfNotice = this.createEditTaskTemplate.get('daysOfNotice').value;
        this.taskTemplate.daysBeforeShowExpiration = this.createEditTaskTemplate.get('daysBeforeShowExpiration').value;
        this.taskTemplate.expirationClosableBy = this.createEditTaskTemplate.get('expirationClosableBy').value;


        this.confirmationTaskTemplateSwal.title = 'Do you want to save: ' + this.taskTemplate.description + '?';
        this.confirmationTaskTemplateSwal.show()
            .then(function (result) {
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
                            item.upload();
                        });

                  //      me.router.navigate(['/back-office/office']);
                    }, error => {
                        me.errorDetails = error.error;
                    //    me.showErrorDescriptionSwal();
                        console.log('TaskTemplateCreateUpdateComponent - createEditTaskTemplateSubmit - error');
                    }
                );
            }, function (dismiss) {
                // dismiss can be "cancel" | "close" | "outside"
            });
    }

    downloadFile(rawFile) {
        console.log('TaskTemplateCreateUpdateComponent - downloadFile');

        importedSaveAs(rawFile);
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

}