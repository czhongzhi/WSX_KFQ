package com.inetgoes.fangdd.IM_Util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.text.Html;
import android.util.Log;

import com.inetgoes.fangdd.FangApplication;
import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.activity.MainActivity;
import com.inetgoes.fangdd.activity.MessageingActivity;
import com.inetgoes.fangdd.activity.MyMessageActivity;
import com.inetgoes.fangdd.model.KfqMessage;
import com.inetgoes.fangdd.service.PushService;
import com.inetgoes.fangdd.util.JacksonMapper;
import com.inetgoes.fangdd.util.L;

import org.jivesoftware.smack.chat.Chat;

import java.io.IOException;
import java.util.Map;

/**
 * Created by czz on 2015/11/25.
 */
public class LoginOpenfire extends AsyncTask<String, Void, Boolean> {
    public static final int MSG_SESSION = 0;            //会话中消息
    public static final int MSG_QIANG_DAN = 1;          //抢单通知
    public static final int MSG_PAI_DAN = 2;            //派单通知
    public static final int MSG_RESULT_DAN = 3;         //抢单结果通知
    public static final int MSG_CALL_NOTIFY = 4;            //打电话通知
    public static final int MSG_CANCEL_NOTIFY = 5;          //取消预约通知

    public static boolean isPullMyMsg = false;  //是否在我的消息界面，是，刷新，否，发通知
    public static boolean isShowSess = false;  //判断是否是在会话界面
    public static String sessionid = "";       //判断消息是否是本会话的，是更新适配器，否发通知
    public static int currMateNum = 0;

    private static int notify_ID = 100;

