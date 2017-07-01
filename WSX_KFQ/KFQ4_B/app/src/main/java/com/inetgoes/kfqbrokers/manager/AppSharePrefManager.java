package com.inetgoes.kfqbrokers.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;


import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.model.UserInfo;
import com.inetgoes.kfqbrokers.utils.BitmapUtil;
import com.inetgoes.kfqbrokers.utils.DateFormatHelp;

import java.util.Date;


/**
 * Created by Jimmy on 2015/5/1.
 * 这个类用于对整个app的共享配置文件的访问管理
 */
public class AppSharePrefManager {
    private static final String CURRENT_CITY_ID = "current_city_id";
    private static final String CURRENT_CITY_NAME = "current_city_name";
    private static final String CUSTOMER_DYNAMIC = "customer_dynamic";
    public static final String ISLOADED = "is_loaded";
    private static final String IS_CITY_SELECT = "select_city";
    private static final String IS_FLOW_MODE_DIALOG_SHOW = "is_flow_mode_dialog_show";
    private static final String IS_PHONE_CHANGE = "is_phone_change";
    private static final String IS_SAVE_FLOW_MODE = "is_save_flow_mode";
    private static final String IS_TIP_ADD_CUSTOMER_SHOWED = "is_tip_add_customer_showed";
    private static final String LAST_REQ_TIME = "last_req_time";
    public static final String SHOW_PROPERY_ACTIVITY_TIMES = "show_propery_activity_times";
    private static final String VERSION_CODE = "version_code";

    //其它页面的的本地缓存
    private static final String FIND_HOT_AREA = "find_hot_area";
    private static final String FIND_HOT_BUILDTYPE = "find_hot_buildtype";
    private static final String FIND_HOT_PRICE = "find_hot_price";
    private static final String FIND_HOT_SORT = "find_hot_sort";

    //摇一摇的设置
    private static final String YAO_SCOPE_EVAL = "yao_scope_eval";  //摇一摇评测范围
    private static final String YAO_SCOPE_GROUP = "yao_scope_group";  //摇一摇加群范围
    private static final String YAO_MUSIC_ON = "yao_music_on";  //摇一摇音效开关

    //引导页的设置
    private static final String GUIDE_AREA_SEL = "GUIDE_AREA_SEL";  //区域的选择
    private static final String GUIDE_HUXING_SEL = "GUIDE_HUXING_SEL";  //户型的选择
    private static final String GUIDE_PRICE_SEL = "GUIDE_PRICE_SEL";  //价格区间的选择
    private static final String GUIDE_SUPPORT_SEL = "GUIDE_SUPPORT_SEL";  //配套的选择


    public static AppSharePrefManager instance_;

    protected Context context;

    public AppSharePrefManager(Context paramContext) {
        this.context = paramContext;
    }

    public static AppSharePrefManager getInstance(Context paramContext) {
        Context localContext = paramContext.getApplicationContext();
        if ((instance_ == null) || (instance_.context != localContext)) {
            instance_ = new AppSharePrefManager(localContext);
        }
        return instance_;
    }

    protected SharedPreferences getSp() {
        return this.context.getSharedPreferences(getSpFileName(), Context.MODE_WORLD_WRITEABLE);
    }

    public SharedPreferences.Editor getEdit() {
        return getSp().edit();
    }


    private void savetoLocalPref(UserInfo userinfo) {
        instance_.setLastest_login_username(userinfo.getName());
        instance_.setLastest_login_id(userinfo.getIdd());
        instance_.setLastest_login_level(userinfo.getLevel());
        instance_.setLastest_login_phone_num(userinfo.getCellphone());
        instance_.setLastest_login_time();
        instance_.setLastest_login_touxiang_imageurl(userinfo.getUserimage());
        instance_.setLastest_login_channel("PHONE_IMEI");
        instance_.setUser_register_time(userinfo.getRegisterdate());
        instance_.setUser_rolename(userinfo.getRolename());  //角色
    }

    //配置文件不需要.xml后缀,系统会自动加.xml的
    //在真机上要查看/data/data/package_name/shared_prefs/ 真机必需要ROOT才行
    protected String getSpFileName() {
        return "app_manager";
    }

    //保存Umeng的 //获取设备的Device Token
    public String getDevicetoken() {
        return getSp().getString("devicetoken", "");
    }

