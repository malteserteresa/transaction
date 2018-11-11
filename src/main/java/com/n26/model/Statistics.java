package com.n26.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.n26.util.Time;

public class Statistics {

	private final HashMap<Long, BigDecimal> transactions;
	private final ArrayList<BigDecimal> amounts;

	public Statistics(HashMap<Long, BigDecimal> transactions) {
		this.transactions = transactions;
		this.amounts = getAmounts();
	}

	public ArrayList<BigDecimal> getAmounts() {
		HashMap<Long, BigDecimal> filteredTransactions = new HashMap<>();

		Set<Long> times = transactions.keySet();

		for (Long time : times) {
			if (time >= Time.oneMinuteAgo()) {
				filteredTransactions.put(time, transactions.get(time));
			}
		}

		ArrayList<BigDecimal> amounts = new ArrayList<BigDecimal>();
		Set<Long> filteredTimes = filteredTransactions.keySet();

		for (Long fTime : filteredTimes) {
			amounts.add(round(filteredTransactions.get(fTime)));

		}
		return amounts;
	}

	public BigDecimal getMax() {
		return amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::max);
	}

	public BigDecimal getMin() {
		Iterator<BigDecimal> it = amounts.iterator();
		return amounts.stream().reduce(it.next(), BigDecimal::min);
	}

	public BigDecimal getSum() {
		return amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getMean() {
		if (amounts.size() != 0) {
			return getSum().divide(new BigDecimal(amounts.size()), 2, RoundingMode.HALF_UP);
		} else {
			return null;
		}

	}

	public HashMap<String, Object> getSummary() {
		if (getCount() == 0) {
			return defaultSummary();
		}
		HashMap<String, Object> summary = new HashMap<>();
		summary.put("sum", getSum().toString());
		summary.put("avg", getMean().toString());
		summary.put("max", getMax().toString());
		summary.put("min", getMin().toString());
		summary.put("count", getCount());

		return summary;

	}

	public long getCount() {
		return amounts.size();
	}

	private HashMap<String, Object> defaultSummary() {
		HashMap<String, Object> summary = new HashMap<>();
		summary.put("sum", "0.00");
		summary.put("avg", "0.00");
		summary.put("max", "0.00");
		summary.put("min", "0.00");
		summary.put("count", 0);

		return summary;
	}

	// TODO FIX THIS
	private BigDecimal round(BigDecimal amount) {
		amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		return amount;
	}
}
