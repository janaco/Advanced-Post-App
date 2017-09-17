package com.nandy.vkchanllenge.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.ui.fragment.LoginFragment;
import com.nandy.vkchanllenge.ui.fragment.PostFragment;
import com.nandy.vkchanllenge.ui.fragment.SendingFragment;
import com.nandy.vkchanllenge.ui.model.PostModel;
import com.nandy.vkchanllenge.ui.presenter.PublishPresenter;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKSdk.wakeUpSession(this, new VKCallback<VKSdk.LoginState>() {
            @Override
            public void onResult(VKSdk.LoginState res) {
                    switch (res) {
                        case LoggedOut:
                            showLoginScreen();
                            break;
                        case LoggedIn:
                            showCreatePostScreen();
                            break;
                        case Pending:
                            break;
                        case Unknown:
                            break;
                    }
            }

            @Override
            public void onError(VKError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {

        PostFragment postFragment = (PostFragment)
                getSupportFragmentManager().findFragmentByTag(PostFragment.class.getSimpleName());

        if (postFragment != null && postFragment.onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (onVkLoginActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

        PostFragment postFragment = (PostFragment)
                getSupportFragmentManager().findFragmentByTag(PostFragment.class.getSimpleName());

        if (postFragment != null) {
            postFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!VKSdk.isLoggedIn()) {
            showLoginScreen();
        }
    }

    public void showCreatePostScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PostFragment.newInstance(this), PostFragment.class.getSimpleName())
                .commit();
    }

    private void showLoginScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment(), LoginFragment.class.getSimpleName())
                .commit();
    }

    private boolean onVkLoginActivityResult(int requestCode, int resultCode, Intent data) {

        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                showCreatePostScreen();
            }

            @Override
            public void onError(VKError error) {
            }
        });
    }
}
