package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.model.BrokerComm;
import com.inetgoes.fangdd.view.CustomTitleBar;

import java.util.ArrayList;
import java.util.List;

public class NotifyActivity extends Activity {

    private PullToRefreshListView listView;
    private List<String> notifys = new ArrayList<>();
    private NotityAdapter adapter;

    private TextView line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar(this, "通知", true, false);
        setContentView(R.layout.activity_notify);

        initView();

        initData();
    }

    private void initData() {
//        for(int i = 0;i < 20;i++){
//            notifys.add(String.valueOf(i));
//        }
//        adapter.notifyDataSetChanged();
    }

    private void initView() {
        line = (TextView) findViewById(R.id.line);
        line.setVisibility(View.INVISIBLE);
        listView = (PullToRefreshListView) findViewById(R.id.listView);

        View none_content = LayoutInflater.from(this).inflate(R.layout.none_content_hint, null);
        ((ImageView) none_content.findViewById(R.id.none_icon)).setImageResource(R.drawable.notice);
        ((TextView) none_content.findViewById(R.id.none_text)).setText("暂无通知");
        listView.setEmptyView(none_content);

        adapter = new NotityAdapter(this, notifys);
        listView.setAdapter(adapter);

    }

    private class NotityAdapter extends BaseAdapter {
        private Context context;
        private List<String> list;
        private LayoutInflater inflater;

        public NotityAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
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
            if(getCount() == 0){
                line.setVisibility(View.INVISIBLE);
            }else{
                line.setVisibility(View.VISIBLE);
            }
            ViewHopler vh = null;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.item_notify, parent, false);
                vh = new ViewHopler();

                convertView.setTag(vh);
            } else {
                vh = (ViewHopler) convertView.getTag();
            }

            return convertView;
        }

        private class ViewHopler {

        }

    }

}
