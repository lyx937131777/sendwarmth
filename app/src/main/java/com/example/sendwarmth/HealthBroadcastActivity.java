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
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.Utility;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class HealthBroadcastActivity extends AppCompatActivity
{
    private HealthBroadcast healthBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_broadcast);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initHealthBroadcast();
    }

    private void initHealthBroadcast()
    {
        healthBroadcast = (HealthBroadcast) getIntent().getSerializableExtra("healthBroadcast");
        ImageView pictrue = findViewById(R.id.picture);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        CircleImageView authorProfile = findViewById(R.id.author_profile);
        TextView author = findViewById(R.id.author);
        TextView time = findViewById(R.id.time);
        TextView description = findViewById(R.id.description);

        Glide.with(this).load(HttpUtil.getResourceURL(healthBroadcast.getTopicPic())).into(pictrue);
        collapsingToolbarLayout.setTitle(healthBroadcast.getTopicName());
        Glide.with(this).load(R.drawable.profile_uri).into(authorProfile);
        author.setText("王专家");
        time.setText(Utility.timeStampToString(healthBroadcast.getTimestamp(),"yyyy-MM-dd HH:mm"));
        description.setText(healthBroadcast.getDes());
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