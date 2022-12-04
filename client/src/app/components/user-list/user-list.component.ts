import {Component} from '@angular/core';
import {Authority, UserDto, UserService} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";
import {AuthService} from "../../services/auth.service";
import {ConfirmDialogComponent} from "../details-view/confirm-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent {

  users: UserDto[];
  displayedColumns: string[] = ['id', 'email', 'role', 'action'];

  constructor(private userService: UserService,
              private authService: AuthService,
              private dialog: MatDialog,
              private snackBarService: SnackBarService) {
    this.loadUsers();
  }

  deleteUser(id: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {title: "Are you sure you'd like to delete the user?"},
    });
    dialogRef.afterClosed().subscribe(successModification => {
      if (successModification) {
        this.userService.deleteUser(id).subscribe({
          next: () => this.snackBarService.success(`User has been successfully deleted with id: ${id}`),
          error: () => this.snackBarService.error(`Failed to delete user with id: ${id}`),
          complete: () => this.loadUsers()
        });
      }
    });
  }

  deleteDisabled(user: UserDto): boolean {
    return user.role === 'ADMINISTRATOR' || !this.hasDeleteRight();
  }

  hasDeleteRight(): boolean {
    return this.authService.hasRightToAccess(Authority.DeleteUser);
  }

  private loadUsers() {
    this.userService.getUsers().subscribe(users => this.users = users);
  }
}
