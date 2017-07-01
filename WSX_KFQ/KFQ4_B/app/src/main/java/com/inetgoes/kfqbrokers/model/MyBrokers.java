package com.inetgoes.kfqbrokers.model;

/**
 * Created by czz on 2015/12/29.
 */
public class MyBrokers {

    private Integer custid; //   客户id      fromtype=baobei时,该值为0
    private String userimage; // 客户大头照  fromtype=baobei时为缺省头像
    private String username; //  客户姓名

    private String fromtype;    //来源:值:normal表示正式用客户基本信息,baobei表示是微信前端经纪人报备的

    public Integer getCustid() {
        return custid;
    }

    public void setCustid(Integer custid) {
        this.custid = custid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFromtype() {
        return fromtype;
    }

    public void setFromtype(String fromtype) {
        this.fromtype = fromtype;
    }
}
