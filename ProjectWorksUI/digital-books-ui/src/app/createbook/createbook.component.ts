import { Component, Input, OnInit } from '@angular/core';
import { BookService } from '../book.service';

@Component({
  selector: 'app-createbook',
  templateUrl: './createbook.component.html',
  styleUrls: ['./createbook.component.css']
})
export class CreatebookComponent implements OnInit {
  book={
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
  @Input() authorId:any;
  constructor(public bookService:BookService) { }
  createBook(){
    const promise=this.bookService.createBook(this.book,this.authorId);
    promise.subscribe((responseBody: any)=>{
      console.log(responseBody);
    },
    (error:any)=>{
      console.log(error);
    }
    );
  }
  ngOnInit(): void {
  }

}
