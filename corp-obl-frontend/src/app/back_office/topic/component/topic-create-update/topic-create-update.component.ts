import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {TopicService} from '../../../topic/service/topic.service';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {Router} from '@angular/router';
import {CompanyService} from '../../../company/service/company.service';
import {Observable} from 'rxjs';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Topic} from '../../../topic/model/topic';
import {Company} from '../../../company/model/company';
import {UserInfoService} from '../../../../user/service/user-info.service';
import {Translation} from '../../../../shared/common/translation/model/translation';

@Component({
    selector: 'app-topic-create-update',
    templateUrl: './topic-create-update.component.html',
    styleUrls: ['./topic-create-update.component.css']
})
export class TopicCreateUpdateComponent implements OnInit {

    isNewForm;
    topic: Topic = new Topic();
    submitted = false;
    hasDescription = false;
    languageNotAvailable = false;
    errorDetails: ApiErrorDetails;

    companiesObservable: Observable<any[]>;
    selectedCompanies: Company[];

    languagesObservable: Observable<any[]>;
    languagesNotAvailable;
    selectedLang: string;
    previousLang: string;
    translationList: Translation[] = [];

    @ViewChild('cancelBtn') cancelBtn;
    @ViewChild('submitBtn') submitBtn;
    @ViewChild('errorDescriptionSwal') private errorDescriptionSwal: SwalComponent;
    @ViewChild('confirmationTopicSwal') private confirmationTopicSwal: SwalComponent;
    createEditTopic: FormGroup;

    constructor(
        private router: Router,
        private transferService: TransferDataService,
        private formBuilder: FormBuilder,
        private topicService: TopicService,
        private companyService: CompanyService,
        private userInfoService: UserInfoService
    ) {
    }

    ngOnInit() {
        console.log('TopicCreateEditComponent - ngOnInit');

        this.topic = this.transferService.objectParam;

        if (this.topic === undefined) {
            this.isNewForm = true;
            this.topic = new Topic();
        } else {
            this.submitBtn.nativeElement.innerText = 'Update topic';
            this.selectedCompanies = this.topic.companyList;
            this.translationList = this.topic.translationList;
        }

        this.createEditTopic = this.formBuilder.group({
            description: new FormControl({value: this.topic.description, disabled: false})
        });

        this.languagesObservable = Observable.of(this.userInfoService.getLanguages());
        this.languagesNotAvailable = this.userInfoService.getLanguagesNotAvailable();
        this.selectedLang = this.userInfoService.getLanguages()[0];
        this.previousLang = this.selectedLang;
        this.getCompanies();
        this.setDefaultDescription();
    }

    getCompanies() {
        console.log('TopicCreateEditComponent - getCompanies');

        const me = this;
        me.companiesObservable = me.companyService.getCompanies();
    }

    // convenience getter for easy access to form fields
    get f() {
        return this.createEditTopic.controls;
    }

    createEditTopicSubmit() {
        console.log('TopicCreateEditComponent - createEditTopicSubmit');

        const me = this;
        this.submitted = true;

        this.topic.description = this.createEditTopic.get('description').value;
        this.onChangeSelectLang(this.selectedLang, this.selectedLang);
        this.topic.companyList = this.selectedCompanies;
        this.topic.translationList = this.translationList;

        if (this.createEditTopic.invalid || this.selectedCompanies === undefined || this.selectedCompanies.length === 0
            || !this.hasDescriptionMultiLang()) {
            return;
        }

        me.topicService.saveUpdateTopic(me.topic).subscribe(
            (data) => {
                me.errorDetails = undefined;
                console.log('TopicCreateEditComponent - createEditTopicSubmit - next');
                me.router.navigate(['/back-office/topic']);
            }, error => {
                me.errorDetails = error.error;
                me.showErrorDescriptionSwal();
                console.error('TopicCreateEditComponent - createEditTopicSubmit - error');
            }
        );
    }

    showErrorDescriptionSwal() {
        console.log('TopicCreateEditComponent - showErrorDescriptionSwal');

        if (this.errorDetails !== undefined) {
            this.errorDescriptionSwal.title = this.errorDetails.message;
            this.errorDescriptionSwal.show();
        }
    }

    onChangeSelectLang(previousValue, actualValue) {
        console.log('TopicCreateEditComponent - onChangeSelectLang');

        if (!previousValue) {
            return;
        }

        this.languagesNotAvailable.forEach((langNotAvail) => {
            if (actualValue === langNotAvail) {
                this.createEditTopic.get('description').disable();
                this.languageNotAvailable = true;
            } else {
                this.createEditTopic.get('description').enable();
                this.languageNotAvailable = false;
            }
        });

        let newTranslation = true;
        let resetDescriptionTF = true;
        const description = this.createEditTopic.get('description').value;
        if (this.translationList.length > 0) {

            this.translationList.forEach((translation) => {
                if (translation.lang === previousValue) {
                    translation.description = description;
                    newTranslation = false;
                } else if (translation.lang === actualValue) {
                    this.f.description.setValue(translation.description);
                    resetDescriptionTF = false;
                }
            });
        }

        if (newTranslation) {
            const createTranslation: Translation = new Translation();
            createTranslation.description = description;
            createTranslation.lang = previousValue;
            createTranslation.tablename = 'co_topic';
            if (!this.isNewForm) {
                createTranslation.entityId = this.topic.idTopic;
            }

            this.translationList.push(createTranslation);
        }

        if (resetDescriptionTF) {
            this.f.description.setValue('');
        }

        this.previousLang = actualValue;

        this.hasDescriptionMultiLang();
    }

    hasDescriptionMultiLang(): boolean {
        this.hasDescription = false;

        if (this.translationList.length > 0) {
            this.translationList.forEach((translation) => {
                if (translation && translation.description && translation.description.trim() !== '') {
                    this.hasDescription = true;
                }
            });
        }
        return this.hasDescription;
    }

    private setDefaultDescription() {
        if (this.translationList.length > 1) {
            this.translationList.forEach((translation) => {
                if (translation.lang === this.selectedLang) {
                    this.f.description.setValue(translation.description);
                    return;
                }
            });
        } else if (this.translationList.length === 1) {
            this.selectedLang = this.translationList[0].lang;
            this.previousLang = this.selectedLang;
        }
    }
}
