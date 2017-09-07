package com.nandy.vkchanllenge.ui.model;

import com.nandy.vkchanllenge.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yana on 07.09.17.
 */

public class ThumnailsListModel {

    public List<Integer> getThumbnails(){

        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.thumb_beach);
        list.add(R.mipmap.thumb_stars);
        list.add(R.mipmap.thumb_beach);
        list.add(R.mipmap.thumb_stars);
        list.add(R.mipmap.thumb_beach);
        list.add(R.mipmap.thumb_stars);

        return list;
    }
}
