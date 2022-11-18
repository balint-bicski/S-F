import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {SnackBarService} from '../../services/snack-bar.service';
import {FormBaseComponent} from '../../util/form-base.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent extends FormBaseComponent implements OnInit {

  defaultEmail = 'user@user.com';
  defaultPassword = 'password';

  invalidLogin = false;
  loginForm: FormGroup;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private snackBar: SnackBarService,
              private authService: AuthService) {
    super();
  }

  ngOnInit(): void {
    this.initLoginForm();
  }

  login(): void {
    this.authService.authenticate(this.loginForm.value.email, this.loginForm.value.password).subscribe({
      next: _ => {
        this.router.navigate(['']);
        this.invalidLogin = false;
      },
      error: _ => this.invalidLogin = true
    });
  }

  register(): void {
    this.router.navigate(['register']);
  }

  private initLoginForm(): void {
    this.loginForm = this.formBuilder.group({
      email: [this.defaultEmail, Validators.required],
      password: [this.defaultPassword, Validators.required]
    });
  }
}
