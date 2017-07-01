package com.inetgoes.kfqbrokers.model;

import java.io.Serializable;

/**
 * Created by czz on 2015/11/26.
 */
public class KfqMessage implements Serializable{

    //三个类型共用
    private String sessionid; // 会话id, 实际上就等于 tranid, 通知类的tranid共用这个字段
    private Long msgid; // 消息id
    private Integer fromuserid;
    private String  fromusername;  //发自方的姓名
    private Integer touserid;
    private String  msgtype;   // 值有: txt, link_fang, link_eval,(指会话中消息)  notify (通知类)
    private String  notifytype; //
    //经纪人端:得到
    //抢单通知: qiangdan
    //派单通知: paidan
    //打电话通知：   callnotify
    //取消预约通知： cancelnotify

    //订单已被抢   selectother
    //看房订单请求通知  kanfangreq
    //看房订单被用户取消通知 kanfangrefuse
    //看房订单和预约同时请求的通知  kanfangandbroker

    //用户端
    //抢单结果通知: resultdan
    private Long   createtime; // 创建时间

    // 链接中显示的内容
    //当msgtype=link_fang, link_eval
    private String loupan_name; // 楼盘名称
    private String loupan_imageurl; // 楼盘图片
    private String link_title; // 链接标题

    //当msgtype=link_fang
    private String loupan_addr;   // 楼盘项目地址
    private String newcode; // 楼盘id

    //当msgtype=link_eval
    private String eval_maintext; // 评测主要内容
    private Integer evalid; // 经纪人评测的id

    //当msgtype= txt时, 或 msgtype=notify时
    private String msgtext;        //文本类消息的正文, ios的通知alert参数请选该值, android的通知 text通知文字描述请选该值


    //当外面的msgtype=notify时,以下key为用到
    private String ticker;  //通知栏提示文字,仅android通知用到
    private String title;   //通知标题,仅android通知用到
    private Integer num;    //该值显示抢单结果的数量,ios和android都用到

    //其它关联的
    private String createtime_str; //创建时间的字符串形式


    public KfqMessage() {

    }

    public String getFromusername() {
        return fromusername;
    }

    public void setFromusername(String fromusername) {
        this.fromusername = fromusername;
    }

    public String getNotifytype() {
        return notifytype;
    }



    public void setNotifytype(String notifytype) {
        this.notifytype = notifytype;
    }



    public String getTicker() {
        return ticker;
    }



    public void setTicker(String ticker) {
        this.ticker = ticker;
    }



    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
    }



    public Integer getNum() {
        return num;
    }



    public void setNum(Integer num) {
        this.num = num;
    }



    public String getMsgtext() {
        return msgtext;
    }



    public void setMsgtext(String msgtext) {
        this.msgtext = msgtext;
    }



    public String getCreatetime_str() {
        return createtime_str;
    }



    public void setCreatetime_str(String createtime_str) {
        this.createtime_str = createtime_str;
    }



    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Long getMsgid() {
        return msgid;
    }

    public void setMsgid(Long msgid) {
        this.msgid = msgid;
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

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }


    public String getNewcode() {
        return newcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    public Integer getEvalid() {
        return evalid;
    }

    public void setEvalid(Integer evalid) {
        this.evalid = evalid;
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

    public String getLoupan_addr() {
        return loupan_addr;
    }

    public void setLoupan_addr(String loupan_addr) {
        this.loupan_addr = loupan_addr;
    }

    public String getLink_title() {
        return link_title;
    }

    public void setLink_title(String link_title) {
        this.link_title = link_title;
    }

    public String getEval_maintext() {
        return eval_maintext;
    }

    public void setEval_maintext(String eval_maintext) {
        this.eval_maintext = eval_maintext;
    }

}
