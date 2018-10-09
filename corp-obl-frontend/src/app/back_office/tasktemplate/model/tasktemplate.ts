import {Topic} from '../../topic/model/topic';
import {Task} from '../../task/model/task';
import {TaskTemplateAttachment} from '../../tasktemplateattachment/tasktemplateattachment';

export class TaskTemplate {

    idTaskTemplate: number;
    topic: Topic;
    taskResults: Task[];
    taskTemplateAttachmentResults: TaskTemplateAttachment[];
    description: string;
    recurrence: string;
    expirationType: string;
    day: number;
    daysOfNotice: number;
    frequenceOfNotice: number;
    daysBeforeShowExpiration: number;
    expirationClosableBy: number;
    descriptionTaskTemplate: string;
}
