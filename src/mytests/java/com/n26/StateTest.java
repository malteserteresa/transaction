package com.n26;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;

import com.n26.model.Transaction;

public class StateTest extends Helper {

	@Test
	public void discard_sortsKeysCorrectly() {
		state = factory.createState((HashMap<String, BigDecimal>) factory.historicTransactions());

		assertTrue(state.transactions.size() != 0);
		assertTrue(state.discard().isEmpty());
	}

	@Test
	public void create_storesTransactions_withTimeAsKey() {
		int initial = state.retrieve().size();

		state.create(new Transaction(factory.incomingAmounts().get(0), now));

		int after = state.retrieve().size();

		assertEquals(0, initial);
		assertEquals(1, after);

	}

	@Test
	public void create_nullTransaction_throwsException() {
		exception.expect(NullPointerException.class);

		state.create(null);

	}

	@Test
	// TODO what should this behaviour be?
	public void create_emptyTransaction_doesNotStoreTransaction() {
		exception.expect(IllegalArgumentException.class);

		state.create(new Transaction(null, null));

	}

	@Test
	public void retrievPreviousTransactions() {
		int initial = state.retrieve().size();

		state = factory.createState((HashMap<String, BigDecimal>) factory.historicTransactions());

		assertEquals(0, initial);
		assertEquals(factory.historicTransactions().size(), state.retrieve().size());
	}

	@Test
	public void retriev_noTransactions_returnsNothing() {
		int initial = state.retrieve().size();

		assertEquals(0, initial);
		assertEquals(0, state.retrieve().size());
		assertTrue(state.retrieve().isEmpty());
	}

}
