package com.digitalbooks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.digitalbooks.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	
	public List<Payment> findByReaderEmail(String emailId);
	
	public Payment findByPaymentIdAndReaderEmail(int paymentId, String emailId);

}
