package com.nandy.vkchanllenge.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.imagetransformation.RoundedCornersTransformation;
import com.nandy.vkchanllenge.util.WindowUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yana on 10.09.17.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private static final int VIEW_CAMERA = 0;
    private static final int VIEW_ALBUM = 1;
    private static final int VIEW_IMAGE = 2;

    private final List<String> files;
    private final Context context;
    private OnBackgroundChooseListener onItemClickListener;

    private int checkedPosition = VIEW_IMAGE;
    private final int margin;
    private final int sideMargin;
    private final int imageSize;
    private final int cornerRadius;
    private final int borderWidth;
    private final int borderMargin;

    private final int borderColor;

    public ImagesAdapter(Context context, List<String> files) {
        this.files = files;
        this.context = context;

        int density = (int) WindowUtils.getDensity(context);
        margin = 4 * density;
        sideMargin = 16 * density;
        imageSize = 84 * density;
        cornerRadius = 4 * density;
        borderWidth = 2 * density;
        borderMargin = 2 * density;
        borderColor = ContextCompat.getColor(context, R.color.cornflower_blue_two);
    }

    public void setOnItemClickListener(OnBackgroundChooseListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public String getSelected() {
        return files.get(checkedPosition - 2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        setupMargins(holder, position);

        String path = loadImage(holder, position);
        holder.imageView.setOnClickListener(view -> {

            switch (holder.getAdapterPosition()) {

                case VIEW_CAMERA:
                    onItemClickListener.onTakeFromCameraClick();
                    break;

                case VIEW_ALBUM:
                    onItemClickListener.onChooseFromGalleryClick();
                    break;

                default:
                    onItemClickListener.onImageSelected(path);
                    checkedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                    break;
            }
        });
    }

    private void setupMargins(ViewHolder holder, int position) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();

        if (position == 0 || position == 1) {
            params.setMargins(sideMargin, margin, margin, margin);
        } else if (position == getItemCount() - 1) {
            params.setMargins(margin, margin, sideMargin, margin);
        } else {
            params.setMargins(margin, margin, margin, margin);
        }
        holder.imageView.setLayoutParams(params);
    }

    private String loadImage(ViewHolder holder, int position) {
        if (position == VIEW_CAMERA) {
            holder.imageView.setImageResource(R.drawable.thumb_camera);
        } else if (position == VIEW_ALBUM) {
            holder.imageView.setImageResource(R.drawable.thumb_gallery);
        } else {
            boolean checked = position == checkedPosition;
            position -= 2;
            String path = files.get(position);

            Glide
                    .with(context)
                    .load(Uri.fromFile(new File(path)))
                    .override(imageSize, imageSize)
                    .bitmapTransform(new CenterCrop(context),
                            checked ?
                                    new RoundedCornersTransformation(context, cornerRadius, borderMargin, borderColor, borderWidth)
                                    : new RoundedCornersTransformation(context, cornerRadius, 0))
                    .into(holder.imageView);
            return path;
        }

        return null;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return files.size() + 2;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface OnBackgroundChooseListener {

        void onImageSelected(String path);

        void onTakeFromCameraClick();

        void onChooseFromGalleryClick();
    }
}
