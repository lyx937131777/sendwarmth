package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
    private TextView descriptionText;
    private ImageView remarkImg;
    private CardView descriptionCard;

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
        descriptionCard = findViewById(R.id.description_card);
        descriptionText = findViewById(R.id.description);
        remarkImg = findViewById(R.id.remark_img);

        Glide.with(this).load(HttpUtil.getResourceURL(product.getProductPic())).into(picture);
        descriptionText.setText(product.getProductDes());

        if(product.getRemarkImg() != null){
            Glide.with(this).load(HttpUtil.getResourceURL(product.getRemarkImg())).into(remarkImg);
            descriptionCard.setVisibility(View.GONE);
        }

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