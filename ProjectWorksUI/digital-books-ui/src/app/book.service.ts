import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

const URL='http://localhost:8082/digitalbooks';
@Injectable({
  providedIn: 'root'
})
export class BookService {
  inputRequest={
    bookId:0,
    reader:{
      name:'',
      emailId:'',
      password:''
    }
  }
  createAccount(author:any):Observable<HttpResponse<any>>{
    return this.http.post(URL+'/author/signup',author,{observe:'response'});
  }

  authorLogin(author:any){
    //return this.http.post(URL+'/author/login',author,{responseType:"text"});
    return this.http.post(URL+'/author/login',author);
  }

  createBook(book:any,authorId:any){
    return this.http.post(URL+'/author/'+authorId+'/books',book);
  }

  createReaderAccount(reader:any):Observable<HttpResponse<any>>{
    return this.http.post(URL+'/reader/signup',reader,{observe:'response'});
  }

  readerLogin(reader:any){
    return this.http.post(URL+'/reader/login',reader);
  }

  searchBook(category:string,authorName:string,price:number,publisher:string){
    let queryParams=new HttpParams().append("category",category)
                                    .append("author",authorName)
                                    .append("price",price)
                                    .append("publisher",publisher);
    return this.http.get(URL+'/books/search',{params:queryParams});
  }

  buyBook(bookId:any,reader:any){
    this.inputRequest.bookId=bookId;
    this.inputRequest.reader=reader;
    return this.http.post(URL+'/books/buy',this.inputRequest);
  }

  allReaderBooks(reader:any){
    return this.http.get(URL+'/readers/'+reader.emailId+'/books');
  }

  readBook(emailId:any,bookId:any){
    return this.http.get(URL+'/readers/'+emailId+'/books/'+bookId,{responseType:"text"});
  }

  findBooksUsingPid(emailId:any,pId:number){
    let params=new HttpParams().append("pid",pId);
    return this.http.post(URL+'/readers/'+emailId+'/book',params);
  }

  constructor(public http:HttpClient) { }

}
