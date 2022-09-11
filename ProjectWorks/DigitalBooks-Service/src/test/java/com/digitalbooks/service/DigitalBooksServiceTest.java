package com.digitalbooks.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.digitalbooks.entity.Author;
import com.digitalbooks.entity.Book;
import com.digitalbooks.entity.Payment;
import com.digitalbooks.entity.Reader;
import com.digitalbooks.repository.AuthorRepository;
import com.digitalbooks.repository.BookRepository;
import com.digitalbooks.repository.PaymentRepository;
import com.digitalbooks.repository.ReaderRepository;
import com.digitalbooks.utils.Constants;

@ExtendWith(MockitoExtension.class)
class DigitalBooksServiceTest {

	@InjectMocks
	DigitalBooksService bookService=new DigitalBooksService();
	@Mock
	AuthorRepository authorRepo;
	@Mock
	BookRepository bookRepo;
	@Mock
	PaymentRepository payRepo;
	@Mock
	ReaderRepository readerRepo;

	@Test
	void testSearchBooks() {
		List<Book> bookList=new ArrayList<>();
		Book book1=new Book();
		book1.setCategory("Fiction");
		book1.setPrice(BigDecimal.valueOf(200));
		book1.setAuthorName("Jones");
		book1.setPublisher("Penguin & Co.");
		book1.setActive(true);
		bookList.add(book1);
		String category="Fiction";
		String authorName="Chetan";
		BigDecimal price=BigDecimal.valueOf(200);
		String publisher="Simon&Co.";
		Mockito.when(bookRepo.findByCategoryOrAuthorNameOrPriceOrPublisher(category, authorName, price, publisher)).thenReturn(bookList);
		List<Book> booksList=bookService.searchBooks(category,authorName,price,publisher);
		assertEquals(bookList, booksList);
	}

	@Test
	void testBuyBook() {
		Reader reader=new Reader();
		reader.setEmailId("anu@gmail.com");
		reader.setName("Anu");
		reader.setPassword("anu");
		int bookId=2;
		Book book =new Book();
		book.setActive(true);
		book.setAuthorName("Chetan");
		book.setCategory("Fiction");
		book.setId(bookId);
		book.setTitle("Book Lovers");
		Payment createdPayment=new Payment();
		createdPayment.setPaymentId(1);
		createdPayment.setBookId(bookId);
		createdPayment.setBookName(book.getTitle());
		createdPayment.setReaderEmail(reader.getEmailId());
		Optional<Book> bookOpt=Optional.of(book);
		Mockito.when(bookRepo.findById(bookId)).thenReturn(bookOpt);
		Mockito.when(payRepo.save(Mockito.any())).thenReturn(createdPayment);
		int paymentId=bookService.buyBook(bookId, reader);
		assertEquals(createdPayment.getPaymentId(), paymentId);
	}

	@Test
	void testCreateAccount() {
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		Author createdAuthor=new Author("Chetan", "chetan@gmail.com", "chetan");
		createdAuthor.setAuthorId(1);
		Mockito.when(authorRepo.save(author)).thenReturn(createdAuthor);
		assertEquals(createdAuthor, bookService.createAccount(author));
	}

	@Test
	void testLogin() {
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		Mockito.when(authorRepo.findByEmailId(author.getEmailId())).thenReturn(author);
		assertEquals(Constants.USER_EXISTS,bookService.login(author));
	}

	@Test
	void testCreateBook() {
		int authorId=1;
		Book book =new Book();
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		Optional<Author> authOpt=Optional.of(author);
		Mockito.when(authorRepo.findById(authorId)).thenReturn(authOpt);
		Mockito.when(bookRepo.save(book)).thenReturn(book);
		assertEquals(book, bookService.createBook(book, authorId));
	}

