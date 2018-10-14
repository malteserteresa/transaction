package com.n26.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public class Transaction {

	public final BigDecimal amount;
	public final String timestamp;

	public Transaction(BigDecimal amount, String timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;

	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public long getEpochMilli() throws DateTimeParseException {
		if (getTimestamp() != null) {
			return Instant.parse(getTimestamp()).toEpochMilli();
		}
		return 0;
	}

}
