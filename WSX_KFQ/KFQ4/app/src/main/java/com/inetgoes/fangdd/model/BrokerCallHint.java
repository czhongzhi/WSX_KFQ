package com.inetgoes.fangdd.model;

/**
 * Created by czz on 2016/1/7.
 */
public class BrokerCallHint {

    private Integer brokerid; // 经纪人id
    private String userimage; // 经纪人大头像
    private String username; // 经纪人姓名

    private Float starlevel; // 等级
    private String cellphone; //用户电话

    public Integer getBrokerid() {
        return brokerid;
    }

    public void setBrokerid(Integer brokerid) {
        this.brokerid = brokerid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(Float starlevel) {
        this.starlevel = starlevel;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
