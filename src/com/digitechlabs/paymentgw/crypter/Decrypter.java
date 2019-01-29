package com.digitechlabs.paymentgw.crypter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.net.URISyntaxException;
import java.util.Date;

public class Decrypter {

    private static final String SECRET = "lrt#u77t!#h+wnlc^uwx=^2d6yr4-4yams(=pfk4px0ex#^$-r";
    private static final String key = "qeZs2cMdnqA5sVBN";
    private static final String initVector = "encryptionIntVec";

    private static Decrypter instance;

    public static Decrypter getInstance() {
        if (instance == null) {
            instance = new Decrypter();
        }

        return instance;
    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException {

//        int a = decryptToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImxoLm5oYXRia0BnbWFpbC5jb20iLCJ1c2VybmFtZSI6ImxoLm5oYXRia0BnbWFpbC5jb20iLCJleHAiOjE1NDY0NTE5MDUsInVzZXJfaWQiOjMyN30.38ou7H2FZL51S03cE2tUrazNXh_E8PJ5X8ojBVgd4jE");
//        System.out.println(a);
//        String b = decodeToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImxoLm5oYXRia0BnbWFpbC5jb20iLCJ1c2VybmFtZSI6ImxoLm5oYXRia0BnbWFpbC5jb20iLCJleHAiOjE1NDY0NDgyMzMsInVzZXJfaWQiOjMyN30.thpB0VHXf7WRVHGHz5H6Xh8uJM-ggczlNmKjTXzA2_0");
//        System.out.println(b);
//        encodeToken("hoapv@gemvietnam.com", 332);
    }

    public static void decrypt(String encrypted) {
        String password = "C";
        AesEncryption aes = new AesEncryption("cbc");
        byte[] dec = aes.decrypt(encrypted, password);

        System.out.println(new String(dec));

    }

    public static void encrypt(String data) {
        String password = "C";
        AesEncryption aes = new AesEncryption("cbc");
        byte[] dec = aes.encrypt(data, password);

        System.out.println(new String(dec));

    }

    public String decryptToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance

            DecodedJWT d = verifier.verify(token);
//            System.out.println(d.getPayload());

//            return true;
            return d.getClaim("user_id").asInt() + "";
        } catch (Exception ex) {
            return null;
        }
    }

    private static String decodeToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        System.out.println(jwt.getPayload());

        return jwt.getPayload();
    }

    public String encodeToken(String email, int userID) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String token = JWT.create()
                .withClaim("email", email)
                .withClaim("username", email)
                .withClaim("user_id", userID)
                .withExpiresAt(new Date(System.currentTimeMillis()))
                .sign(algorithm);
        System.out.println("Token = " + token);
        return token;
    }

}
