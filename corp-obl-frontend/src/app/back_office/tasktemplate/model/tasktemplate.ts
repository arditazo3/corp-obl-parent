import {Topic} from '../../topic/model/topic';
import {Task} from '../../task/model/task';
import {TaskTemplateAttachment} from '../../tasktemplateattachment/tasktemplateattachment';
import {DescriptionLang} from '../../../shared/common/api/model/description-lang';

export class TaskTemplate {

    idTaskTemplate: number;
    topic: Topic;
    taskResults: Task[];
    taskTemplateAttachmentResults: TaskTemplateAttachment[];
    description: string;
    isRapidConfiguration: boolean;
    recurrence: string;
    expirationType: string;
    day: number;
    daysOfNotice: number;
    frequenceOfNotice: number;
    daysBeforeShowExpiration: number;
    expirationClosableBy: number;
    descriptionLangList: DescriptionLang[];
    descriptionTaskTemplate: string;
}
