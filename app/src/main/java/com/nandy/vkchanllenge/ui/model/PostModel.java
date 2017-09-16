package com.nandy.vkchanllenge.ui.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.nandy.vkchanllenge.PostType;
import com.nandy.vkchanllenge.api.VkApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 09.09.17.
 */

public class PostModel {

    private static final int IMAGE_WIDTH = 1080;

    private View view;
    private VkApi api;
    private PostType postType;

    public PostModel(View view, PostType postType) {
        this.view = view;
        this.postType = postType;
    }

    public void initApi(VkApi.Callback callback) {
        this.api = new VkApi(callback);
    }

    public void post() {

        api.uploadImageToWall(convertToBitmap(view));
    }

    public void cancel(){
        api.cancelRequest();
    }

    private Bitmap convertToBitmap(View view) {
        Pair<Integer, Integer> size = getImageSize();
        Bitmap bitmap = Bitmap.createBitmap(size.first, size.second, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private Pair<Integer, Integer> getImageSize(){
        switch (postType){

            case POST:
                return new Pair<>(IMAGE_WIDTH, IMAGE_WIDTH);

            case STORY:
                return new Pair<>(IMAGE_WIDTH, IMAGE_WIDTH * view.getHeight()/view.getWidth());
        }

        return new Pair<>(1080, 1080);
    }

}
