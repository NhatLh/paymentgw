/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.digitechlabs.paymentgw.paypal.token;

/**
 *
 * @author Admin
 */
public class GetTokenResp {

    private String scope;
    private String nonce;
    private String access_token;
    private String token_type;
    private String app_id;
    private long expires_in;

    public GetTokenResp(String scope, String nonce, String access_token, String token_type, String app_id, long expires_in) {
        this.scope = scope;
        this.nonce = nonce;
        this.access_token = access_token;
        this.token_type = token_type;
        this.app_id = app_id;
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

}
