package com.ahirani.jobappstracker.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahirani.jobappstracker.JobAppApplication;
import com.ahirani.jobappstracker.R;
import com.ahirani.jobappstracker.persistence.JobApp;
import com.ahirani.jobappstracker.persistence.JobAppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private CompositeDisposable mDisposables = new CompositeDisposable();
    List<JobApp> mJobApps;

    private FloatingActionButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.my_recycler_view);
        mButton = findViewById(R.id.fab);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mJobApps = new ArrayList<>();
        mAdapter = new AppsAdapter(mJobApps);
        recyclerView.setAdapter(mAdapter);

        final JobAppDatabase db = ((JobAppApplication) this.getApplication()).mDatabase;

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(this, mAdapter, db, mJobApps) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final JobApp temp = mJobApps.get(position);

                super.onSwiped(viewHolder, direction);

                View view = findViewById(R.id.main_layout);
                Snackbar snackbar = Snackbar.make(view, "Job App Deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mJobApps.add(position, temp);
                        mAdapter.notifyItemInserted(position);
                        mDisposables.add(db.jobAppDao().insert(temp)
                                .subscribeOn(Schedulers.io())
                                .subscribeWith(new DisposableSingleObserver<Long>() {
                                    @Override
                                    public void onSuccess(Long id) {
                                        temp.setJobId(id);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        // Do nothing
                                    }
                                }));
                    }
                });
                snackbar.show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mDisposables.add(db.jobAppDao().getAllJobApps()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<JobApp>>() {
                    @Override
                    public void onNext(List<JobApp> jobApps) {
                        Log.d("yolo", "onNext jobApps size = " + jobApps.size());
                        mJobApps.clear();
                        mJobApps.addAll(jobApps);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

        mDisposables.add(mAdapter.getPositionClicks().subscribe(new Consumer<JobApp>() {
            @Override
            public void accept(JobApp app) throws Exception {
                Toast.makeText(MainActivity.this, "Job Title: " + app.getJobTitle(), Toast.LENGTH_SHORT).show();
            }
        }));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                InputJobAppDialog dialogFragment = InputJobAppDialog.newInstance("Input New Application");
                dialogFragment.show(manager, "job_app_dialog_fragment");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.clear();
    }
}
