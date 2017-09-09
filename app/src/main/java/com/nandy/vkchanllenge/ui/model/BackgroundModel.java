package com.nandy.vkchanllenge.ui.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nandy.vkchanllenge.ui.Background;
import com.nandy.vkchanllenge.ui.Part;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yana on 07.09.17.
 */

public class BackgroundModel {

    private Context context;

    public BackgroundModel(Context context) {
        this.context = context;
    }

    public Background[] getThumbnails() {
        return Background.values();
    }

    @Nullable
    public Drawable loadBackground(Background background) {


        switch (background.getType()) {

            case DRAWABLE:
                return ContextCompat.getDrawable(context, background.getThumbnailId());

            case ASSET:
                return loadFromAssets(background.getImageName());

            case CUSTOM:
                return new BitmapDrawable(context.getResources(), BitmapFactory.decodeResource(context.getResources(), background.getThumbnailId()));

            default:
                return null;

        }
    }

    private Drawable loadFromAssets(String name) {
        try {
            InputStream ims = context.getAssets().open(name);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inDensity = DisplayMetrics.DENSITY_HIGH;
            return Drawable.createFromResourceStream(context.getResources(), null, ims, name, opts);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ImageView loadPart(Part part) {
        Drawable drawable = loadFromAssets(part.getSource());

        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(drawable);
        if (part.getScaleType() != null) {
            imageView.setScaleType(part.getScaleType());
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(part.getAlignmentRule());

        imageView.setLayoutParams(params);

        return imageView;
    }

}
