package com.example.demo;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * @author zhangzicheng
 * @date 2021/02/08
 */
public class ProducerAndQueueSelector {
    public static void main(String[] args) throws Exception {
        TransactionMQProducer producer = new TransactionMQProducer("test_group");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        System.out.println("producer started !");

        Message message = new Message("test",
                //tag用于过滤消息
                "ssss",
                "ProducerAndQueueSelector say hello".getBytes());

        SendResult result = producer.send(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                int index = arg.hashCode() % mqs.size();
                return mqs.get(index);
            }
        }, "hello");
        System.out.println(result);
        producer.shutdown();
    }
}
