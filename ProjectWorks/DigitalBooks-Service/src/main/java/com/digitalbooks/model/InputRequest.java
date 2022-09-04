package com.digitalbooks.model;

import com.digitalbooks.entity.Reader;

public class InputRequest {

	private int bookId;
	private Reader reader;
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public Reader getReader() {
		return reader;
	}
	public void setReader(Reader reader) {
		this.reader = reader;
	}
	
	
}
