import {TaskTemplate} from '../../tasktemplate/model/tasktemplate';

export class Task {

    idTask: number;
    taskTemplate: TaskTemplate;
    recurrence: string;
    expirationType: string;
    day: number;
    daysOfNotice: number;
    frequenceOfNotice: number;
    daysBeforeShowExpiration: number;
    idTaskTemplate: number;
}
