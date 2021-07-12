package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sendwarmth.adapter.ProductAdapter;
import com.example.sendwarmth.adapter.ServiceSubjectAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.presenter.ShoppingMallPresenter;

import java.util.ArrayList;
import java.util.List;

public class ProductSearchActivity extends AppCompatActivity {

    private List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private RecyclerView recyclerView;
    private String keyword;

    private ShoppingMallPresenter shoppingMallPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        shoppingMallPresenter = myComponent.shoppingMallPresenter();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList,shoppingMallPresenter);
        recyclerView.setAdapter(productAdapter);

        keyword = getIntent().getStringExtra("keyword");
        shoppingMallPresenter.search(keyword,productAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}