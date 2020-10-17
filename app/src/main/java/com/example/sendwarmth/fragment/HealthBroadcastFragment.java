package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.HealthBroadcastAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.presenter.HealthBroadcastPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HealthBroadcastFragment extends Fragment
{
    private RecyclerView recyclerView;
    private List<HealthBroadcast> healthBroadcastList = new ArrayList<>();
    private HealthBroadcastAdapter healthBroadcastAdapter;

    private HealthBroadcastPresenter healthBroadcastPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_health_broadcast, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        healthBroadcastPresenter = myComponent.healthBroadcastPresenter();

        initHealthBroadcast();
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        healthBroadcastAdapter = new HealthBroadcastAdapter(healthBroadcastList);
        recyclerView.setAdapter(healthBroadcastAdapter);
        return root;
    }

    private void initHealthBroadcast()
    {
        healthBroadcastList.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        healthBroadcastPresenter.updateHealthBroadcast(healthBroadcastAdapter);
    }
}
