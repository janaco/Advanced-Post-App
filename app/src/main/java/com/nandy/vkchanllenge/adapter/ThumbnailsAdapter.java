package com.nandy.vkchanllenge.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nandy.vkchanllenge.CheckableImageButton;
import com.nandy.vkchanllenge.OnListItemClickListener;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.ui.Background;
import com.nandy.vkchanllenge.ui.BackgroundType;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yana on 07.09.17.
 */

public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.ViewHolder> {

    private Background[] backgrounds;
    private OnListItemClickListener<Background> onListItemClickListener;

    private int checkedPosition = 0;


    public ThumbnailsAdapter(Background[] backgrounds) {
        this.backgrounds = backgrounds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Background background = backgrounds[position];

        holder.thumbnailView.setChecked(position == checkedPosition);

        Bitmap bitmap;

        if (background.getType() != BackgroundType.ASSET) {
            Drawable drawable = ContextCompat.getDrawable(holder.thumbnailView.getContext(), background.getThumbnailId());
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

        } else {
            bitmap = BitmapFactory.decodeResource(holder.thumbnailView.getContext().getResources(), background.getThumbnailId());
        }

        holder.thumbnailView.setImageBitmap(bitmap);
        holder.thumbnailView.setOnClickListener(view ->
        {
            onListItemClickListener.onListItemClick(background, holder.getAdapterPosition());
            checkedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });


    }


    @Override
    public int getItemCount() {
        return backgrounds.length;
    }

    public void setOnListItemClickListener(OnListItemClickListener<Background> onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail_view)
        CheckableImageButton thumbnailView;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
