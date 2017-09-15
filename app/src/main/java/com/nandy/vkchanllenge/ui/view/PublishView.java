package com.nandy.vkchanllenge.ui.view;

import com.nandy.vkchanllenge.BaseView;
import com.nandy.vkchanllenge.ui.presenter.PublishPresenter;

/**
 * Created by yana on 15.09.17.
 */

public interface PublishView extends BaseView<PublishPresenter> {

    void showResult(boolean success);
}
