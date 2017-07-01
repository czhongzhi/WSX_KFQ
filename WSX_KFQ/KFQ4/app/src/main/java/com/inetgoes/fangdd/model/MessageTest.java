package com.inetgoes.fangdd.model;

/**
 * Created by Administrator on 2015/11/12.
 */
public class MessageTest {
    int sendtype;   //me: 0    you: 1   mess: 2
    String context;

    public MessageTest(int sendtype) {
        this.sendtype = sendtype;
    }

    public void setSendtype(int sendtype) {
        this.sendtype = sendtype;
    }

    public int getSendtype() {
        return sendtype;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
