import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {AuthService} from '../services/auth.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router,
              private authService: AuthService) {
  }

  canActivate(next: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.authService.isUserLoggedIn) {

      if (next.data.authority && !this.authService.hasAuthority(next.data.authority)) {
        this.router.navigate(['/']);
        return false;
      }

      return true;
    }

    this.router.navigate(['login']);
    return false;
  }

}
