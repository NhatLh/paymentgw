/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.websocket;

import com.digitechlabs.paymentgw.crypter.Decrypter;
import com.digitechlabs.paymentgw.crypter.TransactionEventPayload;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 *
 * @author FOCUS
 */
public class Notify {

    private final Logger logger = Logger.getLogger(Notify.class);

    public void sendMessageToSocket(String email, int userId, String address, float balance) throws URISyntaxException, InterruptedException {
        logger.info("Prepare send socket :" + userId);
        String token = genToken(email, userId);
        WebSocketClient client = new WebSocketClient(new URI("wss://api.travala.com/ws/wallet/?token=" + token)) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                logger.debug("Open...!");
            }

            @Override
            public void onMessage(String s) {
                logger.debug("Message ok...!");
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                logger.debug("Close...!");
            }

            @Override
            public void onError(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }

        };
        client.connectBlocking();
        String payload = new TransactionEventPayload("wallet.update", address, balance).toJson();
        logger.info("Prepare send:" + payload);
        client.send(payload);
        client.close();

        logger.info("send:" + payload + " success");
    }

    private String genToken(String email, int userId) {
        String token = Decrypter.getInstance().encodeToken(email, userId);

        return token;
    }

}
