package com.digitalbooks.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.digitalbooks.entity.Author;
import com.digitalbooks.entity.Book;
import com.digitalbooks.entity.Payment;
import com.digitalbooks.entity.Reader;
import com.digitalbooks.model.InputRequest;
import com.digitalbooks.repository.AuthorRepository;
import com.digitalbooks.repository.BookRepository;
import com.digitalbooks.repository.PaymentRepository;
import com.digitalbooks.repository.ReaderRepository;
import com.digitalbooks.service.DigitalBooksService;
import com.digitalbooks.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(value = DigitalBooksController.class)
class DigitalBooksControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	DigitalBooksService bookService;
	@MockBean
	AuthorRepository authorRepo;
	@MockBean
	BookRepository bookRepo;
	@MockBean
	PaymentRepository payRepo;
	@MockBean
	ReaderRepository readerRepo;

	private static ObjectMapper mapper=new ObjectMapper();

	@Test
	void testSearchBooks() throws Exception {
		String category="Fiction";
		//CATEGORY category=CATEGORY.FICTION;
		String authorName="Chetan";
		String price="150";
		String publisher="Simon&Co.";
		List<Book> bookList=new ArrayList<>();
		Book book=new Book();
		book.setCategory("Fiction");
		//book.setCategory(CATEGORY.FICTION);
		book.setPrice(BigDecimal.valueOf(200));
		book.setAuthorName("Jones");
		book.setPublisher("Penguin & Co.");
		bookList.add(book);
		BigDecimal priceConv=new BigDecimal(price);
		System.out.println(priceConv);
		Mockito.when(bookService.searchBooks(category, authorName, priceConv, publisher)).thenReturn(bookList);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/digitalbooks/books/search")
															.param("category",category)
															.param("author", authorName)
															.param("price", price)
															.param("publisher", publisher);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		System.out.println(result.getResponse().getContentAsString());
		String expected= "[{\"id\":0,\"title\":null,\"price\":200.0,\"category\":\"Fiction\",\"authorName\":\"Jones\",\"publisher\":\"Penguin & Co.\",\"publishedDate\":null,\"logo\":null,\"active\":false,\"chapter\":null}]\r\n" ;
		JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
													
	}
	
	@Test
	void testBuyBook() throws Exception {
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
		InputRequest request=new InputRequest();
		request.setBookId(bookId);
		request.setReader(reader);
		String json=mapper.writeValueAsString(request);
		System.out.println(createdPayment.getPaymentId());
		Mockito.when(bookService.buyBook(Mockito.anyInt(), Mockito.any())).thenReturn(createdPayment.getPaymentId());
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/books/buy")
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
		assertEquals(String.valueOf(createdPayment.getPaymentId()), response.getContentAsString());
	}
	
	@Test
	void testFindPurchasedBooks() throws Exception {
 		String emailId="anu@gmail.com";
		List<Book> bookList=new ArrayList<>();
		Book book=new Book();
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		bookList.add(book);
		Mockito.when(bookService.findPurchasedBooks(Mockito.anyString())).thenReturn(bookList);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/digitalbooks/readers/{emailId}/books",emailId)
															.accept(MediaType.APPLICATION_JSON)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		String expected="[{\"id\":0,\"title\":\"Java Coders\",\"price\":150.0,\"category\":\"Fiction\",\"authorName\":null,\"publisher\":null,\"publishedDate\":null,\"logo\":null,\"active\":true,\"chapter\":null}]";
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
		JSONAssert.assertEquals(expected,result.getResponse().getContentAsString(),false);
									
	}
	
	@Test
	void testReadBookIfBookExists() throws Exception {
 		String emailId="anu@gmail.com";
		Book book=new Book();
		book.setId(1);
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		book.setChapter("Love Coding!");
		Mockito.when(bookRepo.existsById(book.getId())).thenReturn(true);	
		Mockito.when(bookService.readBook(emailId, book.getId())).thenReturn(book.getChapter());
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/digitalbooks/readers/{emailId}/books/{bookId}",emailId,book.getId());
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		String expected="Love Coding!";
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
		assertEquals(expected,result.getResponse().getContentAsString());

	}
	
	@Test
	void testReadBookIfBookDoesNotExist() throws Exception {
 		String emailId="anu@gmail.com";
		Book book=new Book();
		book.setId(1);
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		book.setChapter("Love Coding!");
		Mockito.when(bookRepo.existsById(book.getId())).thenReturn(false);	
		Mockito.when(bookService.readBook(emailId, book.getId())).thenReturn(book.getChapter());
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/digitalbooks/readers/{emailId}/books/{bookId}",emailId,book.getId());
													
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		String expected="Book does not exist";
		assertEquals(HttpStatus.BAD_REQUEST.value(),result.getResponse().getStatus());
		assertEquals(expected,result.getResponse().getContentAsString());

	}
	
	@Test
	void testFindPurchasedBookByPaymentId() throws Exception {
		Book book=new Book();
		book.setTitle("Java Coding");
		String emailId="anu@gmail.com";
 		String pidS="1";
 		int pid=Integer.parseInt(pidS);
 		Payment payment=new Payment();
 		payment.setPaymentId(pid);
 		payment.setReaderEmail(emailId);
 		payment.setBookName(book.getTitle());
		Mockito.when(payRepo.findByPaymentIdAndReaderEmail(pid,emailId)).thenReturn(payment);
		Mockito.when(bookService.findPurchasedBookByPaymentId(emailId,pid,book)).thenReturn(book);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/readers/{emailId}/book",emailId)
															.param("pid", pidS);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
	}

	@Test
	void testFindPurchasedBookByPaymentIdIfIdDoesNotExist() throws Exception {
		Book book=new Book();
		book.setTitle("Java Coding");
		String emailId="anu@gmail.com";
 		String pidS="1";
 		int pid=Integer.parseInt(pidS);
		Mockito.when(payRepo.findByPaymentIdAndReaderEmail(pid,emailId)).thenReturn(null);
		Mockito.when(bookService.findPurchasedBookByPaymentId(emailId,pid,book)).thenReturn(book);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/readers/{emailId}/book",emailId)
															.param("pid", pidS);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(),result.getResponse().getStatus());
	}
	
	@Test
	void testFindPurchasedBookByPaymentIdIsNull() throws Exception {
		Book book=new Book();
		book.setTitle("Java Coding");
		String emailId="anu@gmail.com";
 		String pidS="";
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/readers/{emailId}/book",emailId)
															.param("pid", pidS);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(),result.getResponse().getStatus());
	}
	
	@Test
	void testCreateAccountWhenAuthorAlreadyExists() throws Exception {
		String emailId="anu@gmail.com";
		Author existingAuthor=new Author("Chetan", "chetan@gmail.com", "chetan");
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		Mockito.when(authorRepo.findByEmailId(author.getEmailId())).thenReturn(existingAuthor);
		Mockito.when(bookService.createAccount(Mockito.any())).thenReturn(author);
		String json=mapper.writeValueAsString(author);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/author/signup")
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
				
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(),result.getResponse().getStatus());
		assertEquals(Constants.USER_EXISTS,result.getResponse().getContentAsString());
	}
	
	@Test
	void testCreateAccount() throws Exception {
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		Mockito.when(authorRepo.findByEmailId(author.getEmailId())).thenReturn(null);
		Mockito.when(bookService.createAccount(Mockito.any())).thenReturn(author);
		String json=mapper.writeValueAsString(author);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/author/signup")
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
				
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.CREATED.value(),result.getResponse().getStatus());
	}
	
	@Test
	void testLogin() throws Exception {
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		String json=mapper.writeValueAsString(author);
		Mockito.when(bookService.login(author)).thenReturn(Constants.USER_EXISTS);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/author/login")
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());

	}
	
	@Test
	void testLoginWhenUserDoesNotExist() throws Exception {
		Author author=new Author("Chetan", "chetan@gmail.com", "chetan");
		String json=mapper.writeValueAsString(author);
		Mockito.when(bookService.login(author)).thenReturn(Constants.USER_DOES_NOT_EXIST);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/author/login")
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		assertEquals(HttpStatus.NOT_FOUND.value(),result.getResponse().getStatus());

	}
	
	@Test
	void testCreateBook() throws Exception {
		int authorId=1;
		Book book =new Book();
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		book.setPublisher("Simon");
		book.setChapter("Hello world!");
		Mockito.when(bookService.createBook(book, authorId)).thenReturn(book);
		String json=mapper.writeValueAsString(book);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/author/{authorId}/books",authorId)
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		assertEquals(HttpStatus.CREATED.value(),response.getStatus());
		
	}
	
	@Test
	void testCreateBookWhenFieldsAreUnfilled() throws Exception {
		int authorId=1;
		Book book =new Book();
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		Mockito.when(bookService.createBook(book, authorId)).thenReturn(book);
		String json=mapper.writeValueAsString(book);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/author/{authorId}/books",authorId)
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
		
	}
	
	@Test	
	void testBlockBook() throws Exception {
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
		Mockito.when(bookService.blockBook(authorId, bookId)).thenReturn(book);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.put("/digitalbooks/author/{authorId}/books/{bookId}",authorId,bookId)
															.param("option", "false");;
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getStatus());
		assertEquals(HttpStatus.OK.value(),response.getStatus());
															
	}
	
	@Test	
	void testUnBlockBook() throws Exception {
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
		Mockito.when(bookService.blockBook(authorId, bookId)).thenReturn(book);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.put("/digitalbooks/author/{authorId}/books/{bookId}",authorId,bookId)
															.param("option", "true");
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getStatus());
		assertEquals(HttpStatus.OK.value(),response.getStatus());
															
	}
	
	@Test
	void testCreateReaderAccountWhenReaderAlreadyExists() throws Exception {
		Reader reader=new Reader();
		reader.setEmailId("anu@gmail.com");
		reader.setName("anu");
		reader.setPassword("anu");
		Reader existingReader=reader;
		Optional<Reader> readerOpt=Optional.of(existingReader);
		Mockito.when(readerRepo.findById(reader.getEmailId())).thenReturn(readerOpt);
		Mockito.when(bookService.createReaderAccount(reader)).thenReturn(reader);
		String json=mapper.writeValueAsString(reader);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/reader/signup")
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.BAD_REQUEST.value(),result.getResponse().getStatus());
		assertEquals(Constants.USER_EXISTS,result.getResponse().getContentAsString());
	}
	
	@Test
	void testCreateReaderAccount() throws Exception {
		Reader reader=new Reader();
		reader.setEmailId("anu@gmail.com");
		reader.setName("anu");
		reader.setPassword("anu");
		Optional<Reader> readerOpt=Optional.empty();
		Mockito.when(readerRepo.findById(reader.getEmailId())).thenReturn(readerOpt);
		Mockito.when(bookService.createReaderAccount(reader)).thenReturn(reader);
		String json=mapper.writeValueAsString(reader);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/reader/signup")
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.CREATED.value(),result.getResponse().getStatus());
	}
	
	@Test
	void testReaderLogin() throws Exception {
		Reader reader=new Reader();
		reader.setEmailId("anu@gmail.com");
		reader.setName("anu");
		reader.setPassword("anu");
		String json=mapper.writeValueAsString(reader);
		Mockito.when(bookService.readerLogin(reader)).thenReturn(Constants.USER_EXISTS);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/reader/login")
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.OK.value(),result.getResponse().getStatus());
		
	}

	@Test
	void testReaderLoginWhenDoesNotExist() throws Exception {
		Reader reader=new Reader();
		reader.setEmailId("anu@gmail.com");
		reader.setName("anu");
		reader.setPassword("anu");
		String json=mapper.writeValueAsString(reader);
		Mockito.when(bookService.readerLogin(reader)).thenReturn(Constants.USER_DOES_NOT_EXIST);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/digitalbooks/reader/login")
															.accept(MediaType.APPLICATION_JSON)
															.content(json)
															.contentType(MediaType.APPLICATION_JSON);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		System.out.println(response.getContentAsString());
		assertEquals(HttpStatus.NOT_FOUND.value(),result.getResponse().getStatus());
		
	}
	
	@Test
	void testFindAllAuthorBooks() throws Exception {
		List<Book> bookList=new ArrayList<>();
		int authorId=2;
		Book book =new Book();
		book.setTitle("Java Coders");
		book.setActive(true);
		book.setCategory("Fiction");
		book.setPrice(BigDecimal.valueOf(150));
		bookList.add(book);
		Mockito.when(bookService.findAllAuthorBooks(authorId)).thenReturn(bookList);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.get("/digitalbooks/author/{authorId}",authorId);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		String expected="[{\"id\":0,\"title\":\"Java Coders\",\"price\":150,\"category\":\"Fiction\",\"authorName\":null,\"publisher\":null,\"publishedDate\":null,\"logo\":null,\"active\":true,\"chapter\":null}]";
		assertEquals(HttpStatus.OK.value(),response.getStatus());
		assertEquals(expected,result.getResponse().getContentAsString());

	}
	
	@Test
	void testReturnBook() throws Exception {
		int bookId=2;
		String emailId="anu@gmail.com";
		Mockito.when(bookService.returnBook(emailId, bookId)).thenReturn(1);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.delete("/digitalbooks/reader/return/{emailId}/book/{bookId}",emailId,bookId);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		assertEquals(HttpStatus.OK.value(),response.getStatus());

	}

	@Test
	void testReturnBookWhenReturnIsNotSuccessful() throws Exception {
		int bookId=2;
		String emailId="anu@gmail.com";
		Mockito.when(bookService.returnBook(emailId, bookId)).thenReturn(null);
		RequestBuilder requestBuilder=MockMvcRequestBuilders.delete("/digitalbooks/reader/return/{emailId}/book/{bookId}",emailId,bookId);
		MvcResult result=mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response=result.getResponse();
		assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());

	}
}





