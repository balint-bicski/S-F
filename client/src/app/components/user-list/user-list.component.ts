import {Component} from '@angular/core';
import {UserDto, UserService} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent {

  users: UserDto[];
  displayedColumns: string[] = ['id', 'email', 'role', 'action'];

  constructor(private userService: UserService,
              private snackBarService: SnackBarService) {
    this.loadUsers();
  }

  deleteUser(id: number) {
    this.userService.deleteUser(id).subscribe({
      next: () => this.snackBarService.success(`User has been successfully deleted with id: ${id}`),
      error: () => this.snackBarService.error(`Failed to delete user with id: ${id}`),
      complete: () => this.loadUsers()
    });
  }

  private loadUsers() {
    this.userService.getUsers().subscribe(users => this.users = users);
  }
}
