package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.inetgoes.fangdd.Constants;
import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.asynctast.HttpAsy;
import com.inetgoes.fangdd.asynctast.PostExecute;
import com.inetgoes.fangdd.model.BrokerComm;
import com.inetgoes.fangdd.util.DialogUtil;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.view.CustomTitleBar;
import com.inetgoes.fangdd.view.RightDeal;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 提交经纪人评价页面
 * Created by sunhui on 2015/11/13.
 */
public class BrokerEvaluationActivity extends Activity {
    public static final String KanFangListStateResp_COMM = "KanFangListStateResp_COMM";

    private BrokerComm brokerComm;

    private float ratingnum;


    private ImageView brokerImg;
    private TextView brokerName;
    private TextView introduce;
    private RatingBar brokerStar;
    private TextView brokerScore;
    private TextView orderDetails;
    private RatingBar brokerEvaluationStar;
    private EditText brokerEvaluationEt;
    private Button brokerEvaluationBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        brokerComm = (BrokerComm) getIntent().getSerializableExtra(KanFangListStateResp_COMM);

        initView();

        initData();

    }

    private void initView() {

        CustomTitleBar.getTitleBar(this, "完成评价", "投诉", new RightDeal() {
            @Override
            public void deal() {
                Toast.makeText(BrokerEvaluationActivity.this, "投诉", Toast.LENGTH_SHORT).show();
            }
        });
        setContentView(R.layout.activity_broker_evaluation);
        brokerImg = (ImageView) findViewById(R.id.broker_img);
        brokerName = (TextView) findViewById(R.id.broker_name);
        introduce = (TextView) findViewById(R.id.introduce);
        brokerStar = (RatingBar) findViewById(R.id.broker_star);
        brokerScore = (TextView) findViewById(R.id.broker_score);
        orderDetails = (TextView) findViewById(R.id.order_details);
        brokerEvaluationStar = (RatingBar) findViewById(R.id.broker_evaluation_star);
        brokerEvaluationEt = (EditText) findViewById(R.id.broker_evaluation_et);
        brokerEvaluationBt = (Button) findViewById(R.id.broker_evaluation_bt);

    }

    private void initData() {

        if (null == brokerComm) {
            return;
        }

        if (!TextUtils.isEmpty(brokerComm.getUserimage())) {
            ImageLoader.getInstance().displayImage(brokerComm.getUserimage(), brokerImg, FangApplication.options, FangApplication.animateFirstListener);
        }
        brokerName.setText(brokerComm.getName());
        introduce.setText(brokerComm.getBrokertype() + "(" + brokerComm.getSkillyear() + ")");
        brokerStar.setRating(brokerComm.getStarlevel());
        brokerScore.setText(String.valueOf(brokerComm.getStarlevel()));


        //RatingBar 的触摸监听
        brokerEvaluationStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                ratingBar.setRating(rating);
            }
        });

        //评价确认按钮
        brokerEvaluationBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //获取评价星级
                ratingnum = brokerEvaluationStar.getRating();
                commBroker();
            }
        });
    }

    private void commBroker() {
        Map<String, Object> map = new HashMap<>();
        map.put("recid", String.valueOf(brokerComm.getId()));
        map.put("starlevel", ratingnum);
        String content = brokerEvaluationEt.getText().toString().trim();
        map.put("content", TextUtils.isEmpty(content) ? null : content);
        new HttpAsy(new PostExecute() {
            @Override
            public void onPostExecute(String result) {
                Log.e("czhongzhi", "commBroker result is " + result);
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                String state = (String) JacksonMapper.getInstance().mapObjFromJson(result).get("status");
                Log.e("czhongzhi", "commBroker result is " + state);
                if (state.equals("true")) {//评论成功
                    startActivity(new Intent(BrokerEvaluationActivity.this, SeeHouseListActivity.class));
                    finish();
                } else {//评论失败
                    DialogUtil.showDealFailed(BrokerEvaluationActivity.this, "评论失败", null);
                }
            }
        }).execute(Constants.writeCommentKanUrl, map);
    }
}
