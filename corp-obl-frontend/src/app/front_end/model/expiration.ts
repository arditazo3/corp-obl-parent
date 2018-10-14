import {TaskTemplate} from '../../back_office/tasktemplate/model/tasktemplate';
import {Task} from '../../back_office/task/model/task';
import {Office} from '../../back_office/office/model/office';

export class Expiration {

    idExpiration: number;
    taskTemplate: TaskTemplate;
    task: Task;
    office: Office;
    expirationClosableBy: string;
    username: string;
    expirationDate: Date;
    completed: Date;
    approved: Date;
    registered: Date;
}