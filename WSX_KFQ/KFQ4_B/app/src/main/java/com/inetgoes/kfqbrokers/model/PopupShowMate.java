package com.inetgoes.kfqbrokers.model;

/**
 * Created by czz on 2015/12/7.
 * popup显示的数据 - 客户请求的维度
 */
public class PopupShowMate {
    Boolean state;   //true 表示有效抢单, false表示无效，原因在 reason字段说明
    String reason; // 1) 用户取消了  2)用户选择了别人  3)用户已经选择了你  state=false
    String tranid;   //交易id
    Long reqtime;//请求时间
    String reqtime_str;//请求时间字符串
    Integer requserid;  //请求人的id
    // ---请求维度内容------------
    String cond_reqtype; //请求类型
    String cond_city;    //城市
    String cond_area;    //区域
    String cond_loupanname;    //楼盘名
    String cond_price_low;    //最低价格
    String cond_price_high;    //最高价格
    String cond_huxing;    //户型
    String cond_peitao;    //配套

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTranid() {
        return tranid;
    }

    public void setTranid(String tranid) {
        this.tranid = tranid;
    }

    public Long getReqtime() {
        return reqtime;
    }

    public void setReqtime(Long reqtime) {
        this.reqtime = reqtime;
    }

    public String getReqtime_str() {
        return reqtime_str;
    }

    public void setReqtime_str(String reqtime_str) {
        this.reqtime_str = reqtime_str;
    }

    public Integer getRequserid() {
        return requserid;
    }

    public void setRequserid(Integer requserid) {
        this.requserid = requserid;
    }

    public String getCond_reqtype() {
        return cond_reqtype;
    }

    public void setCond_reqtype(String cond_reqtype) {
        this.cond_reqtype = cond_reqtype;
    }

    public String getCond_city() {
        return cond_city;
    }

    public void setCond_city(String cond_city) {
        this.cond_city = cond_city;
    }

    public String getCond_area() {
        return cond_area;
    }

    public void setCond_area(String cond_area) {
        this.cond_area = cond_area;
    }

    public String getCond_loupanname() {
        return cond_loupanname;
    }

    public void setCond_loupanname(String cond_loupanname) {
        this.cond_loupanname = cond_loupanname;
    }

    public String getCond_price_low() {
        return cond_price_low;
    }

    public void setCond_price_low(String cond_price_low) {
        this.cond_price_low = cond_price_low;
    }

    public String getCond_price_high() {
        return cond_price_high;
    }

    public void setCond_price_high(String cond_price_high) {
        this.cond_price_high = cond_price_high;
    }

    public String getCond_huxing() {
        return cond_huxing;
    }

    public void setCond_huxing(String cond_huxing) {
        this.cond_huxing = cond_huxing;
    }

    public String getCond_peitao() {
        return cond_peitao;
    }

    public void setCond_peitao(String cond_peitao) {
        this.cond_peitao = cond_peitao;
    }
}
