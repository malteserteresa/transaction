package com.n26;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.junit.Test;

import com.n26.model.Transaction;
import com.n26.util.Time;

public class TransactionTest extends Helper {

	private final String time = factory.incomingTimestamps("CURRENT", 9).get(0);

	private final BigDecimal amount = factory.incomingAmounts().get(0);

	private final Transaction transaction = new Transaction(amount, time);

	private final Transaction nullTransaction = new Transaction(null, null);

	@Test
	public void canReturnAmount() {
		assertEquals(amount, transaction.getAmount());

	}

	@Test
	public void nullAmount_setsToNull() {

		assertEquals(new BigDecimal("0.00"), nullTransaction.getAmount());

	}

	@Test
	public void emptyAmount_throwsException() {
		exception.expect(NumberFormatException.class);

		new Transaction(new BigDecimal(""), time);

	}

	@Test
	public void invalidAmount_throwsException() {
		exception.expect(NumberFormatException.class);

		new Transaction(new BigDecimal("123!"), time);

	}

	@Test
	public void canReturnTime() {
		assertEquals(epoch(), transaction.getNanoseconds());

	}

	@Test
	public void nullTime_throwsException() {
		exception.expect(IllegalArgumentException.class);

		assertEquals(time, nullTransaction.getNanoseconds());

	}

	@Test
	public void emptyTime_throwsException() {
		Transaction emptyTransaction = new Transaction(amount, "");

		exception.expect(IllegalArgumentException.class);

		emptyTransaction.getNanoseconds();

	}

	@Test
	public void invalidTime_throwsException() {
		Transaction invalid = new Transaction(amount, "Hello");

		exception.expect(DateTimeParseException.class);

		invalid.getNanoseconds();
	}

	private final long epoch() {
		long epoch = Instant.parse(time).getEpochSecond();
		epoch *= Time.nano;
		epoch += Instant.parse(time).getNano();
		return epoch;
	}

}
