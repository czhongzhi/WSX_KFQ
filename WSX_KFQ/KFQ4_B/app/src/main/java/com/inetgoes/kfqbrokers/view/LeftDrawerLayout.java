package com.inetgoes.kfqbrokers.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 *主界面侧滑的类
 */
public class LeftDrawerLayout extends ViewGroup {
    private static final int MIN_DRAWER_MARGIN = 64;
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400;

    /**
     * drawer离父容器右边的最小外边距
     */
    private int mMinDrawerMargin;

    private View mLeftMenuView;
    private View mContentView;

    private ViewDragHelper mHelper;
    /**
     * drawer显示出来的占自身的百分比
     */
    private float mLeftMenuOnScrren;
    /**
     * drawer显示出来的宽度
     */
    private int childNewVidth;


    private Paint mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mShouldDraw = false;


    public LeftDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);


        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            //左侧位置
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                int newLeft = Math.max(-child.getWidth(), Math.min(left, 0));
                //return left;
                return newLeft;
            }

            @Override
            public boolean tryCaptureView(View child, int pointerId) {

                return child == mLeftMenuView;
            }


            //拖动后的控件动作  收回 还是弹出
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                final int childWidth = releasedChild.getWidth();
                float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;
                mHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth, releasedChild.getTop());
                invalidate();
            }


            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                final int childWidth = changedView.getWidth();
                childNewVidth = childWidth + left;
                float offset = (float) (childNewVidth) / childWidth;
                mLeftMenuOnScrren = offset;

                changedView.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
                invalidate();

                //判断是否划阴影
                mShouldDraw = true;
                if (offset == 0) {
                    mShouldDraw = false;
                }
                //requestLayout();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {

                return mLeftMenuView == child ? child.getWidth() : 0;
            }
        });
        //设置edge_left track
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        //设置minVelocity
        mHelper.setMinVelocity(minVel);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        View leftMenuView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams)
                leftMenuView.getLayoutParams();
        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                mMinDrawerMargin + lp.leftMargin + lp.rightMargin,
                lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                lp.topMargin + lp.bottomMargin,
                lp.height);
        leftMenuView.measure(drawerWidthSpec, drawerHeightSpec);


        View contentView = getChildAt(0);
        lp = (MarginLayoutParams) contentView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWidthSpec, contentHeightSpec);

        mLeftMenuView = leftMenuView;
        mContentView = contentView;


    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View menuView = mLeftMenuView;
        View contentView = mContentView;

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        contentView.layout(lp.leftMargin, lp.topMargin,
                lp.leftMargin + contentView.getMeasuredWidth(),
                lp.topMargin + contentView.getMeasuredHeight());

        lp = (MarginLayoutParams) menuView.getLayoutParams();

        final int menuWidth = menuView.getMeasuredWidth();
        int childLeft = -menuWidth + (int) (menuWidth * mLeftMenuOnScrren);
        menuView.layout(childLeft, lp.topMargin, childLeft + menuWidth,
                lp.topMargin + menuView.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldInterceptTouchEvent = mHelper.shouldInterceptTouchEvent(ev);
        return shouldInterceptTouchEvent;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //触摸时直接选中要移动的侧边菜单
        mHelper.captureChildView(mLeftMenuView, 0);

        try {
            mHelper.processTouchEvent(event);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }


    public void closeDrawer() {
        View menuView = mLeftMenuView;
        mLeftMenuOnScrren = 0f;
        mHelper.smoothSlideViewTo(menuView, -menuView.getWidth(), menuView.getTop());
        invalidate();
    }

    public void openDrawer() {
        View menuView = mLeftMenuView;
        mLeftMenuOnScrren = 1.0f;
        mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
        invalidate();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    //动画的实现 ：

    /**
     * 绘制阴影，随着距离的改变而改变
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        mShadowPaint.setColor(Color.BLACK);
        mShadowPaint.setAlpha((int) (mLeftMenuOnScrren * 180));
        canvas.drawRect(childNewVidth, 0, getWidth(), getHeight(), mShadowPaint);
        //Log.e("drawBackground", "==========" + (mLeftMenuOnScrren) * 180 + "==========");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mShouldDraw) {
            drawBackground(canvas);
        }
    }

}
