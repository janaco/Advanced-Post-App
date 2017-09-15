package com.nandy.vkchanllenge.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nandy.vkchanllenge.MainActivity;
import com.nandy.vkchanllenge.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 15.09.17.
 */

public class ResultFragment extends Fragment {

    @BindView(R.id.image)
    ImageView statusIcon;
    @BindView(R.id.message)
    TextView statusMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.create)
    void onCreateNewButtonClick(){
        ((MainActivity) getActivity()).showCreatePostScreen();
    }
}
