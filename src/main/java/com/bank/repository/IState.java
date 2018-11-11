package com.bank.repository;

import java.math.BigDecimal;
import java.util.HashMap;

import com.bank.model.Transaction;

public interface IState {

	public void create(Transaction transcation);

	public void delete();

	public HashMap<Long, BigDecimal> retrieve();

}
