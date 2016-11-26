package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        mFragment = fm.findFragmentById(R.id.fragmentContainer);
        if (mFragment == null) {
            mFragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, mFragment)
                    .commit();
        }
    }

    protected abstract Fragment createFragment();
}
