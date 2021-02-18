package com.example.demo;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangzicheng
 * @date 2021/02/08
 */
public class TransactionListenerImpl implements TransactionListener {

    private final ConcurrentMap<String, Integer> transactions = new ConcurrentHashMap<>();

    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        int status = count.getAndIncrement() % 3;
        System.out.println("arg->" + arg + " 收到ACK，执行本地事务，status->" + status);
        transactions.put(msg.getTransactionId(), status);
        return getLocalTransactionState(status);
    }

    /**
     * 默认回查15次，第一次6s后回查，以后都是每隔1min后回查
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        System.out.println("有刁民没有报告事务状态，盘他！" + msg);
        Integer status = transactions.get(msg.getTransactionId());
        if (status == null) {
            return LocalTransactionState.UNKNOW;
        }
        return getLocalTransactionState(status);
    }

    private LocalTransactionState getLocalTransactionState(Integer status) {
        switch (status) {
            case 0:
                return LocalTransactionState.COMMIT_MESSAGE;
            case 1:
                return LocalTransactionState.ROLLBACK_MESSAGE;
            case 2:
            default:
                return LocalTransactionState.UNKNOW;
        }
    }
}
