package com.example.demo;

import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangzicheng
 * @date 2021/02/09
 */
public class DelayTimeSender {

    private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);

    private final MQProducer producer;

    private List<Listener> listeners;

    public DelayTimeSender(MQProducer producer) {
        if (null == producer) {
            throw new NullPointerException();
        }
        this.producer = producer;
    }

    public void sendDelayMessage(Message message, long delay, TimeUnit unit) {
        if (null == message) {
            throw new NullPointerException();
        }
        scheduler.schedule(() -> {
            try {
                SendResult result = producer.send(message);
                if (null != this.listeners) {
                    for (Listener listener : this.listeners) {
                        listener.listen(result);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delay, unit);
    }

    public void shutdown() throws InterruptedException {
        scheduler.shutdown();
        //等所有一次性任务完成再关闭
        scheduler.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        producer.shutdown();
    }

    public void addListener(Listener listener) {
        if (null == this.listeners) {
            this.listeners = new ArrayList<>();
        }
        this.listeners.add(listener);
    }

    @FunctionalInterface
    public interface Listener {
        void listen(SendResult result);
    }
}
