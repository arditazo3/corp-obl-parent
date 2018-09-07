import {Company} from '../../company/model/company';
import {CompanyTopic} from '../../company/model/company_topic';

export class Topic {

    idTopic: number;
    description: string;
    companyTopicList: CompanyTopic[];
}
