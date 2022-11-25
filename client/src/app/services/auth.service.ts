import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {switchMap, tap} from 'rxjs/operators';
import {getItemFromStorage, removeItemFromStorage, setItemInStorage} from '../util/session-storage.util';
import {CURRENT_USER, TOKEN} from '../util/session-storage-constant';
import {AuthenticationService, Authority, UserDto, UserService} from "../../../target/generated-sources";
import {fromBase64} from "../util/encoding.util";

export class JwtToken {
  authorities?: string[];
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient,
              private userService: UserService,
              private authenticationService: AuthenticationService) {
  }

  get currentUser(): UserDto {
    return JSON.parse(getItemFromStorage(CURRENT_USER));
  }

  get isUserLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  authenticate(email, password): Observable<UserDto> {
    return this.authenticationService.login({email, password})
    .pipe(
      tap(authResult => setItemInStorage(TOKEN, authResult.jwtToken)),
      switchMap(() => this.userService.getCurrentUser()),
      tap(userInfo => setItemInStorage(CURRENT_USER, JSON.stringify(userInfo))),
    );
  }

  logout(): void {
    removeItemFromStorage(TOKEN);
    removeItemFromStorage(CURRENT_USER);
  }

  getToken(): JwtToken {
    const token = getItemFromStorage(TOKEN);
    return JSON.parse(fromBase64(token.split('.')[1]));
  }

  hasAuthority(authority?: Authority): boolean {
    return !authority || this.getToken().authorities.includes(authority);
  }

  authorities(): string {
    return this.getToken().authorities.join(', ');
  }
}
