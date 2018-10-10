import {PageEnum} from '../enum/page.enum';
import {Company} from '../../../../back_office/company/model/company';
import {Topic} from '../../../../back_office/topic/model/topic';
import {Office} from '../../../../back_office/office/model/office';

export class DataFilter {

    page: PageEnum;
    description: string;
    company: Company;
    companies: Company[];
    topics: Topic[];
    offices: Office[];

    constructor(page: PageEnum) {
        this.page = page;
    }
}