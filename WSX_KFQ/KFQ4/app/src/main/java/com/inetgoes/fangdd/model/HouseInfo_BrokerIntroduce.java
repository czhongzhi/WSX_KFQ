package com.inetgoes.fangdd.model;

import java.io.Serializable;

/**
 * Created by czz on 2015/11/18.
 * 需求匹配楼盘列表信息
 */
public class HouseInfo_BrokerIntroduce implements Serializable {
    private String loupanname;
    private String pricedesc; // 单价描述
    private String loupan_image_url; // 楼盘概览图片地址
    private String newcode;
    private Integer kanfang_time; // 该楼盘的总的看房次数
    private Integer tran_success_num; // 该楼盘的总的成交量
    private String district; // 区域
    private String  match_huxing;  //匹配的户型描述: 二室40㎡、50㎡

    public HouseInfo_BrokerIntroduce() {

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

    public String getLoupan_image_url() {
        return loupan_image_url;
    }

    public void setLoupan_image_url(String loupan_image_url) {
        this.loupan_image_url = loupan_image_url;
    }

    public String getNewcode() {
        return newcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    public Integer getKanfang_time() {
        return kanfang_time;
    }

    public void setKanfang_time(Integer kanfang_time) {
        this.kanfang_time = kanfang_time;
    }

    public Integer getTran_success_num() {
        return tran_success_num;
    }

    public void setTran_success_num(Integer tran_success_num) {
        this.tran_success_num = tran_success_num;
    }

    public String getStrict() {
        return district;
    }

    public void setStrict(String strict) {
        this.district = strict;
    }

    public String getMatch_huxing() {
        return match_huxing;
    }

    public void setMatch_huxing(String match_huxing) {
        this.match_huxing = match_huxing;
    }
}