    public void setDevicetoken(String devicetoken) {
        getEdit().putString("devicetoken", devicetoken).commit();
    }

    public String getVersionName() {
        return getSp().getString("version_name", "0.1");
    }

    public void setVersionName(String paramInt) {
        getEdit().putString("version_name", paramInt).commit();
    }

    //简单的判断用户是否登入
    public boolean isLogined() {
        Integer login_id = getSp().getInt("lastest_login_id", 0);

        return login_id > 0;
    }

    //最近登入用户id的存取
    public void setLastest_login_id(int usrid) {
        getEdit().putInt("lastest_login_id", usrid).commit();
    }

    public int getLastest_login_id() {
        return getSp().getInt("lastest_login_id", 0);
    }

    //用户昵称
    public void setLastest_login_username(String username) {
        getEdit().putString("lastest_login_username", username).commit();
    }

    public String getLastest_login_username() {
        return getSp().getString("lastest_login_username", "");
    }


    //最近登入时间的存取
    public void setLastest_login_time() {
        getEdit().putString("lastest_login_time", DateFormatHelp.DATETIME_FORMAT.format(new Date())).commit();
    }

    public String getLastest_login_time() {
        return getSp().getString("lastest_login_time", "");
    }


    //注册时间的存取
    public void setLastest_regist_time() {
        getEdit().putString("Lastest_regist_time", DateFormatHelp.DATE_FORMAT.format(new Date())).commit();
    }

    public String getLastest_regist_time() {
        return getSp().getString("Lastest_regist_time", "");
    }


    //最近注册头像的url地址存取
    public void setLastest_login_touxiang_imageurl(String login_touxiang_imageurl) {
        getEdit().putString("lastest_login_touxiang_imageurl", login_touxiang_imageurl).commit();
    }

    public String getLastest_login_touxiang_imageurl() {
        return getSp().getString("lastest_login_touxiang_imageurl", Constants.DEFAULT_TOUXIANG_URL);
    }

    //上传后的服务器头像Base64文件
    public void setLastest_login_touxiang_imagebase64(String path) {
        getEdit().putString("setLastest_login_touxiang_imagebase64", path).commit();
    }

    public String getLastest_login_touxiang_imagebase64() {
        return getSp().getString("setLastest_login_touxiang_imagebase64", "");
    }

    //最近注册竖照的url地址存取
    public void setLastest_shuzhao_imageurl(String lastest_shuzhao_imageurl) {
        getEdit().putString("Lastest_shuzhao_imageurl", lastest_shuzhao_imageurl).commit();
    }

    public String getLastest_shuzhao_imageurl() {
        return getSp().getString("Lastest_shuzhao_imageurl", "");
    }

    //上传后的服务器竖照Base64文件
    public void setLastest_shuzhao_imagebase64(String shuzhao_imagebase64) {
        getEdit().putString("Lastest_shuzhao_imagebase64", shuzhao_imagebase64).commit();
    }

    public String getLastest_shuzhao_imagebase64() {
        if ("".equals(getSp().getString("Lastest_shuzhao_imagebase64", ""))) {
            return BitmapUtil.bitmapToBase64(BitmapFactory.decodeResource(context.getResources(), R.drawable.default_boy));
        }
        return getSp().getString("Lastest_shuzhao_imagebase64", "");
    }

    //上传后的服务器横照base64文件
    public void setLastest_hengzhao_imagebase64(String hengzhao){
        getEdit().putString("Lastest_hangzhao_imagebase64",hengzhao).commit();
    }

    public String getLastest_hengzhao_imagebase64(){
        return getSp().getString("Lastest_hangzhao_imagebase64", "");
    }


    //最近用户等级
    public void setLastest_login_level(int login_level) {
        getEdit().putInt("lastest_login_level", login_level).commit();
    }

    public int getLastest_login_level() {
        return getSp().getInt("lastest_login_level", 0);
    }

    //最近登入的手机号码
    public void setLastest_login_phone_num(String login_phone_num) {
        getEdit().putString("lastest_login_phone_num", login_phone_num).commit();
    }

    public String getLastest_login_phone_num() {
        return getSp().getString("lastest_login_phone_num", "");
    }

    //最近用户的登入方式
    public void setLastest_login_channel(String login_channel) {
        getEdit().putString("lastest_login_channel", login_channel).commit();
    }

