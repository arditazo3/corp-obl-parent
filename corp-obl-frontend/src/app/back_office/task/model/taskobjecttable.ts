import {Company} from '../../company/model/company';
import {Topic} from '../../topic/model/topic';

export class TaskObjectTable {

    description: string;
    companies: Company[];
    topics: Topic[];
}
