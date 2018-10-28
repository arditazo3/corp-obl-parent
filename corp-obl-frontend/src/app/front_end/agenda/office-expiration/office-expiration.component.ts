import {Component, Input, OnInit} from '@angular/core';
import {TaskOfficeExpirations} from '../../model/task-office-expirations';
import {Expiration} from '../../model/expiration';
import {StatusExpirationEnum} from '../../../shared/common/api/enum/status.expiration.enum';

@Component({
  selector: 'app-office-expiration',
  templateUrl: './office-expiration.component.html',
  styleUrls: ['./office-expiration.component.css']
})
export class OfficeExpirationComponent implements OnInit {

  @Input() taskExpiration: TaskOfficeExpirations;
  @Input() expiration: Expiration;

    restoreBtn = false;
    archivedBtn = false;
    approvedBtn = false;
    notApprovedBtn = false;

  constructor() { }

  ngOnInit() {

      const statusExp = this.expiration.expirationDetail.statusExpiration;

      if (statusExp === StatusExpirationEnum[StatusExpirationEnum.COMPLETED] ||
          statusExp === StatusExpirationEnum[StatusExpirationEnum.ARCHIVED]) {
          this.restoreBtn = true;
      }
      if (statusExp === StatusExpirationEnum[StatusExpirationEnum.BASE] ||
          statusExp === StatusExpirationEnum[StatusExpirationEnum.COMPLETED] ||
          statusExp === StatusExpirationEnum[StatusExpirationEnum.APPROVED]) {
          this.archivedBtn = true;
      }
      if (statusExp === StatusExpirationEnum[StatusExpirationEnum.COMPLETED]) {
          this.approvedBtn = true;
      }
      if (statusExp === StatusExpirationEnum[StatusExpirationEnum.APPROVED]) {
          this.notApprovedBtn = true;
      }
  }

    restoreExp() {
        console.log('OfficeExpirationComponent - restoreExp');
    }

    archivedExp() {
        console.log('OfficeExpirationComponent - archivedExp');
    }

    approvedExp() {
        console.log('OfficeExpirationComponent - approvedExp');
    }

    notApprovedExp() {
        console.log('OfficeExpirationComponent - archivedExp');
    }

}
