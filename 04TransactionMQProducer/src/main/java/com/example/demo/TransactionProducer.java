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

        for (int i = 0; i < 10; i++) {
            Message message = new Message("test",
                    //tag用于过滤消息
                    "ssss",
                    ("TransactionProducer say hello " + i).getBytes());

            SendResult result = producer.sendMessageInTransaction(message, i);
            System.out.println(result);
        }
        Thread.sleep(90000);
        producer.shutdown();
    }
}
