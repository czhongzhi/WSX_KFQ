package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.IM_Util.LoginOpenfire;
import com.inetgoes.fangdd.IM_Util.XmppUtil;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.manager.AppSharePrefManager;
import com.inetgoes.fangdd.model.KfqMessage;
import com.inetgoes.fangdd.model.KfqMessageResp_Broker;
import com.inetgoes.fangdd.model.KfqMessage_Send;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.view.CustomTitleBar;
import com.inetgoes.fangdd.view.RightDeal;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息聊天页
 * Created by czz on 2015/11/11
 */
public class MessageingActivity extends Activity {
    public static MessageingActivity activity;
    public static final String SESSION_ID = "sessionid";
    public static final String SESSION_FROM = "session_from";
    public static final String SESSION_TOUSERID = "session_touserin";

    public String sessionid;
    private int userid;
    private int touserid;
    private String username;

    private boolean isFromMyYuyue = false;

    private String touser_image;
    private String fromuser_image;

    private int startindex = 0;
    private int pagenum = 10;

    private PullToRefreshListView listView;
    public MessingAdapter adapter;
    public List<KfqMessage> messages = new ArrayList<>();

    private EditText ed_content;
    private Button but_send;

    private TextView actTitle;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 411) {
                messages.add((KfqMessage) msg.obj);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromMyYuyue = getIntent().getBooleanExtra("isFromMyYuyue", false);
        actTitle = CustomTitleBar.getTitleBar(getIntent().getStringExtra(SESSION_FROM), this, new RightDeal() {
            @Override
            public void deal() {
                finish();
                if (!isFromMyYuyue) {
                    startActivity(new Intent(MessageingActivity.this, MyMessageActivity.class));
                }
            }
        });
        setContentView(R.layout.activity_messageing);

        sessionid = getIntent().getStringExtra(SESSION_ID);
        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();
        touserid = getIntent().getIntExtra(SESSION_TOUSERID, 0);
        fromuser_image = AppSharePrefManager.getInstance(this).getLastest_login_touxiang_imageurl();
        username = AppSharePrefManager.getInstance(this).getLastest_login_username();

        activity = MessageingActivity.this;
        LoginOpenfire.isShowSess = true;
        LoginOpenfire.sessionid = sessionid;

        Log.e("czhongzhi", "MessageingActivity " + (MessageingActivity.activity != null));

        Log.e("czhongzhi", "sessionid -- " + sessionid);

        initView();

        reqNewMsgs(startindex, pagenum, false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        activity = MessageingActivity.this;
        LoginOpenfire.isShowSess = true;
        LoginOpenfire.sessionid = sessionid;
    }

    private void initView() {
        ed_content = (EditText) findViewById(R.id.ed_content);
        but_send = (Button) findViewById(R.id.but_send);

        listView = (PullToRefreshListView) findViewById(R.id.listView);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                startindex += pagenum;
                reqNewMsgs(startindex, pagenum, true);
            }
        });

        adapter = new MessingAdapter(this, messages, userid);
        listView.setAdapter(adapter);

        ed_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    but_send.setClickable(false);
                    but_send.setBackgroundResource(R.drawable.button_divider_bg);
                    return;
                }
                but_send.setClickable(true);
                but_send.setBackgroundResource(R.drawable.button_divider_bg_select);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        but_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cntext = ed_content.getText().toString().trim();

                sendMsgToDB(cntext);

                ed_content.setText("");
            }
        });
    }

    /**
     * 请求最新会话消息
     *
     * @param startindex
     * @param pagenum
     */
    private void reqNewMsgs(int startindex, int pagenum, final boolean isPush) {
        Map<String, Object> map = new HashMap<>();
        map.put("sessionid", sessionid);
        map.put("userid", userid);
        map.put("startindex", startindex);
        map.put("pagenum", pagenum);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "MessageingActivity onPostExecute is " + result);
                if (!TextUtils.isEmpty(result)) {
                    try {
                        KfqMessageResp_Broker kfqMessageResp = JacksonMapper.getObjectMapper().readValue(result, KfqMessageResp_Broker.class);
                        touser_image = kfqMessageResp.getTouser_image();
                        actTitle.setText(kfqMessageResp.getTouser_name());
                        List<KfqMessage> messageList = kfqMessageResp.getMessages();
                        if (null != messageList || messages.size() != 0) {
                            for (KfqMessage kfqm : messageList) {
                                messages.add(0, kfqm);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (isPush) {
                    listView.onRefreshComplete();
                }
            }
        }).execute(Constants.readNewSessMsgsUrl, map);
    }

    /**
     * 先发送信息到数据库，在发送openfrie;
     * // 值有: txt, link_fang, link_eval   (必写)  //类型分类没有出来，方法没有写完
     */
    private void sendMsgToDB(final String msgtext) {
        Log.e("czhongzhi", "MessageingActivity sendMsgToDB");

        KfqMessage_Send sendMsg = new KfqMessage_Send();
        sendMsg.setSessionid(sessionid);
        sendMsg.setFromuserid(userid);
        sendMsg.setTouserid(touserid);
        sendMsg.setMsgtype("txt");
        sendMsg.setMsgtext(msgtext);

        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "MessageingActivity sendMsgToDB is " + result);
                if (!TextUtils.isEmpty(result)) {
                    Boolean state = (Boolean) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                    if (state) {

                        KfqMessage kfqMessage = new KfqMessage();
                        kfqMessage.setSessionid(sessionid);
                        kfqMessage.setFromuserid(userid);
                        kfqMessage.setTouserid(touserid);
                        kfqMessage.setFromusername(username);
                        kfqMessage.setMsgtype("txt");
                        kfqMessage.setMsgtext(msgtext);
                        kfqMessage.setCreatetime(System.currentTimeMillis());

                        try {
                            String msg_body = JacksonMapper.getObjectMapper().writeValueAsString(kfqMessage);
                            XmppUtil.getInstance().sendMessage(String.valueOf(touserid), msg_body);

                            messages.add(kfqMessage);
                            adapter.notifyDataSetChanged();
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).execute(Constants.sendNesSessMsgUrl, sendMsg);
    }

    private class MessingAdapter extends BaseAdapter {
        private Context context;
        private List<KfqMessage> list;
        private LayoutInflater inflater;
        private int userid;

        private MessingAdapter(Context context, List<KfqMessage> list, int userid) {
            this.context = context;
            this.list = list;
            this.inflater = LayoutInflater.from(context);
            this.userid = userid;
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
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            ////me: 0    you: 1   mess: 2
            if (userid == list.get(position).getFromuserid()) {
                return 0;
            } else if ("txt".equals(list.get(position).getMsgtype())) {
                return 1;
            } else {
                return 2;
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolper vh = null;
            if (convertView == null) {
                vh = new ViewHolper();
                switch (getItemViewType(position)) {
                    case 0:
                        convertView = inflater.inflate(R.layout.item_messageing_me, parent, false);
                        vh.talk_icon = (ImageView) convertView.findViewById(R.id.talk_icon);
                        vh.talk_time = (TextView) convertView.findViewById(R.id.talk_time);
                        vh.talk_content = (TextView) convertView.findViewById(R.id.talk_content);
                        break;
                    case 1:
                        convertView = inflater.inflate(R.layout.item_messageing_you, parent, false);
                        vh.talk_icon = (ImageView) convertView.findViewById(R.id.talk_icon);
                        vh.talk_time = (TextView) convertView.findViewById(R.id.talk_time);
                        vh.talk_content = (TextView) convertView.findViewById(R.id.talk_content);
                        break;
                    case 2:
                        convertView = inflater.inflate(R.layout.item_messageing_mess, parent, false);
                        vh.talk_icon = (ImageView) convertView.findViewById(R.id.talk_icon);
                        vh.talk_time = (TextView) convertView.findViewById(R.id.talk_time);

                        vh.mess_huxing = (TextView) convertView.findViewById(R.id.mess_huxing);
                        vh.push_msg_click = (LinearLayout) convertView.findViewById(R.id.push_msg_click);
                        vh.mess_title = (TextView) convertView.findViewById(R.id.mess_title);
                        vh.loupen_icon = (ImageView) convertView.findViewById(R.id.loupen_icon);
                        vh.loupen_title = (TextView) convertView.findViewById(R.id.loupen_title);
                        vh.text2 = (TextView) convertView.findViewById(R.id.text2);
                        vh.mess_details = (TextView) convertView.findViewById(R.id.mess_details);
                        break;
                }
                convertView.setTag(vh);
            } else {
                vh = (ViewHolper) convertView.getTag();
            }

            final KfqMessage kfqMessage = list.get(position);

            vh.talk_time.setText(kfqMessage.getCreatetime_str());

            switch (getItemViewType(position)) {
                case 0:
                    if (!TextUtils.isEmpty(fromuser_image)) {
                        ImageLoader.getInstance().displayImage(fromuser_image, vh.talk_icon, FangApplication.options_R, FangApplication.animateFirstListener);
                    }
                    vh.talk_content.setText(kfqMessage.getMsgtext());
                    break;
                case 1:
                    if (!TextUtils.isEmpty(touser_image)) {
                        ImageLoader.getInstance().displayImage(touser_image, vh.talk_icon, FangApplication.options_R, FangApplication.animateFirstListener);
                    }
                    vh.talk_content.setText(kfqMessage.getMsgtext());
                    break;
                case 2:
                    if (!TextUtils.isEmpty(touser_image)) {
                        ImageLoader.getInstance().displayImage(touser_image, vh.talk_icon, FangApplication.options_R, FangApplication.animateFirstListener);
                    }
                    vh.mess_title.setText(kfqMessage.getLink_title());

                    if ("link_fang".equals(kfqMessage.getMsgtype())) {
                        Log.e("czhongzhi", "房源推荐");

                        if (!TextUtils.isEmpty(kfqMessage.getLoupan_imageurl())) {
                            ImageLoader.getInstance().displayImage(kfqMessage.getLoupan_imageurl(), vh.loupen_icon, FangApplication.options, FangApplication.animateFirstListener);
                        }

                        vh.mess_huxing.setText((TextUtils.isEmpty(kfqMessage.getMsgtext()) ? "" : kfqMessage.getMsgtext()));

                        vh.loupen_title.setText(kfqMessage.getLoupan_name());
                        vh.text2.setText(kfqMessage.getLoupan_addr());
                        vh.push_msg_click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MessageingActivity.this, BuildingMainActivity.class);
                                intent.putExtra(BuildingMainActivity.NEWCODE, kfqMessage.getNewcode());
                                intent.putExtra(BuildingMainActivity.TITLEBAR, kfqMessage.getLoupan_name());
                                intent.putExtra(BrokerMainActivity.BROKER_ID, kfqMessage.getTouserid());
                                intent.putExtra(Constants.TRANID, kfqMessage.getSessionid());
                                intent.putExtra("isFromMsgIn", "Y");
                                intent.putExtra("isconnected", "Y");

                                intent.putExtra("huxingtype", kfqMessage.getEval_maintext());
                                intent.putExtra("msgid", kfqMessage.getMsgid());
                                startActivity(intent);
                                //Toast.makeText(context,"查看更多",Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if ("link_eval".equals(kfqMessage.getMsgtype())) {
                        Log.e("czhongzhi", "评测推荐");

                        if (!TextUtils.isEmpty(kfqMessage.getLoupan_imageurl())) {
                            ImageLoader.getInstance().displayImage(kfqMessage.getLoupan_imageurl(), vh.loupen_icon, FangApplication.options, FangApplication.animateFirstListener);
                        }
                        vh.loupen_title.setText(kfqMessage.getLoupan_name());
                        vh.text2.setText(kfqMessage.getEval_maintext());
                        vh.push_msg_click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(context, "查看更多", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    break;
            }
            return convertView;
        }

        private class ViewHolper {
            private ImageView talk_icon;
            private TextView talk_time;
            private TextView talk_content;

            private TextView mess_title;
            private ImageView loupen_icon;
            private TextView loupen_title;
            private TextView text2;

            private TextView mess_details;
            private LinearLayout push_msg_click;
            private TextView mess_huxing;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        messages.clear();
        LoginOpenfire.isShowSess = false;
        LoginOpenfire.sessionid = "";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messages.clear();
        LoginOpenfire.isShowSess = false;
        LoginOpenfire.sessionid = "";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            if (!isFromMyYuyue)
                startActivity(new Intent(MessageingActivity.this, MyMessageActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }
}
