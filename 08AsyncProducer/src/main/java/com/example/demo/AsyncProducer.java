package com.example.demo;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * @author zhangzicheng
 * @date 2021/02/12
 */
public class AsyncProducer {
    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("test_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        System.out.println("async producer started !");

        Message message = new Message("test",
                //tag用于过滤消息
                "ssss",
                "AsyncProducer say hello".getBytes());

        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult result) {
                System.out.println(result);
            }

            @Override
            public void onException(Throwable t) {
                System.out.println(t.toString());
            }
        });
        Thread.sleep(producer.getSendMsgTimeout());
        producer.shutdown();
    }
}
