package com.nandy.vkchanllenge.ui;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nandy.vkchanllenge.R;

/**
 * Created by yana on 09.09.17.
 */

public enum Background {

    WHITE(
            BackgroundType.DRAWABLE,
            R.drawable.thumb_white,
            R.drawable.fon_white,
            R.color.black,
            "fon_white",
            null,
            new Highlight[]{Highlight.WHITE_INVERTED, Highlight.TRANSPARENT_INVERTED, Highlight.NONE}
            ),

    BLUE(
            BackgroundType.DRAWABLE,
            R.drawable.gradient_blue,
            R.drawable.gradient_blue,
            R.color.white,
            "gradient_blue",
            null,
            new Highlight[]{Highlight.WHITE, Highlight.TRANSPARENT, Highlight.NONE}
    ),

    GREEN(
            BackgroundType.DRAWABLE,
            R.drawable.gradient_green,
            R.drawable.gradient_green,
            R.color.white,
            "gradient_green",
            null,
            new Highlight[]{Highlight.WHITE, Highlight.TRANSPARENT, Highlight.NONE}
    ),

    YELLOW(
            BackgroundType.DRAWABLE,
            R.drawable.gradient_orange,
            R.drawable.gradient_orange,
            R.color.white,
            "gradient_orange",
            null,
            new Highlight[]{Highlight.WHITE, Highlight.TRANSPARENT, Highlight.NONE}
    ),

    RED(
            BackgroundType.DRAWABLE,
            R.drawable.gradient_red,
            R.drawable.gradient_red,
            R.color.white,
            "gradient_red",
            null,
            new Highlight[]{Highlight.WHITE, Highlight.TRANSPARENT, Highlight.NONE}
    ),

    VIOLET(
            BackgroundType.DRAWABLE,
            R.drawable.gradient_violet,
            R.drawable.gradient_violet,
            R.color.white,
            "gradient_violet",
            null,
            new Highlight[]{Highlight.WHITE, Highlight.TRANSPARENT, Highlight.NONE}
    ),

    BEACH(
            BackgroundType.ASSET,
            R.mipmap.thumb_beach,
            -1,
            R.color.white,
            "beach/bg_beach_center.png",
            new Part[]{
                    new Part("beach/bg_beach_top.png", RelativeLayout.ALIGN_PARENT_TOP, null),
                    new Part("beach/bg_beach_bottom.png", RelativeLayout.ALIGN_PARENT_BOTTOM, ImageView.ScaleType.FIT_END)},
            new Highlight[]{Highlight.WHITE, Highlight.TRANSPARENT, Highlight.NONE}
    ),

    STARS(
            BackgroundType.ASSET,
            R.mipmap.thumb_stars,
            -1,
            R.color.white,
            "stars/bg_stars_center.png",
            null,
            new Highlight[]{Highlight.WHITE, Highlight.TRANSPARENT, Highlight.NONE}
    ),

    CUSTOM(
            BackgroundType.CUSTOM,
            R.drawable.thumb_new,
            -1,
            R.color.white,
            "ic_toolbar_new",
            null,
            new Highlight[]{Highlight.WHITE, Highlight.TRANSPARENT, Highlight.NONE}
    );

    private int defaultTextColor;
    private int thumbnailId;
    private int backgroundId;
    private BackgroundType type;
    private String imageName;
    private Part[] parts;
    private Highlight []highlights;

    Background(BackgroundType type, int thumbnailId, int backgroundId, int defaultTextColor, String imageName,
               Part[] parts, Highlight []highlights) {
        this.type = type;
        this.thumbnailId = thumbnailId;
        this.backgroundId= backgroundId;
        this.imageName = imageName;
        this.parts = parts;
        this.defaultTextColor = defaultTextColor;
        this.highlights = highlights;


    }

    public Highlight[] getHighlights() {
        return highlights;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public Part[] getParts() {
        return parts;
    }

    public int getDefaultTextColor() {
        return defaultTextColor;
    }


    public BackgroundType getType() {
        return type;
    }


    public int getThumbnailId() {
        return thumbnailId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setHighlights(Highlight[] highlights) {
        this.highlights = highlights;
    }
}
