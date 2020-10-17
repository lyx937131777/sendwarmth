package com.example.sendwarmth.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sendwarmth.adapter.PensionInstitutionAdapter;

public class PensionInstitutionPresenter
{
    private Context context;
    private SharedPreferences pref;

    public PensionInstitutionPresenter(Context context, SharedPreferences pref){
        this.context = context;
        this.pref = pref;
    }

    public void updatePensionInstitution(final PensionInstitutionAdapter pensionInstitutionAdapter){

    }
}
