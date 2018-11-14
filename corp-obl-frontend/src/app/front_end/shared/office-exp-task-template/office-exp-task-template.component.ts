import {Component, Input, OnInit} from '@angular/core';
import {Expiration} from '../../model/expiration';
import {TaskTemplate} from '../../../back_office/tasktemplate/model/tasktemplate';
import {saveAs as importedSaveAs} from 'file-saver';
import {UploadService} from '../../../shared/common/service/upload.service';
import {ApiErrorDetails} from '../../../shared/common/api/model/api-error-details';

@Component({
    selector: 'app-office-exp-task-template',
    templateUrl: './office-exp-task-template.component.html',
    styleUrls: ['./office-exp-task-template.component.css'],
    providers: [UploadService]
})
export class OfficeExpTaskTemplateComponent implements OnInit {

    @Input() taskTemplate: TaskTemplate;

    errorDetails: ApiErrorDetails = new ApiErrorDetails();

    uploader;

    constructor(
        private uploadService: UploadService
    ) {
    }

    ngOnInit() {

        this.uploader = this.uploadService.uploader;
        this.uploadService.uploadFileExpWithAuth();
    }

    downloadFileExp(taskTemplateAttachment) {
        console.log('OfficeExpTaskTemplateComponent - downloadFileExp');

        const me = this;
        if (taskTemplateAttachment) {
            if (taskTemplateAttachment.fileData) {
                importedSaveAs(taskTemplateAttachment.fileData);
            } else {
                this.uploadService.downloadFileExp(taskTemplateAttachment).subscribe(
                    (data) => {
                        importedSaveAs(data, taskTemplateAttachment.fileName);
                        console.log('ExpirationActivityDetailComponent - downloadFileExp - next');
                    },
                    error => {
                        me.errorDetails = error.error;
                        console.error('ExpirationActivityDetailComponent - downloadFileExp - error \n', error);
                    });
            }
        }
    }

}
