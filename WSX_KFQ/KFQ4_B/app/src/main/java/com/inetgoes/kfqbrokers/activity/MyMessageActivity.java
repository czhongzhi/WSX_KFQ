package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JavaType;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.IM_Util.LoginOpenfire;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.MyMessage;
import com.inetgoes.kfqbrokers.utils.DialogUtil;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.RightDeal;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的消息
 * Created by czz on 2015/11/11
 */
public class MyMessageActivity extends Activity {
    public static MyMessageActivity activity;
    private PullToRefreshListView listView;
    private List<MyMessage> messages = new ArrayList<>();
    private MyMessageAdaper adaper;

    private int userid;
    private String roletype = "broker";//角色类型:值有: user,broker,owner分别表示:用户,经纪人,业主

    private int startindex = 0;
    private int pagenum = 20;

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 4011){
                startindex = 0;
                messages.clear();
                reqMessList(startindex, pagenum, true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomTitleBar.getTitleBar("我的消息", this, new RightDeal() {
            @Override
            public void deal() {
                finish();
                startActivity(new Intent(MyMessageActivity.this, MainActivity.class));
            }
        });
        setContentView(R.layout.activity_my_message);

        activity = this;
        LoginOpenfire.isPullMyMsg = true;

        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();

        initView();

        reqMessList(startindex, pagenum, true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        reqMessList(startindex, pagenum, true);

        activity = this;
        LoginOpenfire.isPullMyMsg = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        activity = this;
        LoginOpenfire.isPullMyMsg = true;
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.listView);

        adaper = new MyMessageAdaper(this, messages);
        listView.setAdapter(adaper);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MyMessageActivity.this, "" + (position - 1), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyMessageActivity.this, MessageingActivity.class);
                intent.putExtra(MessageingActivity.SESSION_ID, messages.get(position - 1).getSessionid());
                intent.putExtra(MessageingActivity.SESSION_FROM, messages.get(position - 1).getUsername());
                intent.putExtra(MessageingActivity.SESSION_TOUSERID, messages.get(position - 1).getTouserid());
                startActivity(intent);
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                startindex = 0;
                reqMessList(startindex, pagenum, true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                startindex += pagenum;
                reqMessList(startindex, pagenum, false);
            }
        });
    }

    private void reqMessList(int startindex, int pagenum, final boolean isDown) {
        final Dialog waitDialog = DialogUtil.showWait(MyMessageActivity.this);
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("roletype", roletype);
        map.put("startindex", startindex);
        map.put("pagenum", pagenum);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "MyMessageActivity is " + result);
                waitDialog.dismiss();
                if (null != result && !"".equals(result.toString().trim())) {
                    JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, MyMessage.class);
                    List<MyMessage> temps = null;
                    try {
                        temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                        if (isDown) {
                            messages.clear();
                            messages.addAll(0, temps);
                        } else {
                            messages.addAll(temps);
                        }
                        listView.onRefreshComplete();
                        adaper.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).execute(Constants.readMyMsgListUrl, map);
    }

    private class MyMessageAdaper extends BaseAdapter {
        private Context context;
        private List<MyMessage> list;
        private LayoutInflater inflater;

        public MyMessageAdaper(Context context, List<MyMessage> list) {
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
            ViewHolper vh = null;
            if (convertView == null) {
                vh = new ViewHolper();
                convertView = inflater.inflate(R.layout.item_mymessage, parent, false);
                vh.mess_icon = (ImageView) convertView.findViewById(R.id.mess_icon);
                vh.mess_name = (TextView) convertView.findViewById(R.id.mess_name);
                vh.mess_content = (TextView) convertView.findViewById(R.id.mess_content);
                vh.mess_time = (TextView) convertView.findViewById(R.id.mess_time);
                vh.mess_notloolnum = (TextView) convertView.findViewById(R.id.mess_notloolnum);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolper) convertView.getTag();
            }
            MyMessage myMsg = list.get(position);
            vh.sessionid = myMsg.getSessionid();
            vh.touserid = myMsg.getTouserid();
            if (!TextUtils.isEmpty(myMsg.getUserimage())) {
                ImageLoader.getInstance().displayImage(myMsg.getUserimage(), vh.mess_icon, FangApplication.options_R, FangApplication.animateFirstListener);
            }
            vh.mess_name.setText(myMsg.getUsername());
            vh.mess_content.setText(myMsg.getContent());
            vh.mess_time.setText(myMsg.getCreatetime());
            if (myMsg.getUpdatenum() == 0) {
                vh.mess_notloolnum.setVisibility(View.INVISIBLE);
            } else {
                vh.mess_notloolnum.setVisibility(View.VISIBLE);
                vh.mess_notloolnum.setText(String.valueOf(myMsg.getUpdatenum()));
            }
            return convertView;
        }

        private class ViewHolper {
            private ImageView mess_icon;
            private TextView mess_name;
            private TextView mess_content;
            private TextView mess_time;
            private TextView mess_notloolnum;

            private String sessionid;
            private Integer touserid;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            startActivity(new Intent(MyMessageActivity.this, MainActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        activity = null;
        LoginOpenfire.isPullMyMsg = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
        LoginOpenfire.isPullMyMsg = false;
    }
}
