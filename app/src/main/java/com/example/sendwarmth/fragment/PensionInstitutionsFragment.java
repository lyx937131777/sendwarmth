package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.PensionInstitutionAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.PensionInstitution;
import com.example.sendwarmth.presenter.PensionInstitutionPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PensionInstitutionsFragment extends Fragment
{
    private RecyclerView recyclerView;
    private List<PensionInstitution> pensionInstitutionList = new ArrayList<>();
    private PensionInstitutionAdapter pensionInstitutionAdapter;

    private PensionInstitutionPresenter pensionInstitutionPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_pension_institutions, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        pensionInstitutionPresenter = myComponent.pensionInstitutionPresenter();

        initPensionInstitutions();
        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pensionInstitutionAdapter = new PensionInstitutionAdapter(pensionInstitutionList);
        recyclerView.setAdapter(pensionInstitutionAdapter);
        return root;
    }

    private void initPensionInstitutions()
    {
        pensionInstitutionList.clear();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        pensionInstitutionPresenter.updatePensionInstitution(pensionInstitutionAdapter);
    }
}
