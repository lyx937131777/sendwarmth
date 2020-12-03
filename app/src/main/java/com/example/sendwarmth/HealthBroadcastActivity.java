package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import de.hdodenhof.circleimageview.CircleImageView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.example.sendwarmth.presenter.HealthBroadcastSubCommentPresenter;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.TimeUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class HealthBroadcastActivity extends AppCompatActivity
{
    private HealthBroadcast healthBroadcast;
    private HealthBroadcastCommentPresenter healthBroadcastCommentPresenter;
    private HealthBroadcastSubCommentPresenter healthBroadcastSubCommentPresenter;
    private HealthBroadcastCommentFragment healthBroadcastCommentFragment;
    private SharedPreferences pref;

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
        healthBroadcastSubCommentPresenter = myComponent.healthBroadcastSubCommentPresenter();
        initHealthBroadcast();
    }

    private void initHealthBroadcast()
    {
        healthBroadcast = (HealthBroadcast) getIntent().getSerializableExtra("healthBroadcast");
        ImageView picture = findViewById(R.id.picture);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        TextView title = findViewById(R.id.title);
        CircleImageView authorProfile = findViewById(R.id.author_profile);
        TextView author = findViewById(R.id.profile);
        TextView time = findViewById(R.id.time);
        TextView description = findViewById(R.id.description);
        final EditText comment_content = findViewById(R.id.comment_content);
        TextView release_button = findViewById(R.id.release_button);
        Glide.with(this).load(HttpUtil.getResourceURL(healthBroadcast.getTopicPic())).into(picture);
        collapsingToolbarLayout.setTitle(" ");
        title.setText(healthBroadcast.getTopicName());
        Glide.with(this).load(R.drawable.profile_uri).into(authorProfile);
        if(healthBroadcast.getCreatorInfo() != null && healthBroadcast.getCreatorInfo().getCustomerInfo()!=null)
            author.setText(healthBroadcast.getCreatorInfo().getCustomerInfo().getUserNameWithRole());
        time.setText(TimeUtil.timeStampToString(healthBroadcast.getTimestamp(),"yyyy-MM-dd HH:mm"));
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = pref.edit();
        editor.remove("commentId");
        editor.apply();

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
            comment_content.setText(pref.getString("comment_draft_"+healthBroadcast.getInternetId(),""));
            if(healthBroadcast.getCreatorInfo()!=null)
                healthBroadcastCommentFragment=new HealthBroadcastCommentFragment(healthBroadcast.getInternetId(), healthBroadcast.getCreatorInfo().getInternetId());
            transaction.replace(R.id.fragment_health_broadcast_comment, healthBroadcastCommentFragment);
        }
        transaction.commit();

        release_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                                            String commentId = pref.getString("commentId","");
                                            if(commentId.equals("")){
                                                healthBroadcastCommentPresenter.putHealthBroadcastComment(healthBroadcastCommentFragment.getHealthBroadcastCommentAdapter(),comment_content.getText().toString(),healthBroadcast.getInternetId());
                                            } else{
                                                healthBroadcastSubCommentPresenter.putHealthBroadcastSubComment(healthBroadcastCommentFragment.findAdapterByCommentId(commentId),comment_content.getText().toString(),commentId);
                                            }

                                        }
                                    })
                            .setNegativeButton("取消",null).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("comment_draft_"+healthBroadcast.getInternetId(), ((EditText)findViewById(R.id.comment_content)).getText().toString());
        editor.apply();
        LogUtil.e("HealthBroadcastActivity","comment draft is " + pref.getString("comment_draft"+healthBroadcast.getInternetId(),""));
    }

    @Override
    public void onBackPressed() {
        String commentId = pref.getString("commentId", "");
        if(!commentId.equals("")){
            EditText comment_content = findViewById(R.id.comment_content);
            comment_content.setHint("");
            SharedPreferences.Editor editor = pref.edit();
            editor.remove("commentId");
            editor.apply();
        } else{
            super.onBackPressed();
        }
    }
}