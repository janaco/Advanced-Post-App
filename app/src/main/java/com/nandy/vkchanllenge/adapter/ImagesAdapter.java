package com.nandy.vkchanllenge.adapter;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nandy.vkchanllenge.R;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yana on 10.09.17.
 */

public class ImagesAdapter extends BaseAdapter {

    List<String> files;

    public ImagesAdapter(List<String> files) {
        this.files = files;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public String getItem(int position) {
        return files.get(position);
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        String path = getItem(position);
        holder.imageView.setImageBitmap(BitmapFactory.decodeFile(path));

        Glide
                .with(holder.imageView.getContext())
                .load(Uri.fromFile(new File(path)))
                .override(252, 252)
                .centerCrop()
                .into(holder.imageView);

        return view;
    }

    static class ViewHolder {

        @BindView(R.id.image)
        ImageView imageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
