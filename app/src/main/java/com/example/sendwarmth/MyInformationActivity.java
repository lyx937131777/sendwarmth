package com.example.sendwarmth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.dagger2.DaggerMyComponent;
import com.example.sendwarmth.dagger2.MyComponent;
import com.example.sendwarmth.dagger2.MyModule;
import com.example.sendwarmth.db.Account;
import com.example.sendwarmth.db.Customer;
import com.example.sendwarmth.presenter.MyInformationPresenter;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.MapUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MyInformationActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private static final int REQUEST_CODE = 1024;
    //photo
    private Dialog dialog;
    private Uri imageUri;
    private String imagePath = null;

    private CircleImageView profile;
    private TextView  nameText, genderText, cusAddressText, houseNumText, telText, idNumberText,
            personalDescriptionText, contactNameText, contactPhoneText, relationshipText,commonDiseasesText;
    private CardView profileCard,nameCard, genderCard, cusAddressCard, houseNumCard, idNumberCard,
            personalDesCriptionCard,contactNameCard, contactPhoneCard, relationshipCard,commonDiseasesCard;

    private Account account;
    private Customer customer;
    private SharedPreferences pref;
    private String credential;

    private Button logoutButton,changePasswordButton;

    private Dialog profileDialog;
    private ImageView image;
    private Context context;

    private MyInformationPresenter myInformationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        MyComponent myComponent = DaggerMyComponent.builder().myModule(new MyModule(this)).build();
        myInformationPresenter = myComponent.myInformationPresenter();
        context = this;

        initProfileDialog();
        initDialog();
        initUser();

        changePasswordButton = findViewById(R.id.change_password);
        changePasswordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyInformationActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyInformationActivity.this).edit();
                editor.remove("userId");
                editor.remove("password");
                editor.remove("profile");
                editor.apply();
                Intent intent_logout = new Intent(MyInformationActivity.this, LoginActivity.class);
                startActivity(intent_logout);
                MainActivity.instance.finish();
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        myInformationPresenter.updateAccount();
    }

    private void initProfileDialog()
    {
        profileDialog = new Dialog(MyInformationActivity.this,R.style.FullActivity);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        profileDialog.getWindow().setAttributes(attributes);

        image = getImageView();
        profileDialog.setContentView(image);

        //大图的点击事件（点击让他消失）
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileDialog.dismiss();
            }
        });
    }

    //动态的ImageView
    private ImageView getImageView(){
        ImageView imageView = new ImageView(this);

        //宽高
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //imageView设置图片
        @SuppressLint("ResourceType")
        InputStream is = getResources().openRawResource(R.drawable.profile_uri);

        Drawable drawable = BitmapDrawable.createFromStream(is, null);
        imageView.setImageDrawable(drawable);

        return imageView;
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
    public void onClick(View v) {
        Intent intent = new Intent(context, ModifyInformationActivity.class);
        String attribute,value;
        switch (v.getId()){
            case R.id.name:
            case R.id.name_card:
                attribute = "customerName";
                value = customer.getCustomerName();
                break;
            case R.id.gender:
            case R.id.gender_card:
                attribute = "gender";
                value = customer.getGender();
                break;
            case R.id.address:
            case R.id.address_card:
                attribute = "customerAddress";
                value = customer.getCustomerAddress();
                break;
            case R.id.house_num:
            case R.id.house_num_card:
                attribute = "houseNum";
                value = customer.getHouseNum();
                break;
            case R.id.id:
            case R.id.id_card:
                attribute = "idNumber";
                value = customer.getIdNumber();
                break;
            case R.id.personal_description:
            case R.id.personal_description_card:
                attribute = "personalDescription";
                value = customer.getPersonalDescription();
                break;
            case R.id.contact_name:
            case R.id.contact_name_card:
                attribute = "contactName";
                value = customer.getEmergencyContactName();
                break;
            case R.id.contact_phone:
            case R.id.contact_phone_card:
                attribute = "contactPhone";
                value = customer.getEmergencyContactPhone();
                break;
            case R.id.relationship:
            case R.id.relationship_card:
                attribute = "relationship";
                value = customer.getRelationship();
                break;
            case R.id.common_diseases:
            case R.id.common_diseases_card:
                attribute = "commonDiseases";
                value = customer.getCommonDiseases();
                break;
            default:
                return;
        }
        intent.putExtra("attribute",attribute);
        intent.putExtra("value",value);
        startActivity(intent);
    }

    private void initUser() {
        customer = (Customer) getIntent().getSerializableExtra("customer");

        profile = findViewById(R.id.profile);
        profileCard = findViewById(R.id.profile_card);
        nameText = findViewById(R.id.name);
        nameCard = findViewById(R.id.name_card);
        genderText = findViewById(R.id.gender);
        genderCard = findViewById(R.id.gender_card);
        cusAddressText = findViewById(R.id.address);
        cusAddressCard = findViewById(R.id.address_card);
        houseNumText = findViewById(R.id.house_num);
        houseNumCard = findViewById(R.id.house_num_card);
        telText = findViewById(R.id.tel);
        idNumberText = findViewById(R.id.id);
        idNumberCard = findViewById(R.id.id_card);
        personalDescriptionText = findViewById(R.id.personal_description);
        personalDesCriptionCard = findViewById(R.id.personal_description_card);
        contactNameText = findViewById(R.id.contact_name);
        contactNameCard = findViewById(R.id.contact_name_card);
        contactPhoneText = findViewById(R.id.contact_phone);
        contactPhoneCard = findViewById(R.id.contact_phone_card);
        relationshipText = findViewById(R.id.relationship);
        relationshipCard = findViewById(R.id.relationship_card);
        commonDiseasesText = findViewById(R.id.common_diseases);
        commonDiseasesCard = findViewById(R.id.common_diseases_card);

        refresh();

        nameText.setOnClickListener(this);
        nameCard.setOnClickListener(this);
        genderText.setOnClickListener(this);
        genderCard.setOnClickListener(this);
        cusAddressText.setOnClickListener(this);
        houseNumText.setOnClickListener(this);
        idNumberText.setOnClickListener(this);
        idNumberCard.setOnClickListener(this);
        personalDescriptionText.setOnClickListener(this);
        personalDesCriptionCard.setOnClickListener(this);
        contactNameText.setOnClickListener(this);
        contactNameCard.setOnClickListener(this);
        contactPhoneText.setOnClickListener(this);
        contactPhoneCard.setOnClickListener(this);
        relationshipText.setOnClickListener(this);
        relationshipCard.setOnClickListener(this);
        commonDiseasesText.setOnClickListener(this);
        commonDiseasesCard.setOnClickListener(this);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileDialog.show();
            }
        });

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public void refresh(){
        nameText.setText(customer.getCustomerName());
        genderText.setText(MapUtil.getGender(customer.getGender()));
        cusAddressText.setText(customer.getCustomerAddress());
        houseNumText.setText(customer.getHouseNum());
        telText.setText(customer.getCustomerTel());
        idNumberText.setText(customer.getIdNumber());
        personalDescriptionText.setText(customer.getPersonalDescription());
        contactNameText.setText(customer.getEmergencyContactName());
        contactPhoneText.setText(customer.getEmergencyContactPhone());
        relationshipText.setText(MapUtil.getRelationship(customer.getRelationship()));
        commonDiseasesText.setText(customer.getCommonDiseases());
    }

    public void setAccount(Account account){
        this.account = account;
        customer = account.getCustomerInfo();
        String proFile = account.getProFile();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(proFile != null){
                    Glide.with(context).load(HttpUtil.getResourceURL(proFile)).into(profile);
                    Glide.with(context).load(HttpUtil.getResourceURL(proFile)).into(image);
                }else {
                    Glide.with(context).load(R.drawable.profile_uri).into(profile);
                    Glide.with(context).load(R.drawable.profile_uri).into(image);
                }
                refresh();
            }
        });
    }

    private void initDialog()
    {
        dialog = new Dialog(this, R.style.AppTheme);
        View view = View.inflate(this, R.layout.dialog_choose_image, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        //view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(this).getScreenHeight() *
        // 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.width = (int) (ScreenSizeUtils.getInstance(this).getScreenWidth() * 0.9f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        Button dialogCameraBotton = view.findViewById(R.id.dialog_camera);
        Button dialogAlbumBotton = view.findViewById(R.id.dialog_album);
        Button dialogCancelBotton = view.findViewById(R.id.dialog_cancel);
        dialogCameraBotton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyInformationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, TAKE_PHOTO);
                } else {
                    takePhoto();
                }
                dialog.cancel();
            }
        });

        dialogAlbumBotton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MyInformationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO);
                } else {
                    openAlbum();
                }
                dialog.cancel();
            }

        });
        dialogCancelBotton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.cancel();
            }
        });
    }

    private void takePhoto(){
        long time = System.currentTimeMillis();
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(), time+".jpeg");
        imagePath = outputImage.getAbsolutePath();
        try
        {
            if (outputImage.exists())
            {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24)
        {
            imageUri = Uri.fromFile(outputImage);
        } else
        {
            imageUri = FileProvider.getUriForFile(context,
                    "com.example.sendwarmth.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    // Camera and Album
    private void openAlbum()
    {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请", Toast.LENGTH_SHORT).show();
                }
                break;
            case CHOOSE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try
                    {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        Glide.with(this).load(imageUri).into(profile);
                        resetProfile();
//                        imageView.setImageBitmap(bitmap);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }else{
                    imagePath = null;
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                    resetProfile();
                }
                break;
            case REQUEST_CODE:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()){
                    myInformationPresenter.resetProfile(imagePath);
                }else {
                    Toast.makeText(this,"存储权限未获取，无法使用此功能",Toast.LENGTH_LONG).show();
                }
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data)
    {
        Uri uri = data.getData();
        LogUtil.e("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data)
    {
        Uri uri = data.getData();
        imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection)
    {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath)
    {
        LogUtil.e("album", "imagePath:" + imagePath);
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Glide.with(this).load(imagePath).into(profile);
//            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "打开图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetProfile(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                myInformationPresenter.resetProfile(imagePath);
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        }else {
            myInformationPresenter.resetProfile(imagePath);
        }
    }
}