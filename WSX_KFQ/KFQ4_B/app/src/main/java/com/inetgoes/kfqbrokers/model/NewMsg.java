package com.inetgoes.kfqbrokers.model;

/**
 * Created by czz on 2015/12/16.
 * 主页listview data封装
 */
public class NewMsg {
    private long time;
    private String content;

    public NewMsg(long time, String content) {
        this.time = time;
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