    public String getLastest_login_channel() {
        return getSp().getString("lastest_login_channel", "");
    }

    //本地头像地址
    public void setNativeIconPath(String path) {
        getEdit().putString("nativeIconPath", path).commit();
    }

//    public String getNativeIconPath() {
//        return getSp().getString("nativeIconPath", Constants.DEFAULT_TOUXIANG_URL);
//    }

    //本地头像是否修改
    public void setIsIcon(boolean flag) {
        getEdit().putBoolean("IsIcon", flag).commit();
    }

    public boolean getIsIcon() {
        return getSp().getBoolean("IsIcon", false);
    }

    //最近的个性签名
    public void setLastest_people_desc(String people_desc) {
        getEdit().putString("lastest_people_desc", people_desc).commit();
    }

    public String getLastest_people_desc() {
        return getSp().getString("lastest_people_desc", "");
    }

    //性别
    public void setLastest_sex(String sex) {
        getEdit().putString("lastest_sex", sex).commit();
    }

    public String getLastest_sex() {
        return getSp().getString("lastest_sex", "");
    }


    //证件类型
    public void setLastest_authentype(String authentype) {
        getEdit().putString("lastest_authentype", authentype).commit();

    }

    public String getLastest_authentype() {
        return getSp().getString("lastest_authentype", "");
    }

    //身份证号码
    public void setLastest_shenfenzhengno(String shenfenzhengno) {
        getEdit().putString("lastest_shenfenzhengno", shenfenzhengno).commit();

    }

    public String getLastest_shenfenzhengno() {
        return getSp().getString("lastest_shenfenzhengno", "");
    }

    //身份证号码图片64编码数据
    public void setLastest_shenfenzheng_imagebase64(String shenfenzheng_imagebase64) {
        getEdit().putString("Lastest_shenfenzheng_imagebase64", shenfenzheng_imagebase64).commit();

    }

    public String getLastest_shenfenzheng_imagebase64() {
        return getSp().getString("Lastest_shenfenzheng_imagebase64", "");
    }

    //所在地
    public void setLastest_placedesc(String placedesc) {
        getEdit().putString("Lastest_placedesc", placedesc).commit();

    }

    public String getLastest_placedesc() {
        return getSp().getString("Lastest_placedesc", "");
    }

    //工作单位
    public void setLastest_companyname(String companyname) {
        getEdit().putString("Lastest_companyname", companyname).commit();

    }

    public String getLastest_companyname() {
        return getSp().getString("Lastest_companyname", "");
    }


    //地址
    public void setLastest_address(String address) {
        getEdit().putString("address", address).commit();
    }

    public String getLastest_address() {
        return getSp().getString("address", "");
    }

    //qq
    public void setLastest_qq(String qq) {
        getEdit().putString("qq", qq).commit();
    }

    public String getLastest_qq() {
        return getSp().getString("qq", "");
    }

    //Email
    public void setLastest_email(String email) {
        getEdit().putString("email", email).commit();
    }

    public String getLastest_email() {
        return getSp().getString("email", "");
    }

    //经纪人角色
    public void setLastest_Brokerrole(String brokerrole) {
        getEdit().putString("Lastest_Brokerrole", brokerrole).commit();
    }

    public String getLastest_Brokerrole() {
        return getSp().getString("Lastest_Brokerrole", "");
    }

    //经纪人类型
    public void setLastest_Brokertype(String brokertype) {
        getEdit().putString("Lastest_Brokertype", brokertype).commit();
    }

    public String getLastest_Brokertype() {
        return getSp().getString("Lastest_Brokertype", "");
    }

    //从业经验年数
    public void setLastest_SkillYear(Float skillYear) {
        getEdit().putFloat("Lastest_SkillYear", skillYear).commit();
    }

    public Float getLastest_SkillYear() {
        return getSp().getFloat("Lastest_SkillYear",0);//默认返回0
    }

    //服务星级
    public void setLastest_Starlevel(Float Starlevel) {
        getEdit().putFloat("Lastest_Starlevel", Starlevel).commit();
    }

    public Float getLastest_Starlevel() {
        return getSp().getFloat("Lastest_Starlevel",0);//默认返回0
    }

    //预约次数
    public void setLastest_Appointmentnum(int appointmentnum) {
        getEdit().putInt("Lastest_Appointmentnum", appointmentnum).commit();
    }

