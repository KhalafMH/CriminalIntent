package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

// TODO fix bug where clicking the solved checkbox on the master pane doesn't update the detail pane.

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    protected Fragment mDetailFragment = null;
    private FragmentManager mFragmentManager;

    @Override
    public void onCrimeDeleted(Crime crime) {
        CrimeLab.getInstance(this).deleteCrime(crime.getId());
        ((CrimeListFragment) mFragment).updateUi();
        mFragmentManager.beginTransaction()
                .remove(mDetailFragment)
                .commit();
        mDetailFragment = null;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeLab.getInstance(this).updateCrime(crime);
        ((CrimeListFragment) mFragment).updateUi();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detailFragmentContainer) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            mDetailFragment = CrimeFragment.newInstance(crime.getId());
            mFragmentManager.beginTransaction()
                    .replace(R.id.detailFragmentContainer, mDetailFragment)
                    .commit();
        }
        ((CrimeListFragment) mFragment).updateUi();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
