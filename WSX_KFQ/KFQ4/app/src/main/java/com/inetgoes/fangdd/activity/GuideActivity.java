package com.inetgoes.fangdd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.inetgoes.fangdd.R;
import com.inetgoes.fangdd.manager.AppSharePrefManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * Created by czz on 2015/10/28.
 */
public class GuideActivity extends Activity {
    private ViewPager viewPager;
    private GuideAdapter adapter;
    private RadioGroup radiogroup;
    private List<View> list = new ArrayList<>();
    private int imgSize = 0;

    public EdgeEffectCompat leftEdge;
    public EdgeEffectCompat rightEdge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);

//        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//                if (AppSharePrefManager.getInstance(GuideActivity.this).isLogined()) {
//                    startActivity(new Intent(GuideActivity.this, MainActivity.class));
//                } else {
//                    startActivity(new Intent(GuideActivity.this, RegisterActivity.class));
//                }
//            }
//        });

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);

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

        LayoutInflater inflater = LayoutInflater.from(this);

        //添加图片
        View iv_1 = inflater.inflate(R.layout.guide_layout_one, null);
        list.add(iv_1);

        View iv_2 = inflater.inflate(R.layout.guide_layout_two,null);
        list.add(iv_2);


        View iv_3 = inflater.inflate(R.layout.guide_layout_three, null);
        iv_3.findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(GuideActivity.this, RegisterActivity.class));

//                if (AppSharePrefManager.getInstance(GuideActivity.this).isLogined()) {
//                    startActivity(new Intent(GuideActivity.this, MainActivity.class));
//                } else {
//                    startActivity(new Intent(GuideActivity.this, RegisterActivity.class));
//                }
            }
        });
        list.add(iv_3);

        //对应图片个数生成相应的指示器
        imgSize = list.size();
        for (int i = 0; i < imgSize; i++) {
            //动态生成小图标
            RadioButton rb = (RadioButton) LayoutInflater.from(GuideActivity.this).inflate(R.layout.radiobutton, null);
            RadioGroup.LayoutParams p = new RadioGroup.LayoutParams(15, 15);
            p.setMargins(10, 10, 10, 10);
            rb.setLayoutParams(p);
            radiogroup.addView(rb);
        }
        radiogroup.getChildAt(0).setBackgroundResource(R.drawable.point_selected);

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

                int count = imgSize;
                for (int i = 0; i < count; i++) {
                    View point = radiogroup.getChildAt(i);
                    point.setBackgroundResource(i == position ? R.drawable.point_selected: R.drawable.point_normal);
                }

//                for (int i = 0; i < imgSize; i++) {
//                    radiogroup.getChildAt(i).setBackgroundResource(R.drawable.radiobutton_circle);
//                }
//                radiogroup.getChildAt(position).setBackgroundResource(R.drawable.radiobutton_circle_sele);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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


