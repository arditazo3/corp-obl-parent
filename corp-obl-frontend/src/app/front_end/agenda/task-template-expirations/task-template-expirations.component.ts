import {Component, Input, OnInit} from '@angular/core';
import {TaskOfficeExpirations} from '../../model/task-office-expirations';

@Component({
  selector: 'app-task-template-expirations',
  templateUrl: './task-template-expirations.component.html',
  styleUrls: ['./task-template-expirations.component.css']
})
export class TaskTemplateExpirationsComponent implements OnInit {

  @Input() taskExpirations: TaskOfficeExpirations[];

  constructor() { }

  ngOnInit() {
  }

}
