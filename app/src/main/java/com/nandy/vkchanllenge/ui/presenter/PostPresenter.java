package com.nandy.vkchanllenge.ui.presenter;

import com.nandy.vkchanllenge.BasePresenter;
import com.nandy.vkchanllenge.ui.model.ThumnailsListModel;
import com.nandy.vkchanllenge.ui.view.PostView;

/**
 * Created by yana on 07.09.17.
 */

public class PostPresenter implements BasePresenter{

    private PostView<PostPresenter> view;

    private ThumnailsListModel thumnailsListModel;

    public PostPresenter(PostView<PostPresenter> view){
        this.view = view;
        this.thumnailsListModel = new ThumnailsListModel();
    }

    @Override
    public void start() {
        view.setThumbnails(thumnailsListModel.getThumbnails());
    }

    @Override
    public void destroy() {

    }

}
