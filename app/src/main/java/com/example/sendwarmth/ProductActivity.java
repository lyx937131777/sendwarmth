package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Product;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.TimeUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ProductActivity extends AppCompatActivity
{
    private Product product;

    private ImageView picture;
    private TextView nameText,productClassText,priceText,businessText,brandText,descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();

        initProduct();
    }

    private void initProduct()
    {
        product = (Product) getIntent().getSerializableExtra("product");
        picture = findViewById(R.id.picture);
        nameText = findViewById(R.id.name);
        productClassText = findViewById(R.id.product_class);
        priceText = findViewById(R.id.price);
        businessText = findViewById(R.id.business);
        brandText = findViewById(R.id.brand);
        descriptionText = findViewById(R.id.description);

        Glide.with(this).load(HttpUtil.getResourceURL(product.getProductPic())).into(picture);
        nameText.setText(product.getProductName());
        productClassText.setText(product.getProductClassName());
        priceText.setText(product.getProductPrice() + "元");
        businessText.setText(product.getBusinessName());
        brandText.setText(product.getProductBrand());
        descriptionText.setText(product.getProductDes());
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