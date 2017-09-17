package com.nandy.vkchanllenge.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nandy.vkchanllenge.OnListItemClickListener;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.imagetransformation.RoundedCornersTransformation;
import com.nandy.vkchanllenge.util.WindowUtils;
import com.nandy.vkchanllenge.model.Background;
import com.nandy.vkchanllenge.model.BackgroundType;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yana on 07.09.17.
 */

public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.ViewHolder> {

    private final Background[] backgrounds;
    private OnListItemClickListener<Background> onListItemClickListener;

    private int checkedPosition = 0;

    private final int innerMargin;
    private final int outerMargin;
    private final int imageSize;
    private final int cornerRadius;
    private final int borderWidth;
    private final int borderMargin;
    private final int borderColor;

    public ThumbnailsAdapter(Context context, Background[] backgrounds) {
        this.backgrounds = backgrounds;

        int density = (int) WindowUtils.getDensity(context);

        innerMargin = density * 6;
        outerMargin = density * 16;
        imageSize = density * 32;
        cornerRadius = 4 * density;
        borderWidth = 2 * density;
        borderMargin = 2 * density;
        borderColor = ContextCompat.getColor(context, R.color.cornflower_blue_two);

    }

    public void setSelected(Background selected) {
        for (int i = 0; i < backgrounds.length; i++) {
            if (backgrounds[i] == selected) {
                checkedPosition = i;
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Background background = backgrounds[position];
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.thumbnailView.getLayoutParams();
        if (position == 0) {
            layoutParams.setMargins(outerMargin, innerMargin, innerMargin, innerMargin);
        } else if (position == getItemCount() - 1) {
            layoutParams.setMargins(innerMargin, innerMargin, outerMargin, innerMargin);

        } else {
            layoutParams.setMargins(innerMargin, innerMargin, innerMargin, innerMargin);
        }
        holder.thumbnailView.setLayoutParams(layoutParams);

        Bitmap bitmap;

        if (background.getType() != BackgroundType.ASSET) {
            Drawable drawable = ContextCompat.getDrawable(holder.thumbnailView.getContext(), background.getThumbnailId());
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);


        } else {
            bitmap = BitmapFactory.decodeResource(holder.thumbnailView.getContext().getResources(), background.getThumbnailId());
        }


        if (position == checkedPosition) {
            bitmap = RoundedCornersTransformation.transform(bitmap, imageSize, true, cornerRadius, borderWidth, borderMargin, borderColor);
        } else {
            bitmap = RoundedCornersTransformation.transform(bitmap, imageSize, false, cornerRadius, 0, 0, borderColor);
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
        ImageView thumbnailView;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
