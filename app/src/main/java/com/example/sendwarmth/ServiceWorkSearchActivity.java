package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sendwarmth.adapter.ServiceSubjectAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.presenter.ServiceWorkPresenter;

import java.util.ArrayList;
import java.util.List;

public class ServiceWorkSearchActivity extends AppCompatActivity {

    private List<ServiceSubject> serviceSubjectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ServiceSubjectAdapter serviceSubjectAdapter;

    private String keyword;

    private ServiceWorkPresenter serviceWorkPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_work_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        serviceWorkPresenter = myComponent.serviceWorkPresenter();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceSubjectAdapter = new ServiceSubjectAdapter(serviceSubjectList);
        recyclerView.setAdapter(serviceSubjectAdapter);

        keyword = getIntent().getStringExtra("keyword");
        serviceWorkPresenter.search(keyword,serviceSubjectAdapter);
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