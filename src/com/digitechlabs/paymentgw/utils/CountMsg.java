
package com.digitechlabs.paymentgw.utils;

public class CountMsg {

    private long time;
    private int count;
    
    public CountMsg(long time,int count){
        this.time = time;
        this.count = count;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
