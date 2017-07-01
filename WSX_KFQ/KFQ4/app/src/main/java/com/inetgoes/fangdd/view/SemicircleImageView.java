package com.inetgoes.fangdd.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


/**
 * 自定义图片切角，切指定的边。
 * Created by czz on 2015/11/3.
 */
public class SemicircleImageView extends ImageView {
    private Paint paint;

    public SemicircleImageView(Context context) {
        this(context, null);
    }

    public SemicircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SemicircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
    }

    /**
     * 绘制圆角矩形图片
     *
     * @author caizhiming
     */
    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            //设置图片上半部图片圆角
            Bitmap b = SemicircleImageView.fillet(SemicircleImageView.TOP, bitmap, dp2px(5));
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
            paint.reset();
            canvas.drawBitmap(b, rectSrc, rectDest, paint);

        } else {
            super.onDraw(canvas);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public static final int ALL = 347120;
    public static final int TOP = 547120;
    public static final int LEFT = 647120;
    public static final int RIGHT = 747120;
    public static final int BOTTOM = 847120;

    /**
     * @param type
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap fillet(int type, Bitmap bitmap, int roundPx) {
        try {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            if (TOP == type) {
                clipTop(canvas, paint, roundPx, width, height);
            } else if (LEFT == type) {
                clipLeft(canvas, paint, roundPx, width, height);
            } else if (RIGHT == type) {
                clipRight(canvas, paint, roundPx, width, height);
            } else if (BOTTOM == type) {
                clipBottom(canvas, paint, roundPx, width, height);
            } else {
                clipAll(canvas, paint, roundPx, width, height);
            }

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //帖子图
            final Rect src = new Rect(0, 0, width, height);
            final Rect dst = src;
            canvas.drawBitmap(bitmap, src, dst, paint);
            return paintingBoard;
        } catch (Exception exp) {
            return bitmap;
        }
    }

    private static void clipLeft(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(offset, 0, width, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, 0, offset * 2, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipRight(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, 0, width - offset, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(width - offset * 2, 0, width, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipTop(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, offset, width, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, 0, width, offset * 2);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipBottom(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, 0, width, height - offset);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, height - offset * 2, width, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipAll(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final RectF rectF = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }


}
