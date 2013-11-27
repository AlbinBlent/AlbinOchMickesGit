package com.example.ping;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class NumberPreference extends DialogPreference {
    private int lastValue=0;
    private NumberPicker numberPicker = null;
    
    

    public static int getValue(String time) {

        return(Integer.parseInt(time));
    }

    public NumberPreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @SuppressLint("NewApi")
	@Override
    protected View onCreateDialogView() {
        numberPicker=new NumberPicker(getContext());

        return(numberPicker);
    }

    @SuppressLint("NewApi")
	@Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
//        numberPicker.setCurrentMinute(lastValue);
        String[] nums = new String[21];
        
        for(int i=0; i<nums.length; i++)
           nums[i] = Integer.toString(i+1);
        numberPicker.setMaxValue(nums.length-1);
        numberPicker.setMinValue(1);
        numberPicker.setValue(lastValue);
        numberPicker.setDisplayedValues(nums);
    }

    @SuppressLint("NewApi")
	@Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
//            lastValue=picker.getCurrentMinute();
        	lastValue = numberPicker.getValue();

            String value=String.valueOf(lastValue);

            if (callChangeListener(value)) {
                persistString(value);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String value=null;

        if (restoreValue) {
            if (defaultValue==null) {
            	value=getPersistedString("00:00");
            }
            else {
                value=getPersistedString(defaultValue.toString());
            }
        }
        else {
            value=defaultValue.toString();
        }

        lastValue=getValue(value);
    }
}