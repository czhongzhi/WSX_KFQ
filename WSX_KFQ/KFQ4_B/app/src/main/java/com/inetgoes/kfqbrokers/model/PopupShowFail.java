package com.inetgoes.kfqbrokers.model;

/**
 * Created by czz on 2015/12/7.
 * popup显示的数据 - 抢单失败,即用户选择了别人
 */
public class PopupShowFail {
    String myname;    //自己姓名
    String myimage;   //自己头像地址
    Float mystarlevel;   //自己的服务星级

    Integer otherid;   //别人的userid
    String othername;  //别人的姓名
    String otherimage;   //别人的头像
    Float  otherstarlevel;   //别人的服务星级

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getMyimage() {
        return myimage;
    }

    public void setMyimage(String myimage) {
        this.myimage = myimage;
    }

    public Float getMystarlevel() {
        return mystarlevel;
    }

    public void setMystarlevel(Float mystarlevel) {
        this.mystarlevel = mystarlevel;
    }

    public Integer getOtherid() {
        return otherid;
    }

    public void setOtherid(Integer otherid) {
        this.otherid = otherid;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getOtherimage() {
        return otherimage;
    }

    public void setOtherimage(String otherimage) {
        this.otherimage = otherimage;
    }

    public Float getOtherstarlevel() {
        return otherstarlevel;
    }

    public void setOtherstarlevel(Float otherstarlevel) {
        this.otherstarlevel = otherstarlevel;
    }
}
