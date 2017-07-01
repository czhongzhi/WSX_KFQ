package com.inetgoes.fangdd.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.inetgoes.fangdd.IM_Util.LoginOpenfire;
import com.inetgoes.fangdd.IM_Util.XmppUtil;
import com.inetgoes.fangdd.manager.AppSharePrefManager;


/**
 * Created by czz on 2015/11/25.
 */
public class PushService extends Service {
    public static final String ACTION = "com.inetgoes.fangdd.server.PushService";
    public static Context context;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();

        openfireLogin();


        return super.onStartCommand(intent, flags, startId);
    }

    public void openfireLogin() {
        String u = String.valueOf(AppSharePrefManager.getInstance(getApplicationContext()).getLastest_login_id());
        new LoginOpenfire().execute(u);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XmppUtil.getInstance().closeConnection();
    }
}
