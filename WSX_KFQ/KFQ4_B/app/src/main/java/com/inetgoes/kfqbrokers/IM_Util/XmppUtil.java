package com.inetgoes.kfqbrokers.IM_Util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.utils.HttpUtil;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.iqregister.packet.Registration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by czz on 2015/11/24.
 * 即时聊天类封装 smack4.1.1包
 */
public class XmppUtil {
    private static final String TAG = "czhongzhi";

    public static int SERVER_PORT = 5222;
    public static String SERVER_HOST = "115.28.208.92";//115.28.208.92   //192.168.1.29
    public static String SERVER_NAME = "ay140306172311942d68z";//ay140306172311942d68z   //zhouchunlin

    private static XMPPTCPConnection connection;
    private static ChatManager chatManager;
    private static XmppUtil xmppUtil = new XmppUtil();

    static {//需在代码前静态加载ReconnectionManager，重连才能正常工作:  (之前都忘了加这个 导致无法自动重连)
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    private TaxiConnectionListener connectionListener = new TaxiConnectionListener();
    private ConnectionListener connectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection xmppConnection) {

        }

        @Override
        public void authenticated(XMPPConnection xmppConnection, boolean b) {

        }

        @Override
        public void connectionClosed() {
            Log("connectionClosed--->");
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            Log("connectionClosedOnError--->");

//            new Thread() {
//                @Override
//                public void run() {
//                    Context context = FangApplication.context;
//                    String us = String.valueOf(AppSharePrefManager.getInstance(context).getLastest_login_id());
//                    while (!XmppUtil.getInstance().login(us, us)) {
//                        XmppUtil.getInstance().closeConnection();
//                        Log("尝试重新登录");
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }
//            }.start();

            // 这里就是网络不正常或者被挤掉断线激发的事件
            if (e.getMessage().contains("conflict")) { // 被挤掉线
                Log("被挤下线");
            /*
             * log.e("来自连接监听,conn非正常关闭"); log.e("非正常关闭异常:"+arg0.getMessage());
             * log.e(con.isConnected());
             */
                // 关闭连接，由于是被人挤下线，可能是用户自己，所以关闭连接，让用户重新登录是一个比较好的选择
           //     XmppUtil.getInstance().closeConnection();
                // 接下来你可以通过发送一个广播，提示用户被挤下线，重连很简单，就是重新登录
            } else if (e.getMessage().contains("Connection timed out")) {// 连接超时
                // 不做任何操作，会实现自动重连
                Log("连接超时");
            }
        }

        @Override
        public void reconnectingIn(int arg0) {
            Log("reconnectingIn--->");
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            Log("reconnectionFailed--->" + arg0.getMessage());
            arg0.printStackTrace();
        }

        @Override
        public void reconnectionSuccessful() {
            Log("reconnectionSuccessful--->");
        }

        private void Log(String msg) {
            Log.e("czhongzhi", msg);
        }
    };

    /**
     * 单例模式
     *
     * @return
     */
    synchronized public static XmppUtil getInstance() {
        return xmppUtil;
    }

    /**
     * 创建连接
     */
    public XMPPTCPConnection getConnection() {
        if (connection == null) {
            openConnection();
        }
        return connection;
    }

    /**
     * 打开连接
     */
    public boolean openConnection() {
        if (null == connection || !connection.isAuthenticated()) {
            // 配置连接
            XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
            builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
            XMPPTCPConnectionConfiguration config = builder
                    .setServiceName(SERVER_NAME)
                    .setHost(SERVER_HOST)
                    .setResource("android")
                    .setSendPresence(true)
                    .setPort(SERVER_PORT).build();

            connection = new XMPPTCPConnection(config);
            try {
                connection.connect();
                Log.e(TAG, "conn 连接");
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 关闭连接
     */
    public void closeConnection() {
        if (getConnection() != null) {
            //connection.removeConnectionListener(connectionListener);//移除连接监听器
            if (connection.isConnected())
                connection.disconnect();
            connection = null;
        }
        Log.e(TAG, "conn 关闭连接");
    }

    /**
     * 登录
     *
     * @param account  登录帐号
     * @param password 登录密码
     * @return
     */
    public boolean login(String account, String password) {
        if (getConnection() != null) {
            try {
                getConnection().login(account, password);
                if (getConnection().isConnected()) {
                    getConnection().addConnectionListener(connectionListener); //给连接设置监听器
                }
                Log.i(TAG, "conn 用户登录");
                return true;
            } catch (XMPPException e) {
                e.printStackTrace();
            } catch (SmackException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 注册
     *
     * @param account  注册帐号
     * @param password 注册密码
     * @return 1、注册成功 0、服务器没有返回结果2、这个账号已经存在3、注册失败
     */
    public int regist(String account, String password) {
        if (getConnection() == null) {
            return 0;
        }
        try {
            Map<String, String> map = new HashMap<>();
            map.put("username", account);
            map.put("password", password);
            Registration reg = new Registration(map);
            reg.setType(IQ.Type.set);
            reg.setTo(SERVER_NAME);
            StanzaFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));
            PacketCollector collector = getConnection().createPacketCollector(filter);
            getConnection().sendPacket(reg);
            IQ result = (IQ) collector.nextResult(SmackConfiguration.getDefaultPacketReplyTimeout());
            Log.e(TAG, "----result----" + result);
            collector.cancel();// 停止请求results（是否成功的结果）
            if (result == null) {
                Log.e(TAG, "No response from server.");
                Log.e(TAG, "conn 服务器没有返回结果");
                return 0;
            } else if (result.getType() == IQ.Type.result) {
                Log.e(TAG, "conn 用户注册成功");
                return 1;
            } else { // if (result.getType() == IQ.Type.ERROR)
                if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                    Log.e(TAG, "IQ.Type.ERROR: " + result.getError().toString());
                    Log.e(TAG, "conn 账号已经存在");
                    return 2;
                } else {
                    Log.e(TAG, "IQ.Type.ERROR: " + result.getError().toString());
                    Log.e(TAG, "conn 注册失败");
                    return 3;
                }
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 设置信息的监听
     */
    public void setMessageListener(final MessageCallback callback) {
        chatManager = ChatManager.getInstanceFor(getConnection());
        chatManager.addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean b) {
                if (!b)
                    chat.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {
                            callback.dealMessage(chat, message);
                        }
                    });
            }
        });
    }

    /**
     * 发送消息
     *
     * @param touserid //10508
     * @param content  //发送内容
     */
    public void sendMessage(String touserid, String content) {
        try {
            chatManager = ChatManager.getInstanceFor(getConnection());
            Chat chat = chatManager.createChat(touserid + "@" + XmppUtil.SERVER_NAME);
            chat.getThreadID();
            chat.sendMessage(content);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param touserid //10508
     * @param msg      //发送内容
     */
    public void sendMessage(String touserid, Message msg) {
        try {
            Chat chat = chatManager.createChat(touserid + "@" + XmppUtil.SERVER_NAME);
            chat.getThreadID();
            chat.sendMessage(msg);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除当前用户
     *
     * @return
     */
    public boolean deleteAccount() {
        if (getConnection() == null)
            return false;
        try {
            AccountManager.getInstance(getConnection()).deleteAccount();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(String pwd) {
        if (getConnection() == null)
            return false;
        try {
            AccountManager.getInstance(getConnection()).changePassword(pwd);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
