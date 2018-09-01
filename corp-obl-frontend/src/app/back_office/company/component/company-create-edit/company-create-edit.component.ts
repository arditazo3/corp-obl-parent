import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';
import {UserInfoService} from '../../../../user/service/user-info.service';
import {User} from '../../../../user/model/user';
import {UserService} from '../../../../user/service/user.service';

@Component({
  selector: 'app-company-create-edit',
  templateUrl: './company-create-edit.component.html',
  styleUrls: ['./company-create-edit.component.css']
})
export class CompanyCreateEditComponent implements OnInit {

  isNewForm = true;
  users: User[];

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit() {

    this.getUsers();
  }

  getUsers() {

    const me = this;
    me.userService.getAllUsers().subscribe(
      (data) => {
        me.users = data;
      }
    );
  }

}
