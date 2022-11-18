import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-logout',
  template: ''
})
export class LogoutComponent implements OnInit {

  constructor(private authService: AuthService,
              private router: Router) {
    this.authService.logout();
  }

  ngOnInit(): void {
    this.router.navigate(['login']);
  }

}
