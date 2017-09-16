package com.nandy.vkchanllenge.ui.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nandy.vkchanllenge.MainActivity;
import com.nandy.vkchanllenge.MyFragment;
import com.nandy.vkchanllenge.OnListItemClickListener;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.SimpleOnTabSelectedListener;
import com.nandy.vkchanllenge.SimpleTextWatcher;
import com.nandy.vkchanllenge.adapter.ThumbnailsAdapter;
import com.nandy.vkchanllenge.ui.Background;
import com.nandy.vkchanllenge.ui.dialog.BackgroundPickerView;
import com.nandy.vkchanllenge.ui.dialog.StickersDialog;
import com.nandy.vkchanllenge.ui.model.BackgroundModel;
import com.nandy.vkchanllenge.ui.model.PostModel;
import com.nandy.vkchanllenge.ui.model.StickersModel;
import com.nandy.vkchanllenge.ui.model.TextModel;
import com.nandy.vkchanllenge.ui.presenter.CreatePostPresenter;
import com.nandy.vkchanllenge.ui.presenter.PublishPresenter;
import com.nandy.vkchanllenge.ui.view.PostView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 07.09.17.
 */

public class PostFragment extends MyFragment implements PostView<CreatePostPresenter>,
        OnListItemClickListener<Background> {

    @BindView(R.id.text_view)
    EditText textView;
    @BindView(R.id.content)
    RelativeLayout contentView;
    @BindView(R.id.fon)
    RelativeLayout viewFon;
    @BindView(R.id.thumbnails_list)
    RecyclerView thumbnailsList;
    @BindView(R.id.background_view)
    ImageView backgroundView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.content_root_view)
    View rootView;
    @BindView(R.id.btn_send)
    TextView buttonSend;


    private final List<ImageView> imageParts = new ArrayList<>();

    private CreatePostPresenter presenter;
    private ThumbnailsAdapter thumbnailsAdapter;

    private BackgroundPickerView pickerView;
    private float scaledDensity;
    public int screenHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        scaledDensity = displayMetrics.scaledDensity;
        screenHeight = displayMetrics.heightPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        TabLayout.Tab tabPost = tabLayout.newTab();
        tabPost.setText(R.string.post);
        tabPost.setTag(R.string.post);

        TabLayout.Tab tabStory = tabLayout.newTab();
        tabStory.setText(R.string.story);
        tabStory.setTag(R.string.story);


        tabLayout.addTab(tabPost);
        tabLayout.addTab(tabStory);

        TextView textPost = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0)).getChildAt(1));
        textPost.setScaleY(-1);
        TextView textStory = (TextView) (((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(1)).getChildAt(1));
        textStory.setScaleY(-1);
        tabLayout.addOnTabSelectedListener(new SimpleOnTabSelectedListener() {
            @Override
            public void onTabItemSelected(TabLayout.Tab tab) {
                int tabId = (int) tab.getTag();

                switch (tabId) {

                    case R.string.post:
                        applyPostStyle();
                        break;

                    case R.string.story:
                        applyStoryStyle();
                        break;
                }
            }
        });

        thumbnailsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        presenter.start();
        pickerView = new BackgroundPickerView(view);
        pickerView.setBackgroundModel(new BackgroundModel(getContext()));
        pickerView.setOnItemClickListener(presenter);


        textView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int length) {
                buttonSend.setEnabled(length > 0);
            }
        });
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


    @OnClick(R.id.text_view)
    void onInputClick() {
        if (pickerView.isShowed()) {
            pickerView.dismiss();
        }
    }

    @OnClick(R.id.btn_send)
    void onSendButtonClick() {

        textView.setCursorVisible(false);

        SendingFragment fragment = new SendingFragment();

        PublishPresenter publishPresenter = new PublishPresenter(fragment);
        publishPresenter.setPostModel(new PostModel(viewFon));
        fragment.setPresenter(publishPresenter);


        int commit = (getActivity()).getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment, SendingFragment.class.getSimpleName())
                .commit();
        Log.d("POST_", "send: " + commit);
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
            viewFon.removeView(imageView);
        }
        imageParts.clear();
        backgroundView.setImageDrawable(background);
    }

    @Override
    public void setBackground(String path) {
        for (ImageView imageView : imageParts) {
            viewFon.removeView(imageView);
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
        contentView.addView(imageView);
    }

    @Override
    public void addBackgroundPart(ImageView imageView) {
        viewFon.addView(imageView);
        imageParts.add(imageView);
    }

    @Override
    public void onPostResult(boolean success) {
        //TODO: it's temporary implementation
        Toast.makeText(getContext(), success ? "Successfully saved" : "Failed to save", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void setPresenter(CreatePostPresenter presenter) {
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
        viewFon.addView(viewTrash);
    }

    @Override
    public void remove(View viewTrash) {
        contentView.removeView(viewTrash);
        viewFon.removeView(viewTrash);
    }


    void applyPostStyle() {


        for (int i = 0; i < contentView.getChildCount(); i++) {
            ImageView child = (ImageView) contentView.getChildAt(i);
            Matrix matrix = child.getImageMatrix();
            float[] values = new float[9];
            matrix.getValues(values);
            float x = values[Matrix.MTRANS_X];
            float y = values[Matrix.MTRANS_Y];

            int imgHeight = child.getDrawable().getIntrinsicHeight();

            if (y > 1920 / 2) {
                float bottomMargin = 1920 - y - imgHeight;
                float dy = 1080 - bottomMargin - imgHeight;
                float translation = dy - y;

                Matrix displayMatrix = new Matrix();
                matrix.postTranslate(0, translation);

                displayMatrix.set(matrix);
                child.setImageMatrix(displayMatrix);

                Log.d("STICKER_", "child: " + x + ", " + y + ", dest: " + dy + ", tr: " + translation + ", height: " + imgHeight);

            }
        }
        ValueAnimator anim = ValueAnimator.ofInt(viewFon.getMeasuredHeight(), (int) scaledDensity * 360);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();

            ViewGroup.LayoutParams params = viewFon.getLayoutParams();
            params.height = val;
            viewFon.setLayoutParams(params);
        });

        anim.setDuration(300);
        anim.start();

    }


    void applyStoryStyle() {


        for (int i = 0; i < contentView.getChildCount(); i++) {
            ImageView child = (ImageView) contentView.getChildAt(i);
            Matrix matrix = child.getImageMatrix();
            float[] values = new float[9];
            matrix.getValues(values);
            float x = values[Matrix.MTRANS_X];
            float y = values[Matrix.MTRANS_Y];

            int imgHeight = child.getDrawable().getIntrinsicHeight();

            if (y > 1080 / 2) {
                float bottomMargin = 1080 - y - imgHeight;
                float dy = 1920 - bottomMargin - imgHeight;
                float translation = dy - y;

                Matrix displayMatrix = new Matrix();
                matrix.postTranslate(0, translation);

                displayMatrix.set(matrix);
                child.setImageMatrix(displayMatrix);

            }


        }


        ValueAnimator anim = ValueAnimator.ofInt(viewFon.getMeasuredHeight(), screenHeight);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams params = viewFon.getLayoutParams();
            params.height = val;
            viewFon.setLayoutParams(params);
        });

        anim.setDuration(300);
        anim.start();

    }

    public static PostFragment newInstance(Context context) {

        PostFragment fragment = new PostFragment();

        CreatePostPresenter presenter = new CreatePostPresenter(fragment);
        presenter.setBackgroundModel(new BackgroundModel(context));
        presenter.setTextModel(new TextModel(context));
        StickersModel stickersModel = new StickersModel(context);
        stickersModel.setStickerTouchListener(presenter);
        presenter.setStickersModel(stickersModel);

        fragment.setPresenter(presenter);

        return fragment;
    }
}
