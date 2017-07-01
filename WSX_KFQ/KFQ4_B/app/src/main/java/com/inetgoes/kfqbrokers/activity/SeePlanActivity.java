package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Trace;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JavaType;
import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.model.BackupInfo;
import com.inetgoes.kfqbrokers.model.DanPlan;
import com.inetgoes.kfqbrokers.model.DanPlanList;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 查看进度的Activity
 */
public class SeePlanActivity extends Activity {
    public static final String FROMTYPE = "fromtype";
    public static final String KANRECID = "kanrecid";
    public static final String TITLE = "title";

    private String fromtype;
    private int kanrecid;

    private TextView title;
    private MyListView mListView;
    private List<DanPlanList> datas = new ArrayList<>();
    private TraceAdapter adapter;

    private TextView null_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "查看进度", true, false);
        setContentView(R.layout.activity_see_plan);

        fromtype = getIntent().getStringExtra(FROMTYPE);
        kanrecid = getIntent().getIntExtra(KANRECID, 0);

        initView();
        title.setText(getIntent().getStringExtra(TITLE));


        getData(fromtype, kanrecid);

    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        mListView = (MyListView) findViewById(R.id.listView);
        null_content = (TextView) findViewById(R.id.null_content);

        adapter = new TraceAdapter(this, datas);
        mListView.setAdapter(adapter);

    }



    private Dialog waitDialog;

    private void getData(String fromtype, int kanrecid) {
        waitDialog = DialogUtil.showWait2(SeePlanActivity.this);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogI("查看进度 is " + result);

                waitDialog.dismiss();

                if (TextUtils.isEmpty(result)) {
                    return;
                }

                try {
                    DanPlan danPlan = JacksonMapper.getObjectMapper().readValue(result, DanPlan.class);
                    setData(danPlan);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.seePlanUrl + "?fromtype=" + fromtype + "&kanrecid=" + kanrecid);
    }

    private void setData(DanPlan danPlan) {
        if("Y".equals(danPlan.getFlag_callphone())){
            datas.add(new DanPlanList("客服人员已电话跟进",danPlan.getFlag_callphone_tm()));
        }
        if("Y".equals(danPlan.getFlag_daofang())){
            datas.add(new DanPlanList("客户已到访",danPlan.getFlag_daofang_tm()));
        }
        if("Y".equals(danPlan.getFlag_rengou())){
            datas.add(new DanPlanList("客户已交定金",danPlan.getFlag_rengou_tm()));
        }
        if("Y".equals(danPlan.getFlag_qianyue())){
            datas.add(new DanPlanList("客户已签合同",danPlan.getFlag_qianyue_tm()));
        }
        if("Y".equals(danPlan.getFlag_jieyong())){
            datas.add(new DanPlanList("已结佣，佣金会在3-7个工作日内到账",danPlan.getFlag_jieyong_tm()));
        }
        if(datas.size() != 0){
            null_content.setVisibility(View.GONE);//暂无数据
        }
        adapter.notifyDataSetChanged();
    }


    private class TraceAdapter extends BaseAdapter {
        private List<DanPlanList> list;
        private LayoutInflater inflater;

        public TraceAdapter(Context context, List<DanPlanList> list) {
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
                convertView = inflater.inflate(R.layout.item_lookplan, parent, false);
                vh.trace_img = (ImageView) convertView.findViewById(R.id.trace_img);
                vh.trace_state = (TextView) convertView.findViewById(R.id.trace_state);
                vh.trace_time = (TextView) convertView.findViewById(R.id.trace_time);
                vh.trace_line = (TextView) convertView.findViewById(R.id.trace_line);
                convertView.setTag(vh);
            } else {
                vh = (ViewHopler) convertView.getTag();
            }

            vh.trace_img.setImageResource(R.drawable.order_gray);
            vh.trace_line.setVisibility(View.VISIBLE);
            if (position == getCount() - 1) {
                vh.trace_img.setImageResource(R.drawable.order_yell);
                vh.trace_line.setVisibility(View.GONE);
            }

            vh.trace_state.setText(list.get(position).getText());
            vh.trace_time.setText(list.get(position).getTime());

            return convertView;
        }

        class ViewHopler {
            ImageView trace_img;
            TextView trace_state;
            TextView trace_time;
            TextView trace_line;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (waitDialog != null && waitDialog.isShowing()){
            waitDialog.dismiss();
        }
    }
}
