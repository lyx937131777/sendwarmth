package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.InterestingActivityAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.presenter.InterestingActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InterestingActivityFragment extends Fragment
{
    private RecyclerView recyclerView;
    private List<InterestingActivity> interestingActivityList = new ArrayList<>();
    private List<InterestingActivity> joinedInterestingActivityList = new ArrayList<>();
    private InterestingActivityAdapter interestingActivityAdapter;

    private InterestingActivityPresenter interestingActivityPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_interesting_activity, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        interestingActivityPresenter = myComponent.interestingActivityPresenter();

        initInterestingActivities();
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        interestingActivityAdapter = new InterestingActivityAdapter(interestingActivityList, joinedInterestingActivityList);
        recyclerView.setAdapter(interestingActivityAdapter);
        return root;
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
        interestingActivityPresenter.updateInterestingActivity(interestingActivityAdapter);
    }
}
