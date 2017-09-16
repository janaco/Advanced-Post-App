package com.nandy.vkchanllenge.ui;

import android.support.annotation.ColorRes;

import com.nandy.vkchanllenge.R;

/**
 * Created by yana on 16.09.17.
 */

public enum Highlight {

    WHITE(R.color.white, R.color.black),
    WHITE_INVERTED(R.color.black, R.color.white),
    TRANSPARENT(R.color.white_a24, R.color.white),
    TRANSPARENT_INVERTED(R.color.black_a24, R.color.white),
    NONE(R.color.transparent, R.color.white);

    @ColorRes
    int backgroundColor;
    @ColorRes
    int textColor;

    Highlight(@ColorRes int backgroundColor, @ColorRes int textColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }
}
