package com.nandy.vkchanllenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.nandy.vkchanllenge.ui.Highlight;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 16.09.17.
 */

public class HighlightedEditText extends android.support.v7.widget.AppCompatEditText {

    private Rect bounds = new Rect();
    private Paint backgroundPaint = new Paint();
    private Path path;
    private List<Float> widths = new ArrayList<>();

    private Highlight highlightStyle = Highlight.NONE;


    public HighlightedEditText(Context context) {
        super(context);
        init(context);
    }

    public HighlightedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HighlightedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
      init(context);
    }

    private void init(Context context){
        bounds = new Rect();
        path = new Path();

        backgroundPaint = new Paint();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(ContextCompat.getColor(context, highlightStyle.getBackgroundColor()));
        backgroundPaint.setStrokeWidth(0);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setPathEffect(new CornerPathEffect(12f));
    }


    public void setHighlightStyle(Highlight highlightStyle) {
        this.highlightStyle = highlightStyle;
        backgroundPaint.setColor(ContextCompat.getColor(getContext(), highlightStyle.getBackgroundColor()));
        setTextColor(ContextCompat.getColor(getContext(), highlightStyle.getTextColor()));
    }

    public void setCornerRadius(float cornerRadius) {
        backgroundPaint.setPathEffect(new CornerPathEffect(cornerRadius));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (!TextUtils.isEmpty(getLayout().getText()) && highlightStyle != Highlight.NONE) {
            highlight(canvas);
        }
        super.onDraw(canvas);
    }

    private void highlight(Canvas canvas) {
        widths.clear();

        final int lineCount = getLineCount();
        final CharSequence text = getLayout().getText();


        for (int i = 0, startIndex = 0; i < lineCount; i++) {
            final int endIndex = getLayout().getLineEnd(i);
            CharSequence line = text.subSequence(startIndex, endIndex);
            startIndex = endIndex;
            float width = getPaint().measureText(line.toString()) + getPaddingLeft() ;
            widths.add(i, width);
        }

        highlight(canvas, 0, lineCount - 1, lineCount);
    }


    private void highlight(Canvas canvas, int firstLine, int lastLine, int lineCount) {

        path.reset();

        getLineBounds(firstLine, bounds);
        float firstLineWidth = widths.get(firstLine);
        path.moveTo(bounds.exactCenterX(), bounds.top);

        for (int i = firstLine; i < lineCount; i++) {

            getLineBounds(i, bounds);
            float width = widths.get(i);

            path.lineTo(bounds.exactCenterX() + width / 2, bounds.top);
            path.lineTo(bounds.exactCenterX() + width / 2, bounds.bottom );
        }


        float lastLineWidth = widths.get(lastLine);
        getLineBounds(lastLine, bounds);
        path.lineTo(bounds.exactCenterX() - lastLineWidth / 2, bounds.bottom);

        for (int i = lastLine; i >= firstLine; i--) {
            getLineBounds(i, bounds);
            float width = widths.get(i);

            path.lineTo(bounds.exactCenterX() - width / 2, bounds.top);

            if (i - 1 >= 0) {
                getLineBounds(i - 1, bounds);
                width = widths.get(i - 1);
                path.lineTo(bounds.exactCenterX() - width / 2, bounds.bottom);
            }
        }

        getLineBounds(firstLine, bounds);
        path.lineTo(bounds.exactCenterX() + firstLineWidth / 4, bounds.top);
//        path.lineTo(bounds.exactCenterX() + firstLineWidth / 2, bounds.exactCenterY());

        canvas.drawPath(path, backgroundPaint);
    }


//    private void highlight(Canvas canvas) {
//        path.reset();
//        widths.clear();
//
//        final int lineCount = getLineCount();
//        final CharSequence text = getLayout().getText();
//
//        for (int i = 0, startIndex = 0; i < lineCount; i++) {
//            final int endIndex = getLayout().getLineEnd(i);
//            CharSequence line = text.subSequence(startIndex, endIndex);
//            startIndex = endIndex;
//            float width = getPaint().measureText(line.toString());
//            widths.add(i, width);
//        }
//
//        getLineBounds(0, bounds);
//        float firstLineWidth = widths.get(0);
//        path.moveTo(bounds.exactCenterX(), bounds.top);
//
//        for (int i = 0; i < lineCount; i++) {
//
//            getLineBounds(i, bounds);
//            float width = widths.get(i);
//
//            path.lineTo(bounds.exactCenterX() + width / 2, bounds.top);
//            path.lineTo(bounds.exactCenterX() + width / 2, bounds.bottom);
//        }
//
//
//        float lastLineWidth = widths.get(lineCount - 1);
//        getLineBounds(lineCount - 1, bounds);
//        path.lineTo(bounds.exactCenterX() - lastLineWidth / 2, bounds.bottom);
//
//        for (int i = getLineCount() - 1; i >= 0; i--) {
//            getLineBounds(i, bounds);
//            float width = widths.get(i);
//
//            path.lineTo(bounds.exactCenterX() - width / 2, bounds.top);
//
//            if (i - 1 >= 0) {
//                getLineBounds(i - 1, bounds);
//                width = widths.get(i - 1);
//                path.lineTo(bounds.exactCenterX() - width / 2, bounds.bottom);
//            }
//        }
//
//        getLineBounds(0, bounds);
//        path.lineTo(bounds.exactCenterX() + firstLineWidth / 2, bounds.top);
//        path.lineTo(bounds.exactCenterX() + firstLineWidth / 2, bounds.exactCenterY());
//
//        canvas.drawPath(path, backgroundPaint);
//    }
}
