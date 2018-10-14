package com.n26.controller.strategy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class InFuture implements IStrategy {

	@Override
	public ResponseEntity<Object> requestHandler() {

		return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

}
