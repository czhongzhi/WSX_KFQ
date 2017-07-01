package com.inetgoes.kfqbrokers.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.activity.BuildingMainActivity;
import com.inetgoes.kfqbrokers.activity.HouseSourcePreviewActivity;
import com.inetgoes.kfqbrokers.adapter.GridViewAdapter;
import com.inetgoes.kfqbrokers.adapter.HouseTypeGridViewAdapter;
import com.inetgoes.kfqbrokers.adapter.ListViewAdapter;
import com.inetgoes.kfqbrokers.model.HouseInfoBasedataResp;
import com.inetgoes.kfqbrokers.model.HouseinfoHuxing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun on 2015/11/11.
 * //房源展示以及房源主页的fragment类
 */
public class BuildingFragment extends Fragment {

    private int layout;
    private List<HouseinfoHuxing> huxingarr = new ArrayList<>();
    private HouseInfoBasedataResp houseInfoBasedataResp;
    private GridViewAdapter gridViewAdapter;
    private ListViewAdapter listViewAdapter;

    private TextView basic_open_time;
    private TextView basic_jiaofang_time;
    private TextView basic_chanquan_time;
    private TextView basic_developers;
    private TextView basic_wuye;
    private TextView basic_area;
    private TextView basic_green;
    private TextView basic_volume;
    private TextView basic_homenum;
    private TextView basic_pricedesc;
    private TextView basic_projtype;
    private TextView basic_projfitment;
    private TextView basic_right_year;
    private TextView basic_propertyfee;


    public BuildingFragment() {
        super();
    }

//    public BuildingFragment(HouseInfoBasedataResp houseInfoBasedataResp) {
//        this.houseInfoBasedataResp = houseInfoBasedataResp;
//    }


    public void setLayout(int layout) {

        this.layout = layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        houseInfoBasedataResp = (HouseInfoBasedataResp) getArguments().getSerializable(BuildingMainActivity.HOUSEINFOBEAN);

        switch (layout) {

            case R.layout.fragment1_building: {

                View view = View.inflate(this.getActivity(), R.layout.fragment1_building, null);
//                GridView buidinggridview = (GridView) view.findViewById(R.id.gridview);
//                if (houseInfoBasedataResp != null) {
//                    //Log.e("BuildingFragment", "------houseInfoBasedataResp不是空---------");
//                    huxingarr.clear();
//                    huxingarr.addAll(houseInfoBasedataResp.getHuxingarr());
//                }
//                gridViewAdapter = new GridViewAdapter(getActivity(), huxingarr);
//
//                //添加并且显示
//                buidinggridview.setAdapter(gridViewAdapter);

                MyListView listView = (MyListView) view.findViewById(R.id.listView);
                if(houseInfoBasedataResp != null){
                    huxingarr.clear();
                    huxingarr.addAll(houseInfoBasedataResp.getHuxingarr());
                }
                listViewAdapter = new ListViewAdapter(getActivity(),huxingarr);
                listView.setAdapter(listViewAdapter);

                return view;
            }
            case R.layout.fragment2_building: {

                View view = View.inflate(this.getActivity(), R.layout.fragment2_building, null);

                basic_open_time = (TextView) view.findViewById(R.id.basic_open_time);
                basic_open_time.setText("开盘时间:" + houseInfoBasedataResp.getStartTime());

                basic_jiaofang_time = (TextView) view.findViewById(R.id.basic_jiaofang_time);
                basic_jiaofang_time.setText("交房时间:" + houseInfoBasedataResp.getFinishdate());

                basic_chanquan_time = (TextView) view.findViewById(R.id.basic_chanquan_time);
                basic_chanquan_time.setText("产权年限:" + houseInfoBasedataResp.getRight_year());

                basic_developers = (TextView) view.findViewById(R.id.basic_developers);
                basic_developers.setText("开发商:" + houseInfoBasedataResp.getDeveloper());

                basic_wuye = (TextView) view.findViewById(R.id.basic_wuye);
                basic_wuye.setText("物业公司:" + houseInfoBasedataResp.getPropertymanage());

                basic_area = (TextView) view.findViewById(R.id.basic_area);
                basic_area.setText("建筑面积:" + houseInfoBasedataResp.getBuildarea());

                basic_projtype = (TextView) view.findViewById(R.id.basic_projtype);
                basic_projtype.setText("建筑类型:" + houseInfoBasedataResp.getProjtype());

                basic_projfitment = (TextView) view.findViewById(R.id.basic_projfitment);
                basic_projfitment.setText("装修情况:" + houseInfoBasedataResp.getProjfitment());

                //绿化率
                //  basic_green = (TextView) view.findViewById(R.id.basic_green);
                //  basic_green.setText(houseInfoBasedataResp.getBuildarea());

                basic_volume = (TextView) view.findViewById(R.id.basic_volume);
                basic_volume.setText("容积率:" + houseInfoBasedataResp.getDimension());

                basic_homenum = (TextView) view.findViewById(R.id.basic_homenum);
                basic_homenum.setText("总户数:" + houseInfoBasedataResp.getTotal_door());
                return view;
            }

            case R.layout.fragment3_building: {
                View view = View.inflate(this.getActivity(), R.layout.fragment3_building, null);

                View buidingEat = view.findViewById(R.id.buiding_eat);
                ((ImageView) buidingEat.findViewById(R.id.ItemImage)).setImageResource(R.drawable.show_limit);
                ((TextView) buidingEat.findViewById(R.id.ItemText)).setText("不限");

                View buidingEdu = view.findViewById(R.id.buiding_edu);
                ((ImageView) buidingEdu.findViewById(R.id.ItemImage)).setImageResource(R.drawable.per_edu);
                ((TextView) buidingEdu.findViewById(R.id.ItemText)).setText("教育");

                View buidingPlay = view.findViewById(R.id.buiding_play);
                ((ImageView) buidingPlay.findViewById(R.id.ItemImage)).setImageResource(R.drawable.show_park);
                ((TextView) buidingPlay.findViewById(R.id.ItemText)).setText("公园");

                View buidingShop = view.findViewById(R.id.buiding_shop);
                ((ImageView) buidingShop.findViewById(R.id.ItemImage)).setImageResource(R.drawable.per_shop);
                ((TextView) buidingShop.findViewById(R.id.ItemText)).setText("购物");

                View buidingTra = view.findViewById(R.id.buiding_tra);
                ((ImageView) buidingTra.findViewById(R.id.ItemImage)).setImageResource(R.drawable.show_subway);
                ((TextView) buidingTra.findViewById(R.id.ItemText)).setText("地铁");

                View buidingHos = view.findViewById(R.id.buiding_hos);
                ((ImageView) buidingHos.findViewById(R.id.ItemImage)).setImageResource(R.drawable.per_hos);
                ((TextView) buidingHos.findViewById(R.id.ItemText)).setText("医疗");


                return view;
            }

        }

        return inflater.inflate(layout, container, false);
    }
}
