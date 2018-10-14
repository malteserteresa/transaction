package com.n26;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;

import com.n26.model.Statistics;

public class StatisticsTest {

	/*
	 * Change retrieve Change data
	 */

	private TestDataFactory factory = new TestDataFactory();

	private HashMap<Long, ArrayList<BigDecimal>> transactions = factory.data();
	private Statistics stats = new Statistics(transactions);

	private ArrayList<BigDecimal> targetData = factory.dummyTransactions();

	@Test
	public void nullTranscations_returnsDefaultSummary() {
		transactions.clear();
		Statistics noTransactions = new Statistics(transactions);

		HashMap<String, Object> defaultResponse = factory.defaultSummary();

		assertEqualSummaries(defaultResponse.values(), noTransactions.getSummary().values());
	}

	public boolean assertEqualSummaries(Object targetValues, Collection<Object> sourceValues) {
		return (targetValues.equals(sourceValues)) ? true : false;

	}

	@Test
	public void returnsCount() {

		assertTrue(targetData.size() == stats.getCount());
	}

	@Test
	public void returnsMax() {
		assertTrue(targetData.get(1).equals(stats.getMax()));
	}

	@Test
	public void returnsMin() {
		assertTrue(targetData.get(0).equals(stats.getMin()));
	}

	@Test
	public void returnsMean() {
		BigDecimal sum = targetData.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal mean = (sum).divide(new BigDecimal(targetData.size()), 2, RoundingMode.HALF_UP);

		assertTrue(mean.equals(stats.getMean()));
	}

	@Test
	public void returnsSum() {
		BigDecimal sum = targetData.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		sum = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
		assertTrue(sum.equals(stats.getSum()));
	}

	@Test
	public void getSummary() {
		HashMap<String, BigDecimal> targetSummary = new HashMap<String, BigDecimal>();
		targetSummary.put("min", new BigDecimal("12.3"));
		targetSummary.put("max", new BigDecimal("34.9"));
		targetSummary.put("avg", new BigDecimal("17.98"));
		targetSummary.put("sum", new BigDecimal("71.91"));
		targetSummary.put("count", new BigDecimal(4L));

		assertEqualSummaries(targetSummary.values(), stats.getSummary().values());

	}

}
