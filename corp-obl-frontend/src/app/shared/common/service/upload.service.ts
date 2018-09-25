import {Injectable} from '@angular/core';
import {FileUploader} from 'ng2-file-upload';
import {AppConfig} from '../api/app-config';
import {UserInfoService} from '../../../user/service/user-info.service';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Router} from '@angular/router';
import {FileUploaderOptions} from 'ng2-file-upload/file-upload/file-uploader.class';
import {Observable} from '../../../../../node_modules/rxjs/Rx';
import {ApiRequestService} from './api-request.service';
import {AuthorityEnum} from '../api/enum/authority.enum';

@Injectable()
export class UploadService {

    constructor(
        private appConfig: AppConfig,
        private userInfoService: UserInfoService,
        private apiRequest: ApiRequestService
    ) {
    }

    uploader: FileUploader = new FileUploader({
        url: this.appConfig.baseApiPath + this.appConfig.fileUpload,
        isHTML5: true,
        allowedMimeType: [
            'image/png', 'image/jpg', 'image/jpeg', 'image/gif', // images
            'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', // .doc & .docx
            'application/vnd.ms-powerpoint', 'application/vnd.openxmlformats-officedocument.presentationml.presentation', // .ppt & .pptx
            'application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', // .xls & .xlsx
            'text/plain', 'application/pdf', 'application/zip', 'application/octet-stream', 'application/x-zip-compressed', 'application/x-rar-compressed'
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

        const httpParms: HttpParams = new HttpParams().set('filePath', taskTempAttach.filePath);

        return this.apiRequest.get(this.appConfig.downloadUpload, httpParms);
    }
}
