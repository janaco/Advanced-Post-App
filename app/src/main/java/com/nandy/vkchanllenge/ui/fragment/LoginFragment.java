package com.nandy.vkchanllenge.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.util.ConnectionUtils;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yana on 15.09.17.
 */

public class LoginFragment  extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.login)
    void onLoginButtonClick(){
        if (ConnectionUtils.isInternetConnection(getContext())){
            Toast.makeText(getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        VKSdk.login(getActivity(), VKScope.WALL);
    }


    }

