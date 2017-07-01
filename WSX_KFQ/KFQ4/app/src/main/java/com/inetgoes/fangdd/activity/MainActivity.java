package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JavaType;
import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.IM_Util.LoginOpenfire;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.addressbook.QuickLocationMainActivity;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.fragment.NewHouseFragment;
import com.inetgoes.fangdd.fragment.RentHouseFragment;
import com.inetgoes.fangdd.fragment.SecHandFragment;
import com.inetgoes.fangdd.manager.AppSharePrefManager;
import com.inetgoes.fangdd.model.ActivityFromH5;
import com.inetgoes.fangdd.model.BrokerCallHint;
import com.inetgoes.fangdd.model.UserInfo;
import com.inetgoes.fangdd.opencity.CtiyActivity;
import com.inetgoes.fangdd.service.PushService;
import com.inetgoes.fangdd.util.AloneDeal;
import com.inetgoes.fangdd.util.AppUtil;
import com.inetgoes.fangdd.util.BitmapUtil;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.HttpUtil;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.util.L;
import com.inetgoes.fangdd.util.UIHandler;
import com.inetgoes.fangdd.view.LeftDrawerLayout;
import com.inetgoes.fangdd.view.MyCircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements View.OnClickListener {
    public static final String ISSHOWCALLHINT = "isShowCallHint";
    private boolean isShowCallHint = false;

    public static String city = "深圳";
    public static MainActivity mainActivity;

    private TextView area;

    private MyCircleImageView user_icon;
    private TextView user_name;
    private LinearLayout menu_left;
    private LeftDrawerLayout left_menu;
    private ImageView updata_icon_main; //主页新信息红点提醒
    private ImageView updata_icon_left_msg; //菜单我的消息红点提醒

    private View itemView;
    private FrameLayout layout;
    private Button show_popup;
    private Button show_brokerhint;
    String uptext = "<font size='20' color='#fac72f'>预约看房<br></font><font size='14' color='#fac72f'>约30秒回应</font>";
    private PopupWindow popupWindow;
    private PopupWindow waitPopup;
    private RelativeLayout mate_wait_bar;
    private RelativeLayout main_bar;
    private RelativeLayout mate_wait_bar2;
    private FrameLayout dialog_bgchange;
    private LinearLayout broker_call_hint;

    private ImageButton popupwin_close;
    private View popupwin;
    private TextView spinner_area, spinner_price, spinner_huxing, spinner_peitao;
    private EditText edittext_house;
    private int xoff = 0;
    private int isAddStartH = 1;
    private int starth;

    private RadioGroup layout_tap;

    private TextView mate_wait_time;
    public static TextView mate_wait_text;

    private Dialog dialog_cancel;

    public Handler mHandler = new Handler();

    private int userid;  //请求人的用户id
    private String reqtype = "新房"; //请求类型 (值：新房、二手房、租房)
    private UserInfo userInfo;

    private FrameLayout main_framelayout;//
    private FragmentManager mFm;
    private NewHouseFragment newHouseFm;
    private SecHandFragment secHandFm;
    private RentHouseFragment rentHouseFm;

    private ActivityFromH5 activityFromH5;//判断用户是否是H5引流过来的客户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        // ((FangApplication) this.getApplication()).addActivity(this);
        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();

        //获取设备的Device Token
//        String device_token = UmengRegistrar.getRegistrationId(this);
//        Log.e("czhongzhi", "网络友盟设备id " + device_token + " 推送是否开启 -- " + FangApplication.mPushAgent.isEnabled());
//        if (!TextUtils.isEmpty(device_token) && !AppSharePrefManager.getInstance(this).getDevicetoken().equals(device_token)) {
//            Log.e("czhongzhi", "本地保存设备id " + AppSharePrefManager.getInstance(this).getDevicetoken() + " 推送是否开启 -- " + FangApplication.mPushAgent.isEnabled());
//            //更新数据库的设备id
//            Map<String, Object> map = new HashMap<>();
//            map.put("userid", userid);
//            map.put("device_type", "android");
//            map.put("device_token", device_token);
//            new HttpAsy(new PostExecute() {
//                @Override
//                public void onPostExecute(String result) {
//                    Log.e("czhongzhi", "更新设备id " + result);
//                }
//            }).execute(Constants.setUserInfoUrl, map);
//        }

        initView();

//        //统计应用启动数据
//        PushAgent.getInstance(this).onAppStart();

        setLeftMenuInfo();

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("czhongzhi", "setOnTouchListener -- " + !(boolean) popupwin.getTag());
                if (!(boolean) popupwin.getTag()) {
                    closePopupAnim();
                }
                return false;
            }
        });

        if (TextUtils.isEmpty(AppSharePrefManager.getInstance(this).getLastest_login_username())) {
            //网络获取用户信息
            getUserInfo();

        } else {
            user_name.setText(AppSharePrefManager.getInstance(this).getLastest_login_username());
            String iconString = AppSharePrefManager.getInstance(this).getLastest_login_touxiang_imagebase64();
            if (!TextUtils.isEmpty(iconString)) {
                Bitmap bitmap = BitmapUtil.base64ToBitmap(iconString);
                user_icon.setImageBitmap(bitmap);
            }
        }

        updataIcon_MyMsg();  //更新新消息红点提醒

        isFromH5(userid);  //是否是从h5注册进来的

        Intent pushService = new Intent(PushService.ACTION);
        pushService.setPackage(getPackageName());
        startService(pushService);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivity = this;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        mainActivity = this;

        user_name.setText(AppSharePrefManager.getInstance(this).getLastest_login_username());
        Bitmap bitmap = BitmapUtil.base64ToBitmap(AppSharePrefManager.getInstance(this).getLastest_login_touxiang_imagebase64());
        if (bitmap != null) {
            user_icon.setImageBitmap(bitmap);
        }

        if (show_popup != null)
            show_popup.setText(Html.fromHtml(uptext));

        updataIcon_MyMsg();

        isShowCallHint = getIntent().getBooleanExtra(ISSHOWCALLHINT, false);
        if (isShowCallHint) {
            showBrokerCallHint(getIntent().getStringExtra("tranid_hint"));
        }
    }

    private void initView() {
        main_framelayout = (FrameLayout) findViewById(R.id.main_framelayout);
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


        dialog_bgchange = (FrameLayout) findViewById(R.id.dialog_bgchange);
        mate_wait_bar = (RelativeLayout) findViewById(R.id.mate_wait_bar);
        main_bar = (RelativeLayout) findViewById(R.id.main_bar);
        mate_wait_bar.findViewById(R.id.mate_wait_cancen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(FangApplication.tranid)) {
                    //showCancelSuccess();//tranid为null直接取消
                } else {
                    isRunTime = false;
                    dialog_cancel = DialogUtil.showCanceling(MainActivity.this, "正在取消");
                    FangApplication.currTime = FangApplication.time;
                    //tranid不为null网络取消
                    cancelBroker();
                }
            }
        });
        mate_wait_bar2 = (RelativeLayout) findViewById(R.id.mate_wait_bar_2);
        broker_call_hint = (LinearLayout) findViewById(R.id.broker_call_hint);
        broker_call_hint.setVisibility(View.GONE);
        show_brokerhint = (Button) findViewById(R.id.show_brokerhint);

        layout = (FrameLayout) findViewById(R.id.layout);
        show_popup = (Button) findViewById(R.id.show_popup);
        show_popup.setOnClickListener(this);
        show_popup.setTag(false);
        show_popup.setText(Html.fromHtml(uptext));
        popupwin = findViewById(R.id.popupwin);
        popupwin_close = (ImageButton) popupwin.findViewById(R.id.popupwin_close);
        popupwin_close.setOnClickListener(this);

        initMateData();  //获取上次需求数据
        setPopupwin();
        popupwin.setTag(true);

        layout_tap = (RadioGroup) findViewById(R.id.layout_tap);
        layout_tap.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.new_house:
                        reqtype = "新房"; //请求类型 (值：新房、二手房、租房)
                        switchContent(newHouseFm);
                        layout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.second_hand_house:
                        reqtype = "二手房"; //请求类型 (值：新房、二手房、租房)
                        if (secHandFm == null) {
                            secHandFm = new SecHandFragment();
                        }
                        switchContent(secHandFm);
                        layout.setVisibility(View.GONE);
                        break;
                    case R.id.rent_house:
                        reqtype = "租房"; //请求类型 (值：新房、二手房、租房)
                        if (rentHouseFm == null) {
                            rentHouseFm = new RentHouseFragment();
                        }
                        switchContent(rentHouseFm);
                        layout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        setDefaultFragment();

        area = (TextView) findViewById(R.id.area);
        area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择城市
                //DialogUtil.showHintNoneFunction(MainActivity.this, "提示:", "现暂时只开通深圳服务");
                Intent intent = new Intent(MainActivity.this, CtiyActivity.class);
                startActivityForResult(intent, 1010);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1010 && resultCode == 66) {//选择城市返回
            area.setText(city);
            getCtiyArea(city);
        }
    }

    /**
     * 设置默认的fragment，即新房的fragment;
     */
    private void setDefaultFragment() {
        mFm = getFragmentManager();
        FragmentTransaction mFragmentTrans = mFm.beginTransaction();
        //第一次加载界面
        newHouseFm = new NewHouseFragment();
        mFragmentTrans.add(R.id.main_framelayout, newHouseFm).commit();

        mContent = newHouseFm;
    }

    private Fragment mContent;

    /**
     * 修改显示的内容 不会重新加载 *
     */
    public void switchContent(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = mFm.beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.main_framelayout, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_popup:
                if (!(boolean) show_popup.getTag()) {
                    show_popup.setText("确认");
                    show_popup.setTag(true);
                    if (xoff == 0)
                        xoff = show_popup.getHeight() / 2;
                    showPopupAnim();
                } else {
                    show_popup.setTag(false);
                    popupwin.setTag(true);
                    popupwin.setVisibility(View.GONE);
                    //判断网络是否可用
                    if (HttpUtil.isNetworkAble(this)) {
                        //开启dialog并匹配经纪人
                        //showDialogMateWait();
                        showPopupMateWait();
                        //网络请求 需求匹配
                        requestMate();
                    } else {
                        Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.popupwin_close:
                closePopupAnim();
                break;
        }
    }

    //    private static final String[] m_areas = {"福田区", "龙华新区", "罗湖区",
//            "南山区", "龙岗区", "盐田区", "坪山新区", "大鹏新区", "宝安区", "光明新区"};
    private static String[] m_areas = {"罗湖区", "福田区", "南山区",
            "盐田区", "龙岗区", "宝安区", "龙华新区", "光明新区", "坪山新区", "大鹏新区"};

    private static String[] m_prices = {"0~9000", "0~300", "300~500", "500~800", "800~1000", "1000~9000"};
    private static String[] m_prices_txt = {"不限", "300万以下", "300~500万", "500~800万", "800~1000万", "1000万以上"};
    private static String[] m_huxings = {"不限", "一室", "二室", "三室", "四室", "五室以上"};
    //    private static final String[] m_peitao = {"不限", "教育", "交通", "餐饮", "健康", "购物", "娱乐"};
    private static String[] m_peitao = {"不限", "教育", "地铁", "购物", "公园", "医疗"};

    private String t_area;
    private String t_price;
    private int t_prices_index;
    private String t_huxing;
    private String t_peitao;
    private String t_loupan;

    /**
     * 初始化匹配需求数据
     */
    private void initMateData() {
        t_area = AppSharePrefManager.getInstance(MainActivity.this).getMatePara_area();
        t_price = AppSharePrefManager.getInstance(MainActivity.this).getMatePara_price();
        t_prices_index = AppSharePrefManager.getInstance(MainActivity.this).getMatePara_price_index();
        t_huxing = AppSharePrefManager.getInstance(MainActivity.this).getMatePara_huxing();
        t_peitao = AppSharePrefManager.getInstance(MainActivity.this).getMatePara_peitao();
        t_loupan = AppSharePrefManager.getInstance(MainActivity.this).getMatePara_loupan();
    }

    private void setPopupwin() {
        spinner_area = (TextView) popupwin.findViewById(R.id.spinner_area);
        spinner_area.setText(t_area);
        spinner_price = (TextView) popupwin.findViewById(R.id.spinner_price);
        spinner_price.setText(m_prices_txt[t_prices_index]);
        spinner_huxing = (TextView) popupwin.findViewById(R.id.spinner_huxing);
        spinner_huxing.setText(t_huxing);
        spinner_peitao = (TextView) popupwin.findViewById(R.id.spinner_peitao);
        spinner_peitao.setText(t_peitao);
        edittext_house = (EditText) popupwin.findViewById(R.id.edittext_house);
        if (!TextUtils.isEmpty(t_loupan)) {
            edittext_house.setText(t_loupan);
        }

        spinner_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(spinner_area, m_areas, 0);
            }
        });
        spinner_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(spinner_price, m_prices_txt, 1);
            }
        });
        spinner_huxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(spinner_huxing, m_huxings, 2);
            }
        });
        spinner_peitao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(spinner_peitao, m_peitao, 3);
            }
        });

        edittext_house.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("czhongzhi", s.toString());
                t_loupan = s.toString();
                if (TextUtils.isEmpty(s)) {
                    t_loupan = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 展示需求选项popupwin
     */
    private void showPopup(final View v, final String[] strs, final int pos) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_window, null);
        int width = v.getWidth();
        popupWindow = new PopupWindow(view, width, 300);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, android.R.id.text1, strs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                ((TextView) v).setText(strs[position]);
                switch (pos) {
                    case 0:
                        t_area = strs[position];
                        break;
                    case 1:
                        t_prices_index = position;
                        t_price = m_prices[position];
                        break;
                    case 2:
                        t_huxing = strs[position];
                        break;
                    case 3:
                        t_peitao = strs[position];
                        break;
                }
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        //popupWindow.setAnimationStyle(R.style.popupAnimation);

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAsDropDown(v);
        //popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());
    }

    public void getUserInfo() {
        if (userid == 0)
            return;
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        userInfo = JacksonMapper.getObjectMapper().readValue(result, UserInfo.class);
                        SetUserInfoActivity.setLocalInfo(MainActivity.this, userInfo);
                        user_name.setText(userInfo.getName());

                        ImageLoader.getInstance().displayImage(userInfo.getUserimage(), user_icon, FangApplication.options, new SimpleImageLoadingListener() {
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
        }).execute(Constants.getUserInfoUrl + "?userid=" + userid);
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
     * 判断用户是否是从h5注册进来的
     *
     * @param userid
     */
    private void isFromH5(int userid) {
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("MainActivity isFromH5 is " + result);
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                try {
                    activityFromH5 = JacksonMapper.getObjectMapper().readValue(result, ActivityFromH5.class);
                    dealFromH5A();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.isFromH5Url + "?userid=" + userid);
    }

    /**
     * 对从h5引进的用户操作
     */
    private void dealFromH5A() {
        if (activityFromH5 == null) {
            return;
        }
        L.LogI("activityFromH5 -- " + activityFromH5.isState());
        L.LogI("activityFromH5 -- " + activityFromH5.getNextaction());
        L.LogI("activityFromH5 -- " + activityFromH5.getActivityname());
        for (String i : activityFromH5.getDistrictarr()) {
            L.LogI("activityFromH5 -- " + i);
        }
        L.LogI("activityFromH5 -- " + activityFromH5.getBrokername());
        L.LogI("activityFromH5 -- " + activityFromH5.getActivityurl());

        if (!activityFromH5.isState()) {
            return;
        }

        city = activityFromH5.getBindcity();
        m_areas = activityFromH5.getDistrictarr();
        t_area = m_areas[0];
        spinner_area.setText(m_areas[0]);
        area.setText(city);

        //String title = "尊敬的" + activityFromH5.getUsername();
        final String content = "您已经成功报名“" + activityFromH5.getActivityname() + "”活动，为您服务" +
                "的房产专家是 " + "<font color='#fac72f'>" + activityFromH5.getBrokername() + "</font>";

        final AloneDeal aloneDeal = new AloneDeal() {
            @Override
            public void deal() {
                Intent intent = new Intent(MainActivity.this, MyYuyueActivity.class);
                startActivity(intent);
            }
        };

        if(!TextUtils.isEmpty(activityFromH5.getActivityurl())){
        ImageLoader.getInstance().loadImage(activityFromH5.getActivityurl(),new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                DialogUtil.showFromH5FirstLogin(MainActivity.this, null, content,bitmap, aloneDeal);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        }

    }

    /**
     * 根据城市获取区域
     *
     * @param cityname
     */
    private void getCtiyArea(String cityname) {
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("getCtiyArea is " + result);

                if (TextUtils.isEmpty(result)) {
                    return;
                }

                JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, String.class);
                List<String> temps = null;
                try {
                    temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                    m_areas = (String[]) temps.toArray(new String[temps.size()]);
                    t_area = m_areas[0];
                    spinner_area.setText(m_areas[0]);
                    //showPopup(spinner_area, m_areas, 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).execute(Constants.getCtiyAreaUrl + "?cityname=" + cityname);
    }

    /**
     * 网络请求 需求匹配
     */
    private void requestMate() {
        Map<String, String> map = new HashMap<>();
        map.put("userid", String.valueOf(userid));
        map.put("reqtype", reqtype);
        map.put("city", city);
        map.put("area", t_area);
        map.put("loupanname", t_loupan);
        map.put("huxing", t_huxing.equals("不限") ? null : t_huxing);
        map.put("peitao", t_peitao.equals("不限") ? null : t_peitao);

        String[] prices = t_price.split("~");
        map.put("price_low", prices[0]);
        map.put("price_high", prices[1]);

        AppSharePrefManager.getInstance(this).setMatePara(t_area, t_huxing, t_price, t_prices_index, t_peitao, t_loupan, reqtype, city);

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(MainActivity.this, "网络获取异常", Toast.LENGTH_SHORT).show();
                    dimssDialogMateWait();
                } else {
                    if (mate_wait_text != null) {
                        Map<String, Object> map = JacksonMapper.getInstance().mapObjFromJson(result);
                        int num = (int) map.get("num");
                        FangApplication.tranid = (String) map.get("tranid");
                        mate_wait_text.setText("有" + num + "个人符合你需求");
                        LoginOpenfire.currMateNum = num;
                        Log.e("czhongzhi", "MainActivity. uptext -- " + "有" + num + "个人符合你需求");
                        if (num == 0) {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dimssDialogMateWait();
                                    Toast.makeText(MainActivity.this, "无匹配经纪人，请您重新选择需求", Toast.LENGTH_LONG).show();
                                }
                            }, 1500);
                        }
                    }
                }
            }
        }).execute(Constants.reqMateBrokerUrl, map);

    }

    /**
     * 网络取消 需求匹配
     */
    private void cancelBroker() {
        Map<String, String> map = new HashMap<>();
        map.put("tranid", FangApplication.tranid);
        map.put("reason", "匹配经济人——取消");
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "result -- " + result);
                if (!TextUtils.isEmpty(result)) {
                    String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("state");
                    if (state.equals("true")) {//取消成功
                        showCancelSuccess();
                    } else {//取消失败
                        if (dialog_cancel != null && dialog_cancel.isShowing()) {
                            dialog_cancel.dismiss();
                        }
                        isRunTime = true;
                        countDown(FangApplication.currTime);
                        showCancelFailed();
                        // Toast.makeText(MainActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).execute(Constants.cancelBrokerUrl, map);
    }

    /**
     * 等待dialog
     */
    private void showPopupMateWait() {
        dialog_bgchange.setBackgroundResource(R.color.dialog_bgchange);
        dialog_bgchange.setClickable(true);
        main_bar.setVisibility(View.GONE);
        mate_wait_bar.setVisibility(View.VISIBLE);

        View view = LayoutInflater.from(this).inflate(R.layout.mate_dialog_wait, null);
        ImageView wait_icon = (ImageView) view.findViewById(R.id.mate_wait_icon);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.mate_wait_rotate);
        anim.setInterpolator(new LinearInterpolator());
        wait_icon.setAnimation(anim);
        mate_wait_time = (TextView) view.findViewById(R.id.mate_wait_time);
        countDown(60);

        mate_wait_text = (TextView) view.findViewById(R.id.mate_wait_text);
        int w_h = AppUtil.dp2px(200, this);
        waitPopup = new PopupWindow(view, w_h, w_h);
        waitPopup.setFocusable(false);
        waitPopup.setOutsideTouchable(false);
        waitPopup.showAtLocation(dialog_bgchange, Gravity.CENTER, 0, 0);
    }

    /**
     * 关闭响应等待dialog
     */
    private void dimssDialogMateWait() {
        if (dialog_cancel != null && dialog_cancel.isShowing()) {
            dialog_cancel.dismiss();
        }
        isRunTime = false;
        waitPopup.dismiss();
        dialog_bgchange.setBackgroundResource(R.color.transparent);
        dialog_bgchange.setClickable(false);
        mate_wait_bar.setVisibility(View.GONE);
        main_bar.setVisibility(View.VISIBLE);
        if (show_popup != null)
            show_popup.setText(Html.fromHtml(uptext));
    }

    /**
     * 取消成功
     */
    private void showCancelSuccess() {
        dimssDialogMateWait();
        DialogUtil.showCancelSuccess(this, null);
    }

    /**
     * 取消失败
     */
    private void showCancelFailed() {
        dimssDialogMateWait();
        DialogUtil.showCancelFailed(this, null);
    }


    /**
     * 展示需求选项popupwin
     */
    private void showPopupAnim() {
        isRunTime = true;
        popupwin.setVisibility(View.VISIBLE);
        TranslateAnimation anim = null;
        if (isAddStartH == 1) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(popupwin.getLayoutParams());
            params.setMargins(32, popupwin.getTop(), 32, 0);
            popupwin.setLayoutParams(params);
            anim = new TranslateAnimation(0, 0, 1000, -xoff);
        } else {
            anim = new TranslateAnimation(0, 0, 1000, 0);
        }

        anim.setDuration(500);
        popupwin.setTag(false);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isAddStartH == 1) {
                    starth = popupwin.getTop();
                    isAddStartH++;
                }
                popupwin.clearAnimation();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(popupwin.getLayoutParams());
                params.setMargins(32, starth - xoff, 32, 0);
                popupwin.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        popupwin.startAnimation(anim);
    }

    /**
     * 关闭需求选项popupwin
     */
    private void closePopupAnim() {
        isRunTime = false;
        show_popup.setText(Html.fromHtml(uptext));
        popupwin.setTag(true);
        show_popup.setTag(false);
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 1000);
        anim.setDuration(500);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                popupwin.setVisibility(View.GONE);
                popupwin.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        popupwin.startAnimation(anim);
    }


    private boolean isRunTime = true;

    /**
     * 倒数计时
     */
    private void countDown(int time) {
        FangApplication.time = time;
        runOnUIThread(new Runnable() {
            public void run() {
                if (isRunTime) {
                    FangApplication.time--;
                    String unReceive = "<font color='#8f8f90'>" + FangApplication.time + "</font>s";
                    mate_wait_time.setText(Html.fromHtml(unReceive));
                    if (FangApplication.time == 0) {
                        //预约时间走完
                        dimssDialogMateWait();
                        Intent intent = new Intent(MainActivity.this, MateBrokerActivity.class);
                        intent.putExtra(Constants.TRANID, FangApplication.tranid);
                        startActivity(intent);
                    } else {
                        runOnUIThread(this, 1000);
                    }
                }
            }
        }, 1000);
    }

    private void runOnUIThread(final Runnable paramRunnable, long paramLong) {
        UIHandler.sendEmptyMessageDelayed(0, paramLong, new Handler.Callback() {
            public boolean handleMessage(Message paramAnonymousMessage) {
                paramRunnable.run();
                return false;
            }
        });
    }


    private boolean isRunTimeCallHint = true;
    private int runTimeCallHint;

    private void showBrokerCallHint(final String tranid) {
        isRunTimeCallHint = true;

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                try {
                    BrokerCallHint bch = JacksonMapper.getObjectMapper().readValue(result, BrokerCallHint.class);
                    setBrokerCallHintData(bch, tranid);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.brokerCallHintUrl + "?tranid=" + tranid);

    }

    private void setBrokerCallHintData(final BrokerCallHint bch, final String tranid) {
        dialog_bgchange.setClickable(true);
        main_bar.setVisibility(View.GONE);
        mate_wait_bar2.setVisibility(View.VISIBLE);
        //设置数据
        if (!TextUtils.isEmpty(bch.getUserimage())) {
            ImageLoader.getInstance().displayImage(bch.getUserimage(), (ImageView) broker_call_hint.findViewById(R.id.broker_icon), FangApplication.options, FangApplication.animateFirstListener);
        }
        ((TextView) broker_call_hint.findViewById(R.id.broker_name)).setText(bch.getUsername());
        ((TextView) broker_call_hint.findViewById(R.id.broker_phone)).setText(bch.getCellphone());
        float level = bch.getStarlevel() == null ? 0.0f : bch.getStarlevel();
        ((TextView) broker_call_hint.findViewById(R.id.broker_score)).setText(String.valueOf(level));
        ((RatingBar) broker_call_hint.findViewById(R.id.broker_star)).setRating(level);

        //设置点击事件
        mate_wait_bar2.findViewById(R.id.mate_wait_cancen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消预约
                AloneDeal dealLeft = new AloneDeal() {
                    @Override
                    public void deal() {
                        //确认操作
                        BrokerMainActivity.cancelBroker(MainActivity.this, tranid, "用户取消", new AloneDeal() {
                            @Override
                            public void deal() {
                                closeBrokerCallHint();
                            }
                        });
                    }
                };
                DialogUtil.showDeleteYuyueDeal(MainActivity.this, "确认取消预约", dealLeft, null);
            }
        });
        broker_call_hint.findViewById(R.id.broker_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入聊天界面
                //Toast.makeText(MainActivity.this, "聊天", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MessageingActivity.class);
                intent.putExtra(MessageingActivity.SESSION_ID, tranid);
                intent.putExtra(MessageingActivity.SESSION_FROM, bch.getUsername());
                intent.putExtra(MessageingActivity.SESSION_TOUSERID, bch.getBrokerid());
                intent.putExtra("isFromMyYuyue", true);
                startActivity(intent);
            }
        });
        broker_call_hint.findViewById(R.id.broker_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                //Toast.makeText(MainActivity.this, "call ta", Toast.LENGTH_SHORT).show();
                AppUtil.call2(MainActivity.this, bch.getCellphone());
            }
        });

        //显示布局
        broker_call_hint.setVisibility(View.VISIBLE);
        show_brokerhint.setVisibility(View.VISIBLE);
        show_brokerhint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBrokerCallHint();
            }
        });
        runTimeCallHint(60);
        //
    }

    private void closeBrokerCallHint() {
        isRunTimeCallHint = false;
        dialog_bgchange.setClickable(false);
        main_bar.setVisibility(View.VISIBLE);
        mate_wait_bar2.setVisibility(View.GONE);
        //设置数据

        //显示布局
        broker_call_hint.setVisibility(View.GONE);
        show_brokerhint.setVisibility(View.GONE);
    }

    private void runTimeCallHint(int time) {
        runTimeCallHint = time;
        show_brokerhint.setClickable(false);
        new Thread() {
            @Override
            public void run() {
                while (isRunTimeCallHint) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String unReceive = "<font size='20' color='#fac72f'>" + runTimeCallHint-- + "s<br></font><font size='16' color='#fac72f'>等待来电</font>";
                            show_brokerhint.setText(Html.fromHtml(unReceive));
                        }
                    });
                    if (runTimeCallHint == 0) {
                        //
                        isRunTimeCallHint = false;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                String unReceive = "<font size='20' color='#fac72f'>完成<br></font><font size='16' color='#fac72f'>返回主页</font>";
                                show_brokerhint.setText(Html.fromHtml(unReceive));
                                show_brokerhint.setClickable(true);
                            }
                        });
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        isRunTime = false;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.mainActivity = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mainActivity = null;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SetUserInfoActivity.class);
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
                    Intent intent = new Intent(MainActivity.this, MyYuyueActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_jingjiren);//我的经纪人
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_people_l);
            ((TextView) itemView.findViewById(R.id.text)).setText("我的专家");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, QuickLocationMainActivity.class));
