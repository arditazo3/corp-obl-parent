import {Component, Input, OnInit} from '@angular/core';
import {TaskExpirations} from '../../model/task-expirations';
import {Expiration} from '../../model/expiration';

@Component({
  selector: 'app-office-expiration-activity',
  templateUrl: './office-expiration-activity.component.html',
  styleUrls: ['./office-expiration-activity.component.css']
})
export class OfficeExpirationActivityComponent implements OnInit {

  @Input() taskExpiration: TaskExpirations;
  @Input() expiration: Expiration;

  constructor() { }

  ngOnInit() {
  }

}
