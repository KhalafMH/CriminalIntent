package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private List<Crime> mCrimeList;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCrimeList = CrimeLab.getInstance(getContext()).getCrimes();
    }

    @Override
    public void onStart() {
        super.onStart();

        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new CrimeListAdapter(mCrimeList));

        return v;
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
                }
            });
        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
            getActivity().startActivity(intent);
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

        public CrimeListAdapter(List<Crime> crimeList) {
            mCrimes = crimeList;
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
