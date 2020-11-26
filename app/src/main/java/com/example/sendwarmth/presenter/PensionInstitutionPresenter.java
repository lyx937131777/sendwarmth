package com.example.sendwarmth.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.sendwarmth.MainActivity;
import com.example.sendwarmth.adapter.PensionInstitutionAdapter;
import com.example.sendwarmth.db.HealthBroadcast;
import com.example.sendwarmth.db.PensionInstitution;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.example.sendwarmth.util.Utility;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PensionInstitutionPresenter
{
    private Context context;
    private SharedPreferences pref;

    public PensionInstitutionPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updatePensionInstitution(final PensionInstitutionAdapter pensionInstitutionAdapter){
        final String address = HttpUtil.LocalAddress + "/api/institution/list";
        String credential = pref.getString("credential",null);
        HttpUtil.getHttp(address, credential, new Callback()
        {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e)
            {
                e.printStackTrace();
                ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "网络连接错误", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                final String responseData = response.body().string();
                LogUtil.e("PensionInstitutionPresenter",responseData);
                if(Utility.checkResponse(responseData,context,address)){
                    List<PensionInstitution> pensionInstitutionList = Utility.handlePensionInstitutionList(responseData);
                    pensionInstitutionAdapter.setmList(pensionInstitutionList);
                    ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pensionInstitutionAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}
