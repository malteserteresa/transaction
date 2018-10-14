package com.n26.controller;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import org.apache.commons.lang3.exception.ExceptionUtils;
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

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.n26.controller.strategy.InFuture;
import com.n26.controller.strategy.InPast;
import com.n26.model.Transaction;
import com.n26.repository.State;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {

	private State state;

	@Autowired
	public TransactionController(State state) {
		this.state = state;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createTransaction(@RequestBody Transaction transaction) {

		long now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toInstant().toEpochMilli();
		long minute = 60 * 1000;
		long oneMinuteAgo = now - minute;

		long timeStamp;
		try {
			timeStamp = transaction.getEpochMilli();
		} catch (DateTimeParseException dtp) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		if (timeStamp > now) {
			InFuture strategy = new InFuture();
			return strategy.requestHandler();
		}

		if (timeStamp >= oneMinuteAgo) {
			state.create(transaction);
			return new ResponseEntity<>(HttpStatus.CREATED);
		}

		InPast strategy = new InPast();
		return strategy.requestHandler();
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<Object> exceptionHandler(HttpMessageNotReadableException hmnr) {
		if (ExceptionUtils.getRootCause(hmnr) instanceof InvalidFormatException) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> delete() {
		state.delete();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}