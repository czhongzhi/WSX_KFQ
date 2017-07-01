package com.inetgoes.kfqbrokers.model;

import java.io.Serializable;

/**
 * Created by czz on 2015/11/30.
 */
public class KanFangListStateResp implements Serializable {

    private String    kanrecid;  //看房订单id
    private Integer userid; // 用户id
    private String userimage; // 用户头像
    private String name; // 用户姓名
    private String state;    //订单状态
    //值有:已看房，待看房，取消看房，失败请求
    private String createdate_str;	//订单创建时间
    private String newcode;	//楼盘id
    private String loupan_image_url; // 楼盘概览图片地址
    private String loupanname; // 楼盘名
    private String pricedesc; // 单价描述
    private String loupan_addr;   // 楼盘项目地址

    private String huxing_type;     //字符串  户型类型

    public String getHuxing_type() {
        return huxing_type;
    }

    public void setHuxing_type(String huxing_type) {
        this.huxing_type = huxing_type;
    }

    public String getKanrecid() {
        return kanrecid;
    }

    public void setKanrecid(String kanrecid) {
        this.kanrecid = kanrecid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedate_str() {
        return createdate_str;
    }

    public void setCreatedate_str(String createdate_str) {
        this.createdate_str = createdate_str;
    }

    public String getNewcode() {
        return newcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    public String getLoupan_image_url() {
        return loupan_image_url;
    }

    public void setLoupan_image_url(String loupan_image_url) {
        this.loupan_image_url = loupan_image_url;
    }

    public String getLoupanname() {
        return loupanname;
    }

    public void setLoupanname(String loupanname) {
        this.loupanname = loupanname;
    }

    public String getPricedesc() {
        return pricedesc;
    }

    public void setPricedesc(String pricedesc) {
        this.pricedesc = pricedesc;
    }

    public String getLoupan_addr() {
        return loupan_addr;
    }

    public void setLoupan_addr(String loupan_addr) {
        this.loupan_addr = loupan_addr;
    }
}
