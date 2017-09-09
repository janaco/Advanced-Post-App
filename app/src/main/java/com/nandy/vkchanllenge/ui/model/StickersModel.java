package com.nandy.vkchanllenge.ui.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nandy.vkchanllenge.ui.StickersDialog;

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
 * Created by yana on 09.09.17.
 */

public class StickersModel implements StickersDialog.OnStickerSelectedListener{

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

    @Override
    public void onStickerSelected(Bitmap bitmap) {

    }
}
