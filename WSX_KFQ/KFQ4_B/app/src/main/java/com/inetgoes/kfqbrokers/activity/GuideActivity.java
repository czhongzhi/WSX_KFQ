package com.inetgoes.kfqbrokers.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.inetgoes.kfqbrokers.R;
import com.inetgoes.kfqbrokers.utils.AppUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * 引导页
 */
public class GuideActivity extends Activity {
    private ViewPager viewPager;
    private GuideAdapter adapter;
    private List<View> list = new ArrayList<>();
    private int imgSize = 0;
    private View oneView;
    private View twoView;
    private View threeView;
    private View fourView;
    //private View guideButton;
    private LinearLayout pointContainer;

    public EdgeEffectCompat leftEdge;
    public EdgeEffectCompat rightEdge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

        //guideButton = findViewById(R.id.guide_button_ll);


        pointContainer = (LinearLayout) findViewById(R.id.point_container);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        try {

            Field leftEdgeField = viewPager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = viewPager.getClass().getDeclaredField("mRightEdge");

            if (leftEdgeField != null && rightEdgeField != null) {
                //AndroidUtils.showMsg(AreaGuideActivity.this,"获得");
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewPager);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewPager);

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        oneView = View.inflate(this, R.layout.item_guide_one, null);
        list.add(oneView);

        twoView = View.inflate(this, R.layout.item_guide_two, null);
        list.add(twoView);

        threeView = View.inflate(this, R.layout.item_guide_three, null);
        list.add(threeView);

        fourView = View.inflate(this, R.layout.item_guide_four, null);
        list.add(fourView);
        //经纪人加盟按钮
        fourView.findViewById(R.id.broker_regedit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(GuideActivity.this, RegistMainActivity.class));
            }
        });

        //登录按钮
        fourView.findViewById(R.id.broker_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
            }
        });


        //对应图片个数生成相应的指示器
        imgSize = list.size();
        for (int i = 0; i < imgSize; i++) {
            addPoints(i);
        }


        //添加适配器
        adapter = new GuideAdapter(list);
        viewPager.setAdapter(adapter);

        //指示器更进
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (leftEdge != null && rightEdge != null) {
                    leftEdge.finish();
                    rightEdge.finish();
                    leftEdge.setSize(0, 0);
                    rightEdge.setSize(0, 0);
                }
            }

            @Override
            public void onPageSelected(int position) {

                int count = pointContainer.getChildCount();
                for (int i = 0; i < count; i++) {
                    View point = pointContainer.getChildAt(i);
                    point.setBackgroundResource(i == position ? R.drawable.point_selected : R.drawable.point_normal);
                }
                //guideButton.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 动态的添加点到界面
     *
     * @param i 第几张图片
     */
    private void addPoints(int i) {

        View point = new View(this);
        point.setBackgroundResource(R.drawable.point_normal);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AppUtil.dp2px(8, this), AppUtil.dp2px(8, this));
        if (i != 0) {
            params.leftMargin = 10;
        } else {
            // 设置默认选中第0个
            point.setBackgroundResource(R.drawable.point_selected);
        }
        pointContainer.addView(point, params);
    }

    private class GuideAdapter extends PagerAdapter {
        private List<View> imgList;

        public GuideAdapter(List<View> imgList) {
            this.imgList = imgList;
        }


        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imgList.get(position));
            return imgList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imgList.get(position));
        }
    }
}


