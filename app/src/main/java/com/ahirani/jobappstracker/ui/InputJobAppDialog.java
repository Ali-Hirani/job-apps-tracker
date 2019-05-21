package com.ahirani.jobappstracker.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.ahirani.jobappstracker.JobAppApplication;
import com.ahirani.jobappstracker.R;
import com.ahirani.jobappstracker.persistence.JobApp;
import com.ahirani.jobappstracker.persistence.JobAppDatabase;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class InputJobAppDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    EditText mJobTitleInput;
    EditText mCompanyNameInput;
    EditText mLocationInput;
    Spinner mJobStatusSpinner;
    String mCurStatus = JobApp.STATUS_SEEN;
    CompositeDisposable mCompositeDisposable;

    // Empty constructor is required for DialogFragment
    public InputJobAppDialog() {
    }

    public static InputJobAppDialog newInstance(String title) {
        InputJobAppDialog frag = new InputJobAppDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_input_job_app_dialog, null);

        mJobTitleInput = view.findViewById(R.id.job_title_input);
        mCompanyNameInput = view.findViewById(R.id.company_name_input);
        mLocationInput = view.findViewById(R.id.location_input);

        mJobStatusSpinner = view.findViewById(R.id.job_status_spinner);
        mJobStatusSpinner.setAdapter(getSpinnerArrayAdapter(view));
        mJobStatusSpinner.setOnItemSelectedListener(this);

        mCompositeDisposable = new CompositeDisposable();

        builder.setView(view)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String jobTitle = mJobTitleInput.getText().toString();
                        String companyName = mCompanyNameInput.getText().toString();
                        String location = mLocationInput.getText().toString();
                        final JobApp jobApp = new JobApp(null, companyName, jobTitle, location, "", mCurStatus);

                        JobAppDatabase db = ((JobAppApplication) getActivity().getApplication()).mDatabase;

                        mCompositeDisposable.add(db.jobAppDao().insert(jobApp)
                                .subscribeOn(Schedulers.io())
                                .subscribeWith(new DisposableSingleObserver<Long>() {
                                    @Override
                                    public void onSuccess(Long id) {
                                        jobApp.setJobId(id);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        // Do nothing
                                    }
                                }));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    private ArrayAdapter<CharSequence> getSpinnerArrayAdapter(View view) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.app_status_array,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mCurStatus = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
