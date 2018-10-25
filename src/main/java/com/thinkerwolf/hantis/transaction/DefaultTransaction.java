package com.thinkerwolf.hantis.transaction;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.thinkerwolf.hantis.transaction.AbstractTransactionManager.SuspendResourceHolder;

/**
 * 默认的Transaction
 * 
 * @author wukai
 *
 */
public class DefaultTransaction implements Transaction {

	private Transaction transaction;

	private SuspendResourceHolder suspendResourceHolder;

	private AtomicBoolean completion = new AtomicBoolean(false);

	public DefaultTransaction(Transaction transaction, SuspendResourceHolder suspendResourceHolder) {
		this.transaction = transaction;
		this.suspendResourceHolder = suspendResourceHolder;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public SuspendResourceHolder getSuspendResourceHolder() {
		return suspendResourceHolder;
	}

	public void setSuspendResourceHolder(SuspendResourceHolder suspendResourceHolder) {
		this.suspendResourceHolder = suspendResourceHolder;
	}

	@Override
	public void commit() throws SQLException {

	}

	@Override
	public void rollback() throws SQLException {

	}

	@Override
	public void close() throws SQLException {

	}

	@Override
	public boolean isComplete() {
		return completion.get();
	}

	@Override
	public void setComplete() {
		completion.set(true);
	}

}
