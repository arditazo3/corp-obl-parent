import {TaskTemplate} from '../../tasktemplate/model/tasktemplate';
import {TaskOffice} from './taskoffice';

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
}
