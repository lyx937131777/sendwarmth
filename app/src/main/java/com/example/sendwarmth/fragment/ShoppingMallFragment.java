package com.example.sendwarmth.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sendwarmth.ProductOrderingActivity;
import com.example.sendwarmth.R;
import com.example.sendwarmth.SearchActivity;
import com.example.sendwarmth.adapter.ProductClassAdapter;
import com.example.sendwarmth.adapter.ShoppingCartAdapter;
import com.example.sendwarmth.adapter.TabAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.db.ProductClass;
import com.example.sendwarmth.presenter.ShoppingMallPresenter;

import org.litepal.LitePal;

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

    private Dialog dialog;
    private View shoppingCart;
    private TextView totalCount;
    private TextView totalPrice;
    private Button goToOrder;
    private ImageView searchButton;

    private RecyclerView shoppingCartRecycler;
    private ShoppingCartAdapter shoppingCartAdapter;

    private ShoppingMallPresenter shoppingMallPresenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_shopping_mall, container, false);
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(getContext())).build();
        shoppingMallPresenter = myComponent.shoppingMallPresenter();

        initDialog();

        initGoodsTypes();
        tabRecycler = root.findViewById(R.id.recycler_tab);
        tabRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        productClassRecycler = root.findViewById(R.id.recycler_product_class);
        productClassRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        tabAdapter = new TabAdapter(productClassList,productClassRecycler);
        tabRecycler.setAdapter(tabAdapter);

        productClassAdapter = new ProductClassAdapter(productClassList,shoppingMallPresenter);
        productClassRecycler.setAdapter(productClassAdapter);

        searchButton = root.findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

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

        shoppingCart = root.findViewById(R.id.shopping_cart);
        shoppingCart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(shoppingMallPresenter.getTotalCount() > 0){
                    dialog.show();
                }
            }
        });

        goToOrder = shoppingCart.findViewById(R.id.go_to_order);
        goToOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), ProductOrderingActivity.class);
                intent.putExtra("totalCount",shoppingMallPresenter.getTotalCount());
                intent.putExtra("totalPrice",shoppingMallPresenter.getTotalPrice());
                startActivity(intent);
            }
        });

        totalCount = shoppingCart.findViewById(R.id.total_count);
        totalPrice = shoppingCart.findViewById(R.id.total_price);

        shoppingMallPresenter.setShoppingCartAdapter(shoppingCartAdapter);
        shoppingMallPresenter.setProductClassAdapter(productClassAdapter);
        shoppingMallPresenter.setDialog(dialog);
        shoppingMallPresenter.setTotalCountText(totalCount);
        shoppingMallPresenter.setTotalPriceText(totalPrice);
        shoppingMallPresenter.setGoToOrder(goToOrder);
        shoppingMallPresenter.refresh();
        return root;
    }

    private void initDialog()
    {
        dialog = new Dialog(getContext(), R.style.AppTheme);
        View view = View.inflate(getContext(), R.layout.dialog_shopping_cart, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        //view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() *
        // 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.y = 278;
        dialogWindow.setAttributes(lp);
        shoppingCartRecycler = view.findViewById(R.id.recycler_shopping_cart);
        shoppingCartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Product> productList = LitePal.where("selectedCount > ?","0").find(Product.class);
        shoppingCartAdapter = new ShoppingCartAdapter(productList,shoppingMallPresenter);
        shoppingCartRecycler.setAdapter(shoppingCartAdapter);
    }

    private void initGoodsTypes()
    {
        productClassList.clear();
    }



    @Override
    public void onStart()
    {
        super.onStart();
        shoppingMallPresenter.refresh();
        shoppingMallPresenter.updateProductClass(productClassAdapter,tabAdapter,productClassList);
        shoppingMallPresenter.updateProduct(productClassAdapter);
    }
}
