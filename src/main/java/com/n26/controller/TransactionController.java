package com.n26.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.model.Transaction;
import com.n26.repository.State;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

	State state;

	@Autowired
	public TransactionController(State state) {
		this.state = state;
	}

	private static long ONE_MINUTE = 60 * 1000;
	OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
	Date date = Date.from(utc.toInstant());
	long now = date.getTime();
	long oneMinuteAgo = now - ONE_MINUTE;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createTransaction(@Valid @RequestBody Transaction transaction) {
		Instant fromIso8601 = null;

		try {
			fromIso8601 = Instant.parse(transaction.getTimestamp());
			transaction.getAmount().divide(new BigDecimal(2.0));
			double d = Double.parseDouble(transaction.getAmount().toString());

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		long timeStamp = fromIso8601.toEpochMilli();
		System.out.println(timeStamp);
		System.out.println(now);

		if (timeStamp > now) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		if (timeStamp >= oneMinuteAgo) {
			state.create(transaction);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

	}

	@ExceptionHandler({ HttpMessageNotReadableException.class })
	@ResponseBody
	public ResponseEntity exceptionHandler(Exception exception) {
		return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete() {
		state.delete();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}