import {Company} from '../../company/model/company';
import {CompanyTopic} from '../../company/model/company_topic';
import {Translation} from '../../../shared/common/translation/model/translation';

export class Topic {

    idTopic: number;
    description: string;
    companyList: Company[];
    translationList: Translation[];
}
