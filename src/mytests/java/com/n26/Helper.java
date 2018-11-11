package com.n26;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.n26.repository.State;

public abstract class Helper {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	public final TestDataFactory factory = new TestDataFactory();

	public State state = new State();

	public final String now = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toString();

	public boolean assertEqualSummaries(Object targetValues, Collection<Object> sourceValues) {
		return (targetValues.equals(sourceValues)) ? true : false;

	}

}
