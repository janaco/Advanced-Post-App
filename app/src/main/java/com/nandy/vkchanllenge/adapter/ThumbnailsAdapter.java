package com.nandy.vkchanllenge.adapter;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nandy.vkchanllenge.OnListItemClickListener;
import com.nandy.vkchanllenge.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 07.09.17.
 */

public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.ViewHolder> {

    private List<Integer> thumbnailsResList;
    private OnListItemClickListener<Integer> onListItemClickListener;


    public ThumbnailsAdapter(List<Integer> thumbnailsResList) {
        this.thumbnailsResList = thumbnailsResList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumbnail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int drawableRes = thumbnailsResList.get(position);

        holder.thumbnailView.setImageResource(drawableRes);
        holder.thumbnailView.setOnClickListener(view ->
                onListItemClickListener.onListItemClick(drawableRes, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return thumbnailsResList.size();
    }

    public void setOnListItemClickListener(OnListItemClickListener<Integer> onListItemClickListener) {
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
