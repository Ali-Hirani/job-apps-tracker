package com.ahirani.jobappstracker.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahirani.jobappstracker.R;
import com.ahirani.jobappstracker.persistence.JobApp;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {
    private List<JobApp> mJobApps;
    private final PublishSubject<JobApp> onClickSubject = PublishSubject.create();

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        public TextView companyLabel;
        public TextView locationLabel;
        public TextView jobTitleLabel;
        public TextView appStatusLabel;
        public View statusBar;

        public AppViewHolder(View v) {
            super(v);

            companyLabel = v.findViewById(R.id.company_name_label);
            locationLabel = v.findViewById(R.id.location_label);
            jobTitleLabel = v.findViewById(R.id.job_title_label);
            appStatusLabel = v.findViewById(R.id.job_status);
            statusBar = v.findViewById(R.id.status_color_bar);
        }
    }


    public  AppsAdapter(List<JobApp> jobApps) {
        mJobApps = jobApps;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int i) {
        final JobApp jobApp = mJobApps.get(i);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(jobApp);
            }
        });

        holder.companyLabel.setText(mJobApps.get(i).getCompanyName());
        holder.locationLabel.setText(mJobApps.get(i).getLocation());
        holder.jobTitleLabel.setText(mJobApps.get(i).getJobTitle());
        holder.appStatusLabel.setText(mJobApps.get(i).getStatus());
        holder.statusBar.setBackgroundColor(mJobApps.get(i).getStatusColor());
    }

    @Override
    public int getItemCount() {
        return mJobApps.size();
    }

    public Observable<JobApp> getPositionClicks() {
        return onClickSubject;
    }
}
