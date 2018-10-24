import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Expiration} from '../../model/expiration';
import {TaskTemplateAttachment} from '../../../back_office/tasktemplateattachment/tasktemplateattachment';
import {saveAs as importedSaveAs} from 'file-saver';
import {UploadService} from '../../../shared/common/service/upload.service';
import {ApiErrorDetails} from '../../../shared/common/api/model/api-error-details';
import {StatusExpirationEnum} from '../../../shared/common/api/enum/status.expiration.enum';
import {ExpirationActivityAttachment} from '../../model/expiration-activity-attachment';
import {FileItem, FileLikeObject} from 'ng2-file-upload';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {ExpirationService} from '../../service/expiration.service';
import {ExpirationActivity} from '../../model/expiration-activity';

@Component({
    selector: 'app-expiration-activity-detail',
    templateUrl: './expiration-activity-detail.component.html',
    styleUrls: ['./expiration-activity-detail.component.css'],
    providers: [UploadService]
})
export class ExpirationActivityDetailComponent implements OnInit {

    @Input() expiration: Expiration;
    @ViewChild('errorTaskTemplateSwal') private errorTaskTemplateSwal: SwalComponent;

    errorDetails: ApiErrorDetails = new ApiErrorDetails();

    restoreBtn = false;
    archivedBtn = false;
    approvedBtn = false;
    notApprovedBtn = false;

    expActivityMsg;
    submitted = false;

    uploader;

    constructor(
        private uploadService: UploadService,
        private expirationService: ExpirationService
    ) {
    }

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

        this.uploader = this.uploadService.uploader;
        this.uploadService.uploadFileWithAuth();
        this.uploader.onBeforeUploadItem = (item) => {
            item.withCredentials = false;
        };
        this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);
        this.onLoadFilesUploaded();
    }

    onLoadFilesUploaded() {
        console.log('ExpirationActivityDetailComponent - onLoadFilesUploaded');

        const me = this;
        const expirationActivity = this.expiration.expirationActivity;
        if (expirationActivity.expirationActivityAttachments && expirationActivity.expirationActivityAttachments.length > 0) {
            expirationActivity.expirationActivityAttachments.forEach((attachment) => {
                const file: File = new File(['#'.repeat(attachment.fileSize)], attachment.fileName);
                const fileItem: FileItem = new FileItem(me.uploader, file, null);
                fileItem.isUploaded = true;
                fileItem.formData = attachment;

                me.uploader.queue.push(fileItem);
            });
        }
    }

    onWhenAddingFileFailed(item: FileLikeObject, filter: any, options: any) {
        console.log('ExpirationActivityDetailComponent - onWhenAddingFileFailed');

        switch (filter.name) {
            case 'fileSize':
                this.errorDetails.message = 'Maximum upload size exceeded (' + (item.size / 1024 / 1024).toFixed(2) +
                    ' MB of ' + (this.uploader.options.maxFileSize / 1024 / 1024).toFixed(2) + ' MB allowed)';
                break;
            case 'mimeType':
                const allowedTypes = this.uploader.options.allowedMimeType.join();
                this.errorDetails.message = `Type ${item.type} is not allowed. Allowed types: document, zip, image`;
                break;
            default:
                this.errorDetails.message = `Unknown error (filter is ${filter.name})`;
        }

        this.errorTaskTemplateSwal.title = this.errorDetails.message;
        this.errorTaskTemplateSwal.show();
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

    restoreExp() {
        console.log('ExpirationActivityDetailComponent - restoreExp');
    }

    archivedExp() {
        console.log('ExpirationActivityDetailComponent - archivedExp');
    }

    approvedExp() {
        console.log('ExpirationActivityDetailComponent - approvedExp');
    }

    notApprovedExp() {
        console.log('ExpirationActivityDetailComponent - archivedExp');
    }

    saveExpActivDetail() {
        console.log('ExpirationActivityDetailComponent - saveExpActivDetail');

        const me = this;
        this.submitted = true;

        if (!this.expActivityMsg) {
            return;
        }

        const cloneExpiration = { ...this.expiration };
        const expirationActivity: ExpirationActivity = cloneExpiration.expirationActivity;
        expirationActivity.idExpirationActivity = undefined;
        expirationActivity.expirationActivityAttachments = undefined;

        expirationActivity.body = this.expActivityMsg;

        expirationActivity.expiration = cloneExpiration;

        // avoid infinite JSON cicle parent-child
        expirationActivity.expiration.expirationActivity = undefined;

        this.expirationService.saveUpdateExpirationActivity(expirationActivity).subscribe(
            data => {

            },
            error => {

            }
        );
    }

    downloadFile(item) {
        console.log('ExpirationActivityDetailComponent - downloadFile');

        const me = this;
        if (item.formData && item.formData.length === undefined) {
            const expirationActivityAttachment: ExpirationActivityAttachment = item.formData;
            this.uploadService.downloadFileExp(item.formData).subscribe(
                (data) => {
                    importedSaveAs(data, expirationActivityAttachment.fileName);
                    console.log('ExpirationActivityDetailComponent - downloadFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.error('ExpirationActivityDetailComponent - downloadFile - error \n', error);
                });
        } else {
            importedSaveAs(item.file.rawFile);
        }
    }

    removeFile(item) {
        console.log('ExpirationActivityDetailComponent - removeFile');

        const me = this;
        if (item.formData && item.formData.length === undefined) {
            const expirationActivityAttachment: ExpirationActivityAttachment = item.formData;

            this.uploadService.removeFileExp(expirationActivityAttachment).subscribe(
                data => {
                    console.log('ExpirationActivityDetailComponent - removeFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.error('ExpirationActivityDetailComponent - removeFile - error \n', error);
                }
            );
        }
        item.remove();
    }
}
