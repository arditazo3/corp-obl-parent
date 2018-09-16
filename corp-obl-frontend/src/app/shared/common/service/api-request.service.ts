import {Injectable} from '@angular/core';
import {AppConfig} from '../api/app-config';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Router} from '@angular/router';
import {UserInfoService} from '../../../user/service/user-info.service';
import {Observable} from 'rxjs';
import 'rxjs/add/operator/catch';
import {ErrorObservable} from 'rxjs-compat/observable/ErrorObservable';
import {FileUploader} from 'ng2-file-upload';
import {FileUploaderOptions} from 'ng2-file-upload/file-upload/file-uploader.class';

@Injectable()
export class ApiRequestService {

    constructor(
        private appConfig: AppConfig,
        private http: HttpClient,
        private router: Router,
        private userInfoService: UserInfoService
    ) {
    }

    uploader: FileUploader = new FileUploader({
        url: this.appConfig.baseApiPath + this.appConfig.fileUpload,
        isHTML5: true
    });

    /**
     * This is a Global place to add all the request headers for every REST calls
     */
    getHeaders(): HttpHeaders {
        let headers = new HttpHeaders();
        const token = this.userInfoService.getStoredToken();

        headers = headers.append('Accept', 'application/json');
        headers = headers.append('Content-Type', 'application/json;charset=ISO-8859-1');
        headers = headers.append('Access-Control-Allow-Origin', '*');

        if (token !== null) {
            headers = headers.append('Authorization', 'Bearer ' + token);
        }
        return headers;
    }

    get(url: string, urlParams?: HttpParams): Observable<any> {
//    console.log('ApiRequestService - get');

        const me = this;
        return this.http.get(this.appConfig.baseApiPath + url, {headers: this.getHeaders(), params: urlParams})
            .catch(function (error: any) {
                {
                    if (error.status === 401 || error.status === 403) {
                        me.router.navigate(['/logout']);
                    }
                    return ErrorObservable.create(error || 'Server error');
                }
            });
    }

    post(url: string, body: Object): Observable<any> {
        //  console.log('ApiRequestService - post');

        const me = this;
        return this.http.post(this.appConfig.baseApiPath + url, JSON.stringify(body), {headers: this.getHeaders()})
            .catch(function (error: any) {
                if (error.status === 401) {
                    me.router.navigate(['/logout']);
                }
                return ErrorObservable.create(error || 'Server error');
            });
    }

    put(url: string, body: Object): Observable<any> {
//    console.log('ApiRequestService - put');

        const me = this;
        return this.http.put(this.appConfig.baseApiPath + url, JSON.stringify(body), {headers: this.getHeaders()})
            .catch(function (error: any) {
                if (error.status === 401) {
                    me.router.navigate(['/logout']);
                }
                return ErrorObservable.create(error || 'Server error');
            });
    }

    delete(url: string): Observable<any> {
        //  console.log('ApiRequestService - delete');

        const me = this;
        return this.http.delete(this.appConfig.baseApiPath + url, {headers: this.getHeaders()})
            .catch(function (error: any) {
                if (error.status === 401) {
                    me.router.navigate(['/logout']);
                }
                return ErrorObservable.create(error || 'Server error');
            });
    }

    uploadFileWithAuth() {
        console.log('ApiRequestService - uploadFileWithAuth');

        const authHeader: Array<{
            name: string;
            value: string;
        }> = [];
        authHeader.push({name: 'Authorization' , value: 'Bearer ' + this.userInfoService.getStoredToken()});
        const uploadOptions = <FileUploaderOptions>{headers : authHeader};
        this.uploader.setOptions(uploadOptions);
    }
}
