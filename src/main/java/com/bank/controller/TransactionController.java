package com.bank.controller;

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

import com.bank.controller.strategy.InFuture;
import com.bank.controller.strategy.InPast;
import com.bank.model.Transaction;
import com.bank.repository.State;
import com.bank.util.Time;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

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

		long timeStamp;
		try {
			timeStamp = transaction.getNanoseconds();
		} catch (DateTimeParseException dtp) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}

		if (timeStamp > Time.now()) {
			InFuture strategy = new InFuture();
			return strategy.requestHandler();
		}

		if (timeStamp >= Time.oneMinuteAgo()) {
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