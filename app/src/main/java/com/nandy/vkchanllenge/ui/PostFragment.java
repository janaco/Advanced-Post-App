package com.nandy.vkchanllenge.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nandy.vkchanllenge.MyFragment;
import com.nandy.vkchanllenge.OnListItemClickListener;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.adapter.ThumbnailsAdapter;
import com.nandy.vkchanllenge.ui.model.BackgroundModel;
import com.nandy.vkchanllenge.ui.model.PostModel;
import com.nandy.vkchanllenge.ui.model.StickersModel;
import com.nandy.vkchanllenge.ui.model.TextModel;
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
    EditText textView;
    @BindView(R.id.content)
    RelativeLayout contentView;
    @BindView(R.id.thumbnails_list)
    RecyclerView thumbnailsList;
    @BindView(R.id.background_view)
    ImageView backgroundView;

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
    void onFontButtonClick() {
        presenter.highlightText(textView.getLayout());

    }

    @OnClick(R.id.btn_sticker)
    void onStickersButtonClick() {
        presenter.loadStickers();
    }

    @OnClick(R.id.btn_post)
    void onPostButtonClick() {
    }

    @OnClick(R.id.btn_story)
    void onStoryButtonClick() {

    }

    @OnClick(R.id.btn_send)
    void onSendButtonClick() {
        presenter.post(contentView);
    }

    @Override
    public void onListItemClick(Integer thumnailId, int position) {
        presenter.onThumbnailSelected(thumnailId);
    }


    @Override
    public void setThumbnails(List<Integer> thumbnails) {
        thumbnailsAdapter = new ThumbnailsAdapter(thumbnails);
        thumbnailsAdapter.setOnListItemClickListener(this);
        thumbnailsList.setAdapter(thumbnailsAdapter);
    }

    @Override
    public void setBackground(Drawable background) {
        backgroundView.setImageDrawable(background);
    }

    @Override
    public void highlight(Spannable spannableText) {
        textView.setText(spannableText);
    }

    @Override
    public void showStickersPopup(List<Bitmap> stickers, StickersDialog.OnStickerSelectedListener listener) {
        StickersDialog.show(getContext(), stickers, listener);
    }

    @Override
    public void onPostResult(boolean success) {
        //TODO: it's temporary implementation
        Toast.makeText(getContext(), success ? "Successfully saved" : "Failed to save", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setPresenter(PostPresenter presenter) {
        this.presenter = presenter;
    }

    public static PostFragment newInstance(Context context) {

        PostFragment fragment = new PostFragment();

        PostPresenter presenter = new PostPresenter(fragment);
        presenter.setBackgroundModel(new BackgroundModel(context));
        presenter.setTextModel(new TextModel(context));
        presenter.setStickersModel(new StickersModel(context));
        presenter.setPostModel(new PostModel());

        fragment.setPresenter(presenter);

        return fragment;
    }
}
