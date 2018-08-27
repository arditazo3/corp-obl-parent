import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {LoginService} from '../service/login.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  usernameString: string;
  errorMsgForm = '';
  errorMsgServer = '';
  loginForm: FormGroup;
  loading = false;
  submitted = false;

  constructor(private router: Router,
              private loginService: LoginService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    console.log('LoginComponent - ngOnInit');
    // reset login status
    this.loginService.logout(false);

    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required]
    });
  }

  // convenience getter for easy access to form fields
  get f() {
    return this.loginForm.controls;
  }

  login() {
    console.log('LoginComponent - login');

    this.submitted = true;
    this.errorMsgServer = '';

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      this.errorMsgForm = 'The field is required';
      return;
    }

    this.loginService.logIn(this.f.username.value)
      .subscribe(
        resp => {
          if (resp.userAndToken === undefined ||
            resp.userAndToken.user === undefined ||
            resp.userAndToken.token === undefined ||
            resp.userAndToken.token === 'INVALID') {
            this.errorMsgServer = resp.message;
            return;
          }
          this.router.navigate([resp.landingPage]);
        }
      );
  }
}
