package com.n26;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.n26.controller.TransactionController;
import com.n26.model.Transaction;

public class TransactionsControllerTest extends Helper {

	private final TransactionController controller = new TransactionController(state);

	private final BigDecimal amount = new BigDecimal("1.00");

	@Test
	public void create_returnsSuccessStatus() throws Exception {
		ResponseEntity<Object> response = create(new Transaction(amount, now));

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void create_longerThan60Seconds_returnsCorrectStatus() throws Exception {

		ResponseEntity<Object> response = create(new Transaction(amount, "2018-10-12T20:50:51.312Z"));

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void create_nullRequest_returns422Status() throws Exception {
		ResponseEntity<Object> response = create(new Transaction(null, null));

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	public void create_invaidRequest_returns422Status() throws Exception {
		ResponseEntity<Object> response = create(new Transaction(amount, "2018-10-12T20:50:51.!!"));

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	public void create_futureRequest_returns422Status() throws Exception {
		ResponseEntity<Object> response = create(new Transaction(amount, "2019-10-12T20:50:51.312Z"));

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	public void delete_clearsTransactionsFromState() {
		int initial = numberOfTransactionsStored();

		create(new Transaction(amount, now));

		int afterATransaction = numberOfTransactionsStored();

		controller.delete();

		int afterDeletion = numberOfTransactionsStored();

		assertEquals(0, initial);
		assertEquals(1, afterATransaction);
		assertEquals(0, afterDeletion);

	}

	private int numberOfTransactionsStored() {
		return state.retrieve().size();
	}

	private ResponseEntity<Object> create(Transaction transaction) {

		return controller.createTransaction(transaction);
	}

}
