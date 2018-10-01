import {Company} from '../../company/model/company';
import {Topic} from '../../topic/model/topic';

export class ObjectSearchTaskTemplate {

    descriptionTaskTemplate: string;
    companies: Company[];
    topics: Topic[];

    constructor(descriptionTaskTemplate: string, companies: Company[], topics: Topic[]) {
        this.descriptionTaskTemplate = descriptionTaskTemplate;
        this.companies = companies;
        this.topics = topics;
    }
}
