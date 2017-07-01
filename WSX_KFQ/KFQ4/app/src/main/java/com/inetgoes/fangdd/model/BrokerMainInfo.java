package com.inetgoes.fangdd.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by czz on 2015/11/18.
 * 经济人主页信息
 */
public class BrokerMainInfo implements Serializable {
    private Integer userid; // 经纪人id
    private String userimage_hor; // 经纪人横照
    private String name; // 经纪人姓名
    private Float starlevel; // 经纪人横照
    private String  cellphone;  //经纪人的手机号码

    private String brokertype; // 字符串,表示经纪人类型(自由经纪人、职业经纪人)
    private Float skillyear; // 浮点型,表示从业经验年数
    private String masterarea; // 字符串,表示顾问擅长区域(用逗号分隔)
    private Integer evalnum; // 整型,表示该经纪人楼盘评测数量
    private Integer appointmentnum; // 整型,表示预约次数
    private Integer ordernum; // 整型,表示订单成交量
    private Integer rank; // 整型,表示经纪人排名

    private Integer house_num_matched; // 与该经纪人匹配的房源数量
    private Integer house_num_total;   // 该经纪人总的房源数量

    private List<HouseInfo_BrokerIntroduce> houseinfo_matched_arr; // 第一次显示:
    // 与该经纪人匹配的房源对象列表
    private Integer comment_total; // 与该经纪人的评论总数量

    private List<AllTalk> brokercomment_arr; // 第一次显示: 与该经纪人评论列表

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUserimage_hor() {
        return userimage_hor;
    }

    public void setUserimage_hor(String userimage_hor) {
        this.userimage_hor = userimage_hor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(Float starlevel) {
        this.starlevel = starlevel;
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

    public String getMasterarea() {
        return masterarea;
    }

    public void setMasterarea(String masterarea) {
        this.masterarea = masterarea;
    }

    public Integer getEvalnum() {
        return evalnum;
    }

    public void setEvalnum(Integer evalnum) {
        this.evalnum = evalnum;
    }

    public Integer getAppointmentnum() {
        return appointmentnum;
    }

    public void setAppointmentnum(Integer appointmentnum) {
        this.appointmentnum = appointmentnum;
    }

    public Integer getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(Integer ordernum) {
        this.ordernum = ordernum;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getHouse_num_matched() {
        return house_num_matched;
    }

    public void setHouse_num_matched(Integer house_num_matched) {
        this.house_num_matched = house_num_matched;
    }

    public Integer getHouse_num_total() {
        return house_num_total;
    }

    public void setHouse_num_total(Integer house_num_total) {
        this.house_num_total = house_num_total;
    }

    public List<HouseInfo_BrokerIntroduce> getHouseinfo_matched_arr() {
        return houseinfo_matched_arr;
    }

    public void setHouseinfo_matched_arr(
            List<HouseInfo_BrokerIntroduce> houseinfo_matched_arr) {
        this.houseinfo_matched_arr = houseinfo_matched_arr;
    }

    public Integer getComment_total() {
        return comment_total;
    }

    public void setComment_total(Integer comment_total) {
        this.comment_total = comment_total;
    }

    public List<AllTalk> getBrokercomment_arr() {
        return brokercomment_arr;
    }

    public void setBrokercomment_arr(
            List<AllTalk> brokercomment_arr) {
        this.brokercomment_arr = brokercomment_arr;
    }
}
