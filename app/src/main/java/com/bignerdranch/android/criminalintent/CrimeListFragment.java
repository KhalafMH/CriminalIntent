package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle_visible";

    private List<Crime> mCrimeList;
    private RecyclerView mRecyclerView;
    private CrimeListAdapter mAdapter;
    private RelativeLayout mAddCrimePlaceholderLayout;

    // state variables
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mCrimeList = CrimeLab.getInstance(getContext()).getCrimes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_crime_list, menu);

        MenuItem showSubtitle = menu.findItem(R.id.showSubtitleMenuItem);
        if (mSubtitleVisible) {
            showSubtitle.setTitle(R.string.menu_hide_subtitle);
        } else {
            showSubtitle.setTitle(R.string.menu_show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newCrimeMenuItem:
                createNewCrime();

                return true;

            case R.id.showSubtitleMenuItem:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE, false);
        }
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mAdapter = new CrimeListAdapter(mCrimeList);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mAddCrimePlaceholderLayout = (RelativeLayout) v.findViewById(R.id.addCrimeLayout);
        Button addCrimeButton = (Button) v.findViewById(R.id.addCrimeButton);
        addCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewCrime();
            }
        });

        updateUi();

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    private void updateUi() {
        mCrimeList = CrimeLab.getInstance(getContext()).getCrimes();

        mAdapter.setCrimes(mCrimeList);
        mAdapter.notifyDataSetChanged();
        updateSubtitle();
        if (mCrimeList.size() != 0) {
            mAddCrimePlaceholderLayout.setVisibility(View.GONE);
        } else {
            mAddCrimePlaceholderLayout.setVisibility(View.VISIBLE);
        }
    }

    private void updateSubtitle() {
        int count = mCrimeList.size();
        String subtitle = getString(R.string.subtitle_format, count);
        if (!mSubtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void createNewCrime() {
        Crime crime = new Crime();
        crime.setTitle("Crime #" + mCrimeList.size());
        CrimeLab.getInstance(getContext()).addCrime(crime);

        Intent intent = CrimePagerActivity.newIntent(getContext(), crime.getId());
        startActivity(intent);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View view) {
            super(view);

            view.setOnClickListener(this);

            mTitleTextView = (TextView) view.findViewById(R.id.titleTextView);
            mDateTextView = (TextView) view.findViewById(R.id.dateTextView);
            mSolvedCheckBox = (CheckBox) view.findViewById(R.id.solvedCheckBox);

            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mCrime.setSolved(b);
                    CrimeLab.getInstance(getContext()).updateCrime(mCrime);
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivity(intent);
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDate().toString());
            mSolvedCheckBox.setChecked(crime.isSolved());
        }
    }

    private class CrimeListAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeListAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View itemView = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            holder.bindCrime(mCrimes.get(position));
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
