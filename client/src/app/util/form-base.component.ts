import {FormGroup} from '@angular/forms';
import {Component} from '@angular/core';

@Component({
  template: ''
})
export abstract class FormBaseComponent {

  public hasError(formGroup: FormGroup, controlName: string, validationType: string): boolean {
    const control = formGroup.controls[controlName];
    if (!control) {
      return false;
    }
    return control.hasError(validationType) && (control.dirty || control.touched);
  }

}
