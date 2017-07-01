package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.utils.DateFormatHelp;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 佣金明细
 */
public class MyAssetDetailActivity extends Activity implements View.OnClickListener {

    private LinearLayout asset_detail_click;
    private TextView asset_detail_year;
    private ListView mListView;
    private AssetDetailAdapter adapter;
    private List<String> datas = new ArrayList<>();

    private PopupWindow popupWindow;
    private String[] years;//年份

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "佣金明细", true, false);
        setContentView(R.layout.activity_my_asset_detail);

        initView();

        initData();
    }

    private void initView() {
        asset_detail_click = (LinearLayout) findViewById(R.id.asset_detail_click);
        asset_detail_click.setOnClickListener(this);
        asset_detail_year = (TextView) findViewById(R.id.asset_detail_year);
        mListView = (ListView) findViewById(R.id.listView);
        adapter = new AssetDetailAdapter(this, datas);
        mListView.setAdapter(adapter);

    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            datas.add(String.valueOf(i));
        }
        adapter.notifyDataSetChanged();

        years = new String[10];
        int currYear = Integer.valueOf(DateFormatHelp.TIME_YEAR.format(new Date()));
        for(int i = 0;i < years.length;i++){
            years[i] = String.valueOf(currYear - i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.asset_detail_click:
                showPopup(asset_detail_year,years);
                break;
        }
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

    private class AssetDetailAdapter extends BaseAdapter {
        private List<String> list;
        private LayoutInflater inflater;

        public AssetDetailAdapter(Context context, List<String> list) {
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHopler vh = null;
            if (convertView == null) {
                vh = new ViewHopler();
                convertView = inflater.inflate(R.layout.item_asset_detail, parent, false);
                vh.asset_time = (TextView) convertView.findViewById(R.id.asset_time);
                vh.asset_name = (TextView) convertView.findViewById(R.id.asset_name);
                vh.asset_title = (TextView) convertView.findViewById(R.id.asset_title);
                vh.asset_area = (TextView) convertView.findViewById(R.id.asset_area);
                vh.asset_num = (TextView) convertView.findViewById(R.id.asset_num);
                convertView.setTag(vh);
            } else {
                vh = (ViewHopler) convertView.getTag();
            }

            //do thing

            return convertView;
        }

        class ViewHopler {
            TextView asset_time, asset_name, asset_title, asset_area, asset_num;
        }
    }


}
