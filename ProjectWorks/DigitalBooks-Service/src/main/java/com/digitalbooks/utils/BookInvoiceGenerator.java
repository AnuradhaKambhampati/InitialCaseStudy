package com.digitalbooks.utils;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class BookInvoiceGenerator {
	PDDocument pdf=new PDDocument();
	String invoiceTitle="Digital Books Invoice!";
	BookInvoiceGenerator() {
		PDPage newPage=new PDPage();
		pdf.addPage(newPage);
	}
	
	public void writeInvoice() throws IOException {
		PDPage myPage=pdf.getPage(0);
		PDPageContentStream cs=new PDPageContentStream(pdf, myPage);
		cs.beginText();
		cs.setFont(PDType1Font.TIMES_ROMAN, 20);
		cs.newLineAtOffset(200, 750);
		cs.showText(invoiceTitle);
		cs.endText();
		
		//Reader Details
		cs.beginText();
		cs.setFont(PDType1Font.TIMES_ROMAN, 14);
		cs.setLeading(20f);;
		cs.newLineAtOffset(60, 610);
		cs.showText("Reader Name: ");
		cs.newLine();
		cs.showText("Email Id: ");
		cs.endText();
		
		cs.beginText();
		cs.setFont(PDType1Font.TIMES_ROMAN, 14);
		cs.setLeading(20f);;
		cs.newLineAtOffset(170, 610);
		cs.showText("Anu");
		cs.newLine();
		cs.showText("anu@gmail.com");
		cs.endText();
		
		//Pricing
		cs.beginText();
		cs.setFont(PDType1Font.TIMES_ROMAN, 14);
		cs.newLineAtOffset(60, 540);
		cs.showText("Book Name");
		cs.endText();
		
		cs.beginText();
		cs.setFont(PDType1Font.TIMES_ROMAN, 14);
		cs.newLineAtOffset(180, 540);
		cs.showText("Unit Price");
		cs.endText();
		
		cs.beginText();
		cs.setFont(PDType1Font.TIMES_ROMAN, 14);
		cs.newLineAtOffset(310, 540);
		cs.showText("Quantity");
		cs.endText();
		
		cs.beginText();
		cs.setFont(PDType1Font.TIMES_ROMAN, 14);
		cs.newLineAtOffset(410, 540);
		cs.showText("Total Price");
		cs.endText();
		
		
		cs.close();
		pdf.save("C:\\Users\\cogjava3168\\Documents\\Invoice.pdf");
		
	}
	
	public static void main(String[] args) {
		BookInvoiceGenerator invoice=new BookInvoiceGenerator();
		try {
			invoice.writeInvoice();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
}
