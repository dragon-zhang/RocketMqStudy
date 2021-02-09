package com.example.demo;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.Executors;

/**
 * @author zhangzicheng
 * @date 2021/02/08
 */
public class TransactionProducer {
    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("test_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.setExecutorService(Executors.newFixedThreadPool(2));
        producer.setTransactionListener(new TransactionListenerImpl());
        producer.start();
        System.out.println("producer started !");

        Message message = new Message("test",
                //tag用于过滤消息
                "ssss",
                "TransactionProducer say hello".getBytes());
        message.setDelayTimeLevel(1);

        SendResult result = producer.send(message);
        System.out.println(result);
        producer.shutdown();
    }
}
