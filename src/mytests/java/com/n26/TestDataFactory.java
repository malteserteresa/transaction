package com.n26;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.n26.model.Transaction;
import com.n26.repository.State;

public class TestDataFactory {

	public ArrayList<BigDecimal> dummyTransactions() {
		ArrayList<BigDecimal> data = new ArrayList<>();

		data.add(new BigDecimal("12.30"));
		data.add(new BigDecimal("34.90"));

		data.add(new BigDecimal("12.355"));
		data.add(new BigDecimal("12.354"));
		return data;

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

	public ArrayList<String> createTimestamps(int n) {
		ArrayList<String> timestamps = new ArrayList<>();
		int i = 0;
		while (i < n) {
			i++;
			String ts = ZonedDateTime.of(2018, 1, i, 0, 0, 0, 1, ZoneOffset.UTC).toString();
			timestamps.add(ts);

		}
		return timestamps;
	}

	public HashMap<String, ArrayList<BigDecimal>> incomingData() {

		ArrayList<BigDecimal> transactions = dummyTransactions();
		int size = transactions.size();
		ArrayList<String> timestamps = createTimestamps(size);

		HashMap<String, ArrayList<BigDecimal>> data = new HashMap<>();
		int i = 0;
		while (i < size) {
			ArrayList<BigDecimal> amounts = new ArrayList<>();
			amounts.add(transactions.get(i));

			data.put(timestamps.get(i), amounts);
			i++;
		}
		return data;
	}

	public ArrayList<Long> createEpoch(int n) {
		ArrayList<Long> timestamps = new ArrayList<>();
		int i = 0;
		while (i < n) {
			i++;
			long now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toInstant().toEpochMilli();
			long ago = i * 1000;
			long ts = now - ago;

			timestamps.add(ts);

		}
		return timestamps;
	}

	public HashMap<Long, ArrayList<BigDecimal>> data() {

		ArrayList<BigDecimal> transactions = dummyTransactions();
		int size = transactions.size();
		ArrayList<Long> timestamps = createEpoch(size);

		HashMap<Long, ArrayList<BigDecimal>> data = new HashMap<>();
		int i = 0;
		while (i < size) {
			ArrayList<BigDecimal> amounts = new ArrayList<>();
			amounts.add(transactions.get(i));

			data.put(timestamps.get(i), amounts);
			i++;
		}
		return data;

	}

	public State createState() {
	HashMap<String, ArrayList<BigDecimal>> transactions = incomingData();
	State state = new State();

	Iterator<Entry<String, ArrayList<BigDecimal>>> it = transactions.entrySet().iterator();
	while (it.hasNext()) {
		Entry<String, ArrayList<BigDecimal>> pair = it.next();

		Transaction transaction = new Transaction(pair.getValue().get(0), pair.getKey());
		state.create(transaction);
		
	}
	return state;
	}
}
