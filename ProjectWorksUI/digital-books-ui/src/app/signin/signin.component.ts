import { Component, OnInit } from '@angular/core';
import { BookService } from '../book.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {
  author={
    authorId:0,
    name:'',
    emailId:'',
    password:''
  };
  loginStatus: String ='';

  constructor(public bookService:BookService , private router:Router) { }
  authorLogin(){
    console.log('Clicked!');
    const promise=this.bookService.authorLogin(this.author);
    promise.subscribe((responseBody: any)=>{
      this.loginStatus="User logged in";
      this.author.authorId=responseBody.authorId;
      console.log(responseBody.authorId);
    },
    (error:any)=>{
      console.log(error.error);
      this.loginStatus=error.error;
    }
    );
  }
  onLogout(){
    console.log('Logged Out!');
    this.router.navigateByUrl('/');
  }

  allMyBooks(){
    console.log('Getting books..');
  }
  ngOnInit(): void {
  }

}
