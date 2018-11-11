package com.bank.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.bank.model.Transaction;
import com.bank.util.Time;

@Service
@Primary
@Component
public class State implements IState {

	private final long time = Time.oneMinuteAgo();

	public HashMap<Long, BigDecimal> transactions = new HashMap<>();

	@Override
	public void create(Transaction transaction) {

		BigDecimal amount = transaction.getAmount();

		long time = transaction.getNanoseconds();

		if (transactions.get(time) != null) {
			BigDecimal value = transactions.get(time);
			transactions.put(time, value.add(amount));
		} else {
			transactions.put(time, amount);
		}

	}

	@Override
	public void delete() {
		transactions.clear();
	}

	@Override
	public HashMap<Long, BigDecimal> retrieve() {
		return transactions;
	}

	public HashMap<Long, BigDecimal> discard() {
		HashMap<Long, BigDecimal> out = new HashMap<>();

		long oneMinuteAgo = time;

		Set<Long> keys = transactions.keySet();

		for (Long key : keys) {
			if (key >= oneMinuteAgo) {
				out.put(key, transactions.get(key));
			}
		}

		return out;

	}

}