    public int getLastest_Appointmentnum() {
        return getSp().getInt("Lastest_Appointmentnum",0);//默认返回0
    }

    //订单数量
    public void setLastest_Ordernum(int ordernum){
        getEdit().putInt("Lastest_Ordernum",ordernum).commit();
    }

    public int getLastest_Ordernum(){
        return getSp().getInt("Lastest_Ordernum",0);
    }

    //积分
    public void setLastest_jifen(String jifen) {
        getEdit().putString("email", jifen).commit();
    }

    public String getLastest_jifen() {
        return getSp().getString("jifen", "");
    }

    //用于判断是否网上获取信息或者是本地
    public void setIsLoadFromNet(boolean flag) {
        getEdit().putBoolean("loadFromNet", flag).commit();
    }

    public boolean getIsLoadFromNet() {
        return getSp().getBoolean("loadFromNet", true);
    }

    //热点查询的区域
    public void setFind_hot_area(String area) {
        getEdit().putString(FIND_HOT_AREA, area).commit();
    }

    public String getFind_hot_area() {
        //如果没有缓存,则返回一个提示
        return getSp().getString(FIND_HOT_AREA, Constants.FIND_AREA_PROMPT);
    }

    //热点查询的物业类型
    public void setFind_hot_buildtype(String buildtype) {
        getEdit().putString(FIND_HOT_BUILDTYPE, buildtype).commit();
    }

    public String getFind_hot_buildtype() {
        //如果没有缓存,则返回一个提示
        return getSp().getString(FIND_HOT_BUILDTYPE, Constants.FIND_BUILDTYPE_PROMPT);
    }

    //热点查询的价格区间
    public void setFind_hot_price(String price) {
        getEdit().putString(FIND_HOT_PRICE, price).commit();
    }

    public String getFind_hot_price() {
        //如果没有缓存,则返回一个提示
        return getSp().getString(FIND_HOT_PRICE, Constants.FIND_PRICE_PROMPT);
    }

    //热点查询的排序方式
    public void setFind_hot_sort(String sort) {
        getEdit().putString(FIND_HOT_SORT, sort).commit();
    }

    public String getFind_hot_sort() {
        //如果没有缓存,则返回一个提示
        return getSp().getString(FIND_HOT_SORT, Constants.FIND_SORT_PROMPT);
    }

    //摇评测范围
    public String getYao_scope_eval() {
        return getSp().getString(YAO_SCOPE_EVAL, "");
    }

    public void setYao_scope_eval(String scope_eval) {
        getEdit().putString(YAO_SCOPE_EVAL, scope_eval).commit();
    }

    //摇加群范围
    public String getYao_scope_group() {
        return getSp().getString(YAO_SCOPE_GROUP, "");
    }

    public void setYao_scope_group(String scope_group) {
        getEdit().putString(YAO_SCOPE_GROUP, scope_group).commit();
    }

    //摇一摇的音效开关
    public String getYao_music_on() {
        return getSp().getString(YAO_MUSIC_ON, "");  //true,false
    }

    public void setYao_music_on(String music_on) {
        getEdit().putString(YAO_MUSIC_ON, music_on).commit();
    }

    //保存用户注册时间
    public void setUser_register_time(Long time) {
        getEdit().putLong("register_time", time).commit();
    }

    public Long getUser_register_time() {
        return getSp().getLong("register_time", 0);
    }

    //保存用户权限，即身份：置业者(null或为空),顾问，管理员
    public void setUser_rolename(String role) {
        getEdit().putString("User_rolename", role).commit();
    }

    public String getUser_rolename() {
        return getSp().getString("User_rolename", null);
    }

    //保存GPS设置
    public void setApp_setting_GPS(int time) {
        getEdit().putInt("setting_gps", time).commit();
    }

    public int getApp_setting_GPS() {
        return getSp().getInt("setting_gps", 0);
    }

    //保存GPS的选择位置信息
    public void setGPS_Position(int position) {
        getEdit().putInt("gps_position", position).commit();
    }

    public int getGPS_Position() {
        return getSp().getInt("gps_position", 0);
    }

    //保存字体设置
    public void setApp_setting_FontSize(int time) {
        getEdit().putInt("setting_fontsize", time).commit();
    }

    public int getApp_setting_FontSize() {
        return getSp().getInt("setting_fontsize", 0);
    }

