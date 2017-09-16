package com.nandy.vkchanllenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 16.09.17.
 */

public class HighlightedEditText extends android.support.v7.widget.AppCompatEditText {

    Rect bounds = new Rect();
    Paint linePaint = new Paint();
    Path path;
    List<Float> widths = new ArrayList<>();

    public HighlightedEditText(Context context) {
        super(context);
        bounds = new Rect();
        path = new Path();
        linePaint = new Paint();
        linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(10);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        float radius = 45.0f;
        CornerPathEffect cornerPathEffect = new CornerPathEffect(radius);
        linePaint.setPathEffect(cornerPathEffect);
    }

    public HighlightedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        bounds = new Rect();
        path = new Path();
        linePaint = new Paint();
        linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(10);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        float radius = 45.0f;
        CornerPathEffect cornerPathEffect = new CornerPathEffect(radius);
        linePaint.setPathEffect(cornerPathEffect);
    }

    public HighlightedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bounds = new Rect();
        path = new Path();
        linePaint = new Paint();

        linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(0);
        linePaint.setStyle(Paint.Style.FILL);
        float radius = 45.0f;
        CornerPathEffect cornerPathEffect = new CornerPathEffect(radius);
        linePaint.setPathEffect(cornerPathEffect);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (!TextUtils.isEmpty(getLayout().getText())) {
            highlight(canvas);
        }
        super.onDraw(canvas);
    }

    private void highlight(Canvas canvas) {
        path.reset();
        widths.clear();

        final int lineCount = getLineCount();
        final CharSequence text = getLayout().getText();

        for (int i = 0, startIndex = 0; i < lineCount; i++) {
            final int endIndex = getLayout().getLineEnd(i);
            CharSequence line = text.subSequence(startIndex, endIndex);
            startIndex = endIndex;
            float width = getPaint().measureText(line.toString());
            widths.add(i, width);
        }

        getLineBounds(0, bounds);
        float firstLineWidth = widths.get(0);
        path.moveTo(bounds.exactCenterX(), bounds.top);

        for (int i = 0; i < lineCount; i++) {

            getLineBounds(i, bounds);
            float width = widths.get(i);

            path.lineTo(bounds.exactCenterX() + width / 2, bounds.top);
            path.lineTo(bounds.exactCenterX() + width / 2, bounds.bottom);
        }


        float lastLineWidth = widths.get(lineCount - 1);
        getLineBounds(lineCount - 1, bounds);
        path.lineTo(bounds.exactCenterX() - lastLineWidth / 2, bounds.bottom);

        for (int i = getLineCount() - 1; i >= 0; i--) {
            getLineBounds(i, bounds);
            float width = widths.get(i);

            path.lineTo(bounds.exactCenterX() - width / 2, bounds.top);

            if (i - 1 >= 0) {
                getLineBounds(i - 1, bounds);
                width = widths.get(i - 1);
                path.lineTo(bounds.exactCenterX() - width / 2, bounds.bottom);
            }
        }

        getLineBounds(0, bounds);
        path.lineTo(bounds.exactCenterX() + firstLineWidth / 2, bounds.top);
        path.lineTo(bounds.exactCenterX() + firstLineWidth / 2, bounds.exactCenterY());

        canvas.drawPath(path, linePaint);
    }
}
