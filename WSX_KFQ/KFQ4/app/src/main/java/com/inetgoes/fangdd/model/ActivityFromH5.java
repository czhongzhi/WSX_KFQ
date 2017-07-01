package com.inetgoes.fangdd.model;

/**
 * Created by czz on 2016/1/19.
 * 判断用户是否是H5引流过来的客户
 */
public class ActivityFromH5 {
    private boolean state;  //布尔型，true 或 false
    private String nextaction;  //字符串, 下一步动作
    //如果state=true 时, 表示是H5引流过来的，
    //值为: native         表示用户是原生注册的,不需要做任何动作
    //值为: h5_dispconnect     表示来自H5引入的C端客户,默认打开预约请求, 调用接口
    //值为: h5_dispconnect 表示来自H5引入的B端客户,默认显示成功对接经纪人、活动等

    private String[] districtarr;  //字符串数组对象 , 如果用户是H5导入进来的，会有绑定城市名称，则该属性含有片区名称数组
    private String username;     //字符串，//当nextaction=h5_tranreq或 h5_dispconnect 表示 "用户姓名"
    private String activityname; //字符串, //当nextaction=h5_tranreq或 h5_dispconnect 表示 "活动名称"
    private String brokername;   //字符串，//当nextaction=h5_dispconnect 表示 "预约成功的经纪人名称"
    private String bindcity;     //字符串, //绑定的城市名称
    private String activityurl;  //字符串, //活动图片链接地址...

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getNextaction() {
        return nextaction;
    }

    public void setNextaction(String nextaction) {
        this.nextaction = nextaction;
    }

    public String[] getDistrictarr() {
        return districtarr;
    }

    public void setDistrictarr(String[] districtarr) {
        this.districtarr = districtarr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getBrokername() {
        return brokername;
    }

    public void setBrokername(String brokername) {
        this.brokername = brokername;
    }

    public String getBindcity() {
        return bindcity;
    }

    public void setBindcity(String bindcity) {
        this.bindcity = bindcity;
    }

    public String getActivityurl() {
        return activityurl;
    }

    public void setActivityurl(String activityurl) {
        this.activityurl = activityurl;
    }
}
