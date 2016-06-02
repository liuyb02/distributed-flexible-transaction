package com.distributed.transaction.core.wal.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.distributed.transaction.api.TransactionWriteAheadLog;
import com.distributed.transaction.common.threadpool.CustomAsyncThreadPool;

/**
 * 
 * @author yubing
 *
 */
@Component
public class AsyncMongoTransactionWriteAheadLogComponent extends AbstractAsyncTransactionWriteAheadLogComponent {
	
	private static Logger logger = LoggerFactory.getLogger(AsyncMongoTransactionWriteAheadLogComponent.class);
	
	@Autowired
	private MongoWriteAheadLogger mongoWriteAheadLogger;

	@Override
	public void submitAsyncWriteAheadLogTask(TransactionWriteAheadLog transactionWriteAheadLog) {
		AsyncMongoTransactionWriteAheadLogTask asyncMongoTransactionWriteAheadLogTask = new AsyncMongoTransactionWriteAheadLogTask(mongoWriteAheadLogger,transactionWriteAheadLog);
		CustomAsyncThreadPool.getInstance().submitTask(asyncMongoTransactionWriteAheadLogTask);
	}


}
