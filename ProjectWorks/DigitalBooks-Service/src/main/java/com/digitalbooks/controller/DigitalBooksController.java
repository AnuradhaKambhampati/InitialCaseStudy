package com.digitalbooks.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.digitalbooks.entity.Author;
import com.digitalbooks.entity.Book;
import com.digitalbooks.entity.Reader;
import com.digitalbooks.model.InputRequest;
import com.digitalbooks.repository.AuthorRepository;
import com.digitalbooks.repository.BookRepository;
import com.digitalbooks.repository.PaymentRepository;
import com.digitalbooks.repository.ReaderRepository;
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
	@Autowired
	ReaderRepository readerRepo;
	
	@GetMapping("/books/search")
	public List<Book> searchBooks(@RequestParam("category") String category, @RequestParam("author") String authorName,
			 @RequestParam("price") BigDecimal price, @RequestParam("publisher") String publisher){
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
	
	@GetMapping(path="/readers/{emailId}/books/{bookId}",produces = MediaType.TEXT_PLAIN_VALUE)
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
	public ResponseEntity findPurchasedBookByPaymentId(@PathVariable String emailId, @RequestParam("pid") String pid) {
		ResponseEntity response=null;
		Book purchasedBook=null;
		Integer paymentId=null;
		if(!pid.isEmpty()) {
			paymentId=Integer.parseInt(pid);
		}
		if(paymentId!=null) {
			if(payRepo.findByPaymentIdAndReaderEmail(paymentId, emailId)==null) {
				return new ResponseEntity<>(Constants.PAYMENT_ID_DOES_NOT_EXIST,HttpStatus.BAD_REQUEST);
			}else{
				purchasedBook=bookService.findPurchasedBookByPaymentId(emailId, paymentId,purchasedBook);
				response=new ResponseEntity<>(purchasedBook,HttpStatus.OK);
			}
		}else {
			return new ResponseEntity<>(Constants.PAYMENT_ID_MISSING,HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@PostMapping("/author/signup")
	public ResponseEntity createAccount(@Valid @RequestBody Author author) {
		Author registeredAuthor=null;
		ResponseEntity<Author> response=null;
		Author existingAuthor=authorRepo.findByEmailId(author.getEmailId());
		if(existingAuthor!=null && author.getEmailId().equalsIgnoreCase(existingAuthor.getEmailId())) {
			return new ResponseEntity<>(Constants.USER_EXISTS,HttpStatus.BAD_REQUEST);
		}else {
			registeredAuthor=bookService.createAccount(author);
			response=new ResponseEntity<>(registeredAuthor,HttpStatus.CREATED);
		}
		return response;
	}
	
	@PostMapping(path="/author/login",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity login(@Valid @RequestBody Author author) {
		ResponseEntity<Author> response=null;
		String status=bookService.login(author);
		if(status.equals(Constants.USER_EXISTS)) {
			response= new ResponseEntity<>(author, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
		}
		return response;
	}

	@PostMapping("/author/{authorId}/books")
	public ResponseEntity<Book> createBook(@Valid @RequestBody Book book,@PathVariable int authorId) {
		ResponseEntity<Book> response=null;
		Book createdBook=bookService.createBook(book,authorId);
		response= new ResponseEntity<Book>(createdBook,HttpStatus.CREATED);
		return response;
		
	}
	
	@PutMapping("/author/{authorId}/books/{bookId}")
	public Book updateBook(@PathVariable int authorId,@PathVariable int bookId, @RequestParam("option") String option) {
		Boolean status=Boolean.parseBoolean(option);
		Book book =null;
		if(status==true){
			book= bookService.unblockBook(authorId, bookId);
		}else if(status==false){
			book= bookService.blockBook(authorId, bookId);
		}
		return book;
	}
	
	@GetMapping("/author/{authorId}")
	public List<Book> findAllAuthorBooks(@PathVariable int authorId) {
		List<Book> authorBooks=new ArrayList<>();
		authorBooks=bookService.findAllAuthorBooks(authorId);
		return authorBooks;
	}
	
	//Reader SignUp
	@PostMapping("/reader/signup")
	public ResponseEntity createReaderAccount(@Valid @RequestBody Reader reader) {
		Reader registeredReader=null;
		ResponseEntity<Reader> response=null;
		Optional<Reader> readerOpt=readerRepo.findById(reader.getEmailId());
		Reader existingReader=null;
		if(readerOpt.isPresent()) {
			existingReader=readerOpt.get();
		}
		if(existingReader!=null && reader.getEmailId().equals(existingReader.getEmailId())) {
			return new ResponseEntity<>(Constants.USER_EXISTS,HttpStatus.BAD_REQUEST);
		}else {
			registeredReader=bookService.createReaderAccount(reader);
			response=new ResponseEntity<>(registeredReader,HttpStatus.CREATED);
		}
		return response;
	}
	
	@PostMapping(path="/reader/login",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity readerLogin(@Valid @RequestBody Reader reader) {
		ResponseEntity<Reader> response=null;
		String status=bookService.readerLogin(reader);
		if(status.equals(Constants.USER_EXISTS)) {
			response= new ResponseEntity<>(reader, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	@DeleteMapping("/reader/return/{emailId}/book/{bookId}")
	public ResponseEntity returnBook(@PathVariable String emailId,@PathVariable int bookId ) {
		Integer id=bookService.returnBook(emailId, bookId);
		if(id!=null) {
			return new ResponseEntity<>("Book Successfully returned",HttpStatus.OK);
		}
		return new ResponseEntity<>("Book not returned",HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleException(MethodArgumentNotValidException e) {
		Map<String,String> errorMap=new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error)->{
			String field=((FieldError) error).getField();
			String message=((FieldError) error).getDefaultMessage();
			errorMap.put(field, message);
		});
		return new ResponseEntity<Object>(errorMap,HttpStatus.BAD_REQUEST);
	}
	
	
}


