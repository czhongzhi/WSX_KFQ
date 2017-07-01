package com.inetgoes.kfqbrokers.model;

/**
 * Created by czz on 2015/12/7.
 */
public class PopupShowCell {

    private Boolean state;
    private String reason;   //失败原因,1)用户取消了   state=false
    private String tranid;   //交易id
    private Long connecttime; //对接时间
    private String connecttime_str; //对接时间字符串
    private Integer requserid;  //请求人的id
    private String cellphone;	//请求人电话号码
    //---请求维度内容------------
    private String cond_reqtype; //请求类型
    private String cond_city;	//城市
    private String cond_area;	//区域
    private String cond_loupanname;	//楼盘名
    private String cond_price_low;	//最低价格
    private String cond_price_high;	//最高价格
    private String cond_huxing;	//户型
    private String cond_peitao;	//配套

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
    public Long getConnecttime() {
        return connecttime;
    }
    public void setConnecttime(Long connecttime) {
        this.connecttime = connecttime;
    }
    public String getConnecttime_str() {
        return connecttime_str;
    }
    public void setConnecttime_str(String connecttime_str) {
        this.connecttime_str = connecttime_str;
    }
    public Integer getRequserid() {
        return requserid;
    }
    public void setRequserid(Integer requserid) {
        this.requserid = requserid;
    }
    public String getCellphone() {
        return cellphone;
    }
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
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
