import {Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {Expiration} from '../../model/expiration';
import {ExpirationActivity} from '../../model/expiration-activity';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {ApiErrorDetails} from '../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {UploadService} from '../../../shared/common/service/upload.service';
import {ExpirationService} from '../../service/expiration.service';
import {FileItem, FileLikeObject, ParsedResponseHeaders} from 'ng2-file-upload';
import {ExpirationActivityAttachment} from '../../model/expiration-activity-attachment';
import {saveAs as importedSaveAs} from 'file-saver';

@Component({
    selector: 'app-expiration-activity-controlled',
    templateUrl: './expiration-activity-controlled.component.html',
    styleUrls: ['./expiration-activity-controlled.component.css'],
    providers: [UploadService]
})
export class ExpirationActivityControlledComponent implements OnInit {

    @Output() updateTaskExpirationOnChange = new EventEmitter<boolean>();
    @Input() expiration: Expiration;
    @Input() expirationActivity: ExpirationActivity;
    @ViewChild('errorTaskTemplateSwal') private errorTaskTemplateSwal: SwalComponent;

    errorDetails: ApiErrorDetails = new ApiErrorDetails();

    expActivityMsg;
    submitted = false;
    expirationActivityStored: ExpirationActivity;

    counterUpload = 0;
    counterCallback = 0;
    uploader;

    constructor(
        private router: Router,
        private uploadService: UploadService,
        private expirationService: ExpirationService
    ) {
    }

    ngOnInit() {

        this.uploader = this.uploadService.uploaderExp;
        this.uploadService.uploadFileExpWithAuth();
        this.uploader.onBeforeUploadItem = (item) => {
            item.withCredentials = false;
        };
        this.uploader.onWhenAddingFileFailed = (item, filter, options) => this.onWhenAddingFileFailed(item, filter, options);
        this.onLoadFilesUploaded();
    }

    onLoadFilesUploaded() {
        console.log('ExpirationActivityControlledComponent - onLoadFilesUploaded');

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
        console.log('ExpirationActivityControlledComponent - onWhenAddingFileFailed');

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

    saveExpActivDetail() {
        console.log('ExpirationActivityControlledComponent - saveExpActivDetail');

        const me = this;
        this.submitted = true;

        if (!this.expActivityMsg) {
            return;
        }

        const cloneExpiration = {...this.expiration};
        cloneExpiration.expirationActivities = undefined;

        this.expirationActivity.idExpirationActivity = undefined;
        this.expirationActivity.expirationActivityAttachments = undefined;

        this.expirationActivity.body = this.expActivityMsg;

        this.expirationActivity.expiration = cloneExpiration;


        this.expirationService.saveUpdateExpirationActivity(this.expirationActivity).subscribe(
            data => {
                me.expirationActivityStored = data;

                me.uploader.onBuildItemForm = (fileItem: any, form: any) => {
                    form.append('idExpirationActivityAttachment', me.expirationActivityStored.idExpirationActivity);
                };

                let noFileUpload = true;
                me.uploader.queue.forEach((item) => {
                    if (!item.formData || item.formData.length === 0) {
                        item.upload();
                        noFileUpload = false;
                        me.counterUpload++;
                    }
                });
                me.uploader.onErrorItem = (item, response, status, headers) =>
                    me.onErrorItem(item, response, status, headers);
                me.uploader.onSuccessItem = (item, response, status, headers) =>
                    me.onSuccessItem(item, response, status, headers);

                me.counterCallback = 0;

                if (me.counterUpload === 0) {
                    me.expiration.expirationActivities.splice(1, 0, me.expirationActivityStored);
                    this.updateTaskExpirationOnChange.next(true);
                    this.cleanFormAfterInsert();
                }
            },
            error => {

            }
        );
    }

    onSuccessItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
        this.counterCallback++;
        // storing the attachments after storing the activity
        if (this.expirationActivityStored && item.file) {
            const file: FileLikeObject = item.file;
            const expActAttachment: ExpirationActivityAttachment = new ExpirationActivityAttachment();
            expActAttachment.fileName = file.name;
            expActAttachment.fileSize = file.size;
            expActAttachment.fileType = file.type;
            expActAttachment.fileData = file.rawFile;

            if (!this.expirationActivityStored.expirationActivityAttachments) {
                this.expirationActivityStored.expirationActivityAttachments = [];
            }
            this.expirationActivityStored.expirationActivityAttachments.push(expActAttachment);
            this.updateTaskExpirationOnChange.next(true);
        }
        if (this.counterUpload === this.counterCallback) {
            this.expiration.expirationActivities.splice(1, 0, this.expirationActivityStored);

            this.cleanFormAfterInsert();
        }
    }

    onErrorItem(item: FileItem, response: string, status: number, headers: ParsedResponseHeaders): any {
        this.counterCallback++;
        if (this.counterUpload === this.counterCallback) {
            this.router.navigate(['/front-end/agenda']);
        }
    }

    downloadFileExp(expirationActivityAttachment) {
        console.log('ExpirationActivityControlledComponent - downloadFileExp');

        const me = this;
        if (expirationActivityAttachment) {
            if (expirationActivityAttachment.fileData) {
                importedSaveAs(expirationActivityAttachment.fileData);
            } else {
                this.uploadService.downloadFileExp(expirationActivityAttachment).subscribe(
                    (data) => {
                        importedSaveAs(data, expirationActivityAttachment.fileName);
                        console.log('ExpirationActivityControlledComponent - downloadFileExp - next');
                    },
                    error => {
                        me.errorDetails = error.error;
                        console.error('ExpirationActivityControlledComponent - downloadFileExp - error \n', error);
                    });
            }
        }
    }

    downloadFile(item) {
        console.log('ExpirationActivityControlledComponent - downloadFile');
        importedSaveAs(item.file.rawFile);
    }

    removeFile(item) {
        console.log('ExpirationActivityControlledComponent - removeFile');

        const me = this;
        if (item.formData && item.formData.length === undefined) {
            const expirationActivityAttachment: ExpirationActivityAttachment = item.formData;

            this.uploadService.removeFileExp(expirationActivityAttachment).subscribe(
                data => {
                    console.log('ExpirationActivityControlledComponent - removeFile - next');
                },
                error => {
                    me.errorDetails = error.error;
                    console.error('ExpirationActivityControlledComponent - removeFile - error \n', error);
                }
            );
        }
        item.remove();
    }

    cleanFormAfterInsert() {

        this.expActivityMsg = '';
        this.submitted = false;
        this.expirationActivityStored = new ExpirationActivity();

        this.counterUpload = 0;
        this.counterCallback = 0;
        this.uploader.queue = [];

    }

}
