package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sendwarmth.adapter.ProductOrderingAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.presenter.ProductOrderingPresenter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ProductOrderingActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private List<Product> productList = new ArrayList<>();
    private ProductOrderingAdapter productOrderingAdapter;

    private EditText nameText, telText, addressText;
    private Button location;

    private TextView totalCountText, totalPriceText;
    private int totalCount;
    private double totalPrice;
    private Button order;

    private SharedPreferences pref;
    private String credential;
    private Customer customer;

    private ProductOrderingPresenter productOrderingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_ordering);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        productOrderingPresenter = myComponent.productOrderingPresenter();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        credential = pref.getString("credential","");
        customer = LitePal.where("credential = ?",credential).findFirst(Customer.class);

        nameText = findViewById(R.id.name);
        telText = findViewById(R.id.tel);
        addressText = findViewById(R.id.address);
        nameText.setText(customer.getName());
        telText.setText(customer.getTel());
        addressText.setText(customer.getAddress());

        location = findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

            }
        });

        initProducts();
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productOrderingAdapter = new ProductOrderingAdapter(productList);
        recyclerView.setAdapter(productOrderingAdapter);

        totalCountText = findViewById(R.id.total_count);
        totalPriceText = findViewById(R.id.total_price);
        totalCount = getIntent().getIntExtra("totalCount",0);
        totalPrice = getIntent().getDoubleExtra("totalPrice",0);
        totalCountText.setText("共"+totalCount+"件，合计：");
        totalPriceText.setText("￥"+totalPrice);

        order = findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                productOrderingPresenter.postShoppingCart(nameText.getText().toString(),telText.getText().toString(),addressText.getText().toString());
            }
        });
    }

    private void initProducts()
    {
        productList = LitePal.where("selectedCount > ?","0").find(Product.class);
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