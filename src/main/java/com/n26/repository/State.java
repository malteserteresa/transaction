package com.n26.repository;

import java.util.ArrayList;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.n26.model.Transaction;

@Service
@Primary
@Component
public class State implements IState {

	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

	@Override
	public boolean create(Transaction transcation) {
		return transactions.add(transcation);
	}

	@Override
	public void delete() {
		transactions.clear();
	}

	@Override
	public ArrayList<Transaction> retrieve() {
		return transactions;
	}

}
