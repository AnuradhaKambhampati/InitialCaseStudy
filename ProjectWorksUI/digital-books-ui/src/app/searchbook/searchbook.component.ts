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
  paymentId:number=0;
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
      console.log(this.booksList);
    },
    (error:any)=>{
      console.log(error);
    }
    )
  }

  buyBook(book:any,reader:any){
    console.log(book.id,reader.emailId);
    const promise=this.bookService.buyBook(book.id,reader);
    promise.subscribe((response:any)=>{
      console.log(response);
      this.paymentId=response;
    },
    (error:any)=>{
      console.log(error);
    }
    )
  }
  
  ngOnInit(): void {
  }

}
