/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.rabbitqueue;

/**
 *
 * @author Admin
 */
import com.digitechlabs.paymentgw.configs.ConfigLoader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import utils.BlockQueue;
import utils.ProcessThreadMX;

public class RabbitMQHisWalletSender extends ProcessThreadMX {

    public static RabbitMQHisWalletSender instance;
    private Connection connection;
    private Channel channel;

    public static synchronized RabbitMQHisWalletSender getInstance() {
        if (instance == null) {
            instance = new RabbitMQHisWalletSender("RabbitMQHisWalletSender");
        }

        return instance;
    }

    private BlockQueue queue = new BlockQueue();

    public RabbitMQHisWalletSender(String threadName) {
        super(threadName);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(ConfigLoader.getInstance().getHistoryQueueWalletHost());

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(ConfigLoader.getInstance().getHistoryQueueWalletName(), false, false, false, null);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (TimeoutException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    protected void process() {
        Object o = queue.dequeue();

//        logger.info("queue:" + o);
        if (o != null) {
            String message = o.toString();

            logger.info("enqueue message:" + message);
            try {
                channel.basicPublish("", ConfigLoader.getInstance().getHistoryQueueWalletName(), null, message.getBytes());
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
                resetConn();
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void stop() {

        //close channel and connection
        try {
            if (this.channel != null) {
                this.channel.close();
            }

            if (this.connection != null) {
                this.connection.close();
            }

        } catch (IOException ex) {
            logger.error("IOException:" + ex.getMessage(), ex);
        } catch (TimeoutException ex) {
            logger.error("TimeoutException:" + ex.getMessage(), ex);
        }
    }

    private void resetConn() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(ConfigLoader.getInstance().getHistoryQueueWalletHost());

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(ConfigLoader.getInstance().getHistoryQueueWalletName(), false, false, false, null);
        } catch (IOException ex) {
            logger.error("IOException:" + ex.getMessage(), ex);
        } catch (TimeoutException ex) {
            logger.error("TimeoutException:" + ex.getMessage(), ex);
        }
    }

    public void enqueue(String message) {
        this.queue.enqueue(message);
    }

}
