package com.chinaso.ndktest.dlib1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class TrasparentTitleView extends View {
    private static final float TEXT_SIZE_DIP = 24;
    private String mShowText;
    private final float mTextSizePx;
    private final Paint mFgPaint;
    private final Paint mBgPaint;
    public TrasparentTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        mFgPaint = new Paint();
        mFgPaint.setTextSize(mTextSizePx);

        mBgPaint = new Paint();
        mBgPaint.setColor(0xcc4285f4);
    }
    public void setText(@NonNull String text) {
        this.mShowText = text;
        postInvalidate();
    }

    public void onDraw(final Canvas canvas) {
        final int x = 10;
        int y = (int) (mFgPaint.getTextSize() * 1.5f);

        canvas.drawPaint(mBgPaint);

        if (mShowText != null) {
            canvas.drawText(mShowText, x, y, mFgPaint);
        }
    }


}
