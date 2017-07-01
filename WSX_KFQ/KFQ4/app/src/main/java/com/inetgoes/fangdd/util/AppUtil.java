package com.inetgoes.fangdd.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;

import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.manager.AppSharePrefManager;

/**
 * app相关的方法封装类
 * Created by czz on 2015/10/28.
 */
public class AppUtil {


    public static void call2(final Context paramContext, final String paramString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(paramContext, R.style.kfqAppThemeDialog);
        builder.setMessage("是否拨打电话:" + paramString);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                call(paramContext, paramString);
            }
        });
        builder.setNegativeButton("否", null);
        builder.create().show();
    }

    private static void call(Context paramContext, String paramString) {
        Log.e("czhongzhi", "cell is " + paramString);
        paramContext.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + paramString)));
    }

    /**
     * 是否是第一次使用应用或第一次使用更新的
     *
     * @return
     */
    public static boolean isFirstInApp(Context paramContext) {
        Context con = paramContext.getApplicationContext();
        return !AppSharePrefManager.getInstance(con).getVersionName().equals(AppUtil.getCurrentAppVersionName(paramContext));
    }

    /**
     * 获取app的版本号 VersionCode
     *
     * @param paramContext
     * @return
     */
    public static int getCurrentAppVersionCode(Context paramContext) {
        return getCurrentAppPackageInfo(paramContext.getApplicationContext()).versionCode;
    }

    /**
     * 获取app的版本号名称 versionName
     *
     * @param paramContext
     * @return
     */
    public static String getCurrentAppVersionName(Context paramContext) {
        return getCurrentAppPackageInfo(paramContext.getApplicationContext()).versionName;
    }

    private static PackageInfo getCurrentAppPackageInfo(Context paramContext) {
        try {
            PackageInfo localPackageInfo = paramContext.getPackageManager().getPackageInfo(paramContext.getApplicationContext().getPackageName(), 0);
            return localPackageInfo;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            localNameNotFoundException.printStackTrace();
            throw new RuntimeException(localNameNotFoundException);
        }
    }

    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getApplicationContext().getResources().getDisplayMetrics());
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
