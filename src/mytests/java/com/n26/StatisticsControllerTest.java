package com.n26;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;

import com.n26.controller.StatisticsController;
import com.n26.repository.State;

public class StatisticsControllerTest extends Helper {

	private final StatisticsController controller = new StatisticsController(
			factory.createState((HashMap<String, BigDecimal>) factory.recentTransactions()));

	@Test
	public void retrieveStatistics_returnsCorrectStatistics() throws Exception {

		assertEqualSummaries(factory.targetSummary().values(), controller.retrieveStatistics().values());

	}

	@Test
	public void retrieveStatistics_noTransactions_returnsDefaultStatistics() throws Exception {
		StatisticsController controller = new StatisticsController(new State());

		assertEqualSummaries(factory.defaultSummary().values(), controller.retrieveStatistics().values());

	}

}
