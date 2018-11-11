package com.bank.controller.strategy;

import org.springframework.http.ResponseEntity;

public interface IStrategy {

	ResponseEntity<Object> requestHandler();

}
