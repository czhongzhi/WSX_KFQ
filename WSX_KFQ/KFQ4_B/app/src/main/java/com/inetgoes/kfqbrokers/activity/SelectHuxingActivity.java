package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.inetgoes.kfqbrokers.Constants;
import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.IM_Util.XmppUtil;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.asynctast.HttpAsy;
import com.inetgoes.kfqbrokers.asynctast.PostExecute;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.HouseHuxing;
import com.inetgoes.kfqbrokers.model.HouseInfo_BrokerIntroduce;
import com.inetgoes.kfqbrokers.model.KfqMessage;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;
import com.inetgoes.kfqbrokers.utils.L;
import com.inetgoes.kfqbrokers.view.CustomTitleBar;
import com.inetgoes.kfqbrokers.view.RightDeal;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择户型
 */
public class SelectHuxingActivity extends Activity implements RightDeal {
    private ListView listView;
    private List<HouseHuxing> huxinys = new ArrayList<>();
    private SelectHuxingAdapter adapter;

    private int userid;
    private String sessionid;
    private int touserid;
    private String newcode;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        CustomTitleBar.getTitleBar(this, title, "确定", this);
        setContentView(R.layout.activity_select_huxing);

        userid = AppSharePrefManager.getInstance(this).getLastest_login_id();
        sessionid = getIntent().getStringExtra("sessionid");
        touserid = getIntent().getIntExtra("requserid", 0);
        newcode = getIntent().getStringExtra("newcode");

        initView();

        reqData(newcode);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);

        adapter = new SelectHuxingAdapter(this, huxinys);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectItem(position);
                adapter.notifyDataSetInvalidated();
            }
        });
    }

    private void reqData(String newcode) {
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("selectHuxingAct is " + result);
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                JavaType javaType = JacksonMapper.getCollectionType(ArrayList.class, HouseHuxing.class);
                List<HouseHuxing> temps = null;
                try {
                    temps = JacksonMapper.getObjectMapper().readValue(result, javaType);
                    huxinys.addAll(temps);
                    L.LogI("---------------");
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).execute(Constants.houseinfoHuxingTypeUrl + "?newcode=" + newcode);
    }

    //Title右按钮
    @Override
    public void deal() {
        if (adapter.selectItem != -1) {
            HouseHuxing huxing = (HouseHuxing) adapter.getItem(adapter.selectItem);
            selectHS(sessionid, userid, touserid, newcode, huxing.getHuxing_type(), huxing.getHuxing_desc());
        } else {
            //selectHS(sessionid, userid, touserid, newcode, null, null);
            Toast.makeText(SelectHuxingActivity.this,"请选择推送户型",Toast.LENGTH_SHORT).show();
        }
    }


    private void selectHS(final String sessionid, int fromuserid, final int touserid,
                          String newcode, String huxingtype, String huxingdesc) {
        final Map<String, Object> map = new HashMap<>();
        map.put("sessionid", sessionid);
        map.put("fromuserid", fromuserid);
        map.put("touserid", touserid);
        map.put("newcode", newcode);
        map.put("huxingtype", huxingtype);
        map.put("huxingdesc", huxingdesc);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                L.LogE("selectHs result is " + result);
                if (!TextUtils.isEmpty(result)) {
                    Map<String, Object> resMap = JacksonMapper.getInstance().mapObjFromJson(result);
                    if ((boolean) resMap.get("state")) {
                        //sendMsgTo();
                        //
                        Intent intent = new Intent(SelectHuxingActivity.this, MessageingActivity.class);
                        intent.putExtra(MessageingActivity.SESSION_ID, sessionid);
                        intent.putExtra(MessageingActivity.SESSION_FROM, "***");
                        intent.putExtra(MessageingActivity.SESSION_TOUSERID, touserid);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SelectHuxingActivity.this, "" + resMap.get("reason"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).execute(Constants.selectHouseSourceUrl, map);
    }

    /**
     * 先发送信息到数据库，在发送openfrie;
     * // 值有: txt, link_fang, link_eval   (必写)  //类型分类没有出来，方法没有写完
     */
    private void sendMsgTo() {
        HouseInfo_BrokerIntroduce hbi = (HouseInfo_BrokerIntroduce) adapter.getItem(adapter.selectItem);
        KfqMessage kfqMessage = new KfqMessage();
        kfqMessage.setSessionid(sessionid);
        kfqMessage.setFromuserid(userid);
        kfqMessage.setTouserid(touserid);
        kfqMessage.setMsgtype("link_fang");
        kfqMessage.setFromusername(AppSharePrefManager.getInstance(SelectHuxingActivity.this).getLastest_login_username());

        kfqMessage.setLoupan_name(hbi.getLoupanname());
        kfqMessage.setLoupan_imageurl(hbi.getLoupan_image_url());
        kfqMessage.setLink_title("有兴趣可接受楼盘推荐");
        kfqMessage.setLoupan_addr(hbi.getPricedesc());
        kfqMessage.setNewcode(hbi.getNewcode());

        kfqMessage.setCreatetime(System.currentTimeMillis());
        try {
            String msg_body = JacksonMapper.getObjectMapper().writeValueAsString(kfqMessage);
            XmppUtil.getInstance().sendMessage(String.valueOf(touserid), msg_body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private class SelectHuxingAdapter extends BaseAdapter {
        private List<HouseHuxing> list;
        private Context context;
        private LayoutInflater inflater;

        public SelectHuxingAdapter(Context context, List<HouseHuxing> list) {
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

        private int selectItem = -1;

        private void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder vh;
            if (null == convertView) {
                vh = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_selecthuxing, parent, false);

                vh.item_huxing_icon = (ImageView) convertView.findViewById(R.id.item_huxing_icon);
                vh.item_huxing_title = (TextView) convertView.findViewById(R.id.item_huxing_title);
                vh.item_huxing_content = (TextView) convertView.findViewById(R.id.item_huxing_content);

                convertView.setTag(vh);

            } else {
                vh = (ViewHolder) convertView.getTag();

            }

            HouseHuxing huxing = list.get(position);
            if (!TextUtils.isEmpty(huxing.getHuxing_image())) {
                ImageLoader.getInstance().displayImage(huxing.getHuxing_image(), vh.item_huxing_icon, FangApplication.options, FangApplication.animateFirstListener);
            }
            vh.item_huxing_title.setText(huxing.getHuxing_type());
            vh.item_huxing_content.setText(huxing.getHuxing_desc());


            if (position == selectItem) {
                convertView.setBackgroundResource(R.color.mate_color);
            } else {
                convertView.setBackgroundResource(R.color.white);
            }

            return convertView;
        }

        /**
         * 房源的holder
         */
        class ViewHolder {
            ImageView item_huxing_icon;
            TextView item_huxing_title;
            TextView item_huxing_content;
        }
    }

}
