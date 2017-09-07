package com.nandy.vkchanllenge.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nandy.vkchanllenge.MyFragment;
import com.nandy.vkchanllenge.OnListItemClickListener;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.adapter.ThumbnailsAdapter;
import com.nandy.vkchanllenge.ui.presenter.PostPresenter;
import com.nandy.vkchanllenge.ui.view.PostView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 07.09.17.
 */

public class PostFragment extends MyFragment implements PostView<PostPresenter>, OnListItemClickListener<Integer> {

    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.content)
    RelativeLayout contentView;
    @BindView(R.id.thumbnails_list)
    RecyclerView thumbnailsList;

    private PostPresenter presenter;
    private ThumbnailsAdapter thumbnailsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        thumbnailsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @OnClick(R.id.btn_font)
    void onFontButtonClick(){

    }

    @OnClick(R.id.btn_sticker)
    void onStickersButtonClick(){

    }

    @OnClick(R.id.btn_post)
    void onPostButtonClick(){

    }

    @OnClick(R.id.btn_story)
    void onStoryButtonClick(){

    }

    @OnClick(R.id.btn_send)
    void onSendButtonClick(){

    }

    @Override
    public void onListItemClick(Integer integer, int position) {

    }


    @Override
    public void setThumbnails(List<Integer> thumbnails) {
        thumbnailsAdapter = new ThumbnailsAdapter(thumbnails);
        thumbnailsAdapter.setOnListItemClickListener(this);
        thumbnailsList.setAdapter(thumbnailsAdapter);
    }

    @Override
    public void setPresenter(PostPresenter presenter) {
        this.presenter = presenter;
    }

    public static PostFragment newInstance(){

        PostFragment fragment = new PostFragment();
        fragment.setPresenter(new PostPresenter(fragment));

        return fragment;
    }
}
