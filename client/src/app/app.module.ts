import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FlexLayoutModule} from '@angular/flex-layout';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './components/login/login.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {JwtTokenHttpInterceptor} from './interceptors/jwt-token-http.interceptor';
import {RegistrationComponent} from './components/registration/registration.component';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatCardModule} from '@angular/material/card';
import {LogoutComponent} from './components/logout/logout.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FrameComponent} from './components/frame/frame.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {NoopAnimationsModule} from "@angular/platform-browser/animations";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatIconModule} from "@angular/material/icon";
import { UserListComponent } from './components/user-list/user-list.component';
import {MatTableModule} from "@angular/material/table";
import {DetailsViewComponent} from "./components/details-view/details-view.component";
import {ApiModule} from "../../target/generated-sources";
import {CommentsComponent} from "./components/comments/comments.component";
import {CreateDialogComponent} from "./components/dashboard/create-dialog.component";
import {MatDialogModule} from "@angular/material/dialog";
import {EventEditDialogComponent} from "./components/details-view/event-edit-dialog.component";
import {ConfirmDialogComponent} from "./components/details-view/confirm-dialog.component";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    LogoutComponent,
    FrameComponent,
    DashboardComponent,
    CreateDialogComponent,
    DetailsViewComponent,
    EventEditDialogComponent,
    ConfirmDialogComponent,
    CommentsComponent,
    UserListComponent
  ],
  imports: [
    BrowserModule,
    NoopAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatCheckboxModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatToolbarModule,
    FlexLayoutModule,
    MatTooltipModule,
    MatIconModule,
    MatTableModule,
    ApiModule,
    FormsModule,
    MatDialogModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtTokenHttpInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
