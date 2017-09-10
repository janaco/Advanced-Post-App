package com.nandy.vkchanllenge.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.FutureTarget;
import com.nandy.vkchanllenge.CheckableImageButton;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.RoundedCornersTransformation;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yana on 10.09.17.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private List<String> files;
    private int checkedPosition = 0;

    public ImagesAdapter(List<String> files) {
        this.files = files;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = files.get(position);

//        holder.imageView.setChecked(checkedPosition == position);


        Glide
                .with(holder.imageView.getContext())
                .load(Uri.fromFile(new File(path)))
                .override(252, 252)
                .bitmapTransform(new CenterCrop(holder.imageView.getContext()),
                        new RoundedCornersTransformation(holder.imageView.getContext(), 12, 2))
                .into(holder.imageView);

        holder.imageView.setOnClickListener(view -> {
            Log.d("IMAGE_", "onClick: " + holder.getAdapterPosition());
            checkedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return files.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
