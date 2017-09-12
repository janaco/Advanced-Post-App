package com.nandy.vkchanllenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nandy.vkchanllenge.ui.PostFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PostFragment.newInstance(this), PostFragment.class.getSimpleName())
                .commit();
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
        super.onActivityResult(requestCode, resultCode, data);

        PostFragment postFragment = (PostFragment)
                getSupportFragmentManager().findFragmentByTag(PostFragment.class.getSimpleName());

        if (postFragment != null) {
            postFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
