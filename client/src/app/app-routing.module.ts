import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {RegistrationComponent} from './components/registration/registration.component';
import {AuthGuard} from './guards/auth.guard';
import {LogoutComponent} from './components/logout/logout.component';
import {FrameComponent} from "./components/frame/frame.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {DetailsViewComponent} from "./components/details-view/details-view.component";
import {UserListComponent} from "./components/user-list/user-list.component";
import {Authority} from "../../target/generated-sources";
import {PaymentComponent} from "./components/payment/payment.component";

const routes: Routes = [
  {
    path: '', component: FrameComponent, children: [
      {path: '', pathMatch: 'full', redirectTo: 'dashboard'},
      {path: 'dashboard', component: DashboardComponent},
      {path: 'details/:caffId', component: DetailsViewComponent},
      {
        path: 'details/:caffId/purchase', component: PaymentComponent,
        canActivate: [AuthGuard], data: {authority: Authority.DownloadCaff}},
      {
        path: 'users', component: UserListComponent,
        canActivate: [AuthGuard], data: {authority: Authority.ViewUser},
      },
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