    @Override
    protected Boolean doInBackground(String... params) {
        XmppUtil.getInstance().closeConnection();
        return XmppUtil.getInstance().login(params[0], params[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            XmppUtil.getInstance().setMessageListener(new MessageCallback() {
                @Override
                public void dealMessage(Chat chat, final org.jivesoftware.smack.packet.Message message) {
                    if (message.getBody() != null && !message.getBody().trim().equals("")) {

                        Log.e("czhongzhi", "openfire message.getBody()" + message.getBody());
                        classifyDealMsg(chat, message);

                    }
                }
            });
            Log.e("czhongzhi", "openfire 登录成功");
        } else {
            Log.e("czhongzhi", "openfire 登录失败");
        }
    }

    /**
     * 对消息分类处理
     */
    private void classifyDealMsg(Chat chat, org.jivesoftware.smack.packet.Message message) {
        Log.e("czhongzhi", "isShowSess " + isShowSess);
        KfqMessage kfqMessage = null;
        try {
            kfqMessage = JacksonMapper.getObjectMapper().readValue(message.getBody(), KfqMessage.class);

            int Msg_Type = getDealType(kfqMessage.getMsgtype(), kfqMessage.getNotifytype());

            switch (Msg_Type) {
                case MSG_SESSION: //会话中消息
                    dealSessionMsg(message, kfqMessage);
                    break;
                case MSG_QIANG_DAN: //抢单通知
                    Log.e("czhongzhi", "抢单通知");
                    break;
                case MSG_PAI_DAN:  //派单通知
                    Log.e("czhongzhi", "派单通知");
                    break;
                case MSG_RESULT_DAN: //抢单结果通知
                    Log.e("czhongzhi", "抢单结果通知");
                    dealNotifyMsg_Resultdan(kfqMessage);
                    break;
                case MSG_CALL_NOTIFY:  //打电话通知
                    Log.e("czhongzhi", "打电话通知");
                    break;
                case MSG_CANCEL_NOTIFY: //取消预约通知
                    Log.e("czhongzhi", "取消预约通知");
                    break;
                default:
                    Log.e("czhongzhi", "看看服务器类型是否写错了");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("czhongzhi", "LoginOpenfire KfqMessage 解析错误");
        }
    }

    /**
     * 判断消息的类型
     *
     * @param msgtype
     * @param notifytype
     * @return
     */
    private int getDealType(String msgtype, String notifytype) {
        if ("notify".equals(msgtype)) {//通知消息
            if ("qiangdan".equals(notifytype)) {
                return MSG_QIANG_DAN;
            } else if ("paidan".equals(notifytype)) {
                return MSG_PAI_DAN;
            } else if ("resultdan".equals(notifytype)) {
                return MSG_RESULT_DAN;
            } else if ("callnotify".equals(notifytype)) {
                return MSG_CALL_NOTIFY;
            } else if ("cancelnotify".equals(notifytype)) {
                return MSG_CANCEL_NOTIFY;
            } else {
                return 10086;//该类型不存在
            }
        } else {//会话消息
            return MSG_SESSION;
        }
    }

    /**
     * 会话消息 处理
     *
     * @param message
     * @param kfqMessage
     */
    private void dealSessionMsg(org.jivesoftware.smack.packet.Message message, KfqMessage kfqMessage) {
        if (isShowSess) {
            if (null != kfqMessage) {
                Log.e("czhongzhi", "sesserid " + sessionid + " " + kfqMessage.getSessionid());
                if (sessionid.equals(kfqMessage.getSessionid())) {   //消息是会话页面的内容，更新适配器
                    Message msg = new Message();
                    msg.what = 411;
                    msg.obj = kfqMessage;
                    MessageingActivity.activity.handler.sendMessage(msg);
                } else {  //在会话界面，但不是本会话消息
                    getNotif(message, kfqMessage);
                }
            }
        } else { //不在会话界面，发通知提醒
            if (isPullMyMsg) {
                MyMessageActivity.activity.mHandler.sendEmptyMessage(4011);
            } else {
                getNotif(message, kfqMessage);
                L.LogI("dealSessionMsg mainActivity is null -- " + (MainActivity.mainActivity == null));
                if (MainActivity.mainActivity != null) {

                    MainActivity.mainActivity.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.mainActivity.changeRedUdataIcon(true);
                        }
                    });
                }
            }
        }
    }

    private void dealNotifyMsg_Resultdan(final KfqMessage kfqMessage) {
        MainActivity.mainActivity.mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.mate_wait_text != null) {
                    String oldtext = "有" + LoginOpenfire.currMateNum + "个人符合你需求";
                    FangApplication.brokerNum = kfqMessage.getNum();
                    FangApplication.tranid = kfqMessage.getSessionid();

                    Log.e("czhongzhi", "FangApplication. num -- " + FangApplication.brokerNum);
                    Log.e("czhongzhi", "FangApplication. tranid -- " + FangApplication.tranid);

                    String uptext = "<font size='18' color='#8f8f90'>" + oldtext + "<br></font><font size='14' color='#8f8f90'>有" +
                            FangApplication.brokerNum + "人抢单</font>";

                    if (FangApplication.brokerNum == 3) {
                        FangApplication.time = 3;
                        FangApplication.currTime = FangApplication.time;
                        uptext = "<font size='18' color='#8f8f90'>已有" + FangApplication.brokerNum + "抢单，3秒后跳转<br></font>";
                    } else {

                    }
                    Log.e("czhongzhi", "FangApplication. uptext -- " + uptext);
                    MainActivity.mate_wait_text.setText(Html.fromHtml(uptext));
                }
            }
        });
    }

    /**
     * get通知
     *
     * @param message
     * @param kfqMessage
     */
    private void getNotif(final org.jivesoftware.smack.packet.Message message, final KfqMessage kfqMessage) {
        new Thread() {
            @Override
            public void run() {
                notify_ID++;

                Intent intent = new Intent(PushService.context, MessageingActivity.class);
                intent.putExtra(MessageingActivity.SESSION_ID, kfqMessage.getSessionid());
                intent.putExtra(MessageingActivity.SESSION_TOUSERID, kfqMessage.getTouserid());
                intent.putExtra(MessageingActivity.SESSION_FROM, kfqMessage.getFromusername());
                NotificationUtils.createNotif(PushService.context, R.drawable.ic_launcher, kfqMessage.getMsgtext(), kfqMessage.getFromusername(), kfqMessage.getMsgtext(), intent, notify_ID, 500);
            }
        }.start();
    }

    /**
     * get通知
     *
     * @param message
     * @param kfqMessage
     */
    private void getNotif(final org.jivesoftware.smack.packet.Message message, final KfqMessage kfqMessage, final Intent intent) {
        new Thread() {
            @Override
            public void run() {
                notify_ID++;
                NotificationUtils.createNotif(PushService.context, R.drawable.ic_launcher, kfqMessage.getTicker(), kfqMessage.getTitle(), kfqMessage.getMsgtext(), intent, notify_ID, 500);
            }
        }.start();
    }
}
