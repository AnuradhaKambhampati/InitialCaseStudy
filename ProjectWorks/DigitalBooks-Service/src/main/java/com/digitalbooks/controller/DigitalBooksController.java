package com.digitalbooks.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitalbooks.entity.Author;
import com.digitalbooks.entity.Book;
import com.digitalbooks.entity.Reader;
import com.digitalbooks.model.InputRequest;
import com.digitalbooks.repository.AuthorRepository;
import com.digitalbooks.repository.BookRepository;
import com.digitalbooks.repository.PaymentRepository;
import com.digitalbooks.service.DigitalBooksService;
import com.digitalbooks.utils.Constants;

@CrossOrigin
@RestController
@RequestMapping("/digitalbooks")
public class DigitalBooksController {
	
	@Autowired
	DigitalBooksService bookService;
	@Autowired
	AuthorRepository authorRepo;
	@Autowired
	BookRepository bookRepo;
	@Autowired
	PaymentRepository payRepo;
	
	@GetMapping("/books/search")
	public List<Book> searchBooks(@RequestParam("category") String category, @RequestParam("author") String authorName,
			@RequestParam("price") float price, @RequestParam("publisher") String publisher){
		List<Book> booksList=new ArrayList<>();
		booksList= bookService.searchBooks(category,authorName,price,publisher);
		return booksList;
	}
	
	@PostMapping("/books/buy")
	public int buyBook(@RequestBody InputRequest request) {
		int bookId=request.getBookId();
		Reader reader=request.getReader();
		int paymentId=bookService.buyBook(bookId, reader);
		return paymentId;
	}
	
	@GetMapping("/readers/{emailId}/books")
	public List<Book> findPurchasedBooks(@PathVariable String emailId) {
		List<Book> purchasedBooks=new ArrayList<>();
		purchasedBooks=bookService.findPurchasedBooks(emailId);
		return purchasedBooks;
	}
	
	@GetMapping("/readers/{emailId}/books/{bookId}")
	public ResponseEntity readBook(@PathVariable String emailId, @PathVariable int bookId) {
		ResponseEntity response=null;
		String chapters="";
		if(!bookRepo.existsById(bookId)) {
			return new ResponseEntity<>(Constants.BOOK_DOES_NOT_EXIST,HttpStatus.BAD_REQUEST);
		}else {
			chapters=bookService.readBook(emailId, bookId);
			response=new ResponseEntity<>(chapters,HttpStatus.OK);
		}
		return response;
	}
	
	@PostMapping("/readers/{emailId}/book")
	public ResponseEntity findPurchasedBookByPaymentId(@PathVariable String emailId, @RequestParam String pid) {
		ResponseEntity response=null;
		Book purchasedBook=null;
		int paymentId=Integer.parseInt(pid);
		if(!payRepo.existsById(paymentId)) {
			return new ResponseEntity<>(Constants.PAYMENT_ID_DOES_NOT_EXIST,HttpStatus.BAD_REQUEST);
		}else {
			purchasedBook=bookService.findPurchasedBookByPaymentId(emailId, paymentId);
			response=new ResponseEntity<>(purchasedBook,HttpStatus.OK);
		}
		return response;
	}
	
	@PostMapping("/author/signup")
	public ResponseEntity createAccount(@RequestBody Author author) {
		Author registeredAuthor=null;
		ResponseEntity<Author> response=null;
		Author existingAuthor=authorRepo.findByEmailId(author.getEmailId());
		if(existingAuthor!=null && author.getEmailId().equals(existingAuthor.getEmailId())) {
			return new ResponseEntity<>(Constants.USER_EXISTS,HttpStatus.BAD_REQUEST);
		}else {
			registeredAuthor=bookService.createAccount(author);
			response=new ResponseEntity<>(registeredAuthor,HttpStatus.CREATED);
		}
		return response;
	}
	
	@PostMapping(path="/author/login",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity login(@RequestBody Author author) {
		ResponseEntity<Author> response=null;
		String status=bookService.login(author);
		if(status.equals(Constants.USER_EXISTS)) {
			//response= new ResponseEntity<>(Constants.LOGIN_SUCCESS, HttpStatus.OK);
			response= new ResponseEntity<>(author, HttpStatus.OK);
		}else {
			//return new ResponseEntity<>(status,HttpStatus.NOT_FOUND);
			return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@PostMapping("/author/{authorId}/books")
	public ResponseEntity<Book> createBook(@RequestBody Book book,@PathVariable int authorId) {
		ResponseEntity<Book> response=null;
		Book createdBook=bookService.createBook(book,authorId);
		response= new ResponseEntity<Book>(createdBook,HttpStatus.CREATED);
		return response;
		
	}
	
	@PutMapping("/author/{authorId}/books/{bookId}/option/{option}")
	public Book updateBook(@PathVariable int authorId,@PathVariable int bookId, @PathVariable int option) {
		Book book =null;
		switch(option) {
			case Constants.BLOCK_BOOK  : book= bookService.blockBook(authorId,bookId);
										 break;
			case Constants.UNBLOCK_BOOK: book= bookService.unblockBook(authorId, bookId);
										 break;
		}
		return book;
	}
}


