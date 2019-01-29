/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.rabbitqueue;

import com.digitechlabs.paymentgw.configs.ConfigLoader;
import com.digitechlabs.paymentgw.main.Main;
import com.geneea.celery.Celery;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import utils.ProcessThread;

/**
 *
 * @author FOCUS
 */
public class RabbitQueueProcess extends ProcessThread {

    private final Celery client;
    private final String queueName;
    private final String routingKey;
    private final String brokerURI;

    public RabbitQueueProcess(String threadName) {
        super(threadName);

        this.queueName = ConfigLoader.getInstance().getQueueName();
        this.routingKey = ConfigLoader.getInstance().getRoutingKey();
        this.brokerURI = ConfigLoader.getInstance().getBrokerURI();

        client = Celery.builder().brokerUri(brokerURI)
                .queue(this.queueName)
                .build();
    }

    @Override
    protected void process() {
        Object o = Main.getInstance().getNotifyQueue().dequeue();

        if (o != null) {
            String orderID = o.toString();
//            logger.info("dequeue:" + orderID);
            submit(orderID);
        }
    }

    private void submit(String orderID) {
        long start = System.currentTimeMillis();
        try {
//            logger.info("Enqueue orderid:" + orderID);
            client.submit(this.routingKey, new Object[]{ConfigLoader.getInstance().getServiceKey(), orderID}).get();
            logger.info("Enqueue orderid:" + orderID + " success in " + (System.currentTimeMillis() - start) + " ms");
        } catch (IOException | InterruptedException | ExecutionException ex) {
            logger.info("Enqueue orderid:" + orderID + " fail-->" + ex.getMessage(), ex);
        }
    }
}
