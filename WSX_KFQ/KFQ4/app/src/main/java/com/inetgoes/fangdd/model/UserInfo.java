package com.inetgoes.fangdd.model;


import java.io.Serializable;

/**
 * Created by czz on 2015/4/30.
 */
public class UserInfo implements Serializable {

    private Integer idd;   //用户id

    private String name;  //姓名

    private String device_type;            //设备类型: ios, android

    private String device_token;            //设备ID

    private String pwd; //密码，要经过MD5加密算法, 不能存放明文密码

    private String userimage; //用户头像的url地址，存放心png图片

    private String userid; //用户id, 用于存储手机的序列号，且是唯一性的

    private String regChannel; //注册方式: 通过QQ,微博或微信，或手机终端注册等

    private String email; //email地址

    private String cellphone; //手机号码

    private Integer level; //等级


    private double mapy_lat; //纬度,记录最近用户所处的位置

    private double mapx_lng; //经度,记录最近用户所处的位置

    private String isvip;

    private String sex; //性别

    private Long registerdate;  //注册时间

    private String personaldesc; //个性签名

    private Integer score; //积分

    private Long lastestdate; //最近登入app时间

    private String qq; //qq号码

    private String placedesc;  //所在地

    private Integer stepnum;  //签到数量

    private Integer commentnum; //评论数量


    private String rolename;  //角色名称

    private String skilldesc;  //经验描述

    private Float skillyear;  //经验年限  1.5 年

    private Integer service_customer_num;  //服务客户数量

    private Float goodCommentRatio; //好评率


    private String locdesc;  //从百度定位中获取返回的地址描述信息,如: 翠岭华庭1栋2201

    //关联字段
    private String registerdate_str;  //注册时间的字符串表示

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRegChannel() {
        return regChannel;
    }

    public void setRegChannel(String regChannel) {
        this.regChannel = regChannel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public double getMapy_lat() {
        return mapy_lat;
    }

    public void setMapy_lat(double mapy_lat) {
        this.mapy_lat = mapy_lat;
    }

    public double getMapx_lng() {
        return mapx_lng;
    }

    public void setMapx_lng(double mapx_lng) {
        this.mapx_lng = mapx_lng;
    }

    public String getIsvip() {
        return isvip;
    }

    public void setIsvip(String isvip) {
        this.isvip = isvip;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(Long registerdate) {
        this.registerdate = registerdate;
    }

    public String getPersonaldesc() {
        return personaldesc;
    }

    public void setPersonaldesc(String personaldesc) {
        this.personaldesc = personaldesc;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getLastestdate() {
        return lastestdate;
    }

    public void setLastestdate(Long lastestdate) {
        this.lastestdate = lastestdate;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getPlacedesc() {
        return placedesc;
    }

    public void setPlacedesc(String placedesc) {
        this.placedesc = placedesc;
    }

    public Integer getStepnum() {
        return stepnum;
    }

    public void setStepnum(Integer stepnum) {
        this.stepnum = stepnum;
    }

    public Integer getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(Integer commentnum) {
        this.commentnum = commentnum;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getSkilldesc() {
        return skilldesc;
    }

    public void setSkilldesc(String skilldesc) {
        this.skilldesc = skilldesc;
    }

    public Float getSkillyear() {
        return skillyear;
    }

    public void setSkillyear(Float skillyear) {
        this.skillyear = skillyear;
    }

    public Integer getService_customer_num() {
        return service_customer_num;
    }

    public void setService_customer_num(Integer service_customer_num) {
        this.service_customer_num = service_customer_num;
    }

    public Float getGoodCommentRatio() {
        return goodCommentRatio;
    }

    public void setGoodCommentRatio(Float goodCommentRatio) {
        this.goodCommentRatio = goodCommentRatio;
    }

    public String getLocdesc() {
        return locdesc;
    }

    public void setLocdesc(String locdesc) {
        this.locdesc = locdesc;
    }

    public String getRegisterdate_str() {
        return registerdate_str;
    }

    public void setRegisterdate_str(String registerdate_str) {
        this.registerdate_str = registerdate_str;
    }
}
