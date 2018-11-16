import {Component, OnInit} from '@angular/core';
import {ConsultantService} from '../../service/consultant.service';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {TopicService} from '../../../topic/service/topic.service';
import {TopicConsultant} from '../../../topic/model/topic-consultant';
import {CompanyTopic} from '../../../company/model/company_topic';
import {Company} from '../../../company/model/company';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import {TaskService} from '../../../task/service/task.service';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-topic-consultant',
    templateUrl: './topic-consultant.component.html',
    styleUrls: ['./topic-consultant.component.css']
})
export class TopicConsultantComponent implements OnInit {

    isMobile = false;
    langOnChange = '';
    temp = [];

    companyTopicsArray = [];
    consultantsObservable: Observable<any[]>;
    errorDetails: ApiErrorDetails;
    selectedCompany: Company;

    constructor(
        private router: Router,
        private consultantService: ConsultantService,
        private topicService: TopicService,
        private transferService: TransferDataService,
        private translateService: TranslateService,
        private deviceService: DeviceDetectorService
    ) {

        const me = this;

        me.langOnChange = me.translateService.currentLang;

        me.translateService.onLangChange
            .subscribe((event: LangChangeEvent) => {
                if (event.lang) {
                    me.langOnChange = event.lang;
                    me.descriptionOnChange();
                }
            });

        this.isMobile = this.deviceService.isMobile();
    }

    ngOnInit() {
        console.log('TopicConsultantComponent - ngOnInit');

        const me = this;
        me.getCompanyTopic(null);
    }

    getCompanyTopic(selectedCompany) {
        console.log('TopicConsultantComponent - getConsultants');

        if (!selectedCompany) {
            return;
        }
        this.selectedCompany = selectedCompany;

        const me = this;
        me.consultantService.getCompanyTopic(selectedCompany).subscribe(
            (data) => {
                me.temp = data;
                me.companyTopicsArray = me.descriptionOnChange();
            }
        );

        me.consultantsObservable = me.consultantService.getCompanyConsultant(selectedCompany);

        console.log('consultantsObservable ' + me.consultantsObservable);
    }

    onClearTopicConsultant(topicConsultant) {
        console.log('TopicConsultantComponent - onClearTopicConsultant');


        const me = this;
        const companyTopic: CompanyTopic = new CompanyTopic();
        companyTopic.company = topicConsultant.company;
        companyTopic.topic = topicConsultant.topic;

        this.topicService.deleteTopicConsultants(companyTopic).subscribe(
            (data) => {
                me.errorDetails = undefined;
                console.log('TopicConsultantComponent - onRemoveTopicConsultant - next');
            }, error => {
                me.errorDetails = error.error;
                console.error('TopicConsultantComponent - onRemoveTopicConsultant - error \n', error);
            }
        );
    }

    onAddTopicConsultant(companyTopic, consultant) {
        console.log('TopicConsultantComponent - onAddTopicConsultant');
        const me = this;

        const topicConsultant: TopicConsultant = new TopicConsultant();
        topicConsultant.consultant = consultant;
        topicConsultant.topic = companyTopic.topic;

        this.topicService.saveUpdateTopicConsultant(topicConsultant)
            .subscribe(
                (data) => {
                    me.errorDetails = undefined;
                    console.log('TopicConsultantComponent - onAddTopicConsultant - next');
           //         this.getCompanyTopic(this.selectedCompany);
           //         companyTopic.topic.topicConsultantList.push(consultant);
                }, error => {
                    me.errorDetails = error.error;
                    console.error('TopicConsultantComponent - onAddTopicConsultant - error \n', error);
                }
            );
    }

    onRemoveTopicConsultant(companyTopic, consultant) {
        console.log('TopicConsultantComponent - onRemoveTopicConsultant');

        const me = this;
        const topicConsultant: TopicConsultant = new TopicConsultant();
        topicConsultant.consultant = consultant.value;
        topicConsultant.topic = companyTopic.topic;

        this.topicService.deleteTopicConsultant(topicConsultant).subscribe(
            (data) => {
                me.errorDetails = undefined;
                console.log('TopicConsultantComponent - onRemoveTopicConsultant - next');
            }, error => {
                me.errorDetails = error.error;
                console.error('TopicConsultantComponent - onRemoveTopicConsultant - error \n', error);
            }
        );
    }

    descriptionOnChange(): any {
        const me = this;

        if (me.temp && me.temp.length > 0) {

            me.temp.forEach(companyTopic => {
                if (companyTopic.topic && companyTopic.topic.translationList && companyTopic.topic.translationList.length > 1) {
                    companyTopic.topic.translationList.forEach(translation => {
                        if (translation.lang === me.langOnChange) {
                            companyTopic.topic.description = translation.description;
                        }
                    });
                }
            });

            return me.temp;
        } else {
            return;
        }
    }
}
