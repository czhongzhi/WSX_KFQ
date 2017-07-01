package com.inetgoes.kfqbrokers.model;

import java.io.Serializable;

/**
 * Created by czz on 2015/12/15.
 */
public class MyYuyue implements Serializable{
    private String sessionid;  //预约记录id
    private Integer userid; // 用户id
    private String userimage; // 用户头像
    private String cellphone; //用户电话
    private String state;    //订单状态
    //交易的状态,有: 已预约、已取消、已完成买房、预约失败
    private String connecttime_str;	//对接时间
    private String loupanname;  //楼盘名称
    private String huxing; //户型
    private String area;	//区域
    private String peitao;	//配套
    private String price_low;	//价格下限
    private String price_high;	//价格上限

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getConnecttime_str() {
        return connecttime_str;
    }

    public void setConnecttime_str(String connecttime_str) {
        this.connecttime_str = connecttime_str;
    }

    public String getLoupanname() {
        return loupanname;
    }

    public void setLoupanname(String loupanname) {
        this.loupanname = loupanname;
    }

    public String getHuxing() {
        return huxing;
    }

    public void setHuxing(String huxing) {
        this.huxing = huxing;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPeitao() {
        return peitao;
    }

    public void setPeitao(String peitao) {
        this.peitao = peitao;
    }

    public String getPrice_low() {
        return price_low;
    }

    public void setPrice_low(String price_low) {
        this.price_low = price_low;
    }

    public String getPrice_high() {
        return price_high;
    }

    public void setPrice_high(String price_high) {
        this.price_high = price_high;
    }
}
