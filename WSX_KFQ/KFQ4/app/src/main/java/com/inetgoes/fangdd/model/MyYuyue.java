package com.inetgoes.fangdd.model;


import java.io.Serializable;

/**
 * Created by czz on 2015/11/23.
 */
public class MyYuyue implements Serializable {
    private Integer brokerid; // 经纪人id
    private String sessionid; //会话id

    private String userimage_ver; // 经纪人竖照
    private String username; // 经纪人姓名
    private String profname; // 经纪人职称
    private Float skillyear; // 经验年数
    private Float starlevel; // 等级
    private String cellphone; //用户电话

    private String transtate; // 交易的状态,有: 已预约、已取消、已完成买房、预约失败
    private String requestdate_str; // 申请时间

    public MyYuyue() {

    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Integer getBrokerid() {
        return brokerid;
    }

    public void setBrokerid(Integer brokerid) {
        this.brokerid = brokerid;
    }

    public String getUserimage_ver() {
        return userimage_ver;
    }

    public void setUserimage_ver(String userimage_ver) {
        this.userimage_ver = userimage_ver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfname() {
        return profname;
    }

    public void setProfname(String profname) {
        this.profname = profname;
    }

    public Float getSkillyear() {
        return skillyear;
    }

    public void setSkillyear(Float skillyear) {
        this.skillyear = skillyear;
    }

    public Float getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(Float starlevel) {
        this.starlevel = starlevel;
    }

    public String getTranstate() {
        return transtate;
    }

    public void setTranstate(String transtate) {
        this.transtate = transtate;
    }

    public String getRequestdate_str() {
        return requestdate_str;
    }

    public void setRequestdate_str(String requestdate_str) {
        this.requestdate_str = requestdate_str;
    }
}
