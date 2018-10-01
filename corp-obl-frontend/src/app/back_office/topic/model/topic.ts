import {Company} from '../../company/model/company';
import {Translation} from '../../../shared/common/translation/model/translation';
import {TopicConsultant} from './topic-consultant';
import {Consultant} from '../../consultant/model/consultant';

export class Topic {

    idTopic: number;
    description: string;
    companyList: Company[];
    translationList: Translation[];
    topicConsultantList: TopicConsultant[];
    consultantList: Consultant[];
}
