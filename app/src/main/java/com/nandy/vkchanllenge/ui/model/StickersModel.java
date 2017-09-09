package com.nandy.vkchanllenge.ui.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 09.09.17.
 */

public class StickersModel {

    private Context context;
    private final List<Bitmap> stickers = new ArrayList<>();

    public StickersModel(Context context) {
        this.context = context;
    }

    public Single<List<Bitmap>> loadStickers() {

        Single<List<Bitmap>> single = Single.create(e -> {
            AssetManager assetManager = context.getAssets();

            try {
                String[] stickerFiles = assetManager.list("stickers");
                for (String sticker : stickerFiles) {
                    InputStream is = assetManager.open("stickers/" + sticker);
                    stickers.add(BitmapFactory.decodeStream(is));
                }
            } catch (IOException error) {
                error.printStackTrace();
            }

        });
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        return single;
    }

    public List<Bitmap> getStickers() {
        return stickers;
    }

    public ImageView createStickerView(Bitmap bitmap) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        imageView.setOnTouchListener(new View.OnTouchListener() {
            private int xDelta;
            private int yDelta;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        xDelta = x - params.leftMargin;
                        yDelta = y - params.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                return true;
            }
        });


        return imageView;
    }
}
