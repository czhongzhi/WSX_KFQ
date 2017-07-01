package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.model.HouseInfoBasedataResp;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

import java.util.ArrayList;

/**
 * 上传房源的类3   todo 这个类还缺少一个房源的bean做数据的收集  ,,,展示的activity需要一个房源bean (HouseSourcePreviewActivity)
 */
public class UploadHouseSourceThreeActivity extends Activity implements View.OnClickListener {

    private TextView tvKaipanTime;
    private TextView tvJiaofangTime;
    private TextView tvChanquanYear;
    private TextView tvHousetype;
    private TextView tvZhuangxiuStatus;
    private EditText edDevelopers;
    private EditText edBuildingArea;
    private EditText edGreenRate;
    private EditText edVolumeRate;
    private EditText edHouseholds;
    private EditText edPropertyCom;
    private EditText edProperty;
    private Button housesou;
    private Button housesourc;

    private HouseInfoBasedataResp houseInfo;//上传房源bean
    private ArrayList<Drawable> loufangImages;//上传房源图片集合

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "上传房源", true, false);
        setContentView(R.layout.activity_upload_housesource_three);

        initView();
    }


    private void initView() {

        //获取传递过来的Bean
        houseInfo = (HouseInfoBasedataResp) getIntent().getSerializableExtra("houseInfo");
        loufangImages = (ArrayList) getIntent().getSerializableExtra("imagelist");

        tvKaipanTime = (TextView) findViewById(R.id.tv_kaipan_time);
        tvJiaofangTime = (TextView) findViewById(R.id.tv_jiaofang_time);
        tvChanquanYear = (TextView) findViewById(R.id.tv_chanquan_year);
        tvHousetype = (TextView) findViewById(R.id.tv_housetype);
        tvZhuangxiuStatus = (TextView) findViewById(R.id.tv_zhuangxiu_status);
        edDevelopers = (EditText) findViewById(R.id.ed_developers);
        edBuildingArea = (EditText) findViewById(R.id.ed_building_area);
        edGreenRate = (EditText) findViewById(R.id.ed_green_rate);
        edVolumeRate = (EditText) findViewById(R.id.ed_volume_rate);
        edHouseholds = (EditText) findViewById(R.id.ed_households);
        edPropertyCom = (EditText) findViewById(R.id.ed_property_com);
        edProperty = (EditText) findViewById(R.id.ed_property);
        housesou = (Button) findViewById(R.id.housesou);
        housesourc = (Button) findViewById(R.id.housesourc);


        housesou.setOnClickListener(this);
        housesourc.setOnClickListener(this);
        tvKaipanTime.setOnClickListener(this);
        tvJiaofangTime.setOnClickListener(this);
        tvChanquanYear.setOnClickListener(this);

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
                        //   t_Province = strs[position];
                        break;
                    case 1:
                        //   t_City = strs[position];
                        break;
                    case 2:
                        //  t_Area = strs[position];
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.housesou://预览

                Intent previewIntent = new Intent(this, HouseSourcePreviewActivity.class);
                previewIntent.putExtra("houseInfo", houseInfo);//传递bean;
                previewIntent.putExtra("imagelist", loufangImages);//图片集合
                startActivity(previewIntent);
                break;

            case R.id.housesourc://提交
                finish();

                //TOdo 提交房源
                Intent submitIntent = new Intent(this, null);
                startActivity(submitIntent);
                break;

            case R.id.tv_kaipan_time://开盘时间

                showDialog(DATE_DIALOG_ID);
                break;

            case R.id.tv_jiaofang_time://交房时间

                showDialog(DATE_DIALOG_ID2);
                break;
        }

    }


    private int mYear = 2015;
    private int mMonth = 11;
    private int mDay = 1;
    private int mHour, mMinute;
    private int mHour_2, mMinute_2;
    //声明一个独一无二的标识，来作为要显示DatePicker的Dialog的ID：
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID2 = 3;
    static final int TIME_FROM_DIALOG_ID = 1;
    static final int TIME_TO_DIALOG_ID = 2;

    /**
     * 当Activity调用showDialog函数时会触发该函数的调用：
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        R.style.kfqAppThemeDialog, mDateSetListener, mYear, mMonth, mDay);


            case DATE_DIALOG_ID2:

                return new DatePickerDialog(this,
                        R.style.kfqAppThemeDialog, mDateSetListener2, mYear, mMonth, mDay);

            case TIME_FROM_DIALOG_ID:
                return new TimePickerDialog(this, R.style.kfqAppThemeDialog, mTimeSetListener, mHour, mMinute, true);
            case TIME_TO_DIALOG_ID:
                return new TimePickerDialog(this, R.style.kfqAppThemeDialog, mTimeSetListener_2, mHour_2, mMinute_2, true);
        }
        return null;
    }


    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            //yue_time_from.setText(mHour+":"+mMinute);
        }
    };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener_2 = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour_2 = hourOfDay;
            mMinute_2 = minute;
            //yue_time_to.setText(mHour_2+":"+mMinute_2);
        }
    };

    //需要定义弹出的DatePicker对话框的事件监听器：
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            //设置文本的内容：
            tvKaipanTime.setText(new StringBuilder()
                    .append(mYear).append("年")
                    .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始
                    .append(mDay).append("日"));
        }
    };


    //需要定义弹出的DatePicker对话框的事件监听器：
    private DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            //设置文本的内容：
            tvJiaofangTime.setText(new StringBuilder()
                    .append(mYear).append("年")
                    .append(mMonth + 1).append("月")//得到的月份+1，因为从0开始
                    .append(mDay).append("日"));
        }
    };
}