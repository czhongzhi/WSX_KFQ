package com.inetgoes.kfqbrokers.model;


/**
 * 订单进度
 * Created by czz on 2016/2/25.
 */
public class DanPlanList {

    private String text;

    private String time;

    public DanPlanList(String text,String time){
        this.text = text;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
