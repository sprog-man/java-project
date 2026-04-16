package com.petcompany.platform.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 异步服务
 */
@Service
@Slf4j
public class AsyncService {

    /**
     * 异步处理任务
     */
    @Async("taskExecutor")
    public void processTask(String taskName, Long userId) {
        log.info("开始处理任务：{}，用户ID：{}，线程：{}", taskName, userId, Thread.currentThread().getName());
        
        // 模拟耗时操作
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.error("任务处理异常", e);
        }
        
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("任务处理完成：{}，用户ID：{}，完成时间：{}，线程：{}", taskName, userId, time, Thread.currentThread().getName());
    }

    /**
     * 异步发送通知
     */
    @Async("taskExecutor")
    public void sendNotification(Long userId, String message) {
        log.info("开始发送通知给用户：{}，消息：{}，线程：{}", userId, message, Thread.currentThread().getName());
        
        // 模拟发送通知的耗时操作
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("发送通知异常", e);
        }
        
        log.info("通知发送完成，用户：{}，线程：{}", userId, Thread.currentThread().getName());
    }

    /**
     * 异步处理订单
     */
    @Async("taskExecutor")
    public void processOrder(Long orderId, Long userId) {
        log.info("开始处理订单：{}，用户ID：{}，线程：{}", orderId, userId, Thread.currentThread().getName());
        
        // 模拟订单处理的耗时操作
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("订单处理异常", e);
        }
        
        log.info("订单处理完成：{}，用户ID：{}，线程：{}", orderId, userId, Thread.currentThread().getName());
    }

}