	@Test
	void testFindPurchasedBooks() {
 		String emailId="anu@gmail.com";
		List<Book> bookList=new ArrayList<>();
		Book book=new Book();
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(200));
		bookList.add(book);
		List<Payment> payList=new ArrayList<>();
		Payment payment=new Payment();
		payment.setBookId(book.getId());
		payment.setBookName(book.getTitle());
		payment.setPaymentId(1);
		payment.setReaderEmail(emailId);
		payList.add(payment);
		Optional<Book> bookOpt=Optional.of(book);
		Mockito.when(payRepo.findByReaderEmail(emailId)).thenReturn(payList);
		Mockito.when(bookRepo.findById(book.getId())).thenReturn(bookOpt);
		List<Book> booksList=bookService.findPurchasedBooks(emailId);
		assertEquals(bookList, booksList);
	}
	
	@Test
	void testReadBook() {
 		String emailId="anu@gmail.com";
 		int bookId=2;
 		List<Book> bookList=new ArrayList<>();
		Book book=new Book();
		book.setId(bookId);
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		book.setChapter("Love Coding!");
		bookList.add(book);
 		List<Payment> payList=new ArrayList<>();
		Payment payment=new Payment();
		payment.setBookId(book.getId());
		payment.setBookName(book.getTitle());
		payment.setPaymentId(1);
		payment.setReaderEmail(emailId);
		payList.add(payment);
		Optional<Book> bookOpt=Optional.of(book);
		Mockito.when(payRepo.findByReaderEmail(emailId)).thenReturn(payList);
		Mockito.when(bookRepo.findById(book.getId())).thenReturn(bookOpt);		
 		String chapter=bookService.readBook(emailId, bookId);
 		assertEquals(book.getChapter(), chapter);
	}
	
	@Test
	void testFindPurchasedBookByPaymentId() {
 		String emailId="anu@gmail.com";
 		int pid=1;
 		Book book=new Book();
		book.setId(2);
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		book.setChapter("Love Coding!");
 		Payment payment=new Payment();
		payment.setBookId(book.getId());
		payment.setBookName(book.getTitle());
		payment.setPaymentId(1);
		payment.setReaderEmail(emailId);
		Optional<Book> bookOpt=Optional.of(book);
 		Mockito.when(payRepo.findByPaymentIdAndReaderEmail(pid, emailId)).thenReturn(payment);
		Mockito.when(bookRepo.findById(book.getId())).thenReturn(bookOpt);		
 		Book actualBook=bookService.findPurchasedBookByPaymentId(emailId, pid,book);
 		assertEquals(book, actualBook);
	}
	
	@Test
	void testBlockBook() {
		int authorId=1;
		int bookId=2;
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		author.setAuthorId(authorId);
		Book book=new Book();
		book.setId(bookId);
		book.setTitle("Java Coders");
		book.setActive(false);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		book.setChapter("Love Coding!");
		book.setAuthor(author);
		Optional<Book> bookOpt=Optional.of(book);
		Mockito.when(bookRepo.findById(book.getId())).thenReturn(bookOpt);	
		Mockito.when(bookRepo.save(Mockito.any())).thenReturn(book);
		Book updatedBook=bookService.blockBook(authorId, bookId);
		assertEquals(book,updatedBook);
	}
	
	@Test
	void testUnBlockBook() {
		int authorId=1;
		int bookId=2;
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		author.setAuthorId(authorId);
		Book book=new Book();
		book.setId(bookId);
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		book.setChapter("Love Coding!");
		book.setAuthor(author);
		Optional<Book> bookOpt=Optional.of(book);
		Mockito.when(bookRepo.findById(book.getId())).thenReturn(bookOpt);	
		Mockito.when(bookRepo.save(Mockito.any())).thenReturn(book);
		Book updatedBook=bookService.unblockBook(authorId, bookId);
		assertEquals(book,updatedBook);
	}
	
	@Test
	void testCreateReaderAccount() {
		Reader reader=new Reader();
		reader.setEmailId("anu@gmail.com");
		reader.setName("anu");
		reader.setPassword("anu");
		Reader createdReader=reader;
		
		Mockito.when(readerRepo.save(reader)).thenReturn(createdReader);
		assertEquals(createdReader, bookService.createReaderAccount(reader));
	}
	
	@Test
	void testReaderLogin() {
		Reader reader=new Reader();
		reader.setEmailId("anu@gmail.com");
		reader.setName("anu");
		reader.setPassword("anu");
		Reader existingReader=reader;
		Optional<Reader> readerOpt=Optional.of(existingReader);
		Mockito.when(readerRepo.findById(reader.getEmailId())).thenReturn(readerOpt);
		assertEquals(Constants.USER_EXISTS,bookService.readerLogin(reader));
		
	}
	
	@Test
	void testFindAllAuthorBooks() {
		int authorId=1;
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		author.setAuthorId(authorId);
		List<Book> list=new ArrayList<>();
		Book book=new Book();
		book.setId(1);
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		book.setChapter("Love Coding!");
		book.setAuthor(author);
		list.add(book);
		Optional<Author> authorOpt=Optional.of(author);
		Mockito.when(authorRepo.findById(authorId)).thenReturn(authorOpt);
		Mockito.when(bookRepo.findByAuthor(author)).thenReturn(list);
		assertEquals(list,bookService.findAllAuthorBooks(authorId));
	}
	
	@Test
	void testReturnBook() {
		int bookId=2;
		String emailId="anu@gmail.com";
		Book book=new Book();
		book.setId(bookId);
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		book.setChapter("Love Coding!");
 		List<Payment> payList=new ArrayList<>();
		Payment payment=new Payment();
		payment.setBookId(book.getId());
		payment.setBookName(book.getTitle());
		payment.setPaymentId(1);
		payment.setReaderEmail(emailId);
		payList.add(payment);
		Mockito.when(payRepo.findByReaderEmail(emailId)).thenReturn(payList);
		Mockito.doNothing().when(payRepo).deleteById(payment.getPaymentId());
		assertEquals(1,bookService.returnBook(emailId, bookId));
	}
}






