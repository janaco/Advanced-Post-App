package com.nandy.vkchanllenge.ui.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nandy.vkchanllenge.ui.Background;
import com.nandy.vkchanllenge.ui.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
                return ContextCompat.getDrawable(context, background.getBackgroundId());

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


    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString();
    public static final String CAMERA_IMAGE_BUCKET_ID =
            getBucketId(CAMERA_IMAGE_BUCKET_NAME);

    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    public Single<List<String>> loadImages() {

        Single<List<String>> single = Single.create(e -> {
            ArrayList<String> result = new ArrayList<>();
            int count = 0;
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.DATE_TAKEN};
            final String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";

            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                    null, orderBy);


            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext() && count++ < 100) {
                String path = cursor.getString(dataColumnIndex);


                //Store the path of the image
                Log.i("IMAGES_", "path: " + path);
                result.add(path);

            }
            cursor.close();

            e.onSuccess(result);
        });
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return single;
    }
}
