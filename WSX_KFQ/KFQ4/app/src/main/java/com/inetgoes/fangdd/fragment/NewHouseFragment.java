package com.inetgoes.fangdd.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.activity.MainActivity;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.util.JacksonMapper;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewHouseFragment extends Fragment {

    private TextView brokernum, brokernum_text, housenum, housenum_text;

    public NewHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(this.getActivity(),R.layout.fragment_new_house,null);

        brokernum = (TextView) view.findViewById(R.id.brokernum);
        brokernum_text = (TextView) view.findViewById(R.id.brokernum_text);
        housenum = (TextView) view.findViewById(R.id.housenum);
        housenum_text = (TextView) view.findViewById(R.id.housenum_text);

        fillData();

       return view;
    }

    /**
     * 网络请求填数据并填充
     */
    private void fillData() {
        String url = Constants.MainNewHouseNumUrl + "?city=" + MainActivity.city;
        Log.e("czhongzhi", url);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "" + result);
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                Map<String, String> map = JacksonMapper.getInstance().mapFromJson(result);

                int bnum = Integer.valueOf(map.get("brokernum"));
                brokernum.setText("" + bnum);
                brokernum_text.setText("当前有" + bnum + "房产专家在线");

                int hnum = Integer.valueOf(map.get("housenum"));
                housenum.setText("" + hnum);
                housenum_text.setText("共发布" + hnum + "个楼盘");
            }
        }).execute(url);
    }


}