//                    left_menu.closeDrawer();
//                    DialogUtil.showHintNoneFunction(MainActivity.this, "提示", "功能暂未开放");
                }
            });
        }

        itemView = findViewById(R.id.item_kanfangdingdan);//看房订单
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_list);
            ((TextView) itemView.findViewById(R.id.text)).setText("看房订单");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SeeHouseListActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_eval);//专家评测
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_test);
            ((TextView) itemView.findViewById(R.id.text)).setText("房产专家评测");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    left_menu.closeDrawer();
                    DialogUtil.showHintNoneFunction(MainActivity.this, "提示", "功能暂未开放");
                }
            });
        }

        itemView = findViewById(R.id.item_jingjirenzhaomu);//经纪人招募
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_recruit);
            ((TextView) itemView.findViewById(R.id.text)).setText("房产专家招募");
            itemView.findViewById(R.id.item_click).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, RegistMainActivity.class);
                    startActivity(intent);
                }
            });
        }

        itemView = findViewById(R.id.item_yezhu);//我是业主
        if (itemView != null) {
            ((ImageView) itemView.findViewById(R.id.img)).setImageResource(R.drawable.home_owner);
            ((TextView) itemView.findViewById(R.id.text)).setText("我是房东");
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
                    left_menu.closeDrawer();

                    startActivity(new Intent(MainActivity.this, AboutKfqActivity.class));

                }
            });
        }


    }

}
