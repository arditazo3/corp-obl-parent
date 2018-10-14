import {Component, Input, OnInit} from '@angular/core';
import {TaskTemplateExpiration} from '../../model/task-template-expiration';

@Component({
  selector: 'app-task-template-expirations',
  templateUrl: './task-template-expirations.component.html',
  styleUrls: ['./task-template-expirations.component.css']
})
export class TaskTemplateExpirationsComponent implements OnInit {

  @Input() taskTemplateExpirations: TaskTemplateExpiration[];

  constructor() { }

  ngOnInit() {
  }

}
