package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.db.PensionInstitution;
import com.example.sendwarmth.util.HttpUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class PensionInstitutionActivity extends AppCompatActivity
{
    private PensionInstitution pensionInstitution;

    private ImageView picture;
    private ImageView remarkImg;
    private CardView descriptionCard;
    private TextView descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pension_institution);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initPensionInstitution();
    }

    private void initPensionInstitution()
    {
        pensionInstitution = (PensionInstitution) getIntent().getSerializableExtra("pensionInstitution");
        picture = findViewById(R.id.picture);
//        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
//        TextView address = findViewById(R.id.address);
//        TextView tel = findViewById(R.id.tel);
//        TextView contact = findViewById(R.id.contact);
        descriptionCard = findViewById(R.id.description_card);
        descriptionText = findViewById(R.id.description);
        remarkImg = findViewById(R.id.remark_img);

        Glide.with(this).load(HttpUtil.getResourceURL(pensionInstitution.getInstitutionPic())).into(picture);
//        collapsingToolbarLayout.setTitle(pensionInstitution.getInstitutionName());
//        address.setText(pensionInstitution.getInstitutionLoc());
//        tel.setText(pensionInstitution.getInstitutionTel());
//        contact.setText(pensionInstitution.getContact());
        descriptionText.setText(pensionInstitution.getInstitutionDes());

        if(pensionInstitution.getRemarkImg() != null){
            Glide.with(this).load(HttpUtil.getResourceURL(pensionInstitution.getRemarkImg())).into(remarkImg);
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