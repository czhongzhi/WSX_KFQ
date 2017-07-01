package com.inetgoes.kfqbrokers.IM_Util;


import android.util.Log;

import com.inetgoes.kfqbrokers.FangApplication;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by czz on 2015/11/25.
 * 连接监听类
 */
public class TaxiConnectionListener implements ConnectionListener {
    private static final String TAG = "czhongzhi";
    private Timer tExit;
    private String username;
    private String password;
    private int logintime = 3000;

    @Override
    public void connected(XMPPConnection xmppConnection) {

    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {

    }

    @Override
    public void connectionClosed() {
        Log.e(TAG, "连接关闭");
        // 关闭连接
        XmppUtil.getInstance().closeConnection();
        // 重连服务器
        tExit = new Timer();
        tExit.schedule(new timetask(), logintime);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.e(TAG, "连接关闭异常");
        // 判断账号已被登录
        boolean error = e.getMessage().equals("stream:error (conflict)");
        if (!error) {
            // 关闭连接
            XmppUtil.getInstance().closeConnection();
            // 重连服务器
            tExit = new Timer();
            tExit.schedule(new timetask(), logintime);
        }
    }

    class timetask extends TimerTask {
        @Override
        public void run() {
            username = String.valueOf(AppSharePrefManager.getInstance(FangApplication.context).getLastest_login_id());
            password = username;
//            username = "czz3";
//            password = "123456";
            if (username != null && password != null) {
                Log.e(TAG, "尝试登录");
                // 连接服务器
                if (XmppUtil.getInstance().login(username, password)) {
                    Log.e(TAG, "登录成功");
                } else {
                    Log.e(TAG, "重新登录");
                    tExit.schedule(new timetask(), logintime);
                }
            }
        }
    }

    @Override
    public void reconnectingIn(int arg0) {
    }

    @Override
    public void reconnectionFailed(Exception arg0) {
    }

    @Override
    public void reconnectionSuccessful() {
    }
}
