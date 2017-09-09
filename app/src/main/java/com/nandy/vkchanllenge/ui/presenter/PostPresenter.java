package com.nandy.vkchanllenge.ui.presenter;

import android.text.Layout;

import com.nandy.vkchanllenge.BasePresenter;
import com.nandy.vkchanllenge.ui.model.BackgroundModel;
import com.nandy.vkchanllenge.ui.model.StickersModel;
import com.nandy.vkchanllenge.ui.model.TextModel;
import com.nandy.vkchanllenge.ui.view.PostView;

import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 07.09.17.
 */

public class PostPresenter implements BasePresenter {

    private PostView<PostPresenter> view;

    private BackgroundModel backgroundModel;
    private TextModel textModel;
    private StickersModel stickersModel;

    private Disposable stickersSubscription;

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
    }

    public void onThumbnailSelected(int thumbnailsResId) {
        view.setBackground(backgroundModel.loadBackground(thumbnailsResId));
    }

    public void highlightText(Layout layout) {
        view.highlight(textModel.highlightText(layout));

    }

    public void loadStickers() {
        view.showStickersPopup(stickersModel.getStickers(), stickersModel);
    }

}
