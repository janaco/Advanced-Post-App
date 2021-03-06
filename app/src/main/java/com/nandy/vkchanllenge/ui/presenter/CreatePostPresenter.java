package com.nandy.vkchanllenge.ui.presenter;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;

import com.nandy.vkchanllenge.ui.BasePresenter;
import com.nandy.vkchanllenge.OnListItemClickListener;
import com.nandy.vkchanllenge.adapter.ImagesAdapter;
import com.nandy.vkchanllenge.model.Background;
import com.nandy.vkchanllenge.model.BackgroundType;
import com.nandy.vkchanllenge.model.Part;
import com.nandy.vkchanllenge.ui.dialog.StickersDialog;
import com.nandy.vkchanllenge.ui.model.BackgroundModel;
import com.nandy.vkchanllenge.ui.model.StickersModel;
import com.nandy.vkchanllenge.ui.view.PostView;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 07.09.17.
 */

public class CreatePostPresenter implements BasePresenter, StickersDialog.OnStickerSelectedListener,
        ImagesAdapter.OnBackgroundChooseListener, StickersModel.StickerTouchListener, OnListItemClickListener<Background> {

    private static final int REQUEST_CODE_GALLERY = 108;
    private static final int REQUEST_CODE_CAMERA = 109;
    private static final int REQUEST_CODE_FILE_SYSTEM = 111;

    private PostView<CreatePostPresenter> view;

    private BackgroundModel backgroundModel;
    private StickersModel stickersModel;


    private Disposable stickersSubscription;

    public CreatePostPresenter(PostView<CreatePostPresenter> view) {
        this.view = view;
    }

    public void setBackgroundModel(BackgroundModel backgroundModel) {
        this.backgroundModel = backgroundModel;
    }

    public void setStickersModel(StickersModel stickersModel) {
        this.stickersModel = stickersModel;
    }


    @Override
    public void start() {
        view.setThumbnails(backgroundModel.getThumbnails());
        onThumbnailSelected(Background.WHITE);
        stickersSubscription = stickersModel.loadStickers().subscribe();

    }

    @Override
    public void destroy() {

        if (stickersSubscription != null && !stickersSubscription.isDisposed()) {
            stickersSubscription.dispose();
        }

    }

    @Override
    public void onListItemClick(Background background, int position) {

        if (background == Background.CUSTOM) {

            if (backgroundModel.hasFileSystemAccessPermission()) {
                view.showImagesPopup();
            } else {
                view.requestPermission(REQUEST_CODE_FILE_SYSTEM, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            return;
        }
        onThumbnailSelected(background);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_FILE_SYSTEM && backgroundModel.hasFileSystemAccessPermission()) {
            view.showImagesPopup();
        }
    }

    @Override
    public void onStickerSelected(Bitmap bitmap) {
        view.addSticker(stickersModel.createStickerView(bitmap));
    }

    private void onThumbnailSelected(Background background) {

        stickersModel.setHighlightTrashView(background == Background.WHITE);

        if (background.getType() == BackgroundType.CUSTOM) {
            return;
        }
        view.setBackground(backgroundModel.loadBackground(background));

        if (background.getParts() != null) {

            for (Part part : background.getParts()) {
                view.addBackgroundPart(backgroundModel.loadPart(part));
            }

        }
        view.highlight(backgroundModel.getHighlight());

    }

    @Override
    public void onTakeFromCameraClick() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri outputUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath(), "photo.jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        try {
            view.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChooseFromGalleryClick() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        view.startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
    }

    @Override
    public void onImageSelected(String path) {
        view.setBackground(path);
        view.setThumbnailSelected(Background.CUSTOM);
        backgroundModel.setBackground(Background.CUSTOM);
        view.highlight(backgroundModel.getHighlight());

    }

    public void onActivityResult(int requestCode, Intent data) {
        String path = null;
        switch (requestCode) {

            case REQUEST_CODE_CAMERA:

                path = backgroundModel.getOutputFromCamera();
                break;

            case REQUEST_CODE_GALLERY:
                path = backgroundModel.getImageFromGallery(data.getData());
                break;

        }

        if (path != null) {
            onImageSelected(path);
        }
    }


    public void highlightText() {
        view.highlight(backgroundModel.highlightText());

    }

    public void loadStickers() {
        view.showStickersPopup(stickersModel.getStickers(), this);
    }

    @Override
    public void showTrash(View viewTrash) {
        view.showTrash(viewTrash);
    }

    @Override
    public void remove(View view) {
        this.view.remove(view);
    }
}
