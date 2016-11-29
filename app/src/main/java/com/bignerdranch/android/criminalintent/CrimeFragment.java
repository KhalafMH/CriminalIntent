package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {
    private static final String TAG = CrimeFragment.class.getSimpleName();
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_SUSPECT = 1;
    private final CrimeLab mCrimeLab = CrimeLab.getInstance(getContext());

    // model
    private Crime mCrime;

    // reference fields
    private EditText mTitleEditText;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mSuspectButton;
    private Button mSendReportButton;

    public static CrimeFragment newInstance(UUID crimeId) {
        CrimeFragment crimeFragment = new CrimeFragment();

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        crimeFragment.setArguments(args);

        return crimeFragment;
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.getInstance(getContext()).updateCrime(mCrime);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteCrimeMenuItem:
                mCrimeLab.deleteCrime(mCrime.getId());
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        UUID crimeId = (UUID) args.getSerializable(ARG_CRIME_ID);
        if (crimeId != null) {
            mCrime = mCrimeLab.getCrime(crimeId);
        } else {
            mCrime = new Crime();
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
        updateDateButton();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(getFragmentManager(), DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.solvedCheckBox);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });

        final Intent pickContactIntent =
                new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.chooseSuspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContactIntent, REQUEST_SUSPECT);
            }
        });
        updateSuspectButton();
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mSendReportButton = (Button) v.findViewById(R.id.sendReportButton);
        mSendReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateString = DateFormat.format("EEE, MMM dd", mCrime.getDate()).toString();
                String solvedString = mCrime.isSolved() ?
                        getString(R.string.crime_report_solved) :
                        getString(R.string.crime_report_unsolved);
                String suspectString = mCrime.getSuspect().equals("") ?
                        getString(R.string.crime_report_no_suspect) :
                        getString(R.string.crime_report_suspect, mCrime.getSuspect());
                String report = getString(R.string.crime_report,
                        mCrime.getTitle(),
                        dateString,
                        solvedString,
                        suspectString
                );

                ShareCompat.IntentBuilder.from(getActivity())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setText(report)
                        .setType("text/plain")
                        .setChooserTitle(R.string.send_report)
                        .startChooser();
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_crime, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = DatePickerFragment.getResult(data);
            mCrime.setDate(date);
            if (BuildConfig.DEBUG) {
                if (mCrime.equals(mCrimeLab.getCrime(mCrime.getId()))) {
                    Log.e(TAG, "data update failed");
                }
            }
            updateDateButton();
        }

        if (requestCode == REQUEST_SUSPECT && data != null) {
            ContentResolver resolver = getActivity().getContentResolver();
            final String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = resolver.query(data.getData(), projection, null, null, null);
            if (cursor == null) {
                return;
            }
            try {
                if (cursor.getCount() == 0) {
                    return;
                }
                cursor.moveToFirst();
                mCrime.setSuspect(cursor.getString(0));
                updateSuspectButton();
            } finally {
                cursor.close();
            }
        }
    }

    private void updateDateButton() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private void updateSuspectButton() {
        if (!mCrime.getSuspect().equals("")) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
    }
}
