package com.nandy.vkchanllenge.ui.presenter;

import com.nandy.vkchanllenge.BasePresenter;
import com.nandy.vkchanllenge.api.VkApi;
import com.nandy.vkchanllenge.ui.model.PostModel;
import com.nandy.vkchanllenge.ui.view.PublishView;

import io.reactivex.disposables.Disposable;

/**
 * Created by yana on 15.09.17.
 */

public class PublishPresenter implements BasePresenter , VkApi.Callback{

    private PublishView view;
    private PostModel postModel;


    public PublishPresenter(PublishView view) {
        this.view = view;
    }

    public void setPostModel(PostModel postModel) {
        this.postModel = postModel;
        this.postModel.initApi(this);
    }

    @Override
    public void start() {
       postModel.post();
    }

    @Override
    public void destroy() {
        cancelPost();
    }

    public void cancelPost() {
        postModel.cancel();
    }

    @Override
    public void onResult(boolean success) {
        view.showResult(success);
    }
}
