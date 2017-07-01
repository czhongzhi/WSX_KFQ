package com.inetgoes.kfqbrokers.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by czz on 2015/12/25.
 */
public class BackupInfo implements Serializable {
    private boolean state;  //布尔类型  状态,表示推荐是否成功
    private String reason; // 字符串  原因描述,state=false时表示没有备注, state=true时,可以读取后面的字段值
    private Long markid; // 记录id

    private String custname;   //客户名称
    private String custphone;  //客户电话
    private String sex;	   //客户姓别
    private String intent_area;	 //意向区域
    private String intent_fangname;	 //意向楼盘名称
    private String intent_huxing;	 //意向户型
    private String intent_size;	 //意向面积
    private String othermark;        //其它备注

    private List<CustMarkKanfangOrder> orders;   //关联的订单列表;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getMarkid() {
        return markid;
    }

    public void setMarkid(Long markid) {
        this.markid = markid;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getCustphone() {
        return custphone;
    }

    public void setCustphone(String custphone) {
        this.custphone = custphone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIntent_area() {
        return intent_area;
    }

    public void setIntent_area(String intent_area) {
        this.intent_area = intent_area;
    }

    public String getIntent_fangname() {
        return intent_fangname;
    }

    public void setIntent_fangname(String intent_fangname) {
        this.intent_fangname = intent_fangname;
    }

    public String getIntent_huxing() {
        return intent_huxing;
    }

    public void setIntent_huxing(String intent_huxing) {
        this.intent_huxing = intent_huxing;
    }

    public String getIntent_size() {
        return intent_size;
    }

    public void setIntent_size(String intent_size) {
        this.intent_size = intent_size;
    }

    public String getOthermark() {
        return othermark;
    }

    public void setOthermark(String othermark) {
        this.othermark = othermark;
    }

    public List<CustMarkKanfangOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<CustMarkKanfangOrder> orders) {
        this.orders = orders;
    }
}
