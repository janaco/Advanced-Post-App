package com.nandy.vkchanllenge.ui.model;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.RoundedBackgroundSpan;

/**
 * Created by yana on 07.09.17.
 */

public class TextModel {

    private Context context;

    public TextModel(Context context){
        this.context = context;
    }

    public Spannable highlightText(String text){
        Spannable spanText = Spannable.Factory.getInstance().newSpannable(text);
        spanText.setSpan(new RoundedBackgroundSpan(Color.WHITE, Color.GRAY), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.black)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanText;

    }
}
