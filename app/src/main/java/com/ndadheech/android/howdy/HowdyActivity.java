package com.ndadheech.android.howdy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Copyright Nibha Dadheech
 * Main activity which contains the fragment {@link HowdyFragment}
 * @author ndadheech
 */
public class HowdyActivity extends SingleFragmentActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected Fragment createFragment() {
        return new HowdyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        JodaTimeAndroid.init(getApplicationContext());
    }
}