    //保存FontSize的选择位置信息
    public void setFont_Position(int position) {
        getEdit().putInt("font_position", position).commit();
    }

    public int getFont_Position() {
        return getSp().getInt("font_position", 0);
    }

    //保存通知设置
    public void setApp_setting_Notity_1(boolean flag) {
        getEdit().putBoolean("NO_qiandao_pinglun", flag).commit();
    }

    public boolean getApp_setting_Notity_1() {
        return getSp().getBoolean("NO_qiandao_pinglun", false);
    }

    public void setApp_setting_Notity_2(boolean flag) {
        getEdit().putBoolean("qiandao_huodong", flag).commit();
    }

    public boolean getApp_setting_Notity_2() {
        return getSp().getBoolean("qiandao_huodong", false);
    }

    public void setApp_setting_Notity_3(boolean flag) {
        getEdit().putBoolean("g_qiandao_pinglun", flag).commit();
    }

    public boolean getApp_setting_Notity_3() {
        return getSp().getBoolean("g_qiandao_pinglun", false);
    }

    //引导页的设置
    //区域
    public void setGuideAreaSel(String area_sel) {
        getEdit().putString(GUIDE_AREA_SEL, area_sel).commit();
    }

    public String getGuideAreaSel() {
        return getSp().getString(GUIDE_AREA_SEL, "");
    }

    //户型
    public void setGuideHuxingSel(String huxing_sel) {
        getEdit().putString(GUIDE_HUXING_SEL, huxing_sel).commit();
    }

    public String getGuideHuxingSel() {
        return getSp().getString(GUIDE_HUXING_SEL, "");
    }

    //价格区间
    public void setGuidePriceSel(String price_sel) {
        getEdit().putString(GUIDE_PRICE_SEL, price_sel).commit();
    }

    public String getGuidePriceSel() {
        return getSp().getString(GUIDE_PRICE_SEL, "");
    }

    //配套
    public void setGuideSupportSel(String support_sel) {
        getEdit().putString(GUIDE_SUPPORT_SEL, support_sel).commit();
    }

    public String getGuideSupportSel() {
        return getSp().getString(GUIDE_SUPPORT_SEL, "");
    }


    public String getManagerEvalReq_oper() {
        return getSp().getString("getManagerEvalReq_oper", "");
    }

    public void setManagerEvalReq_oper(String s) {
        getEdit().putString("getManagerEvalReq_oper", s).commit();
    }


    //保存匹配参数
    public String getMatePara_area() {
        return getSp().getString("getMatePara_area", "");
    }

    public void setMatePara_area(String s) {
        getEdit().putString("getMatePara_area", s).commit();
    }

    public String getMatePara_huxing() {
        return getSp().getString("getMatePara_huxing", "");
    }

    public void setMatePara_huxing(String s) {
        getEdit().putString("getMatePara_huxing", s).commit();
    }

    public String getMatePara_price() {
        return getSp().getString("getMatePara_price", "");
    }

    public void setMatePara_price(String s) {
        getEdit().putString("getMatePara_price", s).commit();
    }

    public String getMatePara_peitao() {
        return getSp().getString("getMatePara_peitao", "");
    }

    public void setMatePara_peitao(String s) {
        getEdit().putString("getMatePara_peitao", s).commit();
    }

    public String getMatePara_loupan() {
        return getSp().getString("getMatePara_loupan", null);
    }

    public void setMatePara_loupan(String s) {
        getEdit().putString("getMatePara_loupan", s).commit();
    }

    public String getMatePara_reqtype() {
        return getSp().getString("getMatePara_reqtype", null);
    }

    public void setMatePara_reqtype(String s) {
        getEdit().putString("getMatePara_reqtype", s).commit();
    }

    public String getMatePara_city() {
        return getSp().getString("getMatePara_city", null);
    }

    public void setMatePara_city(String s) {
        getEdit().putString("getMatePara_city", s).commit();
    }

    public void setMatePara(String area, String huxing, String price, String peitao, String loupan, String reqtyoe, String city) {
        instance_.setMatePara_area(area);
        instance_.setMatePara_huxing(huxing);
        instance_.setMatePara_price(price);
        instance_.setMatePara_peitao(peitao);
        instance_.setMatePara_loupan(loupan);
        instance_.setMatePara_reqtype(reqtyoe);
        instance_.setMatePara_city(city);
    }


}
