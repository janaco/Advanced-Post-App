package com.nandy.vkchanllenge;

import android.support.annotation.ColorRes;

/**
 * Created by yana on 09.09.17.
 */

public enum Highlighter {

    WHITE(R.color.white, R.color.black), TRANSPARENT(R.color.highlight, R.color.white), CLEAR(R.color.transparent, R.color.white);

    @ColorRes
    int backgroundColor;
    @ColorRes
    int textColor;

    Highlighter(@ColorRes int backgroundColor, @ColorRes int textColor){
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
