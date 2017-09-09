package com.nandy.vkchanllenge.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nandy.vkchanllenge.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yana on 09.09.17.
 */

public class StickersAdapter extends BaseAdapter {

    List<Bitmap> stickers;

    public StickersAdapter(List<Bitmap> stickers) {
        this.stickers = stickers;
    }

    @Override
    public int getCount() {
        return stickers.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return stickers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.viewSticker.setImageBitmap(getItem(position));

        return view;
    }

    static class ViewHolder{

        @BindView(R.id.sticker)
        ImageView viewSticker;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}