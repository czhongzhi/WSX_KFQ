package com.inetgoes.kfqbrokers.model;

/**
 * Created by czz on 2015/12/7.
 * popup显示的数据 - 看房订单和预约同时请求的通知
 */
public class PopupShowLoupan {
    String  tranid;
    Integer requserid;   //请求人的id
    String requsername;	//请求人姓名
    String cellphone;  //电话号码
    String loupanname;
    String pricedesc; // 单价描述
    String loupan_image_url; // 楼盘概览图片地址
    String newcode;
    Integer kanfang_time; // 该楼盘的总的看房次数
    Integer tran_success_num; // 该楼盘的总的成交量
    String  district; // 区域

    public String getTranid() {
        return tranid;
    }

    public String getRequsername() {
        return requsername;
    }

    public void setRequsername(String requsername) {
        this.requsername = requsername;
    }

    public void setTranid(String tranid) {
        this.tranid = tranid;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


}
