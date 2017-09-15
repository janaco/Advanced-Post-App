package com.nandy.vkchanllenge.ui.presenter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.View;

import com.nandy.vkchanllenge.BasePresenter;
import com.nandy.vkchanllenge.adapter.ImagesAdapter;
import com.nandy.vkchanllenge.ui.Background;
import com.nandy.vkchanllenge.ui.BackgroundType;
import com.nandy.vkchanllenge.ui.Part;
import com.nandy.vkchanllenge.ui.dialog.StickersDialog;
import com.nandy.vkchanllenge.ui.model.BackgroundModel;
import com.nandy.vkchanllenge.ui.model.PostModel;
import com.nandy.vkchanllenge.ui.model.StickersModel;
import com.nandy.vkchanllenge.ui.model.TextModel;
import com.nandy.vkchanllenge.ui.view.PostView;

import java.io.File;

import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 07.09.17.
 */

public class PostPresenter implements BasePresenter, StickersDialog.OnStickerSelectedListener,
        ImagesAdapter.OnBackgroundChooseListener , StickersModel.StickerTouchListener{

    private static final int REQUEST_CODE_GALLERY = 108;
    private static final int REQUEST_CODE_CAMERA = 109;

    private PostView<PostPresenter> view;

    private BackgroundModel backgroundModel;
    private TextModel textModel;
    private StickersModel stickersModel;
    private PostModel postModel;


    private Disposable stickersSubscription;
    private Disposable postSubscription;

    public PostPresenter(PostView<PostPresenter> view) {
        this.view = view;
    }

    public void setBackgroundModel(BackgroundModel backgroundModel) {
        this.backgroundModel = backgroundModel;
    }

    public void setStickersModel(StickersModel stickersModel) {
        this.stickersModel = stickersModel;
    }

    public void setTextModel(TextModel textModel) {
        this.textModel = textModel;
    }

    public void setPostModel(PostModel postModel) {
        this.postModel = postModel;
    }

    @Override
    public void start() {
        view.setThumbnails(backgroundModel.getThumbnails());
        stickersSubscription = stickersModel.loadStickers().subscribe();

    }

    @Override
    public void destroy() {

        if (stickersSubscription != null && !stickersSubscription.isDisposed()) {
            stickersSubscription.dispose();
        }

        if (postSubscription != null && !postSubscription.isDisposed()) {
            postSubscription.dispose();
        }

//        if (imagesSubscription != null && !imagesSubscription.isDisposed()){
//            imagesSubscription.dispose();
//        }
    }

    @Override
    public void onStickerSelected(Bitmap bitmap) {
        view.addSticker(stickersModel.createStickerView(bitmap));
    }

    public void onThumbnailSelected(Background background) {
        if (background.getType() == BackgroundType.CUSTOM) {
            return;
        }
        view.setBackground(backgroundModel.loadBackground(background));

        if (background.getParts() != null) {

            for (Part part : background.getParts()) {
                view.addBackgroundPart(backgroundModel.loadPart(part));
            }

        }
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

        if (path != null){
            view.setBackground(path);
        }
    }


    public void highlightText(Layout layout) {
        view.highlight(textModel.highlightText(layout));

    }

    public void loadStickers() {
        view.showStickersPopup(stickersModel.getStickers(), this);
    }

    public void post(View view) {
        postSubscription = postModel.post(view).subscribe(success -> PostPresenter.this.view.onPostResult(success));
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
