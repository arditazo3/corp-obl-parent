import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Expiration} from '../../model/expiration';
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
    selector: 'app-expiration-activity',
    templateUrl: './expiration-activity.component.html',
    styleUrls: ['./expiration-activity.component.css'],
    providers: [UploadService]
})
export class ExpirationActivityComponent implements OnInit {

    @Input() expiration: Expiration;
    @Input() expirationActivity: ExpirationActivity;
    @ViewChild('errorTaskTemplateSwal') private errorTaskTemplateSwal: SwalComponent;

    errorDetails: ApiErrorDetails = new ApiErrorDetails();

    expActivityMsg;
    submitted = false;

    uploader;

    constructor(
        private uploadService: UploadService,
        private expirationService: ExpirationService
    ) {
    }

    ngOnInit() {

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
        const expirationActivity = this.expirationActivity;
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

    saveExpActivDetail() {
        console.log('ExpirationActivityDetailComponent - saveExpActivDetail');

        const me = this;
        this.submitted = true;

        if (!this.expActivityMsg) {
            return;
        }

        const cloneExpiration = { ...this.expiration };
        this.expirationActivity.idExpirationActivity = undefined;
        this.expirationActivity.expirationActivityAttachments = undefined;

        this.expirationActivity.body = this.expActivityMsg;

        this.expirationActivity.expiration = cloneExpiration;


        this.expirationService.saveUpdateExpirationActivity(this.expirationActivity).subscribe(
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
