package com.nandy.vkchanllenge.ui.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spannable;

import com.nandy.vkchanllenge.Highlighter;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.RoundedBackgroundSpan;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.ButterKnife;

/**
 * Created by yana on 07.09.17.
 */

public class TextModel {

    private int paddingTop;
    private int paddingBottom;
    private float cornerRadius;
    private float layoutMargin;
    private float paddingStart;
    private float paddingEnd;

    private Context context;

    public TextModel(Context context) {

        this.context = context;

        cornerRadius = context.getResources().getDimension(R.dimen.corner_radius);
        layoutMargin = context.getResources().getDimension(R.dimen.text_margin);
        paddingStart = context.getResources().getDimension(R.dimen.padding_start);
        paddingEnd = context.getResources().getDimension(R.dimen.padding_end);
        paddingTop = (int) context.getResources().getDimension(R.dimen.padding_top);
        paddingBottom = (int) context.getResources().getDimension(R.dimen.padding_bottom);

    }

    private Highlighter[] highlighters = Highlighter.values();
    private int highlighterIndex = 0;

    public Spannable highlightText(Layout layout) {

        highlighterIndex = highlighterIndex < highlighters.length ? highlighterIndex : 0;
        Highlighter highlighter = highlighters[highlighterIndex++];


        CharSequence text = layout.getText();
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(text);

        List<CharSequence> lines = getLines(layout);

        int start = 0;
        int end = start;

        for (int i = 0; i < lines.size(); i++) {

            CharSequence line = lines.get(i);
            start = end;
            end = start + line.length();

            RoundedBackgroundSpan backgroundSpan = new RoundedBackgroundSpan();
            backgroundSpan.setBackgroundColor(ContextCompat.getColor(context, highlighter.getBackgroundColor()));
            backgroundSpan.setTextColor(ContextCompat.getColor(context, highlighter.getTextColor()));
            backgroundSpan.setCornerRadius(cornerRadius);
            backgroundSpan.setPaddingEnd(paddingEnd);
            backgroundSpan.setPaddingStart(paddingStart);
            backgroundSpan.setLayoutMargin(layoutMargin);

                if (i == 0) {
                    backgroundSpan.setPaddingTop(paddingTop);
                    backgroundSpan.setPaddingBottom(0);

                } else if (i == lines.size() - 1) {
                    backgroundSpan.setPaddingTop(0);
                    backgroundSpan.setPaddingBottom(paddingBottom);

                } else if (highlighter == Highlighter.WHITE){
                    backgroundSpan.setPaddingTop(paddingTop);
                    backgroundSpan.setPaddingBottom(paddingBottom);
            }

            spanText.setSpan(backgroundSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spanText;

    }

    private List<CharSequence> getLines(Layout layout) {
        final List<CharSequence> lines = new ArrayList<>();

        if (layout != null) {
            final int lineCount = layout.getLineCount();
            final CharSequence text = layout.getText();

            for (int i = 0, startIndex = 0; i < lineCount; i++) {
                final int endIndex = layout.getLineEnd(i);
                lines.add(text.subSequence(startIndex, endIndex));
                startIndex = endIndex;
            }
        }
        return lines;
    }
}
