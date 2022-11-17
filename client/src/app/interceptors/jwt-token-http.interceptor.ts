import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {getItemFromStorage} from '../util/session-storage.util';
import {TOKEN} from '../util/session-storage-constant';

@Injectable()
export class JwtTokenHttpInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = getItemFromStorage(TOKEN);
    if (token) {
      request = request.clone({setHeaders: {Authorization: 'Bearer ' + token}});
    }
    return next.handle(request);
  }
}
