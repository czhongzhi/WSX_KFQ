package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.IM_Util.LoginOpenfire;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.adapter.MainNewMsgAdapter;
import com.inetgoes.kfqbrokers.addressbook.QuickLocationMainActivity;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.BrokerInfo;
import com.inetgoes.kfqbrokers.model.KfqMessage;
import com.inetgoes.kfqbrokers.model.LoginInfo;
import com.inetgoes.kfqbrokers.model.NewMsg;
import com.inetgoes.kfqbrokers.model.PopupShowCell;
import com.inetgoes.kfqbrokers.model.PopupShowFail;
import com.inetgoes.kfqbrokers.model.PopupShowLoupan;
import com.inetgoes.kfqbrokers.model.PopupShowMate;
import com.inetgoes.kfqbrokers.service.PushService;
import com.inetgoes.kfqbrokers.utils.AppUtil;
import com.inetgoes.kfqbrokers.utils.BitmapUtil;
import com.inetgoes.kfqbrokers.utils.DateFormatHelp;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.utils.SoundPoolUtil;
import com.inetgoes.kfqbrokers.view.LeftDrawerLayout;
import com.inetgoes.kfqbrokers.view.MyCircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    public static final String KFQMSG = "kfqmessage";

    public static MainActivity activity;
    public static int grabTime = 20;
    public static int cellTime = 180;
    private static boolean isStopMateThread = false;
    private static boolean isStopCellThread = false;

    private LinearLayout llMainMessageing;
    private TextView tvMessageOrderOnlinetime;
    private TextView tvMessageEvaOrder;
    private TextView tvMessageBrokerhousenum;
    private LinearLayout llUploadHousesource;  //原设计是上传房源，现改为添加客户 by czz
    private LinearLayout llMyHouseSource;
    private LinearLayout llMyAsset;//我的资产
    private Button mainShowPopup;
    private Button mainBtRest;
    private ListView new_msg_list;

    private BrokerInfo brokerInfo;

    private int userid;
    private MyCircleImageView user_icon;
    private TextView user_name;
    private RatingBar user_star;
    private TextView user_score;
    private TextView yuyue_num;
    private TextView dingdan_num;

    private LinearLayout menu_left;
    private LeftDrawerLayout left_menu;
    private View itemView;
    private ImageView updata_icon_main; //主页新信息红点提醒
    private ImageView updata_icon_left_msg; //菜单我的消息红点提醒

    private static List<NewMsg> newMsgs = new ArrayList<>();
    private MainNewMsgAdapter adapter;

    private PopupWindow popupWindow;
    private PopupWindow popupWindow_kf;
    private FrameLayout main_all_bgchage;
    private FrameLayout main_popup_bgchange;
    private FrameLayout msg_top;
    private FrameLayout msg_bottom;
    private LinearLayout msg_all;
    //
    private ImageView call_user;
    //
    private LinearLayout p_top_layout_1, p_top_layout_2, p_top_layout_3, p_top_layout_4, p_top_layout_5, p_top_layout_6;
    private LinearLayout p_bottom_layout_1, p_bottom_layout_3;
    private RelativeLayout p_bottom_layout_2;
    private TextView mate_title, mate_area, mate_price, mate_huxing, mate_peitao;

    private LoginInfo loginInfo;
    private Integer qiangordernum_today;//今日抢单数量
    private Float onlinetime_today;   //今日在线时间
    private Integer bookingnum_today;   //今日预约数量
    private Integer ordernum_today;     //今日看房订单数量
    private Integer fangtotal;          //我的房源数量

    private PopupShowMate popupShowMate;
    private PopupShowCell popupShowCell;
    private PopupShowFail popupShowFail;
    private PopupShowLoupan popupShowLoupan;
    private PopupShowLoupan popupShowLoupan_kf;

    public static final int Close_Popup = 1310;
    public static final int Grab_Buttom = 1314;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Close_Popup:
                    closePopup(true);
                    break;
                case Grab_Buttom://抢单倒计时更新
                    mainShowPopup.setText(Html.fromHtml((String) msg.obj));
                    break;
                case LoginOpenfire.MSG_QIANG_DAN:
                    dealNotityMsg_grad(msg);//处理抢单通知
                    break;
                case LoginOpenfire.MSG_PAI_DAN:
                    dealNotityMsg_paidan(msg);//处理派单通知
                    break;
                case LoginOpenfire.MSG_CALL_NOTIFY://打电话通知
                    dealNotifyMsg_callnotify(msg);
                    break;
                case LoginOpenfire.MSG_CANCEL_NOTIFY://取消预约通知
                    dealNotifyMsg_Yuyue_Cancel(msg);
                    break;
                case LoginOpenfire.MSG_SELECTOTTHER_NOTIFY://订单已被抢
                    dealNotifyMsg_selectother(msg);
                    break;
                case LoginOpenfire.MSG_KF_REQ://看房订单请求通知
                    dealNotifyMsg_KFreq(msg);
                    break;
                case LoginOpenfire.MSG_KF_CANCEL_F_USER://看房订单被用户取消通知
                    break;
                case LoginOpenfire.MSG_KFANDYUYUE_NOTIFY://看房订单和预约同时请求的通知
                    dealNotifyMsg_KFandBroker(msg);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        LoginOpenfire.isMainShow = true;
        activity = MainActivity.this;
        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();

        setLeftMenuInfo();

        initView();

        loginInfo = (LoginInfo) getIntent().getSerializableExtra("loginInfo");
        if (null == loginInfo) {
            getInitializeData(userid);
        } else {
            initData();
        }

        if (TextUtils.isEmpty(AppSharePrefManager.getInstance(this).getLastest_login_username())) {
            //网络获取用户信息
            getBrokerInfo(userid);
        } else {
            user_name.setText(AppSharePrefManager.getInstance(this).getLastest_login_username());
            String iconString = AppSharePrefManager.getInstance(this).getLastest_login_touxiang_imagebase64();
            if (!TextUtils.isEmpty(iconString)) {
                Bitmap bitmap = BitmapUtil.base64ToBitmap(iconString);
                user_icon.setImageBitmap(bitmap);
            }
            float temp = AppSharePrefManager.getInstance(this).getLastest_Starlevel();
            user_score.setText(String.valueOf(temp));
            user_star.setRating(temp);
            yuyue_num.setText("预约 " + AppSharePrefManager.getInstance(this).getLastest_Appointmentnum());
            dingdan_num.setText("订单 " + AppSharePrefManager.getInstance(this).getLastest_Ordernum());
        }

        updataIcon_MyMsg();

        Intent pushService = new Intent(PushService.ACTION);
        pushService.setPackage(getPackageName());
        startService(pushService);


        infoNotify();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginOpenfire.isMainShow = true;
        activity = MainActivity.this;
        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        activity = MainActivity.this;
        updataIcon_MyMsg();

        infoNotify();
    }

    private void infoNotify(){
            KfqMessage kfqMsg = (KfqMessage) getIntent().getSerializableExtra(KFQMSG);
            if (null != kfqMsg) {
                int type = LoginOpenfire.getDealType(kfqMsg.getMsgtype(),kfqMsg.getNotifytype());
                if(type == LoginOpenfire.MSG_QIANG_DAN){
                    int time = DateFormatHelp.timeDiffer(kfqMsg.getCreatetime(),System.currentTimeMillis());
                    L.LogI("time is " + time + "s");
                    if(time > 20){
                        return;
                    }else{
                        grabTime = time;
                    }
                }else if(type == LoginOpenfire.MSG_CALL_NOTIFY){
                    int time = DateFormatHelp.timeDiffer(kfqMsg.getCreatetime(),System.currentTimeMillis());
                    L.LogI("time is " + time + "s");
                    if(time > 180){
                        return;
                    }else{
                        cellTime = time;
                    }
                }

                Message msg = new Message();
                msg.what = type;
                msg.obj = kfqMsg;
                mHandler.sendMessage(msg);
            }
    }

    private void initView() {
        left_menu = (LeftDrawerLayout) findViewById(R.id.id_menu);
        left_menu.setTag(false);
        findViewById(R.id.leftmenu_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("czhongzhi", "openDrawer");
                left_menu.openDrawer();
            }
        });
        updata_icon_main = (ImageView) findViewById(R.id.leftmenu_badge);

        llMainMessageing = (LinearLayout) findViewById(R.id.ll_main_messageing);
        tvMessageOrderOnlinetime = (TextView) findViewById(R.id.tv_message_order_onlinetime);
        tvMessageEvaOrder = (TextView) findViewById(R.id.tv_message_eva_order);
        tvMessageBrokerhousenum = (TextView) findViewById(R.id.tv_message_brokerhousenum);
        llUploadHousesource = (LinearLayout) findViewById(R.id.ll_upload_housesource);  //原设计是上传房源，现改为添加客户 by czz
        llUploadHousesource.setOnClickListener(upLoadHousesListener);
        llMyHouseSource = (LinearLayout) findViewById(R.id.main_my_housesource);
        llMyHouseSource.setOnClickListener(myHouseListenenr);
        llMyAsset = (LinearLayout) findViewById(R.id.main_my_asset);
        llMyAsset.setOnClickListener(myAssetListenenr);
        new_msg_list = (ListView) findViewById(R.id.new_msg_list);
        adapter = new MainNewMsgAdapter(this, newMsgs);
        new_msg_list.setAdapter(adapter);

        mainShowPopup = (Button) findViewById(R.id.main_show_popup);//抢单按钮
        mainShowPopup.setTag(START);
        mainBtRest = (Button) findViewById(R.id.main_bt_rest);//休息按钮
        main_all_bgchage = (FrameLayout) findViewById(R.id.main_all_bgchage);
        main_popup_bgchange = (FrameLayout) findViewById(R.id.main_popup_bgchange);

        mainShowPopup.setOnClickListener(grabListener);
        mainBtRest.setOnClickListener(restListener);
        if (FangApplication.mainButtomState.equals("ready")) {
            mainBtRest.setVisibility(View.INVISIBLE);
            mainShowPopup.setText("开始接单");
        } else {
            mainBtRest.setVisibility(View.VISIBLE);
            mainShowPopup.setText("抢单中");
        }
    }

    private void initData() {

        if (null != loginInfo) {
            qiangordernum_today = loginInfo.getQiangordernum_today();
            onlinetime_today = loginInfo.getOnlinetime_today();

            bookingnum_today = loginInfo.getBookingnum_today();
            ordernum_today = loginInfo.getOrdernum_today();

            fangtotal = loginInfo.getFangtotal();

            tvMessageOrderOnlinetime.setText("抢单" + qiangordernum_today + "单     在线" + onlinetime_today + "小时");
            tvMessageEvaOrder.setText("有" + bookingnum_today + "人预约   看房订单" + ordernum_today + "单");
            tvMessageBrokerhousenum.setText(String.valueOf(fangtotal));
        }
    }

    /**
     * 我的资产
     */
    private View.OnClickListener myAssetListenenr = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this,MyAssetActivity.class));
        }
    };


    /**
     * 我的房源
     */
    private View.OnClickListener myHouseListenenr = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, HouseSourceListActivity.class));
        }
    };


    /**
     * 上传房源  //原设计是上传房源，现改为添加客户 by czz
     */
    private View.OnClickListener upLoadHousesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //startActivity(new Intent(MainActivity.this, UploadHouseSourceMainActivity.class));
            startActivity(new Intent(MainActivity.this, AddClientActivity.class));
        }
    };

    private static final int START = 0;//开始接单
    private static final int GRABING = 1;//抢单运行中
    private static final int FINISH = 2;//抢单完成
    private static final int NOCLICK = 3; //按钮点击没有效果

    /**
     * 抢单点击监听
     */
    private View.OnClickListener grabListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int state = (int) mainShowPopup.getTag();
            switch (state) {
                case START:
                    startGrabListener();
                    break;
                case GRABING:
                    grabingCurrClick();
                    break;
                case FINISH:
                    finishCurrClick();
                    break;
                case NOCLICK:

                    break;
            }
        }
    };

    /**
     * 休息点击监听
     */
    private View.OnClickListener restListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switchRun(userid, "ready", "开始接单", START);
        }
    };

    /**
     * 开始监听操作
     */
    private void startGrabListener() {
        switchRun(userid, "process", "抢单中", NOCLICK);
    }

    /**
     * 抢当前显示的需求单
     */
    private void grabingCurrClick() {
        reqClickGrab(userid, popupShowMate.getTranid());
    }


    /**
     * 完成当前订单操作
     */
    private void finishCurrClick() {
        closePopup(true);
        closePopupKf();
    }

    private void getInitializeData(int userid) {
        //Map<String,Object> map = new HashMap<>();
        //map.put("id",userid);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("getInitializeData onPostExecute is " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    loginInfo = JacksonMapper.getObjectMapper().readValue(result, LoginInfo.class);
                    initData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.initializeUrl + "?id=" + userid);
    }

    /**
     * 我的消息 红点提示
     */
    private void updataIcon_MyMsg() {
        L.LogE("我的消息 红点提示");
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                if (!TextUtils.isEmpty(result)) {
                    String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                    L.LogE("updataIncon_mymsg is state = " + state);
                    changeRedUdataIcon("true".equals(state));
                }
            }
        }).execute(Constants.updataNewMsgHintUrl + "?userid=" + userid);
    }

    public void changeRedUdataIcon(boolean flag) {
        if (flag) {//显示红点
            updata_icon_main.setVisibility(View.VISIBLE);
            updata_icon_left_msg.setVisibility(View.VISIBLE);
        } else {
            updata_icon_main.setVisibility(View.INVISIBLE);
            updata_icon_left_msg.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 经纪人运行模式切换接口
     *
     * @param userid
     * @param currstate 值有: ready, process
     */
    private void switchRun(int userid, final String currstate, final String bottomText, final int bClickType) {
        final Dialog dialog = DialogUtil.showWait(MainActivity.this);
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("currstate", currstate);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("switchRun onPostExecute is " + result);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                if (((boolean) resMap.get("state"))) {
                    mainShowPopup.setText(bottomText);
                    mainShowPopup.setTag(bClickType);
                    FangApplication.mainButtomState = currstate;

                    if (currstate.equals("ready")) {
                        mainBtRest.setVisibility(View.INVISIBLE);
                        SoundPoolUtil.getInstance().play(MainActivity.this, R.raw.stop_grab);
                    } else {
                        SoundPoolUtil.getInstance().play(MainActivity.this, R.raw.start_grab);
                        mainBtRest.setVisibility(View.VISIBLE);
                    }
                }
            }
        }).execute(Constants.switchRunUrl, map);
    }

    /**
     * 点击抢单按钮，请求网络
     */
    private void reqClickGrab(int userid, String tranid) {
        final Dialog waitDialog = DialogUtil.showWait(MainActivity.this);
        final Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("tranid", tranid);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                if (waitDialog != null && waitDialog.isShowing()) {
                    waitDialog.dismiss();
                }
                L.LogE("reqClickGrab onPostExecute is " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, Object> resultMap = JacksonMapper.getInstance().mapObjFromJson(result);
                if (((boolean) resultMap.get("state"))) {
                    //改变popupwin内容，显示你已抢到单
                    {
                        p_top_layout_1.setVisibility(View.GONE);
                        p_top_layout_2.setVisibility(View.VISIBLE);
                        mainShowPopup.setTag(NOCLICK);
                        mainShowPopup.setText("进行中");
                    }
                    isStopMateThread = true;
                } else {
                    //提示抢单失败
                    String fail = (String) resultMap.get("reason");
                    Toast.makeText(MainActivity.this, (TextUtils.isEmpty(fail) ? "抢单失败" : fail), Toast.LENGTH_SHORT).show();
                    //关闭pupopwin
                    MainActivity.this.closePopup(true);
                }
            }
        }).execute(Constants.clickGrabButtonUrl, map);
    }

    /**
     * 打电话接口  (预约确认页面,点击打电话)
     *
     * @param tranid
     */
    private void clickCellBut(String tranid) {
        Map<String, Object> map = new HashMap<>();
        map.put("tranid", tranid);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("getInitializeData onPostExecute is " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }).execute(Constants.clickCellButtonUrl, map);
    }


    /**
     * 展示normal
     */
    private void showPopup(final View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.main_popup_msg_normal, null);
        popupWindow = new PopupWindow(view, AppUtil.dp2px(343, this), AppUtil.dp2px(422, this));
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        view.findViewById(R.id.popup_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopup(true);
            }
        });

        msg_top = (FrameLayout) view.findViewById(R.id.msg_top);
        msg_bottom = (FrameLayout) view.findViewById(R.id.msg_bottom);

        p_top_layout_1 = (LinearLayout) view.findViewById(R.id.p_top_layout_1);
        p_top_layout_2 = (LinearLayout) view.findViewById(R.id.p_top_layout_2);
        p_top_layout_3 = (LinearLayout) view.findViewById(R.id.p_top_layout_3);
        p_top_layout_4 = (LinearLayout) view.findViewById(R.id.p_top_layout_4);
        p_top_layout_5 = (LinearLayout) view.findViewById(R.id.p_top_layout_5);
        p_top_layout_6 = (LinearLayout) view.findViewById(R.id.p_top_layout_6);

        call_user = (ImageView) p_top_layout_3.findViewById(R.id.call_user);

        p_bottom_layout_1 = (LinearLayout) view.findViewById(R.id.p_bottom_layout_1);
        p_bottom_layout_2 = (RelativeLayout) view.findViewById(R.id.p_bottom_layout_2);
        p_bottom_layout_3 = (LinearLayout) view.findViewById(R.id.p_bottom_layout_3);

        mate_title = (TextView) p_bottom_layout_1.findViewById(R.id.mate_title);
        mate_area = (TextView) p_bottom_layout_1.findViewById(R.id.mate_area);
        mate_price = (TextView) p_bottom_layout_1.findViewById(R.id.mate_price);
        mate_huxing = (TextView) p_bottom_layout_1.findViewById(R.id.mate_huxing);
        mate_peitao = (TextView) p_bottom_layout_1.findViewById(R.id.mate_peitao);

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = view.getMeasuredWidth();
        int popupHeight = view.getMeasuredHeight();

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        main_popup_bgchange.setBackgroundResource(R.color.popup_bgchange);
        main_popup_bgchange.setClickable(true);
        mainBtRest.setVisibility(View.INVISIBLE);

