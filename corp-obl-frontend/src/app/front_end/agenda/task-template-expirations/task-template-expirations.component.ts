import {Component, Input, OnInit} from '@angular/core';
import {TaskExpirations} from '../../model/task-expirations';

@Component({
  selector: 'app-task-template-expirations',
  templateUrl: './task-template-expirations.component.html',
  styleUrls: ['./task-template-expirations.component.css']
})
export class TaskTemplateExpirationsComponent implements OnInit {

  @Input() taskExpirations: TaskExpirations[];

  constructor() { }

  ngOnInit() {
  }

}
