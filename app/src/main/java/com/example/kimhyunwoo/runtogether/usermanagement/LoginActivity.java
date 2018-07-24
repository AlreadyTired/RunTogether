package com.example.kimhyunwoo.runtogether.usermanagement;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.mainactivity.MainActivity;
import com.example.kimhyunwoo.runtogether.R;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    // 다이얼로그는 비밀번호 틀렸을때 추가 메세지 띄우기 위함.
    private AlertDialog dialog;
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView ForgetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView registerButton = (TextView) findViewById(R.id.registerButton);         // 회원가입 버튼

        emailText = (EditText)findViewById(R.id.EmailText);
        passwordText= (EditText)findViewById(R.id.passwordText);
        loginButton= (Button) findViewById(R.id.loginButton);
        ForgetButton = (TextView) findViewById(R.id.forgetPasswordButton);

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);      // 회원가입 버튼 눌렀을시 회원가입액티비티로 넘어감
                LoginActivity.this.startActivity(registerIntent);
            }
        });


        ForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent FindPasswordIntent = new Intent(LoginActivity.this,FindUserInfoActivity.class);      // ForgetPassword 버튼 눌렀을시 회원비밀번호찾기 액티비티로 넘어감
                LoginActivity.this.startActivity(FindPasswordIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()                                   // 로그인 버튼
        {
            @Override
            public void onClick(View view)
            {
                String userEmail = emailText.getText().toString();
                String userPassword = passwordText.getText().toString();

                // Volley 사용하기 위한 리스너 정의.
                Response.Listener<String> reponseListener = new Response.Listener<String>() {

                    // Volley 를 통해서 정상적으로 웹서버와 통신이 되면 실행되는 함수
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            // JSON 형식으로 값을 response 에 받아서 넘어온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");               // success 이름으로 boolean 타입의 값이 넘어온다
                            if(success)
                            {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);      // 로그인 성공으로 메인화면으로 넘어감.
                                LoginActivity.this.startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);      // 로그인 실패로 알림을 띄움
                                dialog = builder.setMessage("Please, Check Acount")
                                        .setNegativeButton("Try Again",null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userEmail,userPassword,reponseListener);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                queue.add(loginRequest);                                                            // queue에 로그인 리퀘스트를 넣으면 loginrequest가 실행됨.
            }
        });
    }

    protected void onStop()
    {
        super.onStop();
        if(dialog != null)
        {
            dialog.dismiss();
            dialog=null;
        }
    }
}
