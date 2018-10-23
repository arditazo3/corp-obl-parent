import {Injectable} from '@angular/core';
import {FileUploader} from 'ng2-file-upload';
import {AppConfig} from '../api/app-config';
import {UserInfoService} from '../../../user/service/user-info.service';
import {FileUploaderOptions} from 'ng2-file-upload/file-upload/file-uploader.class';
import {Observable} from 'rxjs';
import {ApiRequestService} from './api-request.service';

@Injectable()
export class UploadService {

    constructor(
        private appConfig: AppConfig,
        private userInfoService: UserInfoService,
        private apiRequest: ApiRequestService
    ) {
    }

    uploader: FileUploader = new FileUploader({
        url: this.appConfig.baseApiPath + this.appConfig.uploadFile,
        isHTML5: true,
        allowedMimeType: [
            'image/png', 'image/jpg', 'image/jpeg', 'image/gif', // images
            'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', // .doc & .docx
            'application/vnd.ms-powerpoint', 'application/vnd.openxmlformats-officedocument.presentationml.presentation', // .ppt & .pptx
            'application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', // .xls & .xlsx
            'text/plain', 'application/pdf', 'application/zip', , 'application/rar', 'application/octet-stream',
            'application/x-zip-compressed', 'application/x-rar', 'application/x-rar-compressed'
        ],
        maxFileSize: 20 * 1024 * 1024 // 20 MB
    });

    uploadFileWithAuth() {
        console.log('UploadService - uploadFileWithAuth');

        const authHeader: Array<{
            name: string;
            value: string;
        }> = [];
        authHeader.push({name: 'Authorization', value: 'Bearer ' + this.userInfoService.getStoredToken()});
        const uploadOptions = <FileUploaderOptions>{headers: authHeader};
        this.uploader.setOptions(uploadOptions);
    }

    downloadFile(taskTempAttach): Observable<any> {
        console.log('UploadService - downloadFile');

        return this.apiRequest.getDownloadFile(this.appConfig.downloadFile, taskTempAttach);
    }

    downloadFileExp(expActAttach): Observable<any> {
        console.log('UploadService - downloadFileExp');

        return this.apiRequest.getDownloadFile(this.appConfig.downloadFileExp, expActAttach);
    }

    removeFile(taskTempAttach): Observable<any> {
        console.log('UploadService - removeFile');

        return this.apiRequest.put(this.appConfig.removeFile, taskTempAttach);
    }

    removeFileExp(expActAttach): Observable<any> {
        console.log('UploadService - removeFileExp');

        return this.apiRequest.put(this.appConfig.removeFileExp, expActAttach);
    }
}
