package com.distributed.transaction.core.wal.log;

import com.distributed.transaction.api.Participant;
import com.distributed.transaction.api.TransactionInvocation;
import com.distributed.transaction.api.TransactionWriteAheadLog;
import com.distributed.transaction.api.TransactionWriteAheadLogType;
import com.distributed.transaction.common.util.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuyb on 2016/6/2.
 */
public abstract class AbstractAsyncTransactionWriteAheadLogComponent {
    private static Logger logger = LoggerFactory.getLogger(AbstractAsyncTransactionWriteAheadLogComponent.class);

    public void writeAheadLog(Participant participant, TransactionWriteAheadLogType transactionWriteAheadLogType){
        AssertUtil.notNull(participant);
        AssertUtil.notNull(transactionWriteAheadLogType);

        logger.info("writeAheadLog,transactionUUID:{},ipHost:{},port:{},serviceName:{}",participant.getTransactionUUID(),participant.getParticipantIpHost(),participant.getParticipantPort(),participant.getParticipantServiceName());

        TransactionInvocation transactionInvocation = null;
        if(transactionWriteAheadLogType == TransactionWriteAheadLogType.LOG_PARTICIPANT_ENROLL_BEFORE_COMMIT){
            transactionInvocation = participant.getCommitTransactionInvcoation();
        } else {
            transactionInvocation = participant.getRollbackTransactionInvocation();
        }
        TransactionWriteAheadLog transactionWriteAheadLog = new TransactionWriteAheadLog(transactionWriteAheadLogType,participant.getParticipantIpHost(),participant.getParticipantPort(),
                participant.getParticipantServiceName(),transactionInvocation,participant.getTransactionUUID(),System.currentTimeMillis());
        submitAsyncWriteAheadLogTask(transactionWriteAheadLog);
    }
    public abstract void submitAsyncWriteAheadLogTask(TransactionWriteAheadLog transactionWriteAheadLog);


}
