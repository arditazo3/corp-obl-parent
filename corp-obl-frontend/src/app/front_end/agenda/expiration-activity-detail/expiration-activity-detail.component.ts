import {Component, Input, OnInit} from '@angular/core';
import {Expiration} from '../../model/expiration';
import {TaskTemplateAttachment} from '../../../back_office/tasktemplateattachment/tasktemplateattachment';
import {saveAs as importedSaveAs} from 'file-saver';
import {UploadService} from '../../../shared/common/service/upload.service';
import {ApiErrorDetails} from '../../../shared/common/api/model/api-error-details';

@Component({
  selector: 'app-expiration-activity-detail',
  templateUrl: './expiration-activity-detail.component.html',
  styleUrls: ['./expiration-activity-detail.component.css']
})
export class ExpirationActivityDetailComponent implements OnInit {

  @Input() expiration: Expiration;

    errorDetails: ApiErrorDetails = new ApiErrorDetails();

  constructor(
      private uploadService: UploadService
  ) { }

  ngOnInit() {
  }

    downloadFileExp(expirationActivityAttachment) {
        console.log('ExpirationActivityDetailComponent - downloadFileExp');

        const me = this;
        if (expirationActivityAttachment) {
            this.uploadService.downloadFileExp(expirationActivityAttachment).subscribe(
                (data) => {
                    importedSaveAs(data, expirationActivityAttachment.fileName);
                    console.log('ExpirationActivityDetailComponent - downloadFileExp - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.error('ExpirationActivityDetailComponent - downloadFileExp - error \n', error);
                });
        }
    }

}
