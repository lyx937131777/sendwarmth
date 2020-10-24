package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.FriendsCircleAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.FriendsCircle;
import com.example.sendwarmth.presenter.FriendsCirclePresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FriendsCircleFragment extends Fragment
{
    private RecyclerView recyclerView;

    private List<FriendsCircle> friendsCircleList = new ArrayList<>();
    private FriendsCircleAdapter friendsCircleAdapter;

    private FriendsCirclePresenter friendsCirclePresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_friends_circle, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        friendsCirclePresenter = myComponent.friendsCirclePresenter();

        initFriendsCircle();
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        friendsCircleAdapter = new FriendsCircleAdapter(friendsCircleList);
        recyclerView.setAdapter(friendsCircleAdapter);
        return root;
    }

    private void initFriendsCircle()
    {
        friendsCircleList.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        friendsCirclePresenter.updateFriendsCircle(friendsCircleAdapter);
    }
}
