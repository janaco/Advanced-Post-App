package com.nandy.vkchanllenge.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.adapter.StickersAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yana on 09.09.17.
 */

public class StickersDialog extends Dialog {

    @BindView(R.id.stickers_grid)
    GridView gridView;

    private List<Bitmap> stickers;
    private OnStickerSelectedListener onStickerSelectedListener;

    public StickersDialog(@NonNull Context context) {
        super(context);
    }

    public StickersDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected StickersDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
        ButterKnife.bind(this, view);

        StickersAdapter adapter = new StickersAdapter(stickers);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener((adapterView, view1, position, l) -> {
            onStickerSelectedListener.onStickerSelected(adapter.getItem(position));
            dismiss();

        });

    }


    private void setStickers(List<Bitmap> stickers) {
        this.stickers = stickers;
    }

    private void setOnStickerSelectedListener(OnStickerSelectedListener onStickerSelectedListener) {
        this.onStickerSelectedListener = onStickerSelectedListener;
    }

    public static void show(Context context, List<Bitmap> stickers, OnStickerSelectedListener onStickerSelectedListener) {
        StickersDialog dialog = new StickersDialog(context, R.style.MaterialDialogSheet);
        dialog.setOnStickerSelectedListener(onStickerSelectedListener);
        dialog.setStickers(stickers);
        dialog.setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_stikers, null));
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
        }
        dialog.show();
    }


    public interface OnStickerSelectedListener {
        void onStickerSelected(Bitmap bitmap);
    }
}
