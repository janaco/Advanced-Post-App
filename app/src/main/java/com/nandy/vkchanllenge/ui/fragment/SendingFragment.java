package com.nandy.vkchanllenge.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.ui.presenter.PublishPresenter;
import com.nandy.vkchanllenge.ui.view.PublishView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 15.09.17.
 */

public class SendingFragment extends Fragment implements PublishView {

    private PublishPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sending, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.start();
    }

    @OnClick(R.id.cancel)
    void onCancelClick() {
        presenter.cancelPost();
    }

    @Override
    public void setPresenter(PublishPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void showResult(boolean success) {

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ResultFragment.newInstance(success), ResultFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }
}
