import { Component, OnInit } from '@angular/core';
import { BookService } from '../book.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reader-signin',
  templateUrl: './reader-signin.component.html',
  styleUrls: ['./reader-signin.component.css']
})
export class ReaderSigninComponent implements OnInit {
  reader={
    name:'',
    emailId:'',
    password:''
  };
  
  readerLoginStatus:String='';
  blankFields={
    name:'',
    emailId:'',
    password:''
  }
  readerBooks:any[]=[];
  bookContent:string='';
  signInFlag:boolean=true;
  readFlag:boolean=true;
  paymentId:any='';
  pIdErrorMsg:string='';
  pIdErrorFlag:boolean=false;
  bookReturnStatus:string='';
  returnFlag:boolean=false;
  bookWithPid={
    id:0,
    title:'',
    price:0.0,
    category:'',
    authorName:'',
    publisher:'',
    publishedDate:Date,
    logo:'',
    active:Boolean,
    chapter:''
  }
  
  constructor(public bookService:BookService , private router:Router) { }

  readerLogin(){
    const promise=this.bookService.readerLogin(this.reader);
    promise.subscribe((responseBody: any)=>{
      this.readerLoginStatus="Reader logged In";
      this.signInFlag=false;
      console.log(this.readerLoginStatus);
    },(error:any)=>{
      console.log(error.error);
      if(error.error=='User is not present'){
        this.readerLoginStatus=error.error;
      }
      this.blankFields.name=error.error.name;
      this.blankFields.emailId=error.error.emailId;
      this.blankFields.password=error.error.password;
    });
  }
  allReaderBooks(reader:any){
    const promise=this.bookService.allReaderBooks(reader);
    promise.subscribe((response:any)=>{
      console.log(response);
      this.readerBooks=response;
      this.readFlag=true;
      this.returnFlag=false;
      console.log(this.readerBooks);
    },(error:any)=>{
      console.log(error.error);
    });
  }
  onLogout(){
    console.log('Logged Out!');
    this.router.navigateByUrl('/');
  }

  readBook(reader:any,id:any){
    const promise=this.bookService.readBook(reader.emailId,id);
    promise.subscribe((response:any)=>{
      console.log(response);
      this.bookContent=response;
      this.readFlag=false;
     },(error:any)=>{
      console.log(error.error);
    });
  }

  findBooksUsingPid(reader:any){
    const promise=this.bookService.findBooksUsingPid(reader.emailId,this.paymentId);
    promise.subscribe((response:any)=>{
      console.log(response);
      this.bookWithPid=response;
      this.pIdErrorFlag=false;
      console.log(this.bookWithPid);
     },(error:any)=>{
      console.log(error.error);
      this.pIdErrorMsg=error.error;
      this.pIdErrorFlag=true;
    });
  }

  returnBook(reader:any,id:any){
    const promise=this.bookService.returnBook(reader.emailId,id);
    promise.subscribe((response:any)=>{
      console.log(response);
      this.bookReturnStatus=response;
      this.returnFlag=true;
     },(error:any)=>{
      console.log(error.error);
      this.bookReturnStatus=error.error;
    });
  }

  ngOnInit(): void {
  }

}
