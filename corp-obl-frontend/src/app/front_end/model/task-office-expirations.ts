import {Task} from '../../back_office/task/model/task';
import {Expiration} from './expiration';
import {Office} from '../../back_office/office/model/office';

export class TaskOfficeExpirations {

    idTaskTemplate: number;
    description: string;
    task: Task;
    office: Office;
    totalExpirations: number;
    totalCompleted: number;
    totalArchived: number;
    colorDefined: string;
    expirationDate: string;
    expirations: Expiration[];
    statusExpirationOnChange;
}