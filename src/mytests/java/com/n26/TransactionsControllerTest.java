package com.n26;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.junit.Ignore;
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
	public void createTransaction_longerThan60Seconds_returnsCorrectStatus() throws Exception {

		ResponseEntity<Object> response = create(new Transaction(amount, "2018-10-12T20:50:51.312Z"));

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void createTransaction_nullRequest_returns422Status() throws Exception {
		ResponseEntity<Object> response = create(new Transaction(null, null));

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

//	@Test
//	public void createTransaction_invaidJSON_returnsCorrectStatus() throws Exception {
//		ResponseEntity<Object> response = create(new Transaction(null, null));
//
//		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//	}

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

	@Test
	public void createTransaction_invaidAmount_returns422Status() throws Exception {
		// ResponseEntity<Object> response = create(new Transaction(" hi ",
		// "2018-10-12T20:50:51.312Z"));

		// assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	private ResponseEntity<Object> create(Transaction transaction) {

		return controller.createTransaction(transaction);
	}

	// TODO remove
	@Test
	@Ignore
	public void timeMadnessClarity() {
		String time = "2019-10-12T20:50:51.312Z";
		System.out.println(time);
		OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
		Date date = Date.from(utc.toInstant());
		long now = date.getTime();
		LocalDateTime currentDateTime = LocalDateTime.now();
		// long now = currentDateTime.toInstant(ZoneOffset.UTC).getEpochSecond();
		System.out.println(now);

	}

}
