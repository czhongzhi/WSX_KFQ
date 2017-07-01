package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.utils.DateFormatHelp;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.LineChart01View;

import java.util.Date;
import java.util.LinkedList;

/**
 * 历史记录
 */
public class MyAssetHistoryActivity extends Activity implements View.OnClickListener{

    private LinearLayout asset_detail_click;
    private TextView asset_detail_year;
    private PopupWindow popupWindow;
    private String[] years;//年份

    private LineChart01View lineChart_succ;
    private LineChart01View lineChart_visit;
    private LineChart01View lineChart_yuyue;
    private TextView asset_total_succ,asset_total_visit,asset_total_yuyue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "历史记录", true, false);
        setContentView(R.layout.activity_my_asset_history);

        initView();

        initData();

        setData();
    }


    private void initView() {
        asset_detail_click = (LinearLayout) findViewById(R.id.asset_detail_click);
        asset_detail_click.setOnClickListener(this);
        asset_detail_year = (TextView) findViewById(R.id.asset_detail_year);

        lineChart_succ = (LineChart01View) findViewById(R.id.lineChart_succ);
        lineChart_visit = (LineChart01View) findViewById(R.id.lineChart_visit);
        lineChart_yuyue = (LineChart01View) findViewById(R.id.lineChart_yuyue);
        asset_total_succ = (TextView) findViewById(R.id.asset_total_succ);
        asset_total_visit = (TextView) findViewById(R.id.asset_total_visit);
        asset_total_yuyue = (TextView) findViewById(R.id.asset_total_yuyue);

    }

    private void initData() {
        years = new String[10];
        int currYear = Integer.valueOf(DateFormatHelp.TIME_YEAR.format(new Date()));
        for(int i = 0;i < years.length;i++){
            years[i] = String.valueOf(currYear - i);
        }
    }

    private void setData() {

        LinkedList<Double> dataSeries2= new LinkedList<Double>();
        dataSeries2.add((double)60);
        dataSeries2.add((double)60);
        dataSeries2.add((double)60);
        dataSeries2.add((double)60);
        dataSeries2.add((double)60);
        dataSeries2.add((double)40);
        dataSeries2.add((double)20);
        dataSeries2.add((double)0);
        dataSeries2.add((double)30);
        dataSeries2.add((double)60);
        dataSeries2.add((double)0);
        dataSeries2.add((double)0);

        //数据轴最大值
        lineChart_visit.getChart().getDataAxis().setAxisMax(80);
        //数据轴刻度间隔
        lineChart_visit.getChart().getDataAxis().setAxisSteps(20);

        lineChart_visit.dataSeries2.clear();
        lineChart_visit.dataSeries2.addAll(dataSeries2);
        lineChart_visit.invalidate();

    }

    /**
     * 选择年份
     */
    private void showPopup(final View v, final String[] strs) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_window2, null);
        int width = v.getWidth();
        popupWindow = new PopupWindow(view, width, 300);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, android.R.id.text1, strs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                ((TextView) v).setText(strs[position] + "年");
                //do
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
        switch (v.getId()){
            case R.id.asset_detail_click:
                showPopup(asset_detail_year,years);
                break;
        }
    }
}
