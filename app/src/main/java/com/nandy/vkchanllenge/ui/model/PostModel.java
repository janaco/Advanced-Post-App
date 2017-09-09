package com.nandy.vkchanllenge.ui.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 09.09.17.
 */

public class PostModel {

    public Single<Boolean> post(View view) {

        Single<Boolean> single = Single.create(e -> {
            Bitmap bitmap = convertToBitmap(view);
            try {
                File file = saveAsImage(bitmap);
                e.onSuccess(true);
            } catch (FileNotFoundException error) {
                error.printStackTrace();
                e.onSuccess(false);
            }
        });
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        return single;
    }

    private Bitmap convertToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

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
