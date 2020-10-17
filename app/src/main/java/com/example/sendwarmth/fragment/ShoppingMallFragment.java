package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.ProductClassAdapter;
import com.example.sendwarmth.adapter.TabAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.ProductClass;
import com.example.sendwarmth.presenter.ShoppingMallPresenter;
import com.example.sendwarmth.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingMallFragment extends Fragment
{
    private List<ProductClass> productClassList = new ArrayList<>();
    private RecyclerView tabRecycler;
    private TabAdapter tabAdapter;

    private RecyclerView productClassRecycler;
    private ProductClassAdapter productClassAdapter;

    private ShoppingMallPresenter shoppingMallPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_shopping_mall, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        shoppingMallPresenter = myComponent.shoppingMallPresenter();

        initGoodsTypes();
        tabRecycler = root.findViewById(R.id.recycler_tab);
        tabRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        productClassRecycler = root.findViewById(R.id.recycler_product_class);
        productClassRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        tabAdapter = new TabAdapter(productClassList,productClassRecycler);
        tabRecycler.setAdapter(tabAdapter);

        productClassAdapter = new ProductClassAdapter(productClassList);
        productClassRecycler.setAdapter(productClassAdapter);


        productClassRecycler.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if(dy == 0){
                    return;
                }
                int position = ((LinearLayoutManager)(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                tabAdapter.setSelectedName(productClassList.get(position).getName());
                tabAdapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    private void initGoodsTypes()
    {
        productClassList.clear();
    }



    @Override
    public void onStart()
    {
        super.onStart();
        shoppingMallPresenter.updateProductClass(productClassAdapter,tabAdapter,productClassList);
        shoppingMallPresenter.updateProduct(productClassAdapter);
    }
}
