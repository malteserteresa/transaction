package com.bank;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.bank.model.Transaction;
import com.bank.repository.State;

public class TestDataFactory {

	// Creates a list of amounts to be used to set up the system
	public ArrayList<BigDecimal> incomingAmounts() {
		ArrayList<BigDecimal> data = new ArrayList<>();

		data.add(new BigDecimal("12.30"));
		data.add(new BigDecimal("34.90"));

		data.add(new BigDecimal("12.355"));
		data.add(new BigDecimal("12.354"));

		return data;

	}

	// creates the fake incoming transaction data that is historic
	public Map<String, BigDecimal> historicTransactions() {

		ArrayList<BigDecimal> amounts = incomingAmounts();

		ArrayList<String> timestamps = incomingTimestamps("HISTORIC", amounts.size());

		return IntStream.range(0, timestamps.size()).boxed()
				.collect(Collectors.toMap(i -> timestamps.get(i), i -> amounts.get(i)));
	}

	public ArrayList<String> incomingTimestamps(String timePeriod, int n) {
		ArrayList<String> timestamps = null;
		switch (timePeriod) {
		case "HISTORIC":
			timestamps = IntStream.range(1, n + 1).boxed()
					.map(i -> ZonedDateTime.of(2018, 1, i, 0, 0, 0, 1, ZoneOffset.UTC).toString())
					.collect(Collectors.toCollection(ArrayList::new));
			break;
		case "CURRENT":
			timestamps = IntStream.range(1, n + 1).boxed()
					.map(i -> ZonedDateTime.now().minusSeconds(1 * n).withZoneSameInstant(ZoneOffset.UTC).toString())
					.collect(Collectors.toCollection(ArrayList::new));
			break;
		}
		return timestamps;

	}

	// STREAMS
	// intstream.range() needs .boxed()
	// stream.concat
	// Collectors.X or Collectors.toCollection()
	// create
	public Map<String, BigDecimal> recentTransactions() {

		ArrayList<BigDecimal> amounts = incomingAmounts();

		ArrayList<String> timestamps = incomingTimestamps("CURRENT", amounts.size());

		return IntStream.range(0, timestamps.size()).boxed()
				.collect(Collectors.toMap(i -> timestamps.get(i), i -> amounts.get(i)));

	}

	public State createState(HashMap<String, BigDecimal> transactions) {
		State state = new State();

		transactions.keySet().stream().forEach(key -> state.create(new Transaction(transactions.get(key), key)));

		return state;
	}

	public HashMap<String, Object> defaultSummary() {
		HashMap<String, Object> summary = new HashMap<>();
		summary.put("sum", "0.00");
		summary.put("avg", "0.00");
		summary.put("max", "0.00");
		summary.put("min", "0.00");
		summary.put("count", 0);

		return summary;
	}

	public HashMap<String, BigDecimal> targetSummary() {
		HashMap<String, BigDecimal> summary = new HashMap<String, BigDecimal>();
		summary.put("min", new BigDecimal("12.3"));
		summary.put("max", new BigDecimal("34.9"));
		summary.put("avg", new BigDecimal("17.98"));
		summary.put("sum", new BigDecimal("71.91"));
		summary.put("count", new BigDecimal(4L));

		return summary;
	}
}
