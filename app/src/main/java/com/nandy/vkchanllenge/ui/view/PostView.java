package com.nandy.vkchanllenge.ui.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;

import com.nandy.vkchanllenge.BaseView;
import com.nandy.vkchanllenge.ui.StickersDialog;

import java.util.List;

/**
 * Created by yana on 07.09.17.
 */

public interface PostView<Presenter> extends BaseView<Presenter> {

    void setThumbnails(List<Integer> thumbnails);

    void setBackground(Drawable background);

    void highlight(Spannable spannableText);

    void showStickersPopup(List<Bitmap> stickers, StickersDialog.OnStickerSelectedListener listener);

    void onPostResult(boolean success);
}
