package com.inetgoes.kfqbrokers.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/25.
 */
public class CustMarkKanfangOrder implements Serializable {
    private String kanfang_state; // 订单状态
    // 值有:已看房，待看房，已取消，已失败
    private String createdate_str; // 订单创建时间
    private String newcode; // 楼盘id
    private int kan_recid;  //看房订单id
    private String loupan_image_url; // 楼盘概览图片地址
    private String loupanname; // 楼盘名
    private String pricedesc; // 单价描述
    private String huxing_type; // 字符串 户型类型 (报备的客户没有这个栏位)
    private String huxing_size; // 字符串 户型大小 (报备的客户没有这个栏位)

    public String getKanfang_state() {
        return kanfang_state;
    }

    public void setKanfang_state(String kanfang_state) {
        this.kanfang_state = kanfang_state;
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

    public int getKan_recid() {
        return kan_recid;
    }

    public void setKan_recid(int kan_recid) {
        this.kan_recid = kan_recid;
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

    public String getHuxing_type() {
        return huxing_type;
    }

    public void setHuxing_type(String huxing_type) {
        this.huxing_type = huxing_type;
    }

    public String getHuxing_size() {
        return huxing_size;
    }

    public void setHuxing_size(String huxing_size) {
        this.huxing_size = huxing_size;
    }
}
