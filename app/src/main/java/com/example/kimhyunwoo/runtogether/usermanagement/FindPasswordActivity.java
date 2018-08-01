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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.ManagementUtil;
import com.example.kimhyunwoo.runtogether.R;

import org.json.JSONObject;

public class FindPasswordActivity extends AppCompatActivity {
    EditText EmailText;
    private String userEmail;
    private TextInputLayout EmailTextLayout;
    private Button FindPasswordButton;
    private boolean EmailFlag;
    private ManagementUtil Util;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user_info);
        EmailText = (EditText)findViewById(R.id.FindPasswordEmailText);EmailText.setHint(" Email");
        EmailTextLayout = (TextInputLayout)findViewById(R.id.FindPasswordEmailTextLayout);
        FindPasswordButton = (Button)findViewById(R.id.PasswordChangeButton);
        EmailFlag = true;
        Util = new ManagementUtil();
        EmailText.addTextChangedListener(new TextWatcher() {
            private String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = EmailText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        EmailText.setHint(" Email");
                        EmailTextLayout.setErrorEnabled(true);
                        EmailTextLayout.setError("Please enter the Email");
                        EmailFlag = true;
                        break;
                    }
                    else
                    {
                        EmailFlag = false;
                    }
                    if(isEmailValid(temporarystring)==false)
                    {
                        EmailTextLayout.setErrorEnabled(true);
                        EmailTextLayout.setError("Email is not valid");
                        EmailFlag = true;
                        break;
                    }
                    else
                    {
                        EmailFlag = false;
                    }
                    if(temporarystring.length()>50)
                    {
                        EmailTextLayout.setErrorEnabled(true);
                        EmailTextLayout.setError("Email can't exceed 50 letters");
                        EmailFlag = true;
                        break;
                    }
                    else
                    {
                        EmailFlag = false;
                    }
                }while(false);
                if(!EmailFlag)
                {
                    EmailTextLayout.setErrorEnabled(false);
                    Util.setEditColor(EmailText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(EmailText,"#ff0000");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        FindPasswordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userEmail = EmailText.getText().toString();

                if(EmailFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPasswordActivity.this);
                    dialog = builder.setMessage("Please Check again EmailAddress")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

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
                                Toast.makeText(FindPasswordActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FindPasswordActivity.this,LoginActivity.class);      // 성공으로 화면으로 넘어감.
                                FindPasswordActivity.this.startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FindPasswordActivity.this);      // 로그인 실패로 알림을 띄움
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
                FindPasswordRequest FindPasswordRequest = new FindPasswordRequest(userEmail,reponseListener,FindPasswordActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                RequestQueue queue = Volley.newRequestQueue(FindPasswordActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                queue.add(FindPasswordRequest);
            }
        });
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
