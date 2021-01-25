package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sendwarmth.adapter.InterestingActivityAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.presenter.InterestingActivityPresenter;

import java.util.ArrayList;
import java.util.List;

public class MyActivityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<InterestingActivity> interestingActivityList = new ArrayList<>();
    private List<InterestingActivity> joinedInterestingActivityList = new ArrayList<>();
    private InterestingActivityAdapter interestingActivityAdapter;

    private InterestingActivityPresenter interestingActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        interestingActivityPresenter = myComponent.interestingActivityPresenter();

        initInterestingActivities();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        interestingActivityAdapter = new InterestingActivityAdapter(interestingActivityList, joinedInterestingActivityList);
        recyclerView.setAdapter(interestingActivityAdapter);
    }

    private void initInterestingActivities()
    {
        interestingActivityList.clear();
        joinedInterestingActivityList.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        interestingActivityPresenter.updateMyActivity(interestingActivityAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}