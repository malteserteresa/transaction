package com.n26.repository;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.n26.model.Transaction;

@Service
@Primary
@Component
public class State implements IState {

	public HashMap<Long, ArrayList<BigDecimal>> transactions = new HashMap<>();

	@Override
	public void create(Transaction transaction) {
		ArrayList<BigDecimal> amounts = new ArrayList<>();
		long key = transaction.getEpochMilli();
		BigDecimal amount = transaction.getAmount();

		if (transactions.get(key) == null) {
			amounts.add(amount);

			transactions.put(transaction.getEpochMilli(), amounts);
		} else {
			ArrayList<BigDecimal> value = transactions.get(key);
			value.add(amount);
		}

	}

	@Override
	public void delete() {
		transactions.clear();
	}

	@Override
	public HashMap<Long, ArrayList<BigDecimal>> retrieve() {
		return transactions;
	}

	public HashMap<Long, ArrayList<BigDecimal>> discard() {
		HashMap<Long, ArrayList<BigDecimal>> out = new HashMap<>();

		long oneMinuteAgo = oneMinuteAgo();

		Set<Long> keys = transactions.keySet();

		for (Long key : keys) {
			if (key >= oneMinuteAgo) {
				out.put(key, transactions.get(key));
			}
		}

		return out;

	}

	private long oneMinuteAgo() {
		long now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toInstant().toEpochMilli();
		long minute = 60 * 1000;
		long oneMinuteAgo = now - minute;
		return oneMinuteAgo;
	}

}
