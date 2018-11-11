package com.bank.controller.strategy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class InPast implements IStrategy {

	@Override
	public ResponseEntity<Object> requestHandler() {
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
