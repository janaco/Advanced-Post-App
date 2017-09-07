package com.nandy.vkchanllenge.ui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.nandy.vkchanllenge.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 07.09.17.
 */

public class BackgroundModel {

    private Context context;

    public BackgroundModel(Context context) {
        this.context = context;
    }

    public List<Integer> getThumbnails() {

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.gradient_blue);
        list.add(R.drawable.gradient_green);
        list.add(R.drawable.gradient_orange);
        list.add(R.drawable.gradient_red);
        list.add(R.drawable.gradient_violet);
        list.add(R.mipmap.thumb_beach);
        list.add(R.mipmap.thumb_stars);

        return list;
    }

    @Nullable
    public Drawable loadBackground( int thumbnailRes) {



            switch (thumbnailRes) {

                case R.mipmap.thumb_beach:
                    return loadFromAssets("beach/bg_beach_center.png");

                case R.mipmap.thumb_stars:
                    return loadFromAssets("stars/bg_stars_center.png");

                default:
                    return ContextCompat.getDrawable(context, thumbnailRes);
            }


    }

    private Drawable loadFromAssets(String name){
        try {
            InputStream ims = context.getAssets().open(name);
            return Drawable.createFromStream(ims, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
