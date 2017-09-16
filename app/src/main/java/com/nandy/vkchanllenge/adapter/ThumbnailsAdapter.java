package com.nandy.vkchanllenge.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
    private int scale = 3;


    public ThumbnailsAdapter(Background[] backgrounds) {
        this.backgrounds = backgrounds;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Background background = backgrounds[position];
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.thumbnailView.getLayoutParams();
        int margin = scale * 6;
        if (position == 0  ){
            layoutParams.setMargins(scale *16, margin, margin, margin);
        }else if (position == getItemCount() - 1){
            layoutParams.setMargins(margin, margin, scale *16, margin);

        }else {
            layoutParams.setMargins(margin, margin, margin, margin);
        }
        holder.thumbnailView.setLayoutParams(layoutParams);

        Bitmap bitmap;

        if (background.getType() != BackgroundType.ASSET) {
            Drawable drawable = ContextCompat.getDrawable(holder.thumbnailView.getContext(), background.getThumbnailId());
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                bitmap = Bitmap.createBitmap(scale *32, scale *32, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);



        } else {
            bitmap = BitmapFactory.decodeResource(holder.thumbnailView.getContext().getResources(), background.getThumbnailId());
        }


        if (position == checkedPosition){
            bitmap = transform(bitmap, true, scale *2, scale *4, scale *2, ContextCompat.getColor(holder.thumbnailView.getContext(), R.color.cornflower_blue_two));
        }else {
            bitmap = transform(bitmap, false, 0, scale *4, 0, ContextCompat.getColor(holder.thumbnailView.getContext(), R.color.cornflower_blue_two));

        }


        holder.thumbnailView.setImageBitmap(bitmap);
        holder.thumbnailView.setOnClickListener(view ->
        {
            onListItemClickListener.onListItemClick(background, holder.getAdapterPosition());
            checkedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });


    }


    private Bitmap transform(Bitmap source, boolean checked, int margin, int radius, int border, int color){

        int width = scale *32;
        int height = scale *32;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        float right = width - margin;
        float bottom = height - margin;

        if (checked){
            Paint strokePaint = new Paint();
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setColor(Color.WHITE);
            strokePaint.setStrokeWidth(border);

            canvas.drawRect(new RectF(margin*2, margin*2, width-margin*2, height-margin*2),  paint);

            canvas.drawRoundRect(new RectF(margin, margin, width-margin, height-margin), radius, radius, strokePaint);

            strokePaint.setColor(color);
            canvas.drawRoundRect(new RectF(margin/2, margin/2, width - margin/2, height - margin/2), radius, radius, strokePaint);

        }else {
            canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
        }
        return bitmap;
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
