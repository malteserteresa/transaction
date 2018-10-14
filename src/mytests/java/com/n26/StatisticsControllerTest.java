package com.n26;

import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;

import com.n26.controller.StatisticsController;
import com.n26.repository.State;

public class StatisticsControllerTest {

	private TestDataFactory factory = new TestDataFactory();

	StatisticsController controller = new StatisticsController(factory.createState());

	@Test
	public void retrieveStatistics_returnsCorrectStatistics() throws Exception {

		HashMap<String, Object> response = controller.retrieveStatistics();

		System.out.println(response);

	}

	@Test
	public void retrieveStatistics_noTransactions_returnsDefaultStatistics() throws Exception {
		StatisticsController controller = new StatisticsController(new State());

		HashMap<String, Object> response = controller.retrieveStatistics();

		assertEqualSummaries(factory.defaultSummary().values(), response.values());

	}

	public boolean assertEqualSummaries(Object targetValues, Collection<Object> sourceValues) {
		return (targetValues.equals(sourceValues)) ? true : false;

	}
}
