package com.n26;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.n26.controller.TransactionController;
import com.n26.model.Transaction;
import com.n26.repository.State;

public class TransactionsControllerTest {

	TransactionController controller = new TransactionController(new State());

	BigDecimal amount = new BigDecimal("1.00");

	@Test
	public void createTransaction_returnsSuccesstStatus() throws Exception {
		String now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toString();

		ResponseEntity<Object> response = create(new Transaction(amount, now));

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void createTransaction_longerThan60Seconds_returnsCorrectStatus() throws Exception {

		ResponseEntity<Object> response = create(new Transaction(amount, "2018-10-12T20:50:51.312Z"));

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void createTransaction_nullRequest_returns422Status() throws Exception {
		ResponseEntity<Object> response = create(new Transaction(null, null));

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	public void createTransaction_invaidRequest_returns422Status() throws Exception {
		ResponseEntity<Object> response = create(new Transaction(amount, "2018-10-12T20:50:51.!!"));

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	public void createTransaction_futureRequest_returns422Status() throws Exception {
		ResponseEntity<Object> response = create(new Transaction(amount, "2019-10-12T20:50:51.312Z"));

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	private ResponseEntity<Object> create(Transaction transaction) {

		return controller.createTransaction(transaction);
	}

}
