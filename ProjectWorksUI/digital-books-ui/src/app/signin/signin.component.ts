import { Component, OnInit } from '@angular/core';
import { BookService } from '../book.service';
import { Router } from '@angular/router';
import { ThrowStmt } from '@angular/compiler';

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
  blankFields={
    name:'',
    emailId:'',
    password:''
  };
  authorBooks:any[]=[];
  editFlag:boolean=false;
  signInFlag:boolean=true;
  bookStatus:String='';
  book={
    id:0,
    title:'',
    price:0,
    category:'',
    authorName:'',
    publisher:'',
    publishedDate:Date,
    logo:'',
    active:Boolean,
    chapter:''
  }

  constructor(public bookService:BookService , private router:Router) { }
  authorLogin(){
    console.log('Clicked!');
    const promise=this.bookService.authorLogin(this.author);
    promise.subscribe((responseBody: any)=>{
      this.loginStatus="User logged in";
      this.author.authorId=responseBody.authorId;
      this.signInFlag=false;
      console.log(responseBody.authorId);
    },
    (error:any)=>{
      console.log(error.error);
      if(error.error=='User is not present'){
        this.loginStatus=error.error;
      }
      this.blankFields.name=error.error.name;
      this.blankFields.emailId=error.error.emailId;
      this.blankFields.password=error.error.password;
    }
    );
  }
  onLogout(){
    console.log('Logged Out!');
    this.router.navigateByUrl('/');
  }

  allMyBooks(id:any){
    const promise=this.bookService.allMyBooks(id);
    promise.subscribe((response:any)=>{
      console.log(response);
      this.authorBooks=response;
      this.editFlag=false;

    },(error:any)=>{
      console.log(error.error);
    }
    )
  }

  updateBook(authorId:any,book:any){
    console.log(authorId,book.id,book.active);
    const promise=this.bookService.updateBook(authorId,book.id,book.active);
    promise.subscribe((response:any)=>{
      console.log(response);
      this.editFlag=true;
      this.bookStatus='Status Updated';
    },(error:any)=>{
      console.log(error.error);
    })
  }

  onEdit(book:any){
    this.editFlag=true;
    this.book.id=book.id;
    this.book.title=book.title;
    this.book.category=book.category;
    this.book.active=book.active;
    this.book.chapter=book.chapter;
    this.book.publishedDate=book.publishedDate;
    this.book.price=book.price;
    this.book.publisher=book.publisher;
    this.book.authorName=book.authorName;
    this.bookStatus='';
  }
  ngOnInit(): void {
  }

}
