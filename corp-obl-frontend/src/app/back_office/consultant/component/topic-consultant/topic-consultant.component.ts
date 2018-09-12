import {Component, OnInit} from '@angular/core';
import {ConsultantService} from '../../service/consultant.service';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Router} from '@angular/router';
import {Observable} from '../../../../../../node_modules/rxjs/Rx';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {TopicService} from '../../../topic/service/topic.service';
import {TopicConsultant} from '../../../topic/model/topic-consultant';
import {CompanyTopic} from '../../../company/model/company_topic';
import {Company} from '../../../company/model/company';
import {forEach} from '@angular/router/src/utils/collection';
import {Consultant} from '../../model/consultant';

@Component({
    selector: 'app-topic-consultant',
    templateUrl: './topic-consultant.component.html',
    styleUrls: ['./topic-consultant.component.css']
})
export class TopicConsultantComponent implements OnInit {

    companyTopicsArray = [];
    consultantsObservable: Observable<any[]>;
    errorDetails: ApiErrorDetails;
    selectedCompany: Company;

    constructor(
        private router: Router,
        private consultantService: ConsultantService,
        private topicService: TopicService,
        private transferService: TransferDataService
    ) {
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
                me.companyTopicsArray = data;
                console.log('companyTopicsArray ' + me.companyTopicsArray);
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
                console.log('TopicConsultantComponent - onRemoveTopicConsultant - error');
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
                    console.log('TopicConsultantComponent - onAddTopicConsultant - error');
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
                console.log('TopicConsultantComponent - onRemoveTopicConsultant - error');
            }
        );
    }

}