//      popupWindow.showAsDropDown(v);
//      popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2 + 20, location[1] - popupHeight);
    }

    /**
     * 展示kanfang
     */
    private void showPopupKF(final View v) {
        View view = LayoutInflater.from(this).inflate(R.layout.main_popup_msg_kanfang, null);
        popupWindow_kf = new PopupWindow(view, AppUtil.dp2px(343, this), AppUtil.dp2px(422, this));
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        view.findViewById(R.id.popup_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupKf();
            }
        });

        msg_all = (LinearLayout) view.findViewById(R.id.msg_all);
        msg_all.findViewById(R.id.p_bottom_layout_2).setVisibility(View.VISIBLE);

        popupWindow_kf.setFocusable(true);
        popupWindow_kf.setOutsideTouchable(true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = view.getMeasuredWidth();
        int popupHeight = view.getMeasuredHeight();

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        main_all_bgchage.setBackgroundResource(R.color.popup_bgchange);

        popupWindow_kf.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2 + 20, location[1] - popupHeight - 30);
    }

    /**
     * 关闭
     */
    private void closePopup(boolean isUpButtomText) {
        isStopMateThread = true;
        isStopCellThread = true;
        grabTime = 20;
        cellTime = 180;
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();
            mainBtRest.setVisibility(View.VISIBLE);
            main_popup_bgchange.setBackgroundResource(R.color.transparent);
            main_popup_bgchange.setClickable(false);
            popupWindow = null;
        }
        if (isUpButtomText) {
            mainShowPopup.setText("抢单中");
            mainShowPopup.setTag(NOCLICK);
        }
    }

    private void closePopupKf() {
        if (null != popupWindow_kf && popupWindow_kf.isShowing()) {
            popupWindow_kf.dismiss();
            main_all_bgchage.setBackgroundResource(R.color.transparent);
            popupWindow_kf = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoginOpenfire.isMainShow = false;
        this.activity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginOpenfire.isMainShow = false;
        this.activity = null;
    }


    private void dealNotityMsg_paidan(Message msg) {
//        mainShowPopup.setTag(GRABING);
//        showPopup(mainShowPopup);
    }

    private void dealNotityMsg_grad(Message msg) {
        KfqMessage kfqMessage = (KfqMessage) msg.obj;
        if (popupWindow == null) {
            reqShowMate(userid, kfqMessage.getSessionid());
        }

        newMsgs.add(0, new NewMsg(kfqMessage.getCreatetime(), kfqMessage.getTicker()));
        adapter.notifyDataSetChanged();
    }

    private void dealNotifyMsg_callnotify(Message msg) {
        KfqMessage kfqMessage = (KfqMessage) msg.obj;
        reqShowCell(userid, kfqMessage.getSessionid());

        newMsgs.add(0, new NewMsg(kfqMessage.getCreatetime(), kfqMessage.getTicker()));
        adapter.notifyDataSetChanged();
    }

    private void dealNotifyMsg_selectother(Message msg) {
        KfqMessage kfqMessage = (KfqMessage) msg.obj;
        reqShowFail(userid, kfqMessage.getSessionid());

        newMsgs.add(0, new NewMsg(kfqMessage.getCreatetime(), kfqMessage.getTicker()));
        adapter.notifyDataSetChanged();
    }

    private void dealNotifyMsg_KFandBroker(Message msg) {
        KfqMessage kfqMessage = (KfqMessage) msg.obj;
        reqShowKFandBroker(userid, kfqMessage.getSessionid(), kfqMessage.getNewcode());

        newMsgs.add(0, new NewMsg(kfqMessage.getCreatetime(), kfqMessage.getTicker()));
        adapter.notifyDataSetChanged();
    }

    private void dealNotifyMsg_KFreq(Message msg) {
        KfqMessage kfqMessage = (KfqMessage) msg.obj;
        reqShowKFreq(userid, kfqMessage.getSessionid(), kfqMessage.getNewcode());

        newMsgs.add(0, new NewMsg(kfqMessage.getCreatetime(), kfqMessage.getTicker()));
        adapter.notifyDataSetChanged();
    }

    private void dealNotifyMsg_Yuyue_Cancel(Message msg) {
        closePopup(true);
        KfqMessage kfqMessage = (KfqMessage) msg.obj;
        newMsgs.add(0, new NewMsg(kfqMessage.getCreatetime(), kfqMessage.getTicker()));
        adapter.notifyDataSetChanged();
    }

    private void fillDataPopupMate() {
        if (null == popupShowMate) {
            return;
        }

        left_menu.closeDrawer();

        SoundPoolUtil.getInstance().play(MainActivity.this, R.raw.hint, R.raw.y_grab_dan);
        mate_title.setText(popupShowMate.getCond_loupanname());
        mate_area.setText(popupShowMate.getCond_area());
        String price = MainActivity.recomPrice(popupShowMate.getCond_price_low(), popupShowMate.getCond_price_high());
        mate_price.setText(price);
        mate_huxing.setText(TextUtils.isEmpty(popupShowMate.getCond_huxing()) ? "不限" : popupShowMate.getCond_huxing());
        mate_peitao.setText(TextUtils.isEmpty(popupShowMate.getCond_peitao()) ? "不限" : popupShowMate.getCond_peitao());
    }

    private void fillDataPopupCell() {
        changeViewShow(2, 0);
        if (null == popupShowCell) {
            return;
        }
        SoundPoolUtil.getInstance().play(MainActivity.this, R.raw.hint, R.raw.cell_hint);
        mate_title.setText(popupShowCell.getCond_loupanname());
        mate_area.setText(popupShowCell.getCond_area());
        String price = MainActivity.recomPrice(popupShowCell.getCond_price_low(), popupShowCell.getCond_price_high());
        mate_price.setText(price);
        mate_huxing.setText(TextUtils.isEmpty(popupShowCell.getCond_huxing()) ? "不限" : popupShowCell.getCond_huxing());
        mate_peitao.setText(TextUtils.isEmpty(popupShowCell.getCond_peitao()) ? "不限" : popupShowCell.getCond_peitao());

        call_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.call2(MainActivity.this, popupShowCell.getCellphone());
                //
                clickCellBut(popupShowCell.getTranid());
                //
                if (popupWindow != null && popupWindow.isShowing()) {
                    p_top_layout_3.setVisibility(View.GONE);
                    p_top_layout_4.setVisibility(View.VISIBLE);
                    mainShowPopup.setTag(FINISH);
                    mainShowPopup.setText("完成");
                    isStopCellThread = true;
                    p_top_layout_4.findViewById(R.id.call_new).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, HouseSourceListPushActivity.class);
                            intent.putExtra("sessionid", popupShowCell.getTranid());
                            intent.putExtra("requserid", popupShowCell.getRequserid());
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void fillDataPopupFail() {
        changeViewShow(5, 2);
        if (popupShowFail != null) {
            SoundPoolUtil.getInstance().play(MainActivity.this, R.raw.hint, R.raw.hint_fail);
            View bView = msg_bottom.getChildAt(2);
            if (!TextUtils.isEmpty(popupShowFail.getMyimage())) {
                ImageLoader.getInstance().displayImage(popupShowFail.getMyimage(), (ImageView) bView.findViewById(R.id.me_icon), FangApplication.options, FangApplication.animateFirstListener);
            }
            if (!TextUtils.isEmpty(popupShowFail.getOtherimage())) {
                ImageLoader.getInstance().displayImage(popupShowFail.getOtherimage(), (ImageView) bView.findViewById(R.id.you_icon), FangApplication.options, FangApplication.animateFirstListener);
            }

            ((TextView) bView.findViewById(R.id.me_name)).setText(popupShowFail.getMyname());
            ((TextView) bView.findViewById(R.id.me_srcoe)).setText(String.valueOf(popupShowFail.getMystarlevel()));

            ((TextView) bView.findViewById(R.id.you_name)).setText(popupShowFail.getOthername());
            ((TextView) bView.findViewById(R.id.you_srcoe)).setText(String.valueOf(popupShowFail.getOtherstarlevel()));
        }
    }

    private void fillDataPopupKFandBroker() {
        changeViewShow(4, 1);
        if (popupShowLoupan != null) {
            SoundPoolUtil.getInstance().play(MainActivity.this, R.raw.hint, R.raw.hint_yuandkf);
            View tView = msg_top.getChildAt(4);
            tView.findViewById(R.id.call_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.call2(MainActivity.this, popupShowLoupan.getCellphone());
                    //
                    if (popupWindow != null && popupWindow.isShowing()) {
                        p_top_layout_5.setVisibility(View.GONE);
                        p_top_layout_4.setVisibility(View.VISIBLE);
                        mainShowPopup.setTag(FINISH);
                        mainShowPopup.setText("完成");
                        isStopCellThread = true;
                        p_top_layout_4.findViewById(R.id.call_new).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, HouseSourceListPushActivity.class);
                                intent.putExtra("sessionid", popupShowLoupan.getTranid());
                                intent.putExtra("requserid", popupShowLoupan.getRequserid());
                                startActivity(intent);
                            }
                        });
                    }
                }
            });
            View bView = msg_bottom.getChildAt(1);
            if (!TextUtils.isEmpty(popupShowLoupan.getLoupan_image_url())) {
                ImageLoader.getInstance().displayImage(popupShowLoupan.getLoupan_image_url(), (ImageView) bView.findViewById(R.id.item_iv_fangyuan), FangApplication.options, FangApplication.animateFirstListener);
            }
            ((TextView) bView.findViewById(R.id.item_tv_fangyuan1)).setText(popupShowLoupan.getLoupanname());
            ((TextView) bView.findViewById(R.id.item_tv_fangyuan2)).setText("看房次数：" + popupShowLoupan.getKanfang_time());
            ((TextView) bView.findViewById(R.id.item_tv_fangyuan3)).setText("成交量：" + popupShowLoupan.getTran_success_num());
            ((TextView) bView.findViewById(R.id.item_tv_fangyuan4)).setText(popupShowLoupan.getPricedesc());
            ((TextView) bView.findViewById(R.id.item_tv_fangyuan5)).setText(popupShowLoupan.getDistrict());

        }
    }

    private void changeViewShow(int topViewId, int bottomViewId) {
        if (popupWindow == null)
            return;
        int l_t = msg_top.getChildCount();
        for (int i = 0; i < l_t; i++) {
            msg_top.getChildAt(i).setVisibility(View.GONE);
        }
        msg_top.getChildAt(topViewId).setVisibility(View.VISIBLE);

        int l_b = msg_bottom.getChildCount();
        for (int i = 0; i < l_b; i++) {
            msg_bottom.getChildAt(i).setVisibility(View.GONE);
        }
        msg_bottom.getChildAt(bottomViewId).setVisibility(View.VISIBLE);
    }

    private void fillDataPopupKF_REQ() {
        if (null != popupWindow_kf && popupWindow_kf.isShowing()) {
            if (popupShowLoupan_kf != null) {
                SoundPoolUtil.getInstance().play(MainActivity.this, R.raw.hint, R.raw.hint_kf);
                msg_all.findViewById(R.id.call_user).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppUtil.call2(MainActivity.this, popupShowLoupan_kf.getCellphone());
                        clickCellBut(popupShowLoupan_kf.getTranid());
                    }
                });
                msg_all.findViewById(R.id.call_new).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "推荐房源", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, HouseSourceListPushActivity.class);
                        intent.putExtra("sessionid", popupShowLoupan_kf.getTranid());
                        intent.putExtra("requserid", popupShowLoupan_kf.getRequserid());
                        startActivity(intent);
                    }
                });

                if (!TextUtils.isEmpty(popupShowLoupan_kf.getLoupan_image_url())) {
                    ImageLoader.getInstance().displayImage(popupShowLoupan_kf.getLoupan_image_url(), (ImageView) msg_all.findViewById(R.id.item_iv_fangyuan), FangApplication.options, FangApplication.animateFirstListener);
                }
                ((TextView) msg_all.findViewById(R.id.item_tv_fangyuan1)).setText(popupShowLoupan_kf.getLoupanname());
                ((TextView) msg_all.findViewById(R.id.item_tv_fangyuan2)).setText("看房次数：" + popupShowLoupan_kf.getKanfang_time());
                ((TextView) msg_all.findViewById(R.id.item_tv_fangyuan3)).setText("成交量：" + popupShowLoupan_kf.getTran_success_num());
                ((TextView) msg_all.findViewById(R.id.item_tv_fangyuan4)).setText(popupShowLoupan_kf.getPricedesc());
                ((TextView) msg_all.findViewById(R.id.item_tv_fangyuan5)).setText(popupShowLoupan_kf.getDistrict());

            }
        }
    }

    /**
     * 获取用户请求的维度
     *
     * @param userid
     * @param tranid
     */
    private void reqShowMate(int userid, String tranid) {
        mainShowPopup.setTag(GRABING);
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("tranid", tranid);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("reqShowMate onPostExecute is " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    popupShowMate = JacksonMapper.getObjectMapper().readValue(result, PopupShowMate.class);

                    //开启popupwin 并 填充popupWin的数据
                    showPopup(mainShowPopup);
                    runGrabTime();
                    fillDataPopupMate();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.showGrabInterfUrl, map);
    }

    /**
     * 获取用户请求的维度
     *
     * @param userid
     * @param tranid
     */
    private void reqShowCell(int userid, String tranid) {
        mainShowPopup.setTag(GRABING);
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("tranid", tranid);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("reqShowMate onPostExecute is " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    popupShowCell = JacksonMapper.getObjectMapper().readValue(result, PopupShowCell.class);

                    //开启popupwin 并 填充popupWin的数据
                    if (null == popupWindow) {
                        showPopup(mainShowPopup);
                    }
                    fillDataPopupCell();
                    runCellTime();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.showCellInterfUrl, map);
    }

    private void reqShowFail(int userid, String tranid) {
        mainShowPopup.setTag(FINISH);
        mainShowPopup.setText("完成");
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("tranid", tranid);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("reqShowFail onPostExecute is " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    popupShowFail = JacksonMapper.getObjectMapper().readValue(result, PopupShowFail.class);
                    //填充数据
                    fillDataPopupFail();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.showFailInterfUrl, map);
    }

    private void reqShowKFandBroker(int userid, String tranid, String newcode) {
        mainShowPopup.setTag(NOCLICK);
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("tranid", tranid);
        map.put("newcode", newcode);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("reqShowFail onPostExecute is " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    popupShowLoupan = JacksonMapper.getObjectMapper().readValue(result, PopupShowLoupan.class);
                    //填充数据
                    fillDataPopupKFandBroker();
                    runCellTime();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.showKFandBrokerInterfUrl, map);
    }

    private void reqShowKFreq(int userid, String tranid, String newcode) {
        mainShowPopup.setTag(NOCLICK);
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("tranid", tranid);
        map.put("newcode", newcode);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("reqShowKFreq onPostExecute is " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    popupShowLoupan_kf = JacksonMapper.getObjectMapper().readValue(result, PopupShowLoupan.class);
                    L.LogE("r " + popupShowLoupan_kf.getLoupanname());
                    //填充数据
                    showPopupKF(mainShowPopup);
                    fillDataPopupKF_REQ();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.showKFreqInterfUrl, map);
    }

    private void runGrabTime() {
        isStopMateThread = false;
        new Thread() {
            @Override
            public void run() {
                while (grabTime > 0 && !isStopMateThread) {
                    String uptext = "<font size='20' color='#fac72f'>" + grabTime-- + "s<br></font><font size='16' color='#fac72f'>抢单</font>";
                    Message msg = new Message();
                    msg.what = Grab_Buttom;
                    msg.obj = uptext;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (grabTime == 0) {
                    mHandler.sendEmptyMessage(Close_Popup);
                }
            }
        }.start();
    }

    private void runCellTime() {
        isStopCellThread = false;
        new Thread() {
            @Override
            public void run() {
                while (cellTime > 0 && !isStopCellThread) {
                    String uptext = "<font size='20' color='#fac72f'>" + cellTime-- + "s<br></font><font size='16' color='#fac72f'>通话等待</font>";
                    Message msg = new Message();
                    msg.what = Grab_Buttom;
                    msg.obj = uptext;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (cellTime == 0) {
                    mHandler.sendEmptyMessage(Close_Popup);
                }
            }
        }.start();
    }

    private void getBrokerInfo(final int brokerid) {
        if (brokerid == 0)
            return;
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        brokerInfo = JacksonMapper.getObjectMapper().readValue(result, BrokerInfo.class);
                        AppUtil.setLocalInfo(MainActivity.this, brokerInfo);
                        user_name.setText(brokerInfo.getName());
                        float temp = (brokerInfo.getStarlevel() == null ? 0.0f : brokerInfo.getStarlevel());
                        user_score.setText(String.valueOf(temp));
                        user_star.setRating(temp);
                        yuyue_num.setText("预约 " + brokerInfo.getAppointmentnum());
                        dingdan_num.setText("订单 " + brokerInfo.getOrdernum());

                        L.LogI("加载横照 1 " + brokerInfo.getUserimage_horizontal());
                        ImageLoader.getInstance().loadImage(brokerInfo.getUserimage_horizontal(), new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                L.LogI("加载横照 2");
                                AppSharePrefManager.getInstance(MainActivity.this).setLastest_hengzhao_imagebase64(BitmapUtil.bitmapToBase64(loadedImage));
                                if (!TextUtils.isEmpty(AppSharePrefManager.getInstance(MainActivity.this).getLastest_hengzhao_imagebase64())) {
                                    L.LogI("横照有值");
                                } else {
                                    L.LogI("横照无值");
                                }
                            }
                        });

                        ImageLoader.getInstance().loadImage(brokerInfo.getUserimage(), new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                user_icon.setImageBitmap(loadedImage);
                                AppSharePrefManager.getInstance(MainActivity.this).setLastest_login_touxiang_imagebase64(BitmapUtil.bitmapToBase64(loadedImage));
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).execute(Constants.userinfoUrl + "?brokerid=" + brokerid);
    }

    public static String recomPrice(String price_low, String price_high) {
        if (TextUtils.isEmpty(price_low) || TextUtils.isEmpty(price_high)) {
            return "";
        }

        if ("9000".equals(price_high)) {
            if ("0".equals(price_low)) {
                return "不限";
            } else if ("1000".equals(price_low)) {
                return "1000万以上";
            }
        }

        if ("300".equals(price_high)) {
            return "300万以下";
        }

        return price_low + "~" + price_high + "万";
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null && popupWindow.isShowing()) {
                return true;
            }
            if (popupWindow_kf != null && popupWindow_kf.isShowing()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /** ----------------------------------下面全是左菜单的代码，不是左菜单代码请勿写在下面--------------------------------------------  **/

    /**
     * 设置左菜单数据内容
     */
    private void setLeftMenuInfo() {
        menu_left = (LinearLayout) findViewById(R.id.menu_left);

        itemView = findViewById(R.id.item_userinfo);//用户信息
        if (itemView != null) {
            user_icon = (MyCircleImageView) itemView.findViewById(R.id.user_icon);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_star = (RatingBar) itemView.findViewById(R.id.user_star);
            user_score = (TextView) itemView.findViewById(R.id.user_score);
            yuyue_num = (TextView) itemView.findViewById(R.id.yuyue_num);
            dingdan_num = (TextView) itemView.findViewById(R.id.dingdan_num);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SetBrokerInfoMainActivity.class);
                    startActivityForResult(intent, 411);
                }
            });
        }

        itemView = findViewById(R.id.item_xiaoxi);//我的消息
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_news);
            ((TextView) itemView.findViewById(R.id.text)).setText("我的消息");
            updata_icon_left_msg = (ImageView) itemView.findViewById(R.id.updata_icon);
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MyMessageActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_yuyue);//我的预约
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_order_l);
            ((TextView) itemView.findViewById(R.id.text)).setText("我的预约");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MyYuYueActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_jingjiren);//我的客户
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_people_l);
            ((TextView) itemView.findViewById(R.id.text)).setText("我的客户");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //left_menu.closeDrawer();
                    //DialogUtil.showHintNoneFunction(MainActivity.this, "提示", "功能暂未开放");
                    startActivity(new Intent(MainActivity.this, QuickLocationMainActivity.class));
                }
            });
        }

        itemView = findViewById(R.id.item_kanfangdingdan);//看房订单
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.join_list);
            ((TextView) itemView.findViewById(R.id.text)).setText("看房订单");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SeeHouseListActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_uphouse);//上传房源
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.join_up);
            ((TextView) itemView.findViewById(R.id.text)).setText("上传房源");

            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, UploadHouseSourceMainActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_eval);//我的房源
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_list);
            ((TextView) itemView.findViewById(R.id.text)).setText("我的房源");

            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, HouseSourceListActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_jingjirenzhaomu);//专家申请
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_test);
            ((TextView) itemView.findViewById(R.id.text)).setText("房产专家申请");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    left_menu.closeDrawer();
                    DialogUtil.showHintNoneFunction(MainActivity.this, "提示", "功能暂未开放");
                }
            });
        }

        itemView = findViewById(R.id.item_yezhu);//推荐经纪人
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_recruit);
            ((TextView) itemView.findViewById(R.id.text)).setText("推荐房产专家");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    left_menu.closeDrawer();
                    DialogUtil.showHintNoneFunction(MainActivity.this, "提示", "功能暂未开放");
                }
            });
        }

        itemView = findViewById(R.id.item_tongzhi);//通知
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_notice);
            ((TextView) itemView.findViewById(R.id.text)).setText("通知");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, NotifyActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_shezhi);//设置
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_settings);
            ((TextView) itemView.findViewById(R.id.text)).setText("设置");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SetAppActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_adout);//关于
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setVisibility(View.GONE);
            ((TextView) itemView.findViewById(R.id.text)).setText("关于");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,AboutKfqActivity.class));
                }
            });
        }


    }

}
