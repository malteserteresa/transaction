package com.n26.repository;

import java.util.ArrayList;

import com.n26.model.Transaction;

public interface IState {

	public boolean create(Transaction transcation);

	public void delete();

	public ArrayList<Transaction> retrieve();

}
