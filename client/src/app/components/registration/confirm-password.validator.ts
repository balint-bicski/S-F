import {AbstractControl, ValidationErrors} from '@angular/forms';

export function ValidatePasswordConfirmation(control: AbstractControl): ValidationErrors | null {
  const password = control.get('password').value;
  const confirmPassword = control.get('confirmPassword').value;

  if (password !== confirmPassword) {
    control.get('confirmPassword').setErrors({confirmation: true});
    return ({confirmation: true});
  } else {
    return null;
  }
}
