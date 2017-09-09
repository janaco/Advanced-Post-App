package com.nandy.vkchanllenge.ui;

import android.widget.ImageView;

/**
 * Created by yana on 10.09.17.
 */

public class Part {

    private final int alignmentRule;
    private final ImageView.ScaleType scaleType;
    private final String source;

    public Part(String source, int alignmentRule, ImageView.ScaleType scaleType) {
        this.alignmentRule = alignmentRule;
        this.scaleType = scaleType;
        this.source = source;
    }

    public int getAlignmentRule() {
        return alignmentRule;
    }

    public ImageView.ScaleType getScaleType() {
        return scaleType;
    }

    public String getSource() {
        return source;
    }
}
