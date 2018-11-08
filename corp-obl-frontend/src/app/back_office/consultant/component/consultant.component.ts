import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {Observable} from 'rxjs';
import {Company} from '../../company/model/company';
import {TransferDataService} from '../../../shared/common/service/transfer-data.service';
import {TopicService} from '../../topic/service/topic.service';
import {CompanyService} from '../../company/service/company.service';
import {Router} from '@angular/router';
import {ConsultantTableComponent} from './consultant-table/consultant-table.component';
import {TopicConsultantComponent} from './topic-consultant/topic-consultant.component';
import {DataFilter} from '../../../shared/common/api/model/data-filter';
import {PageEnum} from '../../../shared/common/api/enum/page.enum';
import {ConsultantService} from '../service/consultant.service';

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
    dataFilter: DataFilter = new DataFilter(PageEnum.BO_CONSULTANT);

    constructor(
        private router: Router,
        private transferService: TransferDataService,
        private topicService: TopicService,
        private companyService: CompanyService,
        private companyConsultantService: ConsultantService
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

        const dataFilterTemp: DataFilter = this.transferService.dataFilter;
        if (dataFilterTemp && dataFilterTemp.page === PageEnum.BO_CONSULTANT) {
            this.dataFilter = dataFilterTemp;
            this.selectedCompany = this.dataFilter.company;
            this.onChangeCompany(this.selectedCompany);
        }
    }

    getCompanies() {
        console.log('ConsultantComponent - getCompanies');

        const me = this;
        me.companiesObservable = me.companyService.getCompaniesByRole();
    }

    onChangeCompany(company) {

        this.selectedCompany = company;

        this.consultantTable.company = company;
        this.consultantTable.getCompanyConsultant(company);
        this.topicConsultant.getCompanyTopic(company);

        this.dataFilter.company = company;
    }

    deletedConsultant($event) {
        if ($event) {
            this.onChangeCompany(this.selectedCompany);
        }
    }

    isSelectedCompany($event) {
        if ($event) {
            this.onChangeCompany(this.selectedCompany);
        }
    }

}
