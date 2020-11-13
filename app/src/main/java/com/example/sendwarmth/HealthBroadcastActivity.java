package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import de.hdodenhof.circleimageview.CircleImageView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.fragment.HealthBroadcastCommentFragment;
import com.example.sendwarmth.presenter.HealthBroadcastCommentPresenter;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.TimeUtil;
import com.example.sendwarmth.util.Utility;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class HealthBroadcastActivity extends AppCompatActivity
{
    private HealthBroadcast healthBroadcast;
    private HealthBroadcastCommentPresenter healthBroadcastCommentPresenter;
    private HealthBroadcastCommentFragment healthBroadcastCommentFragment;
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
        MyComponent myComponent=DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        healthBroadcastCommentPresenter = myComponent.healthBroadcastCommentPresenter();
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
        final EditText comment_content = findViewById(R.id.comment_content);
        TextView release_button = findViewById(R.id.release_button);
        Glide.with(this).load(HttpUtil.getResourceURL(healthBroadcast.getTopicPic())).into(pictrue);
        collapsingToolbarLayout.setTitle(healthBroadcast.getTopicName());
        Glide.with(this).load(R.drawable.profile_uri).into(authorProfile);
        author.setText("王专家");
        time.setText(TimeUtil.timeStampToString(healthBroadcast.getTimestamp(),"yyyy-MM-dd HH:mm"));

        if(healthBroadcast.getDes().length()<100){      //预设内容的TextView为wrap_content，低于一定长度的文本显示于固定大小的文本框中
            ViewGroup.LayoutParams params = description.getLayoutParams();
            params.height = 300;
            description.setLayoutParams(params);
        }

        description.setText(healthBroadcast.getDes());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //topicId在HealthBroadcastActivity中从Intent中获取后，才能传值给HealthBroadcastCommentFragment生成所需Fragment
        if(healthBroadcast.getInternetId()!=null){
            LogUtil.e("HealthBroadcastActivity",healthBroadcast.getInternetId());
            healthBroadcastCommentFragment=new HealthBroadcastCommentFragment(healthBroadcast.getInternetId());
            transaction.replace(R.id.fragment_health_broadcast_comment, healthBroadcastCommentFragment);
        }
        transaction.commit();

        release_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment_content.getText().length()<6){
                    new AlertDialog.Builder(HealthBroadcastActivity.this)
                            .setTitle("提示")
                            .setMessage("留言至少为6个字符")
                            .setPositiveButton("确定",null)
                            .show();
                } else{
                    new AlertDialog.Builder(HealthBroadcastActivity.this)
                            .setTitle("提示")
                            .setMessage("确定发布该评论么？")
                            .setPositiveButton("确定", new
                                    DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            LogUtil.e("HealthBroadcastActivity",comment_content.getText().toString());
                                            healthBroadcastCommentPresenter.putHealthBroadcastComment(healthBroadcastCommentFragment.getHealthBroadcastCommentAdapter(),comment_content.getText().toString(),healthBroadcast.getInternetId());
                                        }
                                    })
                            .setNegativeButton("取消",null).show();
                }
            }
        });
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