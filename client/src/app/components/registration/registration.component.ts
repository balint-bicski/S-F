import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {SnackBarService} from '../../services/snack-bar.service';
import {FormBaseComponent} from '../../util/form-base.component';
import {ValidatePasswordConfirmation} from './confirm-password.validator';
import {AuthenticationService, ValidationExceptionDto} from "../../../../target/generated-sources";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent extends FormBaseComponent implements OnInit {

  userForm: FormGroup;

  constructor(private authenticationService: AuthenticationService,
              private authService: AuthService,
              private router: Router,
              private formBuilder: FormBuilder,
              private snackBar: SnackBarService) {
    super();
  }

  ngOnInit(): void {
    this.initUserForm();
  }

  register(): void {
    this.authenticationService.register({
      email: this.userForm.get("email").value,
      password: this.userForm.get("password").value
    }).subscribe({
      next: () => {
        this.router.navigate(['']);
        this.snackBar.success('Registration was successful');
      },
      // TODO: create general exception handling
      error: (err: HttpErrorResponse) => this.snackBar.error((err.error as ValidationExceptionDto).errors[0].messageKey)
    });
  }

  private initUserForm(): void {
    this.userForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      agree: [false, Validators.requiredTrue],
    }, {
      validators: ValidatePasswordConfirmation
    });
  }
}
