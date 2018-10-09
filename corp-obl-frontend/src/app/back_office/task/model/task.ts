import {TaskTemplate} from '../../tasktemplate/model/tasktemplate';
import {TaskOffice} from './taskoffice';
import {OfficeComponent} from '../../office/component/office.component';
import {Office} from '../../office/model/office';

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
    counterCompany: number;
    office: Office;
    excludeOffice = false;
    counterOffices;
}
