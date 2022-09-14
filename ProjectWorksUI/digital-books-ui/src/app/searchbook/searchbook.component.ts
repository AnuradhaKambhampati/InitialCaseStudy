import { Component, Input, OnInit } from '@angular/core';
import { BookService } from '../book.service';
import { Output,EventEmitter } from '@angular/core';

@Component({
  selector: 'app-searchbook',
  templateUrl: './searchbook.component.html',
  styleUrls: ['./searchbook.component.css']
})
export class SearchbookComponent implements OnInit {
  book={
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
  booksList:any[]=[];
  booksListStatus:string='';
  paymentId:number=0;
  buyBookStatus:string='';
  buyBookFlag:boolean=false;
  @Input() reader={ 
    name:'',
    emailId:'',
    password:''
  };

  constructor(public bookService:BookService) { }

  searchBook(){
    const promise=this.bookService.searchBook(this.book.category,this.book.authorName,this.book.price,this.book.publisher);
    promise.subscribe((response:any)=>{
      console.log(response);
      this.booksList=response;
      this.buyBookFlag=false;
      if(this.booksList.length==0){
        this.booksListStatus="No books found";
      }else{
        this.booksListStatus="";
      }
    },
    (error:any)=>{
      console.log(error.error);
    }
    )
  }

  buyBook(book:any,reader:any){
    console.log(book.id,reader.emailId);
    const promise=this.bookService.buyBook(book.id,reader);
    promise.subscribe((response:any)=>{
      console.log(response);
      this.paymentId=response;
      if(this.paymentId!=null){
        this.buyBookStatus="Purchase Success";
        this.buyBookFlag=true;
      }
    },
    (error:any)=>{
      console.log(error);
      this.buyBookStatus="Purchase Failed";
      this.buyBookFlag=false;
    }
    )
  }
  
  ngOnInit(): void {
  }

}
