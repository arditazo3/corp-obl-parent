import {ChangeDetectionStrategy, Component, OnInit, ViewChild} from '@angular/core';
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
import {FileUploader} from 'ng2-file-upload';
import {AppConfig} from '../../../../shared/common/api/app-config';
import {ApiRequestService} from '../../../../shared/common/service/api-request.service';

@Component({
    selector: 'app-tasktemplate-create-update',
    templateUrl: './task-template-create-update.component.html',
    styleUrls: ['./task-template-create-update.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskTemplateCreateUpdateComponent implements OnInit {

    isNewForm;
    taskTemplate: TaskTemplate = new TaskTemplate();
    submitted = false;
    errorDetails: ApiErrorDetails;

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

        this.uploader.onBuildItemForm = (fileItem: any, form: any) => {
            form.append('id', '1');
        };
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
    }

}
