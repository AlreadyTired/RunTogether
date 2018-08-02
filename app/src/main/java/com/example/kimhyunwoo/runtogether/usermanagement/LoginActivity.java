package com.example.kimhyunwoo.runtogether.usermanagement;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.BackPressCloseHandler;
import com.example.kimhyunwoo.runtogether.ManagementUtil;
import com.example.kimhyunwoo.runtogether.UserInfo;
import com.example.kimhyunwoo.runtogether.mainactivity.MainActivity;
import com.example.kimhyunwoo.runtogether.R;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    //  뒤로가기 버튼을 2번 누르면 종료시키는 클레스
    private BackPressCloseHandler backPressCloseHandler;

    // 다이얼로그는 비밀번호 틀렸을때 추가 메세지 띄우기 위함.
    private AlertDialog dialog;
    private ManagementUtil Util;
    private boolean EmailFlag,PasswordFlag;
    EditText emailText;
    EditText passwordText;
    Button loginButton;
    TextView ForgetButton;
    TextView registerButton;
    TextInputLayout EmailTextLayout,PasswordTextLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backPressCloseHandler = new BackPressCloseHandler(this);
        EmailFlag = PasswordFlag = true;
        registerButton = (TextView) findViewById(R.id.registerButton);         // 회원가입 버튼
        ForgetButton = (TextView) findViewById(R.id.PasswordChangeButton);
        emailText = (EditText)findViewById(R.id.LoginEmailText);emailText.setHint(" Email");
        EmailTextLayout = (TextInputLayout)findViewById(R.id.LoginEmailTextLayout);
        passwordText= (EditText)findViewById(R.id.LoginPasswordText);passwordText.setHint(" Password");
        PasswordTextLayout = (TextInputLayout)findViewById(R.id.LoginpasswordTextLayout);
        loginButton= (Button) findViewById(R.id.loginButton);

        emailText.addTextChangedListener(new TextWatcher() {
            String temporarystring;
            String ErrorMessage="";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = emailText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        emailText.setHint(" Email");
                        ErrorMessage = "Email is not Empty";
                        EmailFlag = true;
                        break;
                    }
                    else
                    {
                        EmailFlag = false;
                    }
                    if(isEmailValid(temporarystring)==false)
                    {
                        ErrorMessage = "Email is not valid";
                        EmailFlag = true;
                        break;
                    }
                    else
                    {
                        EmailFlag = false;
                    }
                    if(temporarystring.length()>50)
                    {
                        ErrorMessage = "Email is less than 50 letters";
                        EmailFlag = true;
                        break;
                    }
                    else
                    {
                        EmailFlag = false;
                    }

                }while(false);
                if(EmailFlag)
                {
                    EmailTextLayout.setErrorEnabled(true);
                    EmailTextLayout.setError(ErrorMessage);
                }
                else
                {
                    EmailTextLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordText.addTextChangedListener(new TextWatcher() {
            private String temporarystring;
            private String ErrorMessage = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = passwordText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        passwordText.setHint(" Password");
                        ErrorMessage = "Please enter the Password";
                        PasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PasswordFlag = false;
                    }

                    if(temporarystring.length() < 8 || temporarystring.length() > 50)
                    {
                        ErrorMessage = "Password is more than 8 letters and less than 50 letters";
                        PasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PasswordFlag = false;
                    }

                    if(!Pattern.matches("(?=.*[a-z])(?=.*[0-9])[a-z0-9]{8,50}", temporarystring)) {
                        ErrorMessage = "password should be mixing with small English letter and number";
                        PasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PasswordFlag = false;
                    }
                }while(false);
                if(PasswordFlag)
                {
                    PasswordTextLayout.setErrorEnabled(true);
                    PasswordTextLayout.setError(ErrorMessage);
                }
                else
                {
                    PasswordTextLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                Intent FindPasswordIntent = new Intent(LoginActivity.this,FindPasswordActivity.class);      // ForgetPassword 버튼 눌렀을시 회원비밀번호찾기 액티비티로 넘어감
                LoginActivity.this.startActivity(FindPasswordIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()                                   // 로그인 버튼
        {
            @Override
            public void onClick(View view)
            {
                final String userEmail = emailText.getText().toString();
                String userPassword = passwordText.getText().toString();

                if(EmailFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);      // 로그인 실패로 알림을 띄움
                    dialog = builder.setMessage("Please, Chech again Email address")
                            .setNegativeButton("Try Again",null)
                            .create();
                    dialog.show();
                    return;
                }

                if(PasswordFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);      // 로그인 실패로 알림을 띄움
                    dialog = builder.setMessage("Please, Chech again Password")
                            .setNegativeButton("Try Again",null)
                            .create();
                    dialog.show();
                    return;
                }

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
                            String message = jsonResponse.getString("message");               // success 이름으로 boolean 타입의 값이 넘어온다
                            if(message.equals("ok"))
                            {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                UserInfo.setUserEmail(userEmail);
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);      // 로그인 성공으로 메인화면으로 넘어감.
                                LoginActivity.this.startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);      // 로그인 실패로 알림을 띄움
                                dialog = builder.setMessage(message)
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
                LoginRequest loginRequest = new LoginRequest(userEmail,userPassword,reponseListener,LoginActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                queue.add(loginRequest);                                                            // queue에 로그인 리퀘스트를 넣으면 loginrequest가 실행됨.
            }
        });
    }

    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();;
    }

    protected void onStop()
    {
        super.onStop();
        if(dialog != null)
        {
            dialog.dismiss();
            dialog=null;
        }
        Util = null;
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
