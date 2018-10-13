package com.n26;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;

import com.n26.controller.StatisticsController;
import com.n26.repository.State;

public class StatisticsControllerTest {

	StatisticsController controller = new StatisticsController(new State());

	BigDecimal amount = new BigDecimal("1.00");

	@Test
	public void retrieveStatistics_returnsCorrectStatistics() throws Exception {

		HashMap<String, String> response = create();

	}

	private HashMap<String, String> create() {

		return controller.retrieveStatistics();
	}

}
