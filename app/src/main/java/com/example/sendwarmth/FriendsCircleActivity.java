package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.FriendsCircle;
import com.example.sendwarmth.presenter.FriendsCirclePresenter;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.TimeUtil;
import com.example.sendwarmth.util.Utility;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class FriendsCircleActivity extends AppCompatActivity
{
    private FriendsCircle friendsCircle;

    private FriendsCirclePresenter friendsCirclePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_circle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        friendsCirclePresenter = myComponent.friendsCirclePresenter();

        initFriendsCircle();
    }

    private void initFriendsCircle()
    {
        friendsCircle= (FriendsCircle) getIntent().getSerializableExtra("friendsCircle");
        ImageView picture = findViewById(R.id.picture);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        CircleImageView authorProfile = findViewById(R.id.author_profile);
        TextView author = findViewById(R.id.author);
        TextView time = findViewById(R.id.time);
        TextView description = findViewById(R.id.description);

        Glide.with(this).load(HttpUtil.getResourceURL(friendsCircle.getPicture())).into(picture);
        collapsingToolbarLayout.setTitle("");
        Glide.with(this).load(R.drawable.profile_uri).into(authorProfile);
        author.setText(friendsCircle.getCustomerInfo().getName());
        time.setText(TimeUtil.timeStampToString(friendsCircle.getTimestamp(),"yyyy-MM-dd HH:mm"));
        description.setText(friendsCircle.getContent());
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(friendsCirclePresenter.createMenu())
        {
            getMenuInflater().inflate(R.menu.delete_menu, menu);
        }
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
            case R.id.delete:
                friendsCirclePresenter.delete();
                break;
        }
        return true;
    }

    public FriendsCircle getFriendsCircle()
    {
        return friendsCircle;
    }

    public void setFriendsCircle(FriendsCircle friendsCircle)
    {
        this.friendsCircle = friendsCircle;
    }
}