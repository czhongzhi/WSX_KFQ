package com.inetgoes.kfqbrokers.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.inetgoes.kfqbrokers.R;


/**
 * Created by czz on 2015/11/17.
 */
public class DialogUtil {

    /**
     * 提示预约成功
     */
    public static void showYuyueSuccess(Activity activity, final AloneDeal dealLeft, final AloneDeal dealRight) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder2.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.yuyue_dialog_success, null);

        view.findViewById(R.id.dialog_look).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (dealLeft != null)
                    dealLeft.deal();
            }
        });
        view.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (dealRight != null)
                    dealRight.deal();
            }
        });

        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
    }

    /**
     * 提示确认取消预约（是，否）
     */
    public static void showDeleteYuyueDeal(Activity activity,String text, final AloneDeal dealLeft, final AloneDeal dealRight) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder2.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.yuyue_dialog_delete, null);

        view.findViewById(R.id.dialog_look).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (dealLeft != null)
                    dealLeft.deal();
            }
        });
        view.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (dealRight != null)
                    dealRight.deal();
            }
        });

        ((TextView)view.findViewById(R.id.dialog_title)).setText(text);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
    }


    /**
     * 提示删除照片
     */
    public static void deleteHousePhoto(Activity activity, final AloneDeal dealLeft, final AloneDeal dealRight) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder2.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.delete_house_photo, null);

        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (dealLeft != null)
                    dealLeft.deal();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (dealRight != null)
                    dealRight.deal();
            }
        });

        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
    }

    /**
     * 提示取消成功
     */
    public static void showCancelSuccess(final Activity activity, final AloneDeal aloneDeal) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder2.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.yuyue_dialog_cancel, null);
        view.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (aloneDeal != null)
                    aloneDeal.deal();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
    }

    /**
     * 提示取消失败
     */
    public static void showCancelFailed(final Activity activity, final AloneDeal aloneDeal) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder2.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.yuyue_dialog_cancel, null);
        ImageView dialogIcon = (ImageView) view.findViewById(R.id.dialog_icon);
        dialogIcon.setImageResource(R.drawable.fail);
        ((TextView) view.findViewById(R.id.dialog_title)).setText("取消失败");
        view.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (aloneDeal != null)
                    aloneDeal.deal();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
    }

    /**
     * 提示操作成功
     */
    public static void showDealSuccess(final Activity activity, String content, final AloneDeal aloneDeal) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder2.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.yuyue_dialog_cancel, null);
        ((TextView) view.findViewById(R.id.dialog_title)).setText(content);
        view.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (aloneDeal != null)
                    aloneDeal.deal();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
    }

    /**
     * 提示操作失败
     */
    public static void showDealFailed(final Activity activity, String content, final AloneDeal aloneDeal) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder2.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.yuyue_dialog_cancel, null);
        ImageView dialogIcon = (ImageView) view.findViewById(R.id.dialog_icon);
        dialogIcon.setImageResource(R.drawable.fail);
        ((TextView) view.findViewById(R.id.dialog_title)).setText(content);
        view.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (aloneDeal != null)
                    aloneDeal.deal();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.95);
        dialogWindow.setAttributes(p);
    }


    /**
     * 开启正在取消dialog
     */
    public static Dialog showCanceling(Context context, String message) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        Dialog dialog_cancel = builder2.show();
        View view = LayoutInflater.from(context).inflate(R.layout.register_dialog_login, null);
        ImageView wait_icon = (ImageView) view.findViewById(R.id.mate_wait_icon);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.mate_wait_rotate);
        anim.setInterpolator(new LinearInterpolator());
        ((TextView) view.findViewById(R.id.message)).setText(message);
        wait_icon.setAnimation(anim);
        dialog_cancel.setCancelable(false);
        dialog_cancel.setContentView(view);
        return dialog_cancel;
    }

    /**
     * 开启等待dialog
     */
    public static Dialog showWait(Activity activity) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
        Dialog dialog_cancel = builder2.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_waitdata, null);
        ImageView wait_icon = (ImageView) view.findViewById(R.id.mate_wait_icon);
        Animation anim = AnimationUtils.loadAnimation(activity, R.anim.mate_wait_rotate);
        anim.setInterpolator(new LinearInterpolator());
        wait_icon.setAnimation(anim);
        dialog_cancel.setCancelable(false);
        dialog_cancel.setContentView(view);
        Window dialogWindow = dialog_cancel.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) AppUtil.dp2px(150, activity);
        p.height = p.width;
        dialogWindow.setAttributes(p);
        return dialog_cancel;
    }

    /**
     * 开启等待dialog
     */
    public static Dialog showWait2(Activity activity) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(activity);
        Dialog dialog_cancel = builder2.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_waitdata, null);
        ImageView wait_icon = (ImageView) view.findViewById(R.id.mate_wait_icon);
        Animation anim = AnimationUtils.loadAnimation(activity, R.anim.mate_wait_rotate);
        anim.setInterpolator(new LinearInterpolator());
        wait_icon.setAnimation(anim);
        dialog_cancel.setCancelable(true);
        dialog_cancel.setContentView(view);
        Window dialogWindow = dialog_cancel.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = (int) AppUtil.dp2px(150, activity);
        p.height = p.width;
        dialogWindow.setAttributes(p);
        return dialog_cancel;
    }

    /**
     * 功能未开发提示
     * @return
     */
    public static Dialog showHintNoneFunction(Context context,String title,String content){

        AlertDialog.Builder builder =  new AlertDialog.Builder(context,R.style.kfqAppThemeDialog);
        builder.setTitle(title);
        builder.setMessage(content);
        Dialog dialog = builder.create();
        dialog.show();

        return dialog;
    }
}
