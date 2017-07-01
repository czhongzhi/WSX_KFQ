package com.inetgoes.kfqbrokers.IM_Util;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.text.Html;
import android.util.Log;

import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.activity.MainActivity;
import com.inetgoes.kfqbrokers.activity.MessageingActivity;
import com.inetgoes.kfqbrokers.activity.MyMessageActivity;
import com.inetgoes.kfqbrokers.model.KfqMessage;
import com.inetgoes.kfqbrokers.service.PushService;
import com.inetgoes.kfqbrokers.utils.JacksonMapper;

import org.jivesoftware.smack.chat.Chat;

import java.io.IOException;

/**
 * Created by czz on 2015/11/25.
 */
public class LoginOpenfire extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "czhongzhi";

    public static final int MSG_SESSION = 10000;            //会话中消息
    public static final int MSG_QIANG_DAN = 10001;          //抢单通知
    public static final int MSG_PAI_DAN = 10002;            //派单通知
    public static final int MSG_RESULT_DAN = 10003;         //抢单结果通知
    public static final int MSG_CALL_NOTIFY = 10004;            //打电话通知
    public static final int MSG_CANCEL_NOTIFY = 10005;          //取消预约通知

    public static final int MSG_SELECTOTTHER_NOTIFY = 10006;     //订单已被抢
    public static final int MSG_KF_REQ = 10007;                     //看房订单请求通知
    public static final int MSG_KF_CANCEL_F_USER = 10008;           //看房订单被用户取消通知
    public static final int MSG_KFANDYUYUE_NOTIFY = 10009;           //看房订单和预约同时请求的通知

    public static boolean isPullMyMsg = false;  //是否在我的消息界面，是，刷新，否，发通知
    public static boolean isShowSess = false;  //判断是否是在会话界面
    public static String sessionid = "";       //判断消息是否是本会话的，是更新适配器，否发通知

    public static boolean isMainShow = false;//判断是否在主页面

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

                        Log.e(TAG, "openfire message.getBody()" + message.getBody());
                        classifyDealMsg(chat, message);

                    }
                }
            });
            Log.e(TAG, "openfire 登录成功");
        } else {
            Log.e(TAG, "openfire 登录失败");
        }
    }

    /**
     * 对消息分类处理
     */
    private void classifyDealMsg(Chat chat, org.jivesoftware.smack.packet.Message message) {
        Log.e(TAG, "isShowSess " + isShowSess);
        KfqMessage kfqMessage = null;
        try {
            kfqMessage = JacksonMapper.getObjectMapper().readValue(message.getBody(), KfqMessage.class);

            int Msg_Type = getDealType(kfqMessage.getMsgtype(), kfqMessage.getNotifytype());

            switch (Msg_Type) {
                case MSG_SESSION: //会话中消息
                    dealSessionMsg(message, kfqMessage);
                    break;
                case MSG_QIANG_DAN: //抢单通知
                    Log.e(TAG, "抢单通知");
                    dealNotityMsg_grad(kfqMessage);
                    break;
                case MSG_PAI_DAN:  //派单通知
                    Log.e(TAG, "派单通知");
                    dealNotityMsg_paidan(kfqMessage);
                    break;
                case MSG_RESULT_DAN: //抢单结果通知
                    Log.e(TAG, "抢单结果通知");
                    dealNotifyMsg_Resultdan(kfqMessage);
                    break;
                case MSG_CALL_NOTIFY:  //打电话通知
                    Log.e(TAG, "打电话通知");
                    dealNotifyMsg_callnotify(kfqMessage);
                    break;
                case MSG_CANCEL_NOTIFY: //取消预约通知
                    Log.e(TAG, "取消预约通知");
                    dealNotifyMsg_cancelnotify(kfqMessage);
                    break;
                case MSG_SELECTOTTHER_NOTIFY: //订单已被抢
                    Log.e(TAG, "订单已被抢");
                    dealNotifyMsg_selectother(kfqMessage);
                    break;
                case MSG_KF_REQ: //看房订单请求通知
                    Log.e(TAG, "看房订单请求通知");
                    dealNotifyMsg_kf_req(kfqMessage);
                    break;
                case MSG_KF_CANCEL_F_USER://看房订单被用户取消通知
                    Log.e(TAG, "看房订单被用户取消通知");
                    dealNotifyMsg_kfcancel_fuser(kfqMessage);
                    break;
                case MSG_KFANDYUYUE_NOTIFY: //看房订单和预约同时请求的通知
                    Log.e(TAG, "看房订单和预约同时请求的通知");
                    dealNotifyMsg_kfAndYbroker(kfqMessage);
                    break;
                default:
                    Log.e(TAG, "看看服务器类型是否写错了");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "LoginOpenfire KfqMessage 解析错误");
        }
    }

    /**
     * 判断消息的类型
     *
     * @param msgtype
     * @param notifytype
     * @return
     */
    public static int getDealType(String msgtype, String notifytype) {
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
            } else if ("selectother".equals(notifytype)) {
                return MSG_SELECTOTTHER_NOTIFY;
            } else if ("kanfangreq".equals(notifytype)) {
                return MSG_KF_REQ;
            } else if ("kanfangrefuse".equals(notifytype)) {
                return MSG_KF_CANCEL_F_USER;
            } else if ("kanfangandbroker".equals(notifytype)) {
                return MSG_KFANDYUYUE_NOTIFY;
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
                Log.e(TAG, "sesserid " + sessionid + " " + kfqMessage.getSessionid());
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
            if(isPullMyMsg){
                MyMessageActivity.activity.mHandler.sendEmptyMessage(4011);
            }else{
                getNotif(message, kfqMessage);
                if (MainActivity.activity != null) {
                    MainActivity.activity.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.activity.changeRedUdataIcon(true);
                        }
                    });
                }
            }
        }
    }

    //处理抢单通知
    private void dealNotityMsg_grad(KfqMessage kfqMessage) {
        Log.e(TAG, "isMainShow is " + isMainShow);
        if (isMainShow) {
            Message message = new Message();
            message.what = LoginOpenfire.MSG_QIANG_DAN;
            message.obj = kfqMessage;
            MainActivity.activity.mHandler.sendMessage(message);
        } else {
            getNotif(kfqMessage.getTicker(), kfqMessage.getTitle(), kfqMessage.getMsgtext(),getMainIntent(kfqMessage));
        }
    }

    //处理派单通知
    private void dealNotityMsg_paidan(KfqMessage kfqMessage) {
        Log.e(TAG, "isMainShow is " + isMainShow);
        if (isMainShow) {
            Message message = new Message();
            message.what = LoginOpenfire.MSG_PAI_DAN;
            message.obj = kfqMessage;
            MainActivity.activity.mHandler.sendMessage(message);
        } else {
            getNotif(kfqMessage.getTicker(), kfqMessage.getTitle(), kfqMessage.getMsgtext(), getMainIntent(kfqMessage));
        }
    }

    //处理打电话通知
    private void dealNotifyMsg_callnotify(KfqMessage kfqMessage) {
        Log.e(TAG, "isMainShow is " + isMainShow);
        if (isMainShow) {
            Message message = new Message();
            message.what = LoginOpenfire.MSG_CALL_NOTIFY;
            message.obj = kfqMessage;
            MainActivity.activity.mHandler.sendMessage(message);
        } else {
            getNotif(kfqMessage.getTicker(), kfqMessage.getTitle(), kfqMessage.getMsgtext(), getMainIntent(kfqMessage));
        }
    }

    //客户已选择了别人，订单已被抢
    private void dealNotifyMsg_selectother(KfqMessage kfqMessage) {
        Log.e(TAG, "isMainShow is " + isMainShow);
        if (isMainShow) {
            Message message = new Message();
            message.what = LoginOpenfire.MSG_SELECTOTTHER_NOTIFY;
            message.obj = kfqMessage;
            MainActivity.activity.mHandler.sendMessage(message);
        } else {
            getNotif(kfqMessage.getTicker(), kfqMessage.getTitle(), kfqMessage.getMsgtext(), getMainIntent(kfqMessage));
        }
    }

    //处理看房请求
    private void dealNotifyMsg_kf_req(KfqMessage kfqMessage) {
        Log.e(TAG, "isMainShow is " + isMainShow);
        if (isMainShow) {
            Message message = new Message();
            message.what = LoginOpenfire.MSG_KF_REQ;
            message.obj = kfqMessage;
            MainActivity.activity.mHandler.sendMessage(message);
        } else {
            getNotif(kfqMessage.getTicker(), kfqMessage.getTitle(), kfqMessage.getMsgtext(), getMainIntent(kfqMessage));
        }
    }

    //处理看房订单被用户取消
    private void dealNotifyMsg_kfcancel_fuser(KfqMessage kfqMessage) {
        Log.e(TAG, "isMainShow is " + isMainShow);
        if (isMainShow) {
            Message message = new Message();
            message.what = LoginOpenfire.MSG_KF_CANCEL_F_USER;
            message.obj = kfqMessage;
            MainActivity.activity.mHandler.sendMessage(message);
        } else {
            getNotif(kfqMessage.getTicker(), kfqMessage.getTitle(), kfqMessage.getMsgtext(), getMainIntent(kfqMessage));
        }
    }

    //处理看房订单和预约同时请求的通知
    private void dealNotifyMsg_kfAndYbroker(KfqMessage kfqMessage) {
        Log.e(TAG, "isMainShow is " + isMainShow);
        if (isMainShow) {
            Message message = new Message();
            message.what = LoginOpenfire.MSG_KFANDYUYUE_NOTIFY;
            message.obj = kfqMessage;
            MainActivity.activity.mHandler.sendMessage(message);
        } else {
            getNotif(kfqMessage.getTicker(), kfqMessage.getTitle(), kfqMessage.getMsgtext(), getMainIntent(kfqMessage));
        }
    }

    //处理取消预约
    private void dealNotifyMsg_cancelnotify(KfqMessage kfqMessage) {
        Log.e(TAG, "isMainShow is " + isMainShow);
        if (isMainShow) {
            Message message = new Message();
            message.what = LoginOpenfire.MSG_CANCEL_NOTIFY;
            message.obj = kfqMessage;
            MainActivity.activity.mHandler.sendMessage(message);
        } else {
            getNotif(kfqMessage.getTicker(), kfqMessage.getTitle(), kfqMessage.getMsgtext(), getMainIntent(kfqMessage));
        }
    }

    //处理抢单结果通知
    private void dealNotifyMsg_Resultdan(KfqMessage kfqMessage) {
        Log.e(TAG, "抢单通知，一定发送错了，经纪人端不应该收到");
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
                intent.putExtra(MessageingActivity.SESSION_FROM, kfqMessage.getFromusername());
                intent.putExtra(MessageingActivity.SESSION_TOUSERID, kfqMessage.getTouserid());
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

    /**
     * get通知
     */
    private void getNotif(final String ticker, final String title, final String content, final Intent intent) {
        new Thread() {
            @Override
            public void run() {
                notify_ID++;
                NotificationUtils.createNotif(PushService.context, R.drawable.ic_launcher, ticker, title, content, intent, notify_ID, 500);
            }
        }.start();
    }

    /**
     *
     * @param kfqMessage
     * @return
     */
    private Intent getMainIntent(KfqMessage kfqMessage){
        Intent intent  = new Intent(PushService.context,MainActivity.class);
        intent.putExtra(MainActivity.KFQMSG,kfqMessage);
        return intent;
    }
}
