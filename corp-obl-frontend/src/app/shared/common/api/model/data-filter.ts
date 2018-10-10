import {PageEnum} from '../enum/page.enum';
import {Company} from '../../../../back_office/company/model/company';
import {Topic} from '../../../../back_office/topic/model/topic';

export class DataFilter {

    page: PageEnum;
    description: string;
    company: Company;
    companies: Company[];
    topics: Topic[];

    constructor(page: PageEnum) {
        this.page = page;
    }
}