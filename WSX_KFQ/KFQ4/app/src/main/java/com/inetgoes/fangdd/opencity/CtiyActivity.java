package com.inetgoes.fangdd.opencity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.activity.MainActivity;
import com.inetgoes.fangdd.opencity.bean.SortModel;
import com.inetgoes.fangdd.opencity.view.SideBar;
import com.inetgoes.fangdd.view.CustomTitleBar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 选择城市 add czz
 */
public class CtiyActivity extends Activity {
    private List<String> cityName;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SideBarAdapter adapter;

//    private TextView headTitle;
    private GridView hotCityView;
    private String[] hotCityName = { "深圳", "中山"};
    private HotCityAdapter hotCityAdapter;
    private String dingWeiName = "广州";

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "选择城市", true, false);
        setContentView(R.layout.activity_city);
        InputStream inputStream = getResources().openRawResource(R.raw.citys_weather);

        cityName = CityNameUtils.cityName(inputStream);

        initView();


    }

    private void initView() {
        // TODO Auto-generated method stub
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);


        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));

                if (position == -1) {
                    sortListView.setSelection(0);
                } else {
                    if (position != -1) {
                        sortListView.setSelection(position + 1);
                    }
                }
            }
        });


        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        //添加listview的头
        View view = LayoutInflater.from(CtiyActivity.this).inflate(R.layout.address_listview_head, null);
        //headTitle = (TextView) view.findViewById(R.id.listview_headTitle);//当前定位地址
        hotCityView = (GridView) view.findViewById(R.id.listview_grid);//热门城市
        hotCityAdapter = new HotCityAdapter(CtiyActivity.this, hotCityName);
        hotCityView.setAdapter(hotCityAdapter);
        sortListView.addHeaderView(view);
//        headTitle.setText("广州");
//        headTitle.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(CtiyActivity.this, dingWeiName, Toast.LENGTH_SHORT).show();
//            }
//        });

        hotCityView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(CtiyActivity.this, hotCityName[position], Toast.LENGTH_SHORT).show();
                MainActivity.city = hotCityName[position];
                CtiyActivity.this.setResult(66);
                CtiyActivity.this.finish();
            }
        });

        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Toast.makeText(getApplication(), ((SortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
            }
        });

        SourceDateList = filledData(cityName);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SideBarAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);

    }


    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(List<String> date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i));
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i));
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }


}
