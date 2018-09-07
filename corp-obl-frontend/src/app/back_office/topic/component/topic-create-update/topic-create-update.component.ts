import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {TopicService} from '../../../topic/service/topic.service';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {Router} from '@angular/router';
import {CompanyService} from '../../../company/service/company.service';
import {Observable} from '../../../../../../node_modules/rxjs/Rx';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Topic} from '../../../topic/model/topic';
import {Company} from '../../../company/model/company';
import {CompanyTopic} from '../../../company/model/company_topic';

@Component({
  selector: 'app-topic-create-update',
  templateUrl: './topic-create-update.component.html',
  styleUrls: ['./topic-create-update.component.css']
})
export class TopicCreateUpdateComponent implements OnInit {

    isNewForm;
    topic: Topic = new Topic();
    submitted = false;
    errorDetails: ApiErrorDetails;

    companiesObservable: Observable<any[]>;
    selectedCompanies: Company[];

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
        private companyService: CompanyService
    ) { }

    ngOnInit() {
        console.log('TopicCreateEditComponent - ngOnInit');

        this.topic = this.transferService.objectParam;

        if (this.topic === undefined) {
            this.isNewForm = true;
            this.topic = new Topic();
        } else {
            this.submitBtn.nativeElement.innerText = 'Update topic';
            this.selectedCompanies = this.getCompaniesFromTopic();
        }

        this.createEditTopic = this.formBuilder.group({
            description: new FormControl({value: this.topic.description, disabled: false}, Validators.required)
        });

        this.getCompanies();
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

        if (this.createEditTopic.invalid) {
            return;
        }

        this.topic.description = this.createEditTopic.get('description').value;
        const companyTopicArray = [];
        this.selectedCompanies.forEach((company) => {
            const companyTopic: CompanyTopic = new CompanyTopic();
            companyTopic.company = company;
            companyTopic.topic = this.topic;
            companyTopicArray.push(companyTopic);
        });

        this.topic.companyTopicList = companyTopicArray;

        me.topicService.saveUpdateTopic(me.topic).subscribe(
            (data) => {
                me.errorDetails = undefined;
                console.log('TopicCreateEditComponent - createEditTopicSubmit - next');
                me.router.navigate(['/back-topic/topic']);
            }, error => {
                me.errorDetails = error.error;
                me.showErrorDescriptionSwal();
                console.log('TopicCreateEditComponent - createEditTopicSubmit - error');
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

    private getCompaniesFromTopic(): any {
        const companiesArray = [];
        this.topic.companyTopicList.forEach( (companyTopic) => {
            companiesArray.push(companyTopic.company);
        });
        return companiesArray;
    }
}
