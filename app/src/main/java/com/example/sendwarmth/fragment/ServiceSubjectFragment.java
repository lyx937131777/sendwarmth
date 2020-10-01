package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.OrderAdapter;
import com.example.sendwarmth.adapter.ServiceSubjectAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.ServiceSubject;
import com.example.sendwarmth.presenter.ServiceSubjectPresenter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ServiceSubjectFragment extends Fragment
{
    private static final String ARG_SECTION_NAME = "section_name";
    private String serviceClassName;

    private RecyclerView recyclerView;
    private List<ServiceSubject> serviceSubjectList = new ArrayList<>();
    private ServiceSubjectAdapter serviceSubjectAdapter;

    private ServiceSubjectPresenter serviceSubjectPresenter;

    public static ServiceSubjectFragment newInstance(String serviceClassName)
    {
        ServiceSubjectFragment serviceSubjectFragment = new ServiceSubjectFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SECTION_NAME,serviceClassName);
        serviceSubjectFragment.setArguments(bundle);
        return serviceSubjectFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            serviceClassName = getArguments().getString(ARG_SECTION_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_service_subject, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        serviceSubjectPresenter = myComponent.serviceSubjectPresenter();

        initServiceSubject();
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        serviceSubjectAdapter = new ServiceSubjectAdapter(serviceSubjectList);
        recyclerView.setAdapter(serviceSubjectAdapter);

        return root;
    }

    private void initServiceSubject()
    {
        serviceSubjectList.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        serviceSubjectPresenter.updateServiceSubject(serviceSubjectAdapter,serviceClassName);
    }
}
