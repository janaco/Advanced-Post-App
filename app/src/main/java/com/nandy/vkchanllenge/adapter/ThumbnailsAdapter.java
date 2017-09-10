package com.nandy.vkchanllenge.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nandy.vkchanllenge.CheckableImageButton;
import com.nandy.vkchanllenge.OnListItemClickListener;
import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.ui.Background;

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
        holder.thumbnailView.setImageResource(background.getThumbnailId());
        holder.thumbnailView.setOnClickListener(view ->
        {
            checkedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            onListItemClickListener.onListItemClick(background, holder.getAdapterPosition());
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
