package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.HealthBroadcastAdapter;
import com.example.sendwarmth.adapter.HealthBroadcastCommentAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Comment;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.presenter.HealthBroadcastCommentPresenter;
import com.example.sendwarmth.presenter.HealthBroadcastPresenter;
import com.example.sendwarmth.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class HealthBroadcastCommentFragment extends Fragment {
    private RecyclerView recyclerView;
    private String topicId;
    private List<Comment> healthBroadcastCommentList = new ArrayList<>();
    private HealthBroadcastCommentAdapter healthBroadcastCommentAdapter;
    private HealthBroadcastCommentPresenter healthBroadcastCommentPresenter;

    public HealthBroadcastCommentFragment(String topicId){
        this.topicId = topicId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_health_broadcast_comment, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this.getContext())).build();
        healthBroadcastCommentPresenter = myComponent.healthBroadcastCommentPresenter();
        initHealthBroadcastComment();
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        healthBroadcastCommentAdapter = new HealthBroadcastCommentAdapter(healthBroadcastCommentList);
        recyclerView.setAdapter(healthBroadcastCommentAdapter);
        return root;
    }

    private void initHealthBroadcastComment()
    {
        healthBroadcastCommentList.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        healthBroadcastCommentPresenter.updateHealthBroadcastComment(healthBroadcastCommentAdapter,topicId);
        LogUtil.e("HealthBroadcastCommentFragment",String.valueOf(healthBroadcastCommentAdapter.getItemCount()));
    }

    public boolean checkCommentNotNull(){
        return healthBroadcastCommentAdapter.getItemCount()>0;
    }
}
