package com.inetgoes.kfqbrokers.model;

import java.io.Serializable;

/**
 * Created by czz on 2015/11/26.
 * 我的消息 数据封装类
 */
public class MyMessage implements Serializable{

    String  sessionid;   //会话id
    Integer touserid;     //对方的userid
    String  userimage;    //对方的头像url
    String  username;     //对方的姓名
    String  createtime;   //最近消息的创建时间
    String  content;      //最近消息的内容
    Integer updatenum;    //更新数量

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Integer getTouserid() {
        return touserid;
    }

    public void setTouserid(Integer touserid) {
        this.touserid = touserid;
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

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUpdatenum() {
        return updatenum;
    }

    public void setUpdatenum(Integer updatenum) {
        this.updatenum = updatenum;
    }
}
