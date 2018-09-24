import {Topic} from '../../topic/model/topic';
import {Task} from '../../task/model/task';

export class TaskTemplate {

    idTaskTemplate: number;
    topic: Topic;
    taskResults: Task[];
    description: string;
    recurrence: string;
    expirationType: string;
    day: number;
    daysOfNotice: number;
    frequenceOfNotice: number;
    daysBeforeShowExpiration: number;
    expirationClosableBy: number;
}
