import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {AuthGuard} from './guards/auth.guard';
import {LogoutComponent} from './components/logout/logout.component';
import {FrameComponent} from "./components/frame/frame.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";

const routes: Routes = [
  {
    path: '', component: FrameComponent, children: [
      {path: '', pathMatch: 'full', redirectTo: 'dashboard'},
      {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
      {path: 'login', component: LoginComponent},
      {path: 'logout', component: LogoutComponent},
      {path: 'register', component: RegistrationComponent},
    ],
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [AuthGuard]
})
export class AppRoutingModule {
}
