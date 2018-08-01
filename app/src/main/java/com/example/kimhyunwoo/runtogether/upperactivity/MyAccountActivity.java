package com.example.kimhyunwoo.runtogether.upperactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kimhyunwoo.runtogether.R;
import com.example.kimhyunwoo.runtogether.usermanagement.FindPasswordActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.PasswordChangeActivity;

public class MyAccountActivity extends AppCompatActivity {
    Button ChangePasswordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ChangePasswordButton = (Button)findViewById(R.id.PasswordChangeButton);
        ChangePasswordButton.setOnClickListener(new View.OnClickListener()                                   // 로그인 버튼
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MyAccountActivity.this,PasswordChangeActivity.class);      // 로그인 성공으로 메인화면으로 넘어감.
                MyAccountActivity.this.startActivity(intent);
            }
        });
    }
}
