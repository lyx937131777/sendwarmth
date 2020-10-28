package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.util.HttpUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class InterestringActivityActivity extends AppCompatActivity
{
    private InterestingActivity interestingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interestring_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initInterestingActivity();
    }

    private void initInterestingActivity()
    {
        interestingActivity = (InterestingActivity) getIntent().getSerializableExtra("interestingActivity");
        ImageView pictrue = findViewById(R.id.picture);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        CircleImageView authorProfile = findViewById(R.id.author_profile);
        TextView author = findViewById(R.id.author);
        TextView time = findViewById(R.id.time);
        TextView number = findViewById(R.id.number);
        TextView location = findViewById(R.id.location);
        TextView host = findViewById(R.id.host);
        TextView tel = findViewById(R.id.tel);
        TextView description = findViewById(R.id.description);

        Glide.with(this).load(HttpUtil.getResourceURL(interestingActivity.getImage())).into(pictrue);
        collapsingToolbarLayout.setTitle(interestingActivity.getTitle());
        Glide.with(this).load(R.drawable.profile_uri).into(authorProfile);
        author.setText(interestingActivity.getPromoterName());
        time.setText(interestingActivity.getTime());
        if(interestingActivity.getDescription().length()<100){      //预设内容的TextView为wrap_content，低于一定长度的文本显示于固定大小的文本框中
            ViewGroup.LayoutParams params = description.getLayoutParams();
            params.height = 500;
            description.setLayoutParams(params);
        }
        description.setText(interestingActivity.getDescription());
//        number.setText(interestingActivity.getMaxNum());
        location.setText(interestingActivity.getLocation());
//        host.setText(interestingActivity.getPromoter().getName());
//        tel.setText(interestingActivity.getContactTel());
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