package com.inetgoes.kfqbrokers.model;

/**
 * Created by czz on 2015/12/9.
 * 经纪人信息
 */
public class BrokerInfo {
    Integer idd;   					//经纪人id
    String name;   					//姓名
    String cellphone;				//电话号码
    String userimage; 				//经纪人头像url(一般是圆形)
    String userimage_vertical;		//经纪人竖照url
    String userimage_horizontal;	//经纪人横照url
    String sex; 					//性别
    String placedesc;  				//所在地
    String qq; 						//qq号码
    String email; 					//email地址
    Long registerdate;  			//注册时间
    String registerdate_str;  		//注册时间(字符串表达形式)
    Long authendate;				//认证时间
    String authendate_str;			//认证时间(字符串表达形式)
    String authentype;				//认证类型(名片、身份证)
    String namecardimg;				//名片图片url
    String shenfenzhengimg;			//身份证图片地址
    String shenfenzhengno;			//身份证号码
    String companyname;				//工作单位名称，例如中介公司
    String personaldesc;			//个性签名
    String brokerrole;				//经纪人角色(新房、二手房、租房)
    String brokertype;				//经纪人类型(职业经纪人、房产砖家)
    String freezestate;				//冻结状态(Y/N)
    Long freezetime_to;				//冻结时间
    String freezetime_to_str;		//冻结时间(字符串表达形式)
    Float skillyear;			//从业经验年数
    Float starlevel;			//服务星级
    Integer appointmentnum;		//预约次数
    Integer ordernum;			//订单成交数量
    Integer comment_total;		//经纪人的评论总数

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

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUserimage_vertical() {
        return userimage_vertical;
    }

    public void setUserimage_vertical(String userimage_vertical) {
        this.userimage_vertical = userimage_vertical;
    }

    public String getUserimage_horizontal() {
        return userimage_horizontal;
    }

    public void setUserimage_horizontal(String userimage_horizontal) {
        this.userimage_horizontal = userimage_horizontal;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPlacedesc() {
        return placedesc;
    }

    public void setPlacedesc(String placedesc) {
        this.placedesc = placedesc;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(Long registerdate) {
        this.registerdate = registerdate;
    }

    public String getRegisterdate_str() {
        return registerdate_str;
    }

    public void setRegisterdate_str(String registerdate_str) {
        this.registerdate_str = registerdate_str;
    }

    public Long getAuthendate() {
        return authendate;
    }

    public void setAuthendate(Long authendate) {
        this.authendate = authendate;
    }

    public String getAuthendate_str() {
        return authendate_str;
    }

    public void setAuthendate_str(String authendate_str) {
        this.authendate_str = authendate_str;
    }

    public String getAuthentype() {
        return authentype;
    }

    public void setAuthentype(String authentype) {
        this.authentype = authentype;
    }

    public String getNamecardimg() {
        return namecardimg;
    }

    public void setNamecardimg(String namecardimg) {
        this.namecardimg = namecardimg;
    }

    public String getShenfenzhengimg() {
        return shenfenzhengimg;
    }

    public void setShenfenzhengimg(String shenfenzhengimg) {
        this.shenfenzhengimg = shenfenzhengimg;
    }

    public String getShenfenzhengno() {
        return shenfenzhengno;
    }

    public void setShenfenzhengno(String shenfenzhengno) {
        this.shenfenzhengno = shenfenzhengno;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getPersonaldesc() {
        return personaldesc;
    }

    public void setPersonaldesc(String personaldesc) {
        this.personaldesc = personaldesc;
    }

    public String getBrokerrole() {
        return brokerrole;
    }

    public void setBrokerrole(String brokerrole) {
        this.brokerrole = brokerrole;
    }

    public String getBrokertype() {
        return brokertype;
    }

    public void setBrokertype(String brokertype) {
        this.brokertype = brokertype;
    }

    public String getFreezestate() {
        return freezestate;
    }

    public void setFreezestate(String freezestate) {
        this.freezestate = freezestate;
    }

    public Long getFreezetime_to() {
        return freezetime_to;
    }

    public void setFreezetime_to(Long freezetime_to) {
        this.freezetime_to = freezetime_to;
    }

    public String getFreezetime_to_str() {
        return freezetime_to_str;
    }

    public void setFreezetime_to_str(String freezetime_to_str) {
        this.freezetime_to_str = freezetime_to_str;
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

    public Integer getComment_total() {
        return comment_total;
    }

    public void setComment_total(Integer comment_total) {
        this.comment_total = comment_total;
    }
}
