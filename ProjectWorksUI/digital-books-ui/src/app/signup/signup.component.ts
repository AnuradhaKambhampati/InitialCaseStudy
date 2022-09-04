import { Component, OnInit } from '@angular/core';
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
  constructor(public bookService:BookService) { }
  createAccount(){
    console.log('Clicked!');
    const promise=this.bookService.createAccount(this.author);
    promise.subscribe((responseBody:any)=>{
      console.log(responseBody);
    },(error:any)=>{
      console.log(error);
    }
    )
    
  }

  ngOnInit(): void {
  }

}
