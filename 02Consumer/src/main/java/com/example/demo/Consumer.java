package com.example.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhangzicheng
 * @date 2021/02/07
 */
public class Consumer {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test_group");
        consumer.setNamesrvAddr("localhost:9876");
        //设置从哪里开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //设置一次性拉32条消息
        consumer.setPullBatchSize(32);
        //默认并发数为1，跟顺序消费没有区别
        consumer.setConsumeMessageBatchMaxSize(32);
        //默认线程数为20
        consumer.setConsumeThreadMin(32);
        consumer.setConsumeThreadMax(32);
        consumer.subscribe("test", MessageSelector.byTag("ssss"));
        //并发消费
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    String topic = msg.getTopic();
                    String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                    String tags = msg.getTags();
                    System.out.println("consumeTime:" + sdf.format(new Date()) + " topic:" + topic + " tags:" + tags + " body:" + body);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.println("consumer started !");
    }
}
