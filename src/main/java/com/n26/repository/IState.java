package com.n26.repository;

import java.math.BigDecimal;
import java.util.HashMap;

import com.n26.model.Transaction;

public interface IState {

	public void create(Transaction transcation);

	public void delete();

	public HashMap<Long, BigDecimal> retrieve();

}
