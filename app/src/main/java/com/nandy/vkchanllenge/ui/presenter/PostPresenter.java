package com.nandy.vkchanllenge.ui.presenter;

import com.nandy.vkchanllenge.BasePresenter;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.ui.model.BackgroundModel;
import com.nandy.vkchanllenge.ui.view.PostView;

/**
 * Created by yana on 07.09.17.
 */

public class PostPresenter implements BasePresenter{

    private PostView<PostPresenter> view;

    private BackgroundModel backgroundModel;

    public PostPresenter(PostView<PostPresenter> view){
        this.view = view;
    }

    public void setBackgroundModel(BackgroundModel backgroundModel) {
        this.backgroundModel = backgroundModel;
    }

    @Override
    public void start() {
        view.setThumbnails(backgroundModel.getThumbnails());
    }

    @Override
    public void destroy() {

    }

    public void onThumbnailSelected(int thumbnailsResId){
                view.setBackground(backgroundModel.loadBackground(thumbnailsResId));
    }

}
