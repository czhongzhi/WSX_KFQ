package com.inetgoes.kfqbrokers.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/2.
 */
public class LoginInfo implements Serializable {

    private boolean state;  // true :登陆成功  false 失败


    private String reason;   //原因，当state=false时，reason给出失败理由

    private Integer userid;           //经纪人ID

    private Integer qiangordernum_today;//今日抢单数量

    private Float onlinetime_today;   //今日在线时间

    private Integer bookingnum_today;   //今日预约数量

    private Integer ordernum_today;     //今日看房订单数量

    private Integer fangtotal;          //我的房源数量

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getQiangordernum_today() {
        return qiangordernum_today;
    }

    public void setQiangordernum_today(Integer qiangordernum_today) {
        this.qiangordernum_today = qiangordernum_today;
    }

    public Float getOnlinetime_today() {
        return onlinetime_today;
    }

    public void setOnlinetime_today(Float onlinetime_today) {
        this.onlinetime_today = onlinetime_today;
    }

    public Integer getBookingnum_today() {
        return bookingnum_today;
    }

    public void setBookingnum_today(Integer bookingnum_today) {
        this.bookingnum_today = bookingnum_today;
    }

    public Integer getOrdernum_today() {
        return ordernum_today;
    }

    public void setOrdernum_today(Integer ordernum_today) {
        this.ordernum_today = ordernum_today;
    }

    public Integer getFangtotal() {
        return fangtotal;
    }

    public void setFangtotal(Integer fangtotal) {
        this.fangtotal = fangtotal;
    }
}
