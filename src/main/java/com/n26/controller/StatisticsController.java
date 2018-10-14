package com.n26.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.n26.model.Statistics;
import com.n26.repository.State;

@RestController
public class StatisticsController {

	State state;

	@Autowired
	public StatisticsController(State state) {
		this.state = state;
	}

	@RequestMapping(value = "/statistics", method = RequestMethod.GET)
	public HashMap<String, Object> retrieveStatistics() {
		Statistics stats = new Statistics(state.retrieve());
		return stats.getSummary();
	}

}