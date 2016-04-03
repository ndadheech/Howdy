package com.ndadheech.android.howdy;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * Copyright Nibha Dadheech
 * Main activity which contains the fragment {@link HowdyFragment}
 * @author ndadheech
 */
public class HowdyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_howdy);
        FragmentManager fm = getSupportFragmentManager();

        if(savedInstanceState == null){
            fm.beginTransaction()
                .add(R.id.fragment_container, new HowdyFragment())
                .commit();
        } else {
            fm.findFragmentById(R.id.fragment_container);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
