package com.inetgoes.fangdd.model;

import java.io.Serializable;

/**
 * Created by czz on 2015/12/1.
 */
public class BrokerComm implements Serializable {
    private boolean status;    //布尔型，true 或 false
    private Long id;        //Long,看房订单id
    private String userimage;    //字符串,用户头像url
    private String name;        //字符串,经纪人姓名
    private String brokertype;//字符串,经纪人类型(自由经纪人、职业经纪人)
    private Float skillyear;    //Float,从业经验
    private Float starlevel;    //Float,服务星级

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long recid) {
        this.id = recid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrokertype() {
        return brokertype;
    }

    public void setBrokertype(String brokertype) {
        this.brokertype = brokertype;
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
}
