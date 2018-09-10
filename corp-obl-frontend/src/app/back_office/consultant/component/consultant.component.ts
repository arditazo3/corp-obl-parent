import {Component, OnInit, ViewChild} from '@angular/core';
import {Observable} from '../../../../../node_modules/rxjs/Rx';
import {Company} from '../../company/model/company';
import {TransferDataService} from '../../../shared/common/service/transfer-data.service';
import {TopicService} from '../../topic/service/topic.service';
import {CompanyService} from '../../company/service/company.service';
import {UserInfoService} from '../../../user/service/user-info.service';
import {Router} from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {ConsultantTableComponent} from './consultant-table/consultant-table.component';

@Component({
    selector: 'app-consultant',
    templateUrl: './consultant.component.html',
    styleUrls: ['./consultant.component.css']
})
export class ConsultantComponent implements OnInit {

    @ViewChild('consultantTable') consultantTable: ConsultantTableComponent;

    companiesObservable: Observable<any[]>;
    selectedCompany: Company;

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
    }

    getCompanies() {
        console.log('TopicCreateEditComponent - getCompanies');

        const me = this;
        me.companiesObservable = me.companyService.getCompanies();
    }

    onChangeCompany(company) {
        this.consultantTable.company = company;
        this.consultantTable.getCompanyConsultant(company);
    }

}
