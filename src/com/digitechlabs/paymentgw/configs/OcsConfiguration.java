package com.digitechlabs.paymentgw.configs;

public class OcsConfiguration {

    private String ip;
    private int port;
    private String usename;
    private String password;
    private String[] prefix;
    private String cmd;

    public OcsConfiguration(String ip, int port, String usr, String pwd, String[] prefix, String cmd) {
        this.ip = ip;
        this.port = port;
        this.usename = usr;
        this.password = pwd;
        this.prefix = prefix;
        this.cmd = cmd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getPrefix() {
        return prefix;
    }

    public void setPrefix(String[] prefix) {
        this.prefix = prefix;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

}
