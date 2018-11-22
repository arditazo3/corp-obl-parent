import {TaskTemplate} from '../../tasktemplate/model/tasktemplate';
import {Office} from '../../office/model/office';
import {Task} from '../../task/model/task';

export class TaskTemplateOffice {

    taskTemplate: TaskTemplate;
    office: Office;
    task: Task;
    isSavingTaskTemplateTask = false;
}