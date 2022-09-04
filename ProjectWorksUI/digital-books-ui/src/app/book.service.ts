import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

const URL='http://localhost:8082/digitalbooks';
@Injectable({
  providedIn: 'root'
})
export class BookService {
  createAccount(author:any){
    return this.http.post(URL+'/author/signup',author);
  }

  authorLogin(author:any){
    //return this.http.post(URL+'/author/login',author,{responseType:"text"});
    return this.http.post(URL+'/author/login',author);
  }

  createBook(book:any,authorId:any){
    return this.http.post(URL+'/author/'+authorId+'/books',book);
  }
  constructor(public http:HttpClient) { }

}
