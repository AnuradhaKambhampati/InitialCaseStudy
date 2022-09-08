import { HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { BookService } from '../book.service';

@Component({
  selector: 'app-reader-signup',
  templateUrl: './reader-signup.component.html',
  styleUrls: ['./reader-signup.component.css']
})
export class ReaderSignupComponent implements OnInit {
  reader={
    name:'',
    emailId:'',
    password:''
  };
  emailExists:boolean=false;
  signUpStatus:String='';
  blankFields={
    name:'',
    emailId:'',
    password:''
  };

  constructor(public bookService:BookService) { }

  createReaderAccount(){
    console.log('Clicked!');
    const promise=this.bookService.createReaderAccount(this.reader);
    promise.subscribe((response:any)=>{
      console.log(response);
      if(response.status==201){
        this.signUpStatus='User Registered!'
      }
    },(error:any)=>{
      console.log(error.error);
      if(error.error=='User is present'){
        this.emailExists=true;
        this.signUpStatus=error.error;
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
