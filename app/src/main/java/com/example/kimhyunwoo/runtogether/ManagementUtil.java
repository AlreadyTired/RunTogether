package com.example.kimhyunwoo.runtogether;

import android.graphics.Color;
import android.widget.EditText;

public class ManagementUtil {

    public void setEditColor(EditText editText, String color)
    {
        editText.setTextColor(Color.parseColor(color));
    }

    public void setEditHintColor(EditText editText, String color)
    {
        editText.setHintTextColor(Color.parseColor(color));
    }
    public boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
