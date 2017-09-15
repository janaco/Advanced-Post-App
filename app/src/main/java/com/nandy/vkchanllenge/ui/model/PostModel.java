package com.nandy.vkchanllenge.ui.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.View;

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

    private View view;
    private VkApi api;

    public PostModel(View view) {
        this.view = view;
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
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        Log.d("POST_", "bitmap: " + bitmap);
        return bitmap;
    }

    private File saveAsImage(Bitmap bitmap) throws FileNotFoundException {


        File file = new File(Environment.getExternalStorageDirectory(), "image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;

    }
}
