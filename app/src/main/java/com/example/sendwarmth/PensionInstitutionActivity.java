package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.db.PensionInstitution;
import com.example.sendwarmth.util.HttpUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class PensionInstitutionActivity extends AppCompatActivity
{
    private PensionInstitution pensionInstitution;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pension_institution);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        ImageView pictrue = findViewById(R.id.picture);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        TextView address = findViewById(R.id.address);
        TextView tel = findViewById(R.id.tel);
//        TextView contact = findViewById(R.id.contact);
        TextView description = findViewById(R.id.description);

        Glide.with(this).load(HttpUtil.getResourceURL(pensionInstitution.getInstitutionPic())).into(pictrue);
        collapsingToolbarLayout.setTitle(pensionInstitution.getInstitutionName());
        address.setText(pensionInstitution.getInstitutionLoc());
        tel.setText(pensionInstitution.getInstitutionTel());
//        contact.setText(pensionInstitution.getContact());
        description.setText(pensionInstitution.getInstitutionDes());
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