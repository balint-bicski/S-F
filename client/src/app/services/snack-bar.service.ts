import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class SnackBarService {

  constructor(private snackBar: MatSnackBar) {
  }

  success(message: string): void {
    this.openSnackBar(message, 'bg-success');
  }

  warning(message: string): void {
    this.openSnackBar(message, 'bg-warning');
  }

  error(message: string): void {
    this.openSnackBar(message, 'bg-danger');
  }

  openSnackBar(message: string, panelClass: string): void {
    this.snackBar.open(message, 'X', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass
    });
  }
}
