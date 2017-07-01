package com.inetgoes.fangdd.model;

/**
 * Created by czz on 2015/12/29.
 */
public class MyBrokers {
    private Integer brokerid; // 经纪人id
    private String userimage; // 经纪人大头照
    private String username; // 经纪人姓名
    private Float starlevel; // 等级

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
}
