package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.sendwarmth.adapter.ProductItemAdapter;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.ProductItem;
import com.example.sendwarmth.db.ProductOrder;
import com.example.sendwarmth.presenter.ProductOrderCommentPresenter;

import java.util.List;

public class ProductOrderCommentActivity extends AppCompatActivity
{
    private ProductOrder productOrder;

    private RecyclerView recyclerView;

    private EditText commentText;

    private ProductOrderCommentPresenter productOrderCommentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order_comment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        productOrderCommentPresenter = myComponent.productOrderCommentPresenter();

        productOrder = (ProductOrder) getIntent().getSerializableExtra("productOrder");

        commentText = findViewById(R.id.comment);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ProductItem> productItemList = productOrder.getProductItemInfos();
        ProductItemAdapter productItemAdapter = new ProductItemAdapter(productItemList);
        recyclerView.setAdapter(productItemAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.commit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.commit:
                productOrderCommentPresenter.commentProductOrder(productOrder.getInternetId(),commentText.getText().toString());
                break;
        }
        return true;
    }
}