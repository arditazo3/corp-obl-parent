import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-tasktemplate-table',
  templateUrl: './tasktemplate-table.component.html',
  styleUrls: ['./tasktemplate-table.component.css']
})
export class TaskTemplateTableComponent implements OnInit {

  @Input() taskTemplatesArray = [];

  constructor() { }

  ngOnInit() {
  }

    modifyTaskTemplate(taskTemplate) {

    }

}
