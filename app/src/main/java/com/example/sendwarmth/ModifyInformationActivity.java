package com.example.sendwarmth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.presenter.ModifyInformationPresenter;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.MapUtil;

import java.util.ArrayList;
import java.util.List;

public class ModifyInformationActivity extends AppCompatActivity {

    private EditText valueText;
    private Button locationButton;

    private String attribute;
    private String oldValue;

    private CardView defaultCard;

    private RadioGroup genderRadioGroup;
    private RadioButton maleButton,femaleButton;

    private RadioGroup relationshipRadioGroup;
    private RadioButton childrenButton,relativesButton,friendsButton;

    private LinearLayout commonDiseasesLayout;
    private CheckBox[] checkBoxes = new CheckBox[9];
    private int[] checkBoxIds = {R.id.coronary_heart_disease,R.id.angina_pectoris,R.id.hypertension,R.id.diabetes, R.id.cerebral_infarction,
                    R.id.chronic_gastritis,R.id.osteoporosis,R.id.chronic_bronchitis,R.id.gout};
    private String[] commonDiseaseName = {"冠心病","心绞痛","高血压","糖尿病","脑梗塞","慢性胃炎","骨质疏松","慢性支气管炎","痛风"};
    List<String> commonDiseasesList = new ArrayList<>();
    private CheckBox otherCheckBox;
    private EditText otherEdit;
    private boolean otherChecked;

    private ModifyInformationPresenter modifyInformationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        modifyInformationPresenter = myComponent.modifyInformationPresenter();

        attribute = getIntent().getStringExtra("attribute");
        oldValue = getIntent().getStringExtra("value");

        toolbar.setTitle("设置" + MapUtil.getAttribute(attribute));

        valueText = findViewById(R.id.value);
        valueText.setText(oldValue);
        locationButton = findViewById(R.id.location);
        defaultCard = findViewById(R.id.default_card);
        genderRadioGroup = findViewById(R.id.gender);
        maleButton = findViewById(R.id.male);
        femaleButton = findViewById(R.id.female);
        relationshipRadioGroup = findViewById(R.id.relationship);
        childrenButton = findViewById(R.id.children);
        relativesButton = findViewById(R.id.relatives);
        friendsButton = findViewById(R.id.friends);
        commonDiseasesLayout = findViewById(R.id.common_diseases);
        otherCheckBox = findViewById(R.id.other);
        otherEdit = findViewById(R.id.other_edit);

        if(attribute.equals("customerAddress")){
            valueText.setEnabled(false);
            locationButton.setVisibility(View.VISIBLE);
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ModifyInformationActivity.this, LocationSelectActivity.class);
                    startActivityForResult(intent,1);
                }
            });
        }
        if(attribute.equals("gender")){
            defaultCard.setVisibility(View.GONE);
            genderRadioGroup.setVisibility(View.VISIBLE);
            genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(maleButton.getId() == checkedId){
                        valueText.setText("male");
                        LogUtil.e("ModifyInformationActiviy","male check!");
                    }else {
                        valueText.setText("female");
                        LogUtil.e("ModifyInformationActiviy","female check!");
                    }
                }
            });
            if(oldValue == null || oldValue.equals("male")){
                maleButton.toggle();
                LogUtil.e("ModifyInformationActiviy","male toggle!");
//                valueText.setText("male");
            }else{
                femaleButton.toggle();
                LogUtil.e("ModifyInformationActiviy","female toggle!");
//                valueText.setText("female");
            }
        }

        if(attribute.equals("relationship")){
            defaultCard.setVisibility(View.GONE);
            relationshipRadioGroup.setVisibility(View.VISIBLE);
            relationshipRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(childrenButton.getId() == checkedId){
                        valueText.setText("children");
                    }else if(relativesButton.getId() == checkedId) {
                        valueText.setText("relatives");
                    }else{
                        valueText.setText("friends");
                    }
                }
            });
            if(oldValue == null || oldValue.equals("children")){
                childrenButton.toggle();
            }else if(oldValue.equals("relatives")){
                relativesButton.toggle();
            }else{
                friendsButton.toggle();
            }
        }

        if(attribute.equals("commonDiseases")){
            defaultCard.setVisibility(View.GONE);
            commonDiseasesLayout.setVisibility(View.VISIBLE);
            otherCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    otherChecked = isChecked;
                }
            });
            if(oldValue != null){
                List<String> commonDiseaseNameList = new ArrayList<>();
                for(String s : commonDiseaseName){
                    commonDiseaseNameList.add(s);
                }
                String[] strings = oldValue.split(",");
                String otherString = "";
                for(String s : strings){
                    if(commonDiseaseNameList.contains(s)){
                        commonDiseasesList.add(s);
                    }else {
                        if(otherString == ""){
                            otherString += s;
                        }else{
                            otherString += "," + s;
                        }
                    }
                }
                if(otherString != ""){
                    otherEdit.setText(otherString);
                    otherCheckBox.setChecked(true);
                }
            }
            for(int i = 0; i < checkBoxIds.length; i++){
                checkBoxes[i] = findViewById(checkBoxIds[i]);
                for(String s : commonDiseasesList){
                    if(commonDiseaseName[i].equals(s)){
                        checkBoxes[i].setChecked(true);
                        LogUtil.e("ModifyInformationActivity","add: " + s);
                    }

                }
                checkBoxes[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            commonDiseasesList.add(buttonView.getText().toString());
                        }else {
                            commonDiseasesList.remove(buttonView.getText().toString());
                        }
                        LogUtil.e("ModifyInformationActivity",commonDiseasesList.toString());
                    }
                });
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.commit:{
                if(attribute.equals("commonDiseases")){
                    String newValue = "";
                    for(int i = 0; i < commonDiseasesList.size(); i++){
                        newValue += commonDiseasesList.get(i);
                        if(i < commonDiseasesList.size()-1)
                            newValue += ",";
                    }
                    if(otherChecked){
                        if(commonDiseasesList.size() != 0)
                            newValue += ",";
                        newValue += otherEdit.getText().toString();
                    }
                    valueText.setText(newValue);
                }
                modifyInformationPresenter.modifyCustomer(attribute,valueText.getText().toString());
                break;
            }
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.commit_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 1:
            {
                if (resultCode == RESULT_OK)
                {
                    String address = data.getStringExtra("address");
                    valueText.setText(address);
                    double longitude = data.getDoubleExtra("longitude",0);
                    double latitude = data.getDoubleExtra("latitude",0);
                    modifyInformationPresenter.setLL(longitude,latitude);
                }
                break;
            }
            default:
                break;
        }
    }
}