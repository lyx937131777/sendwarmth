package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.presenter.InterestingActivityPresenter;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class InterestringActivityActivity extends AppCompatActivity
{
    private InterestingActivity interestingActivity;
    private InterestingActivityPresenter interestingActivityPresenter;
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
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(InterestringActivityActivity.this)).build();
        interestingActivityPresenter = myComponent.interestingActivityPresenter();
        initInterestingActivity();
    }

    private void initInterestingActivity()
    {
        interestingActivity = (InterestingActivity) getIntent().getSerializableExtra("interestingActivity");
        ImageView pictrue = findViewById(R.id.picture);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        CircleImageView authorProfile = findViewById(R.id.author_profile);
        TextView title = findViewById(R.id.title);
        TextView author = findViewById(R.id.profile);
        TextView time = findViewById(R.id.time);
        TextView number = findViewById(R.id.number);
        TextView location = findViewById(R.id.location);
        TextView host = findViewById(R.id.host);
        TextView tel = findViewById(R.id.tel);
        TextView description = findViewById(R.id.description);
        Button addButton = findViewById(R.id.button);

        Glide.with(this).load(HttpUtil.getResourceURL(interestingActivity.getImage())).into(pictrue);
        collapsingToolbarLayout.setTitle(" ");
        title.setText(interestingActivity.getTitle());

        Glide.with(this).load(R.drawable.profile_uri).into(authorProfile);
        if(interestingActivity.getTime()!=null){
            time.setText(interestingActivity.getTime());
            LogUtil.d("will time be shown:","yes");
        } else {
            time.setVisibility(View.GONE);
            LogUtil.d("will time be shown","no");
        }

        if(interestingActivity.getDescription().length()<100){      //预设内容的TextView为wrap_content，低于一定长度的文本显示于固定大小的文本框中
            ViewGroup.LayoutParams params = description.getLayoutParams();
            params.height = 300;
            description.setLayoutParams(params);
        }
        description.setText(interestingActivity.getDescription());
        number.setText(String.valueOf(interestingActivity.getMaxNum()));
        location.setText(interestingActivity.getLocation());
        host.setText(interestingActivity.getHost());
        author.setText(interestingActivity.getPromoterName());
        tel.setText(interestingActivity.getContactTel());
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(InterestringActivityActivity.this)
                        .setTitle("提示")
                        .setMessage("确定参加该活动么？")
                        .setPositiveButton("确定", new
                                DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        LogUtil.e("InterestingActivityActivity",interestingActivity.getInternetId());
                                        interestingActivityPresenter.addParticipant(interestingActivity.getInternetId());
                                    }
                                })
                        .setNegativeButton("取消",null).show();
            }
        });

//        if(interestingActivity.getPromoterName()!=null){
//            host.setText(interestingActivity.getPromoterName());
//            author.setText(interestingActivity.getPromoterName());
//        } else {
//            if(interestingActivity.getPromoter()!=null&&interestingActivity.getPromoter().getLoginName()!=null){
//                host.setText(interestingActivity.getPromoter().getLoginName());
//                author.setText(interestingActivity.getPromoter().getLoginName());
//            }
//        }
//        if(interestingActivity.getContactTel()!=null){
//            tel.setText(interestingActivity.getContactTel());
//        } else if(interestingActivity.getPromoter()!=null&&interestingActivity.getPromoter().getCustomer()!=null&&interestingActivity.getPromoter().getCustomer().getTel()!=null){
//            tel.setText(String.valueOf(interestingActivity.getPromoter().getCustomer().getTel()));
//        }
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