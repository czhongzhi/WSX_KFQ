package com.inetgoes.kfqbrokers.model;

import java.io.Serializable;

/**
 * 订单进度
 * Created by czz on 2016/2/25.
 */
public class DanPlan implements Serializable {

    private String flag_callphone;  //字符串  电话跟进否 Y/N
    private String flag_callphone_tm; //字符串  电话跟进时间

    private String flag_daofang;      //字符串  到访否 Y/N
    private String flag_daofang_tm;    //字符串  到访时间

    private String flag_renchou;       //字符串  认筹否 Y/N
    private String flag_renchou_tm;    //字符串  认筹时间

    private String flag_rengou;       //字符串  认购否 Y/N
    private String flag_rengou_tm;    //字符串  认购时间

    private String flag_qianyue;      ///字符串  签约否 Y/N
    private String flag_qianyue_tm;    //字符串  签约时间

    private String flag_jieyong;       //字符串  结佣否 Y/N
    private String flag_jieyong_tm;    //字符串  结佣时间

    private int xianjin_money;     // 现金奖
    private int yongjin_money;     // 佣金奖

    public String getFlag_callphone() {
        return flag_callphone;
    }

    public void setFlag_callphone(String flag_callphone) {
        this.flag_callphone = flag_callphone;
    }

    public String getFlag_callphone_tm() {
        return flag_callphone_tm;
    }

    public void setFlag_callphone_tm(String flag_callphone_tm) {
        this.flag_callphone_tm = flag_callphone_tm;
    }

    public String getFlag_daofang() {
        return flag_daofang;
    }

    public void setFlag_daofang(String flag_daofang) {
        this.flag_daofang = flag_daofang;
    }

    public String getFlag_daofang_tm() {
        return flag_daofang_tm;
    }

    public void setFlag_daofang_tm(String flag_daofang_tm) {
        this.flag_daofang_tm = flag_daofang_tm;
    }

    public String getFlag_renchou() {
        return flag_renchou;
    }

    public void setFlag_renchou(String flag_renchou) {
        this.flag_renchou = flag_renchou;
    }

    public String getFlag_renchou_tm() {
        return flag_renchou_tm;
    }

    public void setFlag_renchou_tm(String flag_renchou_tm) {
        this.flag_renchou_tm = flag_renchou_tm;
    }

    public String getFlag_rengou() {
        return flag_rengou;
    }

    public void setFlag_rengou(String flag_rengou) {
        this.flag_rengou = flag_rengou;
    }

    public String getFlag_rengou_tm() {
        return flag_rengou_tm;
    }

    public void setFlag_rengou_tm(String flag_rengou_tm) {
        this.flag_rengou_tm = flag_rengou_tm;
    }

    public String getFlag_qianyue() {
        return flag_qianyue;
    }

    public void setFlag_qianyue(String flag_qianyue) {
        this.flag_qianyue = flag_qianyue;
    }

    public String getFlag_qianyue_tm() {
        return flag_qianyue_tm;
    }

    public void setFlag_qianyue_tm(String flag_qianyue_tm) {
        this.flag_qianyue_tm = flag_qianyue_tm;
    }

    public String getFlag_jieyong() {
        return flag_jieyong;
    }

    public void setFlag_jieyong(String flag_jieyong) {
        this.flag_jieyong = flag_jieyong;
    }

    public String getFlag_jieyong_tm() {
        return flag_jieyong_tm;
    }

    public void setFlag_jieyong_tm(String flag_jieyong_tm) {
        this.flag_jieyong_tm = flag_jieyong_tm;
    }

    public int getXianjin_money() {
        return xianjin_money;
    }

    public void setXianjin_money(int xianjin_money) {
        this.xianjin_money = xianjin_money;
    }

    public int getYongjin_money() {
        return yongjin_money;
    }

    public void setYongjin_money(int yongjin_money) {
        this.yongjin_money = yongjin_money;
    }
}
