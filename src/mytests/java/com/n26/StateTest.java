package com.n26;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.n26.repository.State;

public class StateTest {

	private TestDataFactory factory = new TestDataFactory();
	private State state = factory.createState();

	@Test
	public void canCreateState() {
		assertNotNull(state);
	}

	@Test
	public void discard_sortsKeysCorrectly() {
		assertTrue(state.transactions.size() != 0);
		assertTrue(state.discard().isEmpty());
	}

}
