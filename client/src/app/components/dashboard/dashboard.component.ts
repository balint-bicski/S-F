import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  text: string;

  constructor(private authService: AuthService) {
    this.text = `${authService.currentUser.id} - ${authService.currentUser.email} - ${authService.authorities()}`;
  }

}
