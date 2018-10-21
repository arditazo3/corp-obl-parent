import {Task} from '../../back_office/task/model/task';
import {Expiration} from './expiration';

export class TaskExpirations {

    idTaskTemplate: number;
    description: string;
    task: Task;
    totalExpirations: number;
    totalCompleted: number;
    colorDefined: string;
    expirationDate: string;
    expirations: Expiration[];
}