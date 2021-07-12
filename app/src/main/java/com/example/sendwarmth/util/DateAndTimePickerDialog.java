package com.example.sendwarmth.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.example.sendwarmth.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lyx on 2020/11/12.
 */

public class DateAndTimePickerDialog
{
    public static final int TAG_TIME_PICKER = 0;
    public static final int TAG_DATE_PICKER = 1;
    public static final int TAG_DATE_AND_TIME_PICKER = 2;

    private Context mContext;
    private AlertDialog.Builder mAlertDialog;
    private int mHour, mMinute;
    private DateAndTimePickerDialogInterface dateAndTimePickerDialogInterface;
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    private int mTag = 0;
    private int mYear, mDay, mMonth;

    public DateAndTimePickerDialog(Context context)
    {
        super();
        mContext = context;
        dateAndTimePickerDialogInterface = (DateAndTimePickerDialogInterface) context;
    }

    /**
     * 初始化DatePicker
     *
     * @return
     */

    private View initDatePicker() {
        View dialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_date_and_time_picker, null);
        mTimePicker = dialog.findViewById(R.id.time_picker);
        mDatePicker = dialog.findViewById(R.id.date_picker);
        mTimePicker.setVisibility(View.GONE);
        resizePikcer(mDatePicker);
        return dialog;
    }

    /**
     * 初始化TimePicker
     *
     * @return
     */


    private View initTimePicker() {
        View dialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_date_and_time_picker, null);
        mTimePicker = dialog.findViewById(R.id.time_picker);
        mDatePicker = dialog.findViewById(R.id.date_picker);
        mDatePicker.setVisibility(View.GONE);
        mTimePicker.setIs24HourView(true);
        resizePikcer(mTimePicker);
        return dialog;
    }

    private View initDateAndTimePicker()
    {
        View dialog = LayoutInflater.from(mContext).inflate(R.layout.dialog_date_and_time_picker, null);
        mTimePicker = dialog.findViewById(R.id.time_picker);
        mDatePicker = dialog.findViewById(R.id.date_picker);
        mTimePicker.setIs24HourView(true);
        resizePikcer(mTimePicker);
        resizePikcer(mDatePicker);
        return dialog;
    }

    /**
     * 创建dialog
     *
     * @param view
     */
    private void initDialog(View view)
    {
        mAlertDialog.setPositiveButton("确定",
                new android.content.DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();

                        if (mTag == TAG_TIME_PICKER)
                        {
                            getTimePickerValue();
                        } else if (mTag == TAG_DATE_PICKER)
                        {
                            getDatePickerValue();
                        } else if (mTag == TAG_DATE_AND_TIME_PICKER)
                        {
                            getDatePickerValue();
                            getTimePickerValue();
                        }
                        dateAndTimePickerDialogInterface.positiveListener();

                    }
                });
        mAlertDialog.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dateAndTimePickerDialogInterface.negativeListener();
                        dialog.dismiss();
                    }
                });
        mAlertDialog.setView(view);
    }

    /**
     * 显示时间选择器
     */
    /*
    public void showTimePickerDialog() {
        mTag=0;
        View view = initTimePicker();
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setTitle("选择时间");
        initDialog(view);
        mAlertDialog.show();

    }

    /**
     * 显示日期选择器
     */
    /*
    public void showDatePickerDialog() {
        mTag=1;
        View view = initDatePicker();
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setTitle("选择时间");
        initDialog(view);
        mAlertDialog.show();
    }
    /**
     * 显示日期选择器
     */
    public void showDateAndTimePickerDialog(int tag, long time)
    {
        mTag = tag;
        View view;
        if(mTag == TAG_DATE_AND_TIME_PICKER){
            view = initDateAndTimePicker();
        }else if(mTag == TAG_TIME_PICKER){
            view = initTimePicker();
        }else {
            view = initDatePicker();
        }
        LogUtil.e("DateAndTimePickerDialog","time: " + time);
        if(time > 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            if(mTag != TAG_TIME_PICKER){
                mDatePicker.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            }
            if(mTag != TAG_DATE_PICKER)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    mTimePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                    mTimePicker.setMinute(calendar.get(Calendar.MINUTE));
                } else
                {
                    mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                    mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                }
            }
        }
        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setTitle("选择时间");
        initDialog(view);
        mAlertDialog.show();
    }

    /*
    * 调整numberpicker大小
    */
    private void resizeNumberPicker(NumberPicker np)
    {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }

    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    private void resizePikcer(FrameLayout tp)
    {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList)
        {
            resizeNumberPicker(np);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup)
    {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup)
        {
            for (int i = 0; i < viewGroup.getChildCount(); i++)
            {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker)
                {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout)
                {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0)
                    {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    public int getYear()
    {
        return mYear;
    }

    public int getDay()
    {
        return mDay;
    }

    public int getMonth()
    {
        //返回的时间是0-11
        return mMonth + 1;
    }

    public int getMinute()
    {
        return mMinute;
    }

    public int getHour()
    {
        return mHour;
    }

    public Calendar getCalendar(){
        if(mTag != TAG_TIME_PICKER){
            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear,mMonth,mDay,mHour,mMinute,0);
            calendar.set(Calendar.MILLISECOND,0);
            return calendar;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,mHour);
        calendar.set(Calendar.MINUTE,mMinute);
        return calendar;
    }

    public long getTime(){
        return getCalendar().getTimeInMillis();
    }

    public Date getDate(){
        return getCalendar().getTime();
    }

    /**
     * 获取日期选择的值
     */
    private void getDatePickerValue()
    {
        mYear = mDatePicker.getYear();
        mMonth = mDatePicker.getMonth();
        mDay = mDatePicker.getDayOfMonth();
    }

    /**
     * 获取时间选择的值
     */
    private void getTimePickerValue()
    {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            mHour = mTimePicker.getHour();
            mMinute = mTimePicker.getMinute();
        }else{
            // api23这两个方法过时
            mHour = mTimePicker.getCurrentHour();// timePicker.getHour();
            mMinute = mTimePicker.getCurrentMinute();// timePicker.getMinute();
        }
    }


    public interface DateAndTimePickerDialogInterface
    {
        public void positiveListener();

        public void negativeListener();
    }

}