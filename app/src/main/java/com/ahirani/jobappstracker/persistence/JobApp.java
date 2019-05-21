package com.ahirani.jobappstracker.persistence;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "job_app")
public class JobApp {
    public static final String STATUS_APPLIED = "Applied";
    public static final String STATUS_INTERVIEW = "Interview";
    public static final String STATUS_REJECTED = "Rejected";
    public static final String STATUS_OFFER = "Offer";
    public static final String STATUS_SEEN = "Seen";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "job_id")
    private Long mJobId;

    @ColumnInfo(name = "status")
    private String mStatus;

    @ColumnInfo(name = "status_color")
    @ColorInt
    private int mStatusColor;

    @ColumnInfo(name = "company_name")
    private String mCompanyName;

    @ColumnInfo(name = "job_title")
    private String mJobTitle;

    @ColumnInfo(name = "location")
    private String mLocation;

    @ColumnInfo(name = "notes")
    private String mNotes;

    public JobApp(Long jobId, String companyName, String jobTitle, String location, String notes, String status) {
        mJobId = jobId;
        mCompanyName = companyName;
        mJobTitle = jobTitle;
        mLocation = location;
        mNotes = notes;
        mStatus = status;
        mStatusColor = assignStatusColor(status);
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public String getJobTitle() {
        return mJobTitle;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getNotes() {
        return mNotes;
    }

    public String getStatus() {
        return mStatus;
    }

    public int getStatusColor() {
        return mStatusColor;
    }

    public void setStatusColor(int statusColor) {
        mStatusColor = mStatusColor;
    }

    public Long getJobId() {
        return mJobId;
    }

    public void setJobId(Long jobId) {
        mJobId = mJobId;
    }

    private int assignStatusColor(String status) {
        switch (status) {
            case STATUS_APPLIED:
                return Color.parseColor("#78909c");
            case STATUS_INTERVIEW:
                return Color.parseColor("#42a5f5");
            case STATUS_REJECTED:
                return Color.parseColor("#ef5350");
            case STATUS_OFFER:
                return Color.parseColor("#66bb6a");
            case STATUS_SEEN:
            default:
                return Color.parseColor("#bdbdbd");
        }
    }
}
