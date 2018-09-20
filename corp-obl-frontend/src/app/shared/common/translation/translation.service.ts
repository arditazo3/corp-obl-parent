import {Injectable} from '@angular/core';
import {ApiRequestService} from '../service/api-request.service';
import {AppConfig} from '../api/app-config';
import {Observable} from '../../../../../node_modules/rxjs/Rx';
import {HttpParams} from '@angular/common/http';
import {UserInfoService} from '../../../user/service/user-info.service';
import {Translation} from './model/translation';

@Injectable()
export class TranslationService {

    translation: Translation = new Translation();

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
