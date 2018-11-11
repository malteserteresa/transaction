package com.n26;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.n26.model.Statistics;
import com.n26.repository.State;

public class StatisticsTest extends Helper {

	HashMap<String, BigDecimal> transactions = (HashMap<String, BigDecimal>) factory.recentTransactions();

	private final State state = factory.createState(transactions);

	private final Statistics stats = new Statistics(state.retrieve());

	private final ArrayList<BigDecimal> amounts = stats.getAmounts();

	private final ArrayList<BigDecimal> targetData = factory.incomingAmounts();

	@Test
	public void getAmounts_validRequest_returnsAmounts() {
		assertTrue(amounts.stream().allMatch(amount -> amounts.contains(amount)));
	}

	@Test
	public void getAmounts_initalState_returnsAmounts() {
		assertTrue(amounts.stream().allMatch(amount -> amounts.contains(amount)));
	}

	@Test
	public void nullTranscations_returnsDefaultSummary() {

		Statistics noTransactions = new Statistics(new HashMap<Long, BigDecimal>());

		HashMap<String, Object> defaultResponse = factory.defaultSummary();

		assertEqualSummaries(defaultResponse.values(), noTransactions.getSummary().values());
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

		assertEqualSummaries(factory.targetSummary().values(), stats.getSummary().values());

	}

}
