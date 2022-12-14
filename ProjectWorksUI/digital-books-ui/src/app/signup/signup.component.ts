import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { BookService } from '../book.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  author={
    name:'',
    emailId:'',
    password:''
  };
  authorSignUpStatus:String='';
  blankFields={
    name:'',
    emailId:'',
    password:''
  };
  constructor(public bookService:BookService) { }
  createAccount(){
    console.log('Clicked!');
    const promise=this.bookService.createAccount(this.author);
    promise.subscribe((response:any)=>{
      console.log(response);
      if(response.status==201){
        this.authorSignUpStatus='Author Registered!';
      }
    },(error:any)=>{
      console.log(error.error);
      if(error.error=='User is present'){
        this.authorSignUpStatus=error.error;
      }
      this.blankFields.name=error.error.name;
      this.blankFields.emailId=error.error.emailId;
      this.blankFields.password=error.error.password;
    }
    )
    
  }

  ngOnInit(): void {
  }

}
