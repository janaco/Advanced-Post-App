package com.nandy.vkchanllenge.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nandy.vkchanllenge.acitivity.MainActivity;
import com.nandy.vkchanllenge.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 15.09.17.
 */

public class ResultFragment extends Fragment {

    private static final String KEY_SUCCESS = "success";
    @BindView(R.id.image)
    ImageView statusIcon;
    @BindView(R.id.message)
    TextView statusMessage;

    private boolean success;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        success = getArguments().getBoolean(KEY_SUCCESS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        statusMessage.setText(success ? R.string.published : R.string.failed_to_publish);
    }

    @OnClick(R.id.create)
    void onCreateNewButtonClick(){
        ((MainActivity) getActivity()).showCreatePostScreen();
    }

    public static ResultFragment newInstance(boolean success){

        Bundle args = new Bundle();
        args.putBoolean(KEY_SUCCESS, success);

        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
