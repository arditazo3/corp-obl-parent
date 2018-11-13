import {TaskTemplate} from '../../tasktemplate/model/tasktemplate';
import {TaskOffice} from './taskoffice';
import {Office} from '../../office/model/office';
import {DescriptionLang} from '../../../shared/common/api/model/description-lang';

export class Task {

    idTask: number;
    idTaskTemplate: number;
    taskTemplate: TaskTemplate;
    recurrence: string;
    expirationType: string;
    day: number;
    daysOfNotice: number;
    frequenceOfNotice: number;
    daysBeforeShowExpiration: number;
    taskOffices: TaskOffice[];
    descriptionTask: string;
    descriptionLangList: DescriptionLang[];
    counterCompany: number;
    office: Office;
    excludeOffice = false;
    counterOffices;
}
