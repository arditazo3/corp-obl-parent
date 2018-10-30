import {Component, Input, OnInit} from '@angular/core';
import {TaskOfficeExpirations} from '../../model/task-office-expirations';

@Component({
  selector: 'app-task-template-expirations-controlled',
  templateUrl: './task-template-expirations-controlled.component.html',
  styleUrls: ['./task-template-expirations-controlled.component.css']
})
export class TaskTemplateExpirationsControlledComponent implements OnInit {

    @Input() taskExpirations: TaskOfficeExpirations[];

    constructor() { }

    ngOnInit() {
    }

}
