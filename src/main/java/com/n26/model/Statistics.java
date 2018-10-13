package com.n26.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

public class Statistics {

	ArrayList<Transaction> transactions;
	private ArrayList<BigDecimal> amounts;

	public Statistics(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
		this.amounts = getAmounts();
	}

	public ArrayList<BigDecimal> getAmounts() {
		ArrayList<BigDecimal> amounts = new ArrayList<>();

		for (Transaction t : transactions) {
			amounts.add(round(t.getAmount()));
		}
		return amounts;
	}

	public BigDecimal getMax() {
		return amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::max);
	}

	public BigDecimal getMin() {
		return amounts.stream().reduce(amounts.get(0), BigDecimal::min);
	}

	public BigDecimal getSum() {
		BigDecimal sum = amounts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		return sum;
	}

	public BigDecimal getMean() {
		if (amounts.size() != 0) {
			return getSum().divide(new BigDecimal(amounts.size()), 2, RoundingMode.HALF_UP);
		} else {
			return null;
		}

	}

	public HashMap<String, String> getSummary() {
		if (getSum() != null && getMean() != null && getMax() != null && getMin() != null) {

			HashMap<String, String> summary = new HashMap<>();
			summary.put("sum", getSum().toString());
			summary.put("avg", getMean().toString());
			summary.put("max", getMax().toString());
			summary.put("min", getMin().toString());
			summary.put("count", Long.toString(getCount()));

			return summary;
		} else {
			HashMap<String, String> summary = new HashMap<>();
			summary.put("sum", "0");
			summary.put("avg", "0");
			summary.put("max", null);
			summary.put("min", null);
			summary.put("count", "0");

			return summary;
		}
	}

	public long getCount() {
		return amounts.size();
	}

	// TODO FIX THIS
	private BigDecimal round(BigDecimal amount) {
		amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
		return amount;
	}
}
