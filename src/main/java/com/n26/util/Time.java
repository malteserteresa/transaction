package com.n26.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public enum Time {

	;

	public static long nano = 1000000000l;

	public static final long now() {
		long time = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toInstant().getEpochSecond();
		time *= nano;
		time += ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toInstant().getNano();
		return time;
	}

	public static long oneMinuteAgo() {
		long minute = 60 * nano;
		long oneMinuteAgo = now() - minute;
		return oneMinuteAgo;
	}

	public static long convert(String timestamp) {
		long time = Instant.parse(timestamp).getEpochSecond();
		time *= 1000000000l; // convert to nanoseconds
		time += Instant.parse(timestamp).getNano();
		return time;
	}
}
