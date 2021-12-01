package com.example.sendwarmth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sendwarmth.R;
import com.example.sendwarmth.adapter.OrderAdapter;
import com.example.sendwarmth.adapter.ProductOrderAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Order;
import com.example.sendwarmth.db.ProductOrder;
import com.example.sendwarmth.presenter.OrderPresenter;
import com.example.sendwarmth.presenter.ProductOrderPresenter;
import com.example.sendwarmth.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductOrderFragment extends Fragment
{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int index;

    private RecyclerView recyclerView;
    private List<ProductOrder> productOrderList = new ArrayList<>();
    private ProductOrderAdapter productOrderAdapter;

    private ProductOrderPresenter productOrderPresenter;

    public static ProductOrderFragment newInstance(int index)
    {
        ProductOrderFragment productOrderFragment = new ProductOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER,index);
        productOrderFragment.setArguments(bundle);
        return productOrderFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        index = 0;
        if (getArguments() != null)
        {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_product_order, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        productOrderPresenter = myComponent.productOrderPresenter();
        productOrderPresenter.setProductOrderFragment(this);

        recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productOrderAdapter = new ProductOrderAdapter(productOrderList,productOrderPresenter);
        recyclerView.setAdapter(productOrderAdapter);
        return root;
    }



    public void refresh(){
        LogUtil.e("ProductOrderFragment", "refresh!!");
        dismissDialog();
        productOrderPresenter.updateOrderList(productOrderAdapter,getTypes());
    }

    public void dismissDialog() {
        if(productOrderAdapter != null){
            productOrderAdapter.dismissDialog();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        productOrderPresenter.updateOrderList(productOrderAdapter,getTypes());
    }

//    private String getType(){
//        switch (index){
//            case 0:
//                return "all";
//            case 1:
//                return "toBeAccept";
//            case 2:
//                return "Accepted";
//            case 3:
//                return "toBeEvaluated";
//            case 4:
//                return "closed";
//            default:
//                return "unknown";
//        }
//    }

    private String[] getTypes(){
        switch (index){
            case 0:
                return new String[]{"un_paid","paid","delivered","received","cancel","evaluated"};
            case 1:
                return new String[]{"un_paid"};
            case 2:
                return new String[]{"paid","delivered"};
            case 3:
                return new String[]{"received"};
            case 4:
                return new String[]{"cancel","evaluated","refunded","refund_request"};
            default:
                return new String[]{};
        }
    }

}
