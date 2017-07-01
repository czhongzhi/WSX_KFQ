package com.inetgoes.kfqbrokers.asynctast;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.inetgoes.kfqbrokers.utils.HttpUtil;

import java.util.Map;

/**
 * 通用网络异步请求
 * GET传一参数：
 * String url   例：http://192.168.1.37:8090/v2/userinfo/view_userdata.json?userid=10065;
 * POST传两参数：
 * 1.String url 例： http://192.168.1.37:8090/v2/userinfo/action_register.json
 * 2,Map<String,Object> 或 Object bean 或 String json
 * <p/>
 * Created by czz on 2015/11/2.
 */
public class HttpAsy extends AsyncTask<Object, Integer, String> {
    private PostExecute postExecute;

    public HttpAsy(PostExecute postExecute) {
        this.postExecute = postExecute;
    }


    @Override
    protected String doInBackground(Object... params) {
        String result = null;
        int len = params.length;
        Log.e("czhongzhi","请求params len is " + len);
        if (len == 0) {
            Log.e("czhongzhi", "doInBackground params is null");
            return result;
        }
        String url = (String) params[0];
        if (len == 1) {//get 请求
            Log.e("czhongzhi","get 请求");
            result = HttpUtil.getHttpReq(url);
        } else {//post 请求
            Log.e("czhongzhi","post 请求");
            if (params[1] instanceof Map) {
                result = HttpUtil.postHttpReq(url, (Map<String, Object>) params[1]);
            } else if (params[1] instanceof String) {
                result = HttpUtil.postHttpReq(url, (String) params[1]);
            } else if (params[1] instanceof Object) {
                result = HttpUtil.postHttpReq(url, params[1]);
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        if (TextUtils.isEmpty(s)) {
            Log.e("czhongzhi", "AsyncTask onPostExecute erro result is null or \"\"");
        }
        postExecute.onPostExecute(s);
    }
}