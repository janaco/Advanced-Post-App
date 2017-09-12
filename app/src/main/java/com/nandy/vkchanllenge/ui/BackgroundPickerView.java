package com.nandy.vkchanllenge.ui;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.adapter.ImagesAdapter;
import com.nandy.vkchanllenge.ui.model.BackgroundModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 11.09.17.
 */

public class BackgroundPickerView extends BottomPopupWindow {

    @BindView(R.id.images_list)
    RecyclerView imagesList;

    private Disposable imagesSubscription;
    private BackgroundModel backgroundModel;


    public BackgroundPickerView(View parentView) {
        super(parentView);
    }

    public void setBackgroundModel(BackgroundModel backgroundModel) {
        this.backgroundModel = backgroundModel;
    }

    @Override
    public View onCreateView(Context context, View parent) {
        return LayoutInflater.from(context).inflate(R.layout.popup_backgrounds, null);
    }

    @Override
    public void onViewCreated(View view) {
        ButterKnife.bind(this, view);
        imagesList.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));

        imagesSubscription = backgroundModel.loadImages().subscribe(files -> {
            ImagesAdapter adapter = new ImagesAdapter(files);
            setImagesAdapter(adapter);
        });
    }

    public void setImagesAdapter(ImagesAdapter imagesAdapter) {
        imagesList.setAdapter(imagesAdapter);

    }

}