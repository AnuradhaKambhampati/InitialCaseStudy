package com.digitalbooks.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitalbooks.entity.Author;
import com.digitalbooks.entity.Book;
import com.digitalbooks.entity.Payment;
import com.digitalbooks.entity.Reader;
import com.digitalbooks.repository.AuthorRepository;
import com.digitalbooks.repository.BookRepository;
import com.digitalbooks.repository.PaymentRepository;
import com.digitalbooks.repository.ReaderRepository;
import com.digitalbooks.utils.Constants;

@Service
public class DigitalBooksService {
	
	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	AuthorRepository authorRepo;
	
	@Autowired
	PaymentRepository payRepo;
	
	@Autowired
	ReaderRepository readerRepo;
	
	public List<Book> searchBooks(String category,String authorName, float price, String publisher){
		List<Book> books=new ArrayList<>();
		books=bookRepo.findByCategoryOrAuthorNameOrPriceOrPublisher(category, authorName, price, publisher);
		return books;
	}

	public int buyBook(int bookId, Reader reader) {
		//List<Book> readerBookList=new ArrayList<>();
		Payment payment=new Payment();
		Book book=new Book();
		Optional<Book> bookOpt=bookRepo.findById(bookId);
		if(bookOpt.isPresent()) {
			book=bookOpt.get();
		}
		//Book exists and payment success
		if(book!=null) {
			payment=calculatePayment(book,reader);
		}
		
//		if(book!=null) {
//			readerBookList.add(book);
//			reader.setReaderBooks(readerBookList);
//			readerRepo.save(reader);
//		}
		return payment.getPaymentId();
	}
	
	public List<Book> findPurchasedBooks(String emailId) {
		List<Book> bookList=new ArrayList<>();
		List<Payment> payList=payRepo.findByReaderEmail(emailId);
		if(payList!=null) {
			for(Payment pay:payList) {
				int bookId=pay.getBookId();
				Optional<Book> bookOpt=bookRepo.findById(bookId);
				if(bookOpt.isPresent()) {
					bookList.add(bookOpt.get());
				}
			}
		}
		return bookList;
	}
	
	public String readBook(String emailId, int bookId) {
		Book book=new Book();
		List<Payment> payList=payRepo.findByReaderEmail(emailId);
		if(payList!=null) {
			for(Payment pay:payList) {
				if(pay.getBookId()==bookId) {
					Optional<Book> bookOpt=bookRepo.findById(bookId);
					if(bookOpt.isPresent()) {
						book=bookOpt.get();
					}
				}
			}
		}
		return book.getChapter();
	}
	
	public Book findPurchasedBookByPaymentId(String emailId,int pid) {
		Book book=null;
		Payment pay=payRepo.findByPaymentIdAndReaderEmail(pid, emailId);
		int bookId=pay.getBookId();
		Optional<Book> bookOpt=bookRepo.findById(bookId);
		if(bookOpt.isPresent()) {
			book=bookOpt.get();
		}
		return book;
	}
	
	public Author createAccount(Author author) {
		return authorRepo.save(author);
		
	}
	
	public String login(Author author) {
		String status=Constants.USER_DOES_NOT_EXIST;
		Author existingAuthor=authorRepo.findByEmailId(author.getEmailId());
		if(existingAuthor!=null) {
			if(author.getEmailId().equals(existingAuthor.getEmailId()) && author.getPassword().equals(existingAuthor.getPassword())) {
				{
					status=Constants.USER_EXISTS;
					author.setAuthorId(existingAuthor.getAuthorId());
					author.setName(existingAuthor.getName());
					author.setBooks(existingAuthor.getBooks());
				}
			}
		}
		return status;
	}
	
	public Book createBook(Book book, int authorId) {
		Author author=new Author();
		Optional<Author> authorOpt=authorRepo.findById(authorId);
		if(authorOpt.isPresent()) {
			author=authorOpt.get();
		}
		book.setAuthorName(author.getName());
		book.setAuthor(author);
		return bookRepo.save(book);
	}
	
	public Book blockBook(int authorId,int bookId) {
		Book book=new Book();
		Optional<Book> bookOpt=bookRepo.findById(bookId);
		if(bookOpt.isPresent()) {
			book=bookOpt.get();
		}
		if(book!=null && book.getAuthor().getAuthorId()==authorId) {
			book.setActive(false);
		}
		return bookRepo.save(book);
	}
	
	public Book unblockBook(int authorId,int bookId) {
		Book book=new Book();
		Optional<Book> bookOpt=bookRepo.findById(bookId);
		if(bookOpt.isPresent()) {
			book=bookOpt.get();
		}
		if(book!=null && book.getAuthor().getAuthorId()==authorId) {
			book.setActive(true);
		}
		return bookRepo.save(book);
	}
	
	private Payment calculatePayment(Book book,Reader reader) {
		Payment payment=new Payment();
		payment.setBookId(book.getId());
		payment.setBookName(book.getTitle());
		payment.setReaderEmail(reader.getEmailId());
		return payRepo.save(payment);
	}
}