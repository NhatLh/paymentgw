package com.digitechlabs.paymentgw.crypter;

public class Crypter {

    public Crypter() {

    }

    public String decrypt(String encrypted) {
        String password = "qeZs2cMdnqA5sVBN";
        AesEncryption aes = new AesEncryption("cbc");
        byte[] dec = aes.decrypt(encrypted, password);

        System.out.println(new String(dec));

        return new String(dec);
    }

    public String encrypt(String encrypted) {
        String password = "qeZs2cMdnqA5sVBN";
        AesEncryption aes = new AesEncryption("cbc");
        byte[] dec = aes.encrypt(encrypted, password);

        return new String(dec);

    }
}
