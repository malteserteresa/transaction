package com.bank.model;

import java.math.BigDecimal;
import java.time.format.DateTimeParseException;

import com.bank.util.Time;

public class Transaction {

	public BigDecimal amount;
	public String timestamp;

	public BigDecimal getAmount() {
		// TODO NOT CORRECT BEHAVIOUR BUT WHAT IS?
		return amount != null ? amount : new BigDecimal("0.00");
	}

	public Transaction() {

	}

	public Transaction(BigDecimal amount, String timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public long getNanoseconds() throws DateTimeParseException {
		if (timestamp == null || timestamp.isEmpty()) {
			// TODO NOT CORRECT BEHAVIOUR BUT WHAT IS? - DEFENSIVE CODING
			throw new IllegalArgumentException();
		}
		return Time.convert(timestamp);
	}

}
