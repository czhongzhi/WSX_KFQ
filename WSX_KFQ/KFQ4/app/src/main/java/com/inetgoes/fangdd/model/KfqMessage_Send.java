package com.inetgoes.fangdd.model;

import java.io.Serializable;

/**
 * Created by czz on 2015/11/26.
 */
public class KfqMessage_Send implements Serializable {
    String sessionid; // 会话id, 实际上就等于 tranid      (必写)
    Integer fromuserid;                                  //(必写)
    Integer touserid;                                   // (必写)
    String msgtype; // 值有: txt, link_fang, link_eval   (必写)


    // 链接中显示的内容
    //当msgtype=link_fang, link_eval
    String loupan_name; // 楼盘名称
    String loupan_imageurl; // 楼盘图片
    String link_title; // 链接标题

    //当msgtype=link_fang
    String loupan_addr;   // 楼盘项目地址
    String newcode; // 楼盘id

    //当msgtype=link_eval
    String eval_maintext; // 评测主要内容
    Integer evalid; // 经纪人评测的id

    //当msgtype= txt
    String msgtext;        //文本类消息的正文

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Integer getFromuserid() {
        return fromuserid;
    }

    public void setFromuserid(Integer fromuserid) {
        this.fromuserid = fromuserid;
    }

    public Integer getTouserid() {
        return touserid;
    }

    public void setTouserid(Integer touserid) {
        this.touserid = touserid;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getLoupan_name() {
        return loupan_name;
    }

    public void setLoupan_name(String loupan_name) {
        this.loupan_name = loupan_name;
    }

    public String getLoupan_imageurl() {
        return loupan_imageurl;
    }

    public void setLoupan_imageurl(String loupan_imageurl) {
        this.loupan_imageurl = loupan_imageurl;
    }

    public String getLink_title() {
        return link_title;
    }

    public void setLink_title(String link_title) {
        this.link_title = link_title;
    }

    public String getLoupan_addr() {
        return loupan_addr;
    }

    public void setLoupan_addr(String loupan_addr) {
        this.loupan_addr = loupan_addr;
    }

    public String getNewcode() {
        return newcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    public String getEval_maintext() {
        return eval_maintext;
    }

    public void setEval_maintext(String eval_maintext) {
        this.eval_maintext = eval_maintext;
    }

    public Integer getEvalid() {
        return evalid;
    }

    public void setEvalid(Integer evalid) {
        this.evalid = evalid;
    }

    public String getMsgtext() {
        return msgtext;
    }

    public void setMsgtext(String msgtext) {
        this.msgtext = msgtext;
    }
}
