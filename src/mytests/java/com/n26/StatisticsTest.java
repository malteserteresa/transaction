package com.n26;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.n26.model.Statistics;
import com.n26.model.Transaction;

public class StatisticsTest {

	TestDataFactory factory = new TestDataFactory();
	ArrayList<BigDecimal> targetAmounts = factory.retrieve();
	HashMap<String, Transaction> data;
	Statistics stats;

	@Before
	public void setUp() {
		data = factory.createData();
		stats = new Statistics(factory.validTransactions(data));
	}

	@Test
	public void nullAmount() {
		ArrayList<Transaction> t = new ArrayList<>();
		t.add(data.get("nullAmount"));
		Statistics nullStat = new Statistics(t);

	}

	@Test
	public void roundsCorrectly() {

		Statistics roundedStatistic = new Statistics(factory.roundingTransactions(data));
		// System.out.println(roundedStatistic.getAmounts().get(1));
		assertTrue(roundedStatistic.getAmounts().get(0).equals(new BigDecimal("12.35")));
		assertTrue(roundedStatistic.getAmounts().get(1).equals(new BigDecimal("12.36")));
	}

	@Test
	public void returnsCount() {
		assertTrue(targetAmounts.size() == stats.getCount());
	}

	@Test
	public void returnsMax() {
//		System.out.println(stats.getMax());
//		System.out.println(targetAmounts.get(1));
		assertTrue(targetAmounts.get(0).equals(stats.getMax()));
	}

	@Test
	public void returnsMin() {
		assertTrue(targetAmounts.get(1).equals(stats.getMin()));
	}

	@Test
	public void returnsMean() {
		BigDecimal sum = targetAmounts.get(0).add(targetAmounts.get(1));
		BigDecimal mean = (sum).divide(new BigDecimal(2.0), 2, RoundingMode.HALF_UP);
		assertTrue(mean.equals(stats.getMean()));
	}

	// TODO FIX THISs
	@Test
	public void returnsSum() {
		BigDecimal sum = targetAmounts.get(0).add(targetAmounts.get(1));
		sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
		assertTrue(sum.equals(stats.getSum()));
	}

	@Test
	public void noTransactions() {
		ArrayList<Transaction> transcations = new ArrayList<>();
		Statistics stats = new Statistics(transcations);
		assertNull(stats.getMean());
	}

	// TODO fix this
	@Test
	public void getSummary() {
		HashMap<String, BigDecimal> targetSummary = new HashMap<String, BigDecimal>();
		targetSummary.put("min", new BigDecimal("12.3"));
		targetSummary.put("max", new BigDecimal("34.9"));
		targetSummary.put("avg", new BigDecimal("23.6"));
		targetSummary.put("sum", new BigDecimal("47.2"));
		targetSummary.put("count", new BigDecimal(2L));

		HashMap<String, String> summary = stats.getSummary();
		System.out.println(stats.getSummary().values());
		System.out.println(summary.values());
		assertTrue(summary.values().containsAll(targetSummary.values()));
	}

}
