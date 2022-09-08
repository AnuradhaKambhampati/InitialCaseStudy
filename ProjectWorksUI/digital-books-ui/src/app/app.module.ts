import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { SignupComponent } from './signup/signup.component';
import { SigninComponent } from './signin/signin.component';
import { CreatebookComponent } from './createbook/createbook.component';
import { ReaderSigninComponent } from './reader-signin/reader-signin.component';
import { ReaderSignupComponent } from './reader-signup/reader-signup.component';
import { SearchbookComponent } from './searchbook/searchbook.component';

const routes:Routes=[
  {path:'signup',component:SignupComponent},
  {path:'signin',component:SigninComponent},
  {path:'readerSignup',component:ReaderSignupComponent},
  {path:'readerSignin',component:ReaderSigninComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    SigninComponent,
    CreatebookComponent,
    ReaderSigninComponent,
    ReaderSignupComponent,
    SearchbookComponent,
  ],
  imports: [
    BrowserModule,FormsModule,HttpClientModule,RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
