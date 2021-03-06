package com.nandy.vkchanllenge.model;

import android.support.annotation.ColorRes;

import com.nandy.vkchanllenge.R;

/**
 * Created by yana on 16.09.17.
 */

public enum Highlight {

    WHITE(0, R.color.white, R.color.black),
    WHITE_NEGATIVE(0, R.color.black, R.color.white),
    TRANSPARENT(1, R.color.white_a24, R.color.white),
    TRANSPARENT_NEGATIVE(1, R.color.black_a24, R.color.white),
    NONE(2, R.color.transparent, R.color.white),
    NONE_NEGATIVE(2, R.color.transparent, R.color.black);

    @ColorRes
    private int backgroundColor;
    @ColorRes
    private int textColor;

    private int index;

    Highlight(int index, @ColorRes int backgroundColor, @ColorRes int textColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.index = index;
    }


    public int getIndex() {
        return index;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }
}
