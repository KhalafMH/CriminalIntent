package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;


public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";

    // model
    Crime mCrime;

    // reference fields
    EditText mTitleEditText;
    Button mDateButton;
    CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        CrimeFragment crimeFragment = new CrimeFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        crimeFragment.setArguments(args);

        return crimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        UUID crimeId = (UUID) args.getSerializable(ARG_CRIME_ID);
        if (crimeId != null) {
            mCrime = CrimeLab.getInstance(getContext()).getCrime(crimeId);
        } else {
            mCrime = new Crime("");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleEditText = (EditText) v.findViewById(R.id.crimeTitleEditText);
        mTitleEditText.setText(mCrime.getTitle());
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mDateButton = (Button) v.findViewById(R.id.dateButton);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.solvedCheckBox);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });


        return v;
    }
}
