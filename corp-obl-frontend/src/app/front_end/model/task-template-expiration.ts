import {TaskExpirations} from './task-expirations';

export class TaskTemplateExpiration {

    idTaskTemplate: number;
    description: string;
    totalExpirations: number;
    totalCompleted: number;
    colorDefined: string;
    taskExpirations: TaskExpirations[];
}