package com.nandy.vkchanllenge;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;
import android.util.Log;

/**
 * Created by yana on 08.09.17.
 */

public class RoundedBackgroundSpan extends ReplacementSpan {

    private int paddingTop = 0;
    private int paddingBottom = 0;
    private float paddingStart = 0;
    private float paddingEnd = 0;

    private float layoutMargin = 0;

    private float cornerRadius = 15;

    private int backgroundColor = Color.WHITE;
    private int textColor = Color.BLACK;

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) (paddingStart + paint.measureText(text.subSequence(start, end).toString()) + paddingEnd);
    }

    @Override
    public void draw(@NonNull Canvas canvas,
                     CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        float width = paint.measureText(text.subSequence(start, end).toString());

        float left = x - paddingStart + layoutMargin;
        float right = x + width + paddingEnd + layoutMargin;
        top = top - paddingTop;
        bottom = bottom + paddingBottom;

        RectF rect = new RectF(left, top, right, bottom);

        CornerPathEffect pathEffect = new CornerPathEffect(cornerRadius);

        paint.setPathEffect(pathEffect);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);

        paint.setColor(textColor);
        canvas.drawText(text.subSequence(start, end).toString(), x + layoutMargin, y, paint);

    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public void setPaddingStart(float paddingStart) {
        this.paddingStart = paddingStart;
    }

    public void setPaddingEnd(float paddingEnd) {
        this.paddingEnd = paddingEnd;
    }

    public void setLayoutMargin(float layoutMargin) {
        this.layoutMargin = layoutMargin;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
