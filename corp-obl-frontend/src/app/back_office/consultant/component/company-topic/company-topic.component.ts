import { Component, OnInit } from '@angular/core';
import {ConsultantService} from '../../service/consultant.service';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {Router} from '@angular/router';
import {Observable} from '../../../../../../node_modules/rxjs/Rx';

@Component({
  selector: 'app-company-topic',
  templateUrl: './company-topic.component.html',
  styleUrls: ['./company-topic.component.css']
})
export class CompanyTopicComponent implements OnInit {

  companyTopicsArray = [];
  consultantsObservable: Observable<any[]>;

  constructor(
      private router: Router,
      private companyConsultantService: ConsultantService,
      private transferService: TransferDataService
  ) { }

  ngOnInit() {
      console.log('CompanyTopicComponent - ngOnInit');

      const me = this;
      me.getCompanyTopic(null);
  }

    getCompanyTopic(selectedCompany) {
        console.log('ConsultantTableComponent - getConsultants');

        if (!selectedCompany) {
            return;
        }

        const me = this;
        me.companyConsultantService.getCompanyTopic(selectedCompany).subscribe(
            (data) => {
                me.companyTopicsArray = data;
            }
        );

        me.consultantsObservable = me.companyConsultantService.getCompanyConsultant(selectedCompany);
    }

    onClearTopicConsultant(companyTopic) {
        console.log('CompanyTopicComponent - onClearTopicConsultant');
    }

    onAddTopicConsultant(companyTopic, consultant) {
        console.log('CompanyTopicComponent - onAddTopicConsultant');
    }

    onRemoveTopicConsultant(companyTopic, consultant) {
        console.log('CompanyTopicComponent - onRemoveTopicConsultant');
    }

}
