package com.n26.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Statistics {

	private HashMap<Long, ArrayList<BigDecimal>> transactions;
	private Collection<BigDecimal> amounts;

	public Statistics(HashMap<Long, ArrayList<BigDecimal>> transactions) {
		this.transactions = transactions;
		this.amounts = getAmounts();
	}

	public HashMap<Long, ArrayList<BigDecimal>> filter() {
		return new HashMap<Long, ArrayList<BigDecimal>>();
	}

	public ArrayList<BigDecimal> getAmounts() {
		HashMap<Long, ArrayList<BigDecimal>> out = new HashMap<>();

		long now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toInstant().toEpochMilli();
		long minute = 60 * 1000;
		long oneMinuteAgo = now - minute;

		Set<Long> keys = transactions.keySet();

		for (Long key : keys) {
			if (key >= oneMinuteAgo) {
				out.put(key, transactions.get(key));
			}
		}

		ArrayList<BigDecimal> flattenedAmounts = new ArrayList<BigDecimal>();
		Set<Long> filteredKeys = out.keySet();

		for (Long k : filteredKeys) {
			ArrayList<BigDecimal> values = out.get(k);
			for (BigDecimal value : values) {
				flattenedAmounts.add(round(value));
			}
		}
		return flattenedAmounts;
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
