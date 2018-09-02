import {Component, AfterViewInit, EventEmitter, Output, ChangeDetectorRef} from '@angular/core';
import {
  NgbModal,
  ModalDismissReasons,
  NgbPanelChangeEvent,
  NgbCarouselConfig
} from '@ng-bootstrap/ng-bootstrap';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import {LoginService} from '../../authentication/service/login.service';
import {UserInfoService} from '../../user/service/user-info.service';
declare var $: any;

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html'
})
export class NavigationComponent implements AfterViewInit {
  @Output() toggleSidebar = new EventEmitter<void>();

  public config: PerfectScrollbarConfigInterface = {};

  constructor(
  ) {}

  username = '';

  ngAfterViewInit() {
  }
}
