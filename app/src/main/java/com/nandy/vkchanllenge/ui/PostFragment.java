package com.nandy.vkchanllenge.ui;

import android.content.Context;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nandy.vkchanllenge.MyFragment;
import com.nandy.vkchanllenge.OnListItemClickListener;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.adapter.ImagesAdapter;
import com.nandy.vkchanllenge.adapter.ThumbnailsAdapter;
import com.nandy.vkchanllenge.ui.model.BackgroundModel;
import com.nandy.vkchanllenge.ui.model.PostModel;
import com.nandy.vkchanllenge.ui.model.StickersModel;
import com.nandy.vkchanllenge.ui.model.TextModel;
import com.nandy.vkchanllenge.ui.presenter.PostPresenter;
import com.nandy.vkchanllenge.ui.view.PostView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 07.09.17.
 */

public class PostFragment extends MyFragment implements PostView<PostPresenter>,
        OnListItemClickListener<Background> {

    @BindView(R.id.text_view)
    EditText textView;
    @BindView(R.id.content)
    FrameLayout contentView;
    @BindView(R.id.thumbnails_list)
    RecyclerView thumbnailsList;
    @BindView(R.id.background_view)
    ImageView backgroundView;


    private final List<ImageView> imageParts = new ArrayList<>();

    private PostPresenter presenter;
    private ThumbnailsAdapter thumbnailsAdapter;

    private BackgroundPickerView pickerView;

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
        pickerView = new BackgroundPickerView(view);
        pickerView.setBackgroundModel(new BackgroundModel(getContext()));
        pickerView.setOnItemClickListener(presenter);
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

    @OnClick(R.id.text_view)
    void onInputClick() {
        if (pickerView.isShowed()) {
            pickerView.dismiss();
        }
    }

    @OnClick(R.id.btn_send)
    void onSendButtonClick() {
        presenter.post(contentView);
    }

    @Override
    public void onListItemClick(Background background, int position) {

        if (background == Background.CUSTOM) {
            pickerView.showPopup();
            return;
        }
        presenter.onThumbnailSelected(background);
    }


    @Override
    public void setThumbnails(Background[] background) {
        thumbnailsAdapter = new ThumbnailsAdapter(background);
        thumbnailsAdapter.setOnListItemClickListener(this);
        thumbnailsList.setAdapter(thumbnailsAdapter);
    }

    @Override
    public void setBackground(Drawable background) {
        for (ImageView imageView : imageParts) {
            contentView.removeView(imageView);
        }
        imageParts.clear();
        backgroundView.setImageDrawable(background);
    }

    @Override
    public void setBackground(String path) {
        for (ImageView imageView : imageParts) {
            contentView.removeView(imageView);
        }
        imageParts.clear();
        Glide.with(getContext())
                .load(path)
                .into(backgroundView);
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
    public void addSticker(ImageView imageView) {
        contentView.addView(imageView, contentView.getChildCount() - 2);
    }

    @Override
    public void addBackgroundPart(ImageView imageView) {
        contentView.addView(imageView, 1);
        imageParts.add(imageView);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, data);
    }

    public boolean onBackPressed() {
        if (pickerView.isShowed()) {
            pickerView.dismiss();
            return true;
        }

        return false;
    }


    @Override
    public void showTrash(View viewTrash) {
        contentView.addView(viewTrash, contentView.getChildCount() - 2);
    }

    @Override
    public void remove(View viewTrash) {
        contentView.removeView(viewTrash);
    }

    public static PostFragment newInstance(Context context) {

        PostFragment fragment = new PostFragment();

        PostPresenter presenter = new PostPresenter(fragment);
        presenter.setBackgroundModel(new BackgroundModel(context));
        presenter.setTextModel(new TextModel(context));
        StickersModel stickersModel = new StickersModel(context);
        stickersModel.setStickerTouchListener(presenter);
        presenter.setStickersModel(stickersModel);
        presenter.setPostModel(new PostModel());

        fragment.setPresenter(presenter);

        return fragment;
    }
}
