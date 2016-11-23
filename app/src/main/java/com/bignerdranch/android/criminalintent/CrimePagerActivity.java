package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends FragmentActivity {

    private static final String EXTRA_CRIME_ID =
            CrimePagerActivity.class.getPackage().getName() + ".crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimeList;

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCrimeList = CrimeLab.getInstance(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.newInstance(mCrimeList.get(position).getId());
            }

            @Override
            public int getCount() {
                return mCrimeList.size();
            }
        });

        UUID selectedCrimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        if (selectedCrimeId != null) {
            for (int i = 0; i < mCrimeList.size(); ++i) {
                if (mCrimeList.get(i).getId().equals(selectedCrimeId)) {
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }
}
