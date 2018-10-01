import {Injectable} from '@angular/core';
import {ApiRequestService} from '../service/api-request.service';
import {AppConfig} from '../api/app-config';
import {Observable} from 'rxjs';
import {HttpParams} from '@angular/common/http';
import {UserInfoService} from '../../../user/service/user-info.service';

@Injectable()
export class TranslationService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig,
        private userInfoService: UserInfoService
    ) {}

    getTranslationsLikeTablename(tablename): Observable<any> {

        const httpParms: HttpParams = new HttpParams().set('tablename', tablename)
                                                      .set('lang', this.userInfoService.getUserLang());

        return this.apiRequest.get(this.appConfig.getTranslationsLikeTablename, httpParms);
    }
}
