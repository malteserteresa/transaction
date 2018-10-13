package com.n26;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.n26.model.Statistics;
import com.n26.model.Transaction;
import com.n26.repository.State;

public class TestDataFactory {

	public HashMap<String, Transaction> createData() {
		HashMap<String, Transaction> data = new HashMap<>();
		data.put("firstValid", new Transaction(new BigDecimal("12.30"), ""));
		data.put("secondValid", new Transaction(new BigDecimal("34.90"), ""));

		data.put("firstRound", new Transaction(new BigDecimal("12.355"), ""));
		data.put("secondRound", new Transaction(new BigDecimal("12.354"), ""));

		data.put("nullAmount", new Transaction(null, ""));
		data.put("invalidAmount", new Transaction(new BigDecimal("!!!"), ""));
		data.put("emptyAmount", new Transaction(new BigDecimal(""), ""));
		return data;
	}

	public ArrayList<Transaction> validTransactions(HashMap<String, Transaction> t) {
		ArrayList<Transaction> transactions = new ArrayList<>();
		Set<String> keys = t.keySet();

		for (String key : keys) {
			if (key.contains("Valid")) {
				transactions.add(t.get(key));
			}
		}

		return transactions;
	}

	public State createdState() {
		State currentState = new State();
		ArrayList<Transaction> transactions = validTransactions(createData());
		currentState.create(transactions.get(0));
		currentState.create(transactions.get(1));
		return currentState;
	}

	public ArrayList<BigDecimal> retrieve() {
		State currentState = createdState();
		ArrayList<Transaction> transactions = currentState.retrieve();
		ArrayList<BigDecimal> amounts = new Statistics(transactions).getAmounts();
		return amounts;

	}

	public ArrayList<Transaction> roundingTransactions(HashMap<String, Transaction> t) {
		ArrayList<Transaction> transactions = new ArrayList<>();
		Set<String> keys = t.keySet();

		for (String key : keys) {
			if (key.contains("Round")) {
				transactions.add(t.get(key));
			}
		}

		return transactions;
	}

}
