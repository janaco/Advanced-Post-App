package com.nandy.vkchanllenge.adapter;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.RoundedCornersTransformation;

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


    private List<String> files;
    private int checkedPosition = VIEW_IMAGE;
    private OnBackgroundChooseListener onItemClickListener;

    private int scale = 3;

    public ImagesAdapter(List<String> files) {
        this.files = files;
    }

    public void setOnItemClickListener(OnBackgroundChooseListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getSelected() {
        return files.get(checkedPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String path = null;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();

        int margin = 4 * scale;
        if (position == 0 || position == 1) {
            params.setMargins(16*scale, margin, margin, margin);
        } else if (position == getItemCount() - 1) {
            params.setMargins(margin, margin,margin*16,margin);
        } else {
            params.setMargins(margin, margin, margin, margin);
        }
        holder.imageView.setLayoutParams(params);

        if (position == VIEW_CAMERA) {

            holder.imageView.setImageResource(R.drawable.thumb_camera);
        } else if (position == VIEW_ALBUM) {
            holder.imageView.setImageResource(R.drawable.thumb_gallery);
        } else {

            boolean checked = position == checkedPosition;
            position -= 2;
            path = files.get(position);


            if (checked) {

                Glide
                        .with(holder.imageView.getContext())
                        .load(Uri.fromFile(new File(path)))
                        .override(scale*84, scale*84)
                        .bitmapTransform(new CenterCrop(holder.imageView.getContext()),
                                new RoundedCornersTransformation(holder.imageView.getContext(), scale*4, scale*2, ContextCompat.getColor(holder.imageView.getContext(), R.color.cornflower_blue_two), scale*2))
                        .into(holder.imageView);
            } else {

                Glide
                        .with(holder.imageView.getContext())
                        .load(Uri.fromFile(new File(path)))
                        .override(scale*84, scale*84)
                        .bitmapTransform(new CenterCrop(holder.imageView.getContext()),
                                new RoundedCornersTransformation(holder.imageView.getContext(), scale*4, 0))
                        .into(holder.imageView);
            }
        }

        String finalPath = path;
        holder.imageView.setOnClickListener(view -> {

            switch (holder.getAdapterPosition()) {

                case VIEW_CAMERA:
                    onItemClickListener.onTakeFromCameraClick();
                    break;

                case VIEW_ALBUM:
                    onItemClickListener.onChooseFromGalleryClick();
                    break;

                default:
                    onItemClickListener.onImageSelected(finalPath);
                    checkedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                    break;
            }
        });
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
