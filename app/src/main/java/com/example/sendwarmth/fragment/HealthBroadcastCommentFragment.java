package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.HealthBroadcastCommentAdapter;
import com.example.sendwarmth.adapter.HealthBroadcastSubCommentAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Comment;
import com.example.sendwarmth.presenter.HealthBroadcastCommentPresenter;
import com.example.sendwarmth.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class HealthBroadcastCommentFragment extends Fragment {
    private RecyclerView recyclerView;
    private String topicId;
    private String topicCreatorId;
    private List<Comment> healthBroadcastCommentList = new ArrayList<>();
    private List<HealthBroadcastSubCommentAdapter> healthBroadcastSubCommentAdapterList = new ArrayList<>();
    private HealthBroadcastCommentAdapter healthBroadcastCommentAdapter;
    private HealthBroadcastCommentPresenter healthBroadcastCommentPresenter;

    public HealthBroadcastCommentFragment(String topicId, String topicCreatorId){
        this.topicId = topicId;
        this.topicCreatorId = topicCreatorId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_health_broadcast_comment, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this.getContext())).build();
        healthBroadcastCommentPresenter = myComponent.healthBroadcastCommentPresenter();
        initHealthBroadcastComment();
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        healthBroadcastCommentAdapter = new HealthBroadcastCommentAdapter(healthBroadcastCommentList, healthBroadcastSubCommentAdapterList, topicCreatorId);
        recyclerView.setAdapter(healthBroadcastCommentAdapter);
        return root;
    }

    private void initHealthBroadcastComment()
    {
        healthBroadcastCommentList.clear();
        healthBroadcastSubCommentAdapterList.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        healthBroadcastCommentPresenter.updateHealthBroadcastComment(healthBroadcastCommentAdapter, topicId);
    }

    public HealthBroadcastCommentAdapter getHealthBroadcastCommentAdapter() {
        return healthBroadcastCommentAdapter;
    }

    public List<HealthBroadcastSubCommentAdapter> getHealthBroadcastSubCommentAdapterList() {
        return healthBroadcastSubCommentAdapterList;
    }

    public HealthBroadcastSubCommentAdapter findAdapterByCommentId(String commentId){
        int sub, len = healthBroadcastCommentAdapter.getmList().size();
        for (sub = 0; sub < len; sub++) {
            LogUtil.e("HealthBroadcastCommentFragment",healthBroadcastCommentAdapter.getmList().get(sub).getInternetId());
            if(healthBroadcastCommentAdapter.getmList().get(sub).getInternetId().equals(commentId)){
                break;
            }
        }
        if(sub != len){
            return healthBroadcastSubCommentAdapterList.get(sub);
        }
        return null;
    }
}
