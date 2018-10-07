import {ChangeDetectionStrategy, Component, OnInit, ViewChild} from '@angular/core';
import {Observable} from 'rxjs';
import {Company} from '../../company/model/company';
import {TransferDataService} from '../../../shared/common/service/transfer-data.service';
import {TopicService} from '../../topic/service/topic.service';
import {CompanyService} from '../../company/service/company.service';
import {UserInfoService} from '../../../user/service/user-info.service';
import {Router} from '@angular/router';
import {ConsultantTableComponent} from './consultant-table/consultant-table.component';
import {TopicConsultantComponent} from './topic-consultant/topic-consultant.component';
import {SwalComponent} from '@toverux/ngx-sweetalert2';

@Component({
    selector: 'app-consultant',
    templateUrl: './consultant.component.html',
    styleUrls: ['./consultant.component.css']
})
export class ConsultantComponent implements OnInit {

    @ViewChild(ConsultantTableComponent) consultantTable: ConsultantTableComponent;
    @ViewChild(TopicConsultantComponent) topicConsultant: TopicConsultantComponent;

    companiesObservable: Observable<any[]>;
    selectedCompany: Company;
    validationSelectCompany = false;

    constructor(
        private router: Router,
        private transferService: TransferDataService,
        private topicService: TopicService,
        private companyService: CompanyService,
        private userInfoService: UserInfoService
    ) {
    }

    ngOnInit() {
        console.log('ConsultantComponent - ngOnInit');

        this.selectedCompany = this.transferService.objectParam;
        if (this.selectedCompany) {
            this.onChangeCompany(this.selectedCompany);
        }

        this.getCompanies();

        const alertFromChild = this.transferService.aloneParam;
        if (alertFromChild) {
            this.validationSelectCompany = true;
        } else {
            this.validationSelectCompany = false;
        }

    }

    getCompanies() {
        console.log('TopicCreateEditComponent - getCompanies');

        const me = this;
        me.companiesObservable = me.companyService.getCompaniesByRole();
    }

    onChangeCompany(company) {
        this.consultantTable.company = company;
        this.consultantTable.getCompanyConsultant(company);
        this.topicConsultant.getCompanyTopic(company);
    }

    deletedConsultant($event) {
        if ($event) {
            this.onChangeCompany(this.selectedCompany);
        }
    }

}
