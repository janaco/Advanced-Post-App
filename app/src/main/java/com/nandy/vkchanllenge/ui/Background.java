package com.nandy.vkchanllenge.ui;

import android.util.Pair;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nandy.vkchanllenge.R;

/**
 * Created by yana on 09.09.17.
 */

public enum Background {

    WHITE(BackgroundType.DRAWABLE, R.drawable.thumb_white, R.drawable.fon_white, R.color.white, R.color.black, "fon_white", null),
    BLUE(BackgroundType.DRAWABLE, R.drawable.gradient_blue, R.drawable.gradient_blue, R.color.black, R.color.white, "gradient_blue", null),
    GREEN(BackgroundType.DRAWABLE, R.drawable.gradient_green, R.drawable.gradient_green, R.color.black, R.color.white, "gradient_green", null),
    YELLOW(BackgroundType.DRAWABLE, R.drawable.gradient_orange, R.drawable.gradient_orange, R.color.black, R.color.white, "gradient_orange", null),
    RED(BackgroundType.DRAWABLE, R.drawable.gradient_red, R.drawable.gradient_red, R.color.black, R.color.white, "gradient_red", null),
    VIOLET(BackgroundType.DRAWABLE, R.drawable.gradient_violet, R.drawable.gradient_violet, R.color.black, R.color.white, "gradient_violet", null),
    BEACH(BackgroundType.ASSET, R.mipmap.thumb_beach, -1, R.color.black, R.color.white, "beach/bg_beach_center.png",
            new Part[]{
                    new Part("beach/bg_beach_top.png", RelativeLayout.ALIGN_PARENT_TOP, null),
                    new Part("beach/bg_beach_bottom.png", RelativeLayout.ALIGN_PARENT_BOTTOM, ImageView.ScaleType.FIT_END)}),
    STARS(BackgroundType.ASSET, R.mipmap.thumb_stars, -1, R.color.black, R.color.white, "stars/bg_stars_center.png", null),
    CUSTOM(BackgroundType.CUSTOM, R.drawable.thumb_new, -1, R.color.black, R.color.white, "ic_toolbar_new", null);

    private int textColor;
    private int highlightColor;
    private int thumbnailId;
    private int backgroundId;
    private BackgroundType type;
    private String imageName;
    private Part[] parts;

    Background(BackgroundType type, int thumbnailId, int backgroundId, int textColor, int highlightColor, String imageName,
               Part[] parts) {
        this.type = type;
        this.highlightColor = highlightColor;
        this.thumbnailId = thumbnailId;
        this.backgroundId= backgroundId;
        this.imageName = imageName;
        this.parts = parts;
        this.textColor = textColor;


    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public Part[] getParts() {
        return parts;
    }

    public int getTextColor() {
        return textColor;
    }


    public BackgroundType getType() {
        return type;
    }

    public int getHighlightColor() {
        return highlightColor;
    }

    public int getThumbnailId() {
        return thumbnailId;
    }

    public String getImageName() {
        return imageName;
    }
}
