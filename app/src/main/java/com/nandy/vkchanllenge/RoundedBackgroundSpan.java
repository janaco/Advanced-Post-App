package com.nandy.vkchanllenge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;

/**
 * Created by yana on 08.09.17.
 */

public class RoundedBackgroundSpan extends ReplacementSpan {


    private final int _padding = 20;
    private int _backgroundColor;
    private int _textColor;

    public RoundedBackgroundSpan(int backgroundColor, int textColor) {
        super();
        _backgroundColor = backgroundColor;
        _textColor = textColor;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) (_padding + paint.measureText(text.subSequence(start, end).toString()) + _padding);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint)
    {
        float width = paint.measureText(text.subSequence(start, end).toString());
        RectF rect = new RectF(x - _padding, top, x + width + _padding, bottom);
        paint.setColor(_backgroundColor);
        canvas.drawRoundRect(rect, 20, 20, paint);
        paint.setColor(_textColor);
        canvas.drawText(text, start, end, x, y, paint);
    }

}


//    private final int mBackgroundColor;
//    private final int mTextColor;
//    private final float mCornerRadius;
//    private final float mPaddingStart;
//    private final float mPaddingEnd;
//    private final float mMarginStart;
//
//    public CoolBackgroundColorSpan(int backgroundColor, int textColor, float cornerRadius, float paddingStart, float paddingEnd, float marginStart) {
//        super();
//        mBackgroundColor = backgroundColor;
//        mTextColor = textColor;
//        mCornerRadius = cornerRadius;
//        mPaddingStart = paddingStart;
//        mPaddingEnd = paddingEnd;
//        mMarginStart = marginStart;
//    }
//
//    @Override
//    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
//        return (int) (mPaddingStart + paint.measureText(text.subSequence(start, end).toString()) + mPaddingEnd);
//    }
//
//    @Override
//    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
//        float width = paint.measureText(text.subSequence(start, end).toString());
//        RectF rect = new RectF(x - mPaddingStart + mMarginStart, top, x + width + mPaddingEnd + mMarginStart, bottom);
//        paint.setColor(mBackgroundColor);
//        canvas.drawRoundRect(rect, mCornerRadius, mCornerRadius, paint);
//        paint.setColor(mTextColor);
//        canvas.drawText(text, start, end, x + mMarginStart, y, paint);
//    }
//}