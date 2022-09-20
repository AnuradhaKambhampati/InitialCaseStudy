import { HttpClient } from '@angular/common/http';
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
    price:0,
    category:'',
    authorName:'',
    publisher:'',
    publishedDate:Date,
    logo:'',
    active:Boolean,
    chapter:''
  }
  blankFields={
    title:'',
    price:'',
    category:'',
    authorName:'',
    publisher:'',
    publishedDate:'',
    logo:'',
    active:Boolean,
    chapter:''
  }
  bookStatus:string='';
  @Input() authorId:any;
  selectedFile:any=null;
  fileLink:any='';

  constructor(public bookService:BookService,private http:HttpClient) { }
  createBook(){
    this.book.logo=this.fileLink;
    const promise=this.bookService.createBook(this.book,this.authorId);
    promise.subscribe((responseBody: any)=>{
      console.log(responseBody);
      this.bookStatus="Book Created";
    },
    (error:any)=>{
      console.log(error);
      this.blankFields.title=error.error.title;
      this.blankFields.price=error.error.price;
      this.blankFields.category=error.error.category;
      this.blankFields.publisher=error.error.publisher;
      this.blankFields.chapter=error.error.chapter;
      this.blankFields.publishedDate=error.error.publishedDate;
    }
    );
  }

  onFileSelected(event:any){
    console.log(event);
    this.selectedFile=<File> event.target.files[0];
    console.log(this.selectedFile);
  }
  
  onUpload(){
    const promise=this.bookService.uploadFile(this.selectedFile);
    promise.subscribe((event:any)=>{
      if(typeof event==='object'){
        console.log(event);
        this.fileLink=event.link;
      }
    });
  }
  ngOnInit(): void {
  }

}
