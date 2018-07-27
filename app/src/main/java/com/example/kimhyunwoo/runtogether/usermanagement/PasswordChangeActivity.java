package com.example.kimhyunwoo.runtogether.usermanagement;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kimhyunwoo.runtogether.R;

public class PasswordChangeActivity extends AppCompatActivity {

    private EditText PresentPasswordText;
    private EditText ConfirmPresentPasswordText;
    private EditText NewPasswordText;
    private EditText ConfirmNewPasswordText;
    private Button ChangePasswordButton;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        PresentPasswordText = (EditText)findViewById(R.id.presentpasswordText);
        NewPasswordText = (EditText)findViewById(R.id.newpasswordText);
        ConfirmNewPasswordText = (EditText)findViewById(R.id.confirmnewpasswordText);
        ChangePasswordButton = (Button)findViewById(R.id.ChangeButton);

        ChangePasswordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String PresentPassword = PresentPasswordText.getText().toString();
                String NewPassword = NewPasswordText.getText().toString();
                String ConfirmNewPassword = ConfirmNewPasswordText.getText().toString();

                // 새 비밀번호 잘썻는가 확인
                if(!NewPassword.equals(ConfirmNewPassword))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChangeActivity.this);
                    dialog = builder.setMessage("Check the Password")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    ConfirmNewPasswordText.setTextColor(Color.parseColor("#ff0000"));
                    NewPasswordText.setTextColor(Color.parseColor("#ff0000"));
                    return;
                }
                else
                {
                    ConfirmNewPasswordText.setTextColor(Color.parseColor("#ffffff"));
                    NewPasswordText.setTextColor(Color.parseColor("#ffffff"));
                }

                //새 비밀번호 양식 체크
                if(NewPassword.length() < 8 && NewPassword.length() < 50)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChangeActivity.this);
                    dialog = builder.setMessage("Password is more than 8 letters and less than 50 letters")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    ConfirmNewPasswordText.setTextColor(Color.parseColor("#ff0000"));
                    NewPasswordText.setTextColor(Color.parseColor("#ff0000"));
                    return;
                }
                else
                {
                    ConfirmNewPasswordText.setTextColor(Color.parseColor("#00ff00"));
                    NewPasswordText.setTextColor(Color.parseColor("#00ff00"));
                }
            }
        });

    }
}
