package com.inetgoes.kfqbrokers.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.manager.AppSharePrefManager;
import com.inetgoes.kfqbrokers.model.BrokerInfo;


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
     * 保存用户信息到本地
     */
    public static void setLocalInfo(Context context, BrokerInfo userInfo) {
        AppSharePrefManager sharePrefManager = AppSharePrefManager.getInstance(context);

        //经纪人id
        if (userInfo.getIdd() != null) {
            sharePrefManager.setLastest_login_id(userInfo.getIdd());
        }
        //经纪人姓名
        if (!TextUtils.isEmpty(userInfo.getName())) {
            sharePrefManager.setLastest_login_username(userInfo.getName());
        }
        //电话
        if (!TextUtils.isEmpty(userInfo.getCellphone())) {
            sharePrefManager.setLastest_login_phone_num(userInfo.getCellphone());
        }
        //经纪人头像
        if (!TextUtils.isEmpty(userInfo.getUserimage())) {
            sharePrefManager.setLastest_login_touxiang_imageurl(userInfo.getUserimage());
        }

        //性别
        if (!TextUtils.isEmpty(userInfo.getSex())) {
            sharePrefManager.setLastest_sex(userInfo.getSex());
        }

        //authentype 证件类型
        if (!TextUtils.isEmpty(userInfo.getAuthentype())) {
            sharePrefManager.setLastest_authentype(userInfo.getAuthentype());
        }
        //shenfenzhengno 身份证号码
        if (!TextUtils.isEmpty(userInfo.getShenfenzhengno())) {
            sharePrefManager.setLastest_shenfenzhengno(userInfo.getShenfenzhengno());
        }
        //Email
        if (!TextUtils.isEmpty(userInfo.getEmail())) {
            sharePrefManager.setLastest_email(userInfo.getEmail());
        }
        //placedesc 所在地
        if (!TextUtils.isEmpty(userInfo.getPlacedesc())) {
            sharePrefManager.setLastest_placedesc(userInfo.getPlacedesc());
        }
        //companyname 工作单位
        if (!TextUtils.isEmpty(userInfo.getPlacedesc())) {
            sharePrefManager.setLastest_companyname(userInfo.getPlacedesc());
        }

        //个性签名
        if (!TextUtils.isEmpty(userInfo.getPersonaldesc())) {
            sharePrefManager.setLastest_people_desc(userInfo.getPersonaldesc());
        }

        //QQ
        if (!TextUtils.isEmpty(userInfo.getQq())) {
            sharePrefManager.setLastest_qq(userInfo.getQq());
        }

        //经纪人角色
        if (!TextUtils.isEmpty(userInfo.getBrokerrole())) {

            sharePrefManager.setLastest_Brokerrole(userInfo.getBrokerrole());
        }

        //经纪人类型
        if (!TextUtils.isEmpty(userInfo.getBrokertype())) {
            sharePrefManager.setLastest_Brokertype(userInfo.getBrokertype());
        }

        //从业经验年数
        if (userInfo.getSkillyear() != null) {
            sharePrefManager.setLastest_SkillYear(userInfo.getSkillyear());
        }

        //服务星级
        if (userInfo.getStarlevel() != null) {
            sharePrefManager.setLastest_Starlevel(userInfo.getStarlevel());
        }

        //预约次数
        if (userInfo.getAppointmentnum() != null) {
            sharePrefManager.setLastest_Appointmentnum(userInfo.getAppointmentnum());
        }
        //订单数量
        if(userInfo.getOrdernum() != null){
            sharePrefManager.setLastest_Ordernum(userInfo.getOrdernum());
        }

    }

    /**
     * 是否是第一次使用应用或第一次使用更新的
     *
     * @return
     */
    public static boolean isFirstInApp(Context paramContext) {
        return !AppSharePrefManager.getInstance(paramContext.getApplicationContext()).getVersionName().equals(AppUtil.getCurrentAppVersionName(paramContext));
    }

    /**
     * 获取app的版本号 VersionCode
     *
     * @param paramContext
     * @return
     */
    public static int getCurrentAppVersionCode(Context paramContext) {
        return getCurrentAppPackageInfo(paramContext).versionCode;
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
            PackageInfo localPackageInfo = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0);
            return localPackageInfo;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            localNameNotFoundException.printStackTrace();
            throw new RuntimeException(localNameNotFoundException);
        }
    }

    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
