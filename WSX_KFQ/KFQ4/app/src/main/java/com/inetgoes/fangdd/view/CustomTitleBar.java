package com.inetgoes.fangdd.view;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.inetgoes.fangdd.R;


/**
 * Created by czz on 2015/10/28.
 */
public class CustomTitleBar {

    public static void getTitleBar(Activity activity, String title) {
        getTitleBar(activity, title, true, true);
    }

    public static void getTitleBar(final Activity activity, String title, boolean isShowBack, boolean isShowRight) {
        activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        activity.setContentView(R.layout.layout_titlebar);
        activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar);

        ImageView bar_back = (ImageView) activity.findViewById(R.id.bar_back);
        bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        if (!isShowBack) {
            bar_back.setVisibility(View.INVISIBLE);
        }

        TextView bar_right = (TextView) activity.findViewById(R.id.bar_right);
        if (!isShowRight) {
            bar_right.setVisibility(View.INVISIBLE);
        }

        ((TextView) activity.findViewById(R.id.bar_title)).setText(title);

    }

    public static TextView getTitleBar(String title, final Activity activity,final RightDeal rightDeal) {
        activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        activity.setContentView(R.layout.layout_titlebar);
        activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar);

        ImageView bar_back = (ImageView) activity.findViewById(R.id.bar_back);
        bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightDeal.deal();
            }
        });


        TextView bar_right = (TextView) activity.findViewById(R.id.bar_right);
        bar_right.setVisibility(View.INVISIBLE);

        ((TextView) activity.findViewById(R.id.bar_title)).setText(title);

        return (TextView) activity.findViewById(R.id.bar_title);

    }

    public static void getTitleBar(final Activity activity, String title, String rightText, final RightDeal rightDeal) {
        activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        activity.setContentView(R.layout.layout_titlebar);
        activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar);

        ImageView bar_back = (ImageView) activity.findViewById(R.id.bar_back);
        bar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        TextView bar_right = (TextView) activity.findViewById(R.id.bar_right);
        bar_right.setText(rightText);
        bar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightDeal.deal();
            }
        });

        ((TextView) activity.findViewById(R.id.bar_title)).setText(title);

    }

    public static void getTitleBarCancel(final Activity activity, String title, String rightText, final RightDeal rightDeal) {
        activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        activity.setContentView(R.layout.layout_titlebar_waitdialog);
        activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_waitdialog);
        activity.findViewById(R.id.mate_wait_bar).setVisibility(View.VISIBLE);

        TextView bar_right = (TextView) activity.findViewById(R.id.mate_wait_cancen);
        bar_right.setText(rightText);
        bar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightDeal.deal();
            }
        });

        ((TextView) activity.findViewById(R.id.mate_wait_cancen_title)).setText(title);

    }

}
