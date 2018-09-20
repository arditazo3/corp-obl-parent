import {Topic} from '../../topic/model/topic';

export class TaskTemplate {

    idTaskTemplate: number;
    topic: Topic;
    description: string;
    recurrence: string;
    expirationType: string;
    day: number;
    daysOfNotice: number;
    frequenceOfNotice: number;
    daysBeforeShowExpiration: number;
    expirationClosableBy: number;
}
