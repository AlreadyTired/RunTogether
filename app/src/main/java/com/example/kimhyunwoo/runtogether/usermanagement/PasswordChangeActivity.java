package com.example.kimhyunwoo.runtogether.usermanagement;

import android.content.Intent;
import android.graphics.Color;
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

import java.util.regex.Pattern;
import com.example.kimhyunwoo.runtogether.ManagementUtil.*;
import com.example.kimhyunwoo.runtogether.mainactivity.MainActivity;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class PasswordChangeActivity extends AppCompatActivity {

    private String userNewPassword,userPresentPassword;
    private EditText PresentPasswordText;
    private EditText NewPasswordText;
    private EditText ConfirmNewPasswordText;
    private Button ChangePasswordButton;
    private AlertDialog dialog;
    private ManagementUtil Util;

    private TextInputLayout PresentPasswordTextLayout ,NewPasswordTextLayout, ConfirmPasswordTextLayout;
    private boolean PresentPasswordFlag,NewPasswordFlag,ConfirmPasswordFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        Util = new ManagementUtil();
        PresentPasswordFlag = NewPasswordFlag = ConfirmPasswordFlag = true;

        PresentPasswordText = (EditText)findViewById(R.id.presentpasswordText);PresentPasswordText.setHint(" Present Password");
        NewPasswordText = (EditText)findViewById(R.id.newpasswordText);NewPasswordText.setHint(" New Password");
        ConfirmNewPasswordText = (EditText)findViewById(R.id.confirmnewpasswordText);ConfirmNewPasswordText.setHint(" Confirm New Password");
        ChangePasswordButton = (Button)findViewById(R.id.ChangeButton);
        PresentPasswordTextLayout = (TextInputLayout)findViewById(R.id.presentpasswordTextLayout);
        NewPasswordTextLayout  = (TextInputLayout)findViewById(R.id.NewpasswordTextLayout);
        ConfirmPasswordTextLayout = (TextInputLayout)findViewById(R.id.confirmpasswordTextInputLayout);

        PresentPasswordText.addTextChangedListener(new TextWatcher() {
            String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = PresentPasswordText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        PresentPasswordText.setHint(" Present Password");
                        PresentPasswordTextLayout.setErrorEnabled(false);
                        PresentPasswordTextLayout.setError("Please enter the Present Password");
                        PresentPasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PresentPasswordFlag = false;
                    }
                    if(temporarystring.length()<8 || temporarystring.length()>50)
                    {
                        PresentPasswordTextLayout.setErrorEnabled(false);
                        PresentPasswordTextLayout.setError("Password is more than 8 letters and less than 50 letters");
                        PresentPasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PresentPasswordFlag = false;
                    }

                    if(!Pattern.matches("(?=.*[a-z])(?=.*[0-9])[a-z0-9]{8,20}", temporarystring)) {
                        PresentPasswordTextLayout.setErrorEnabled(true);
                        PresentPasswordTextLayout.setError("Password should be mixing with small English letter and number");
                        PresentPasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PresentPasswordFlag = false;
                    }

                }while(false);
                if(!PresentPasswordFlag)
                {
                    PresentPasswordTextLayout.setErrorEnabled(false);
                    Util.setEditColor(PresentPasswordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(PresentPasswordText,"#ff0000");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NewPasswordText.addTextChangedListener(new TextWatcher() {
            String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = NewPasswordText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        NewPasswordText.setHint(" New Password");
                        NewPasswordTextLayout.setErrorEnabled(true);
                        NewPasswordTextLayout.setError("Please enter the New Password");
                        NewPasswordFlag = true;
                        break;
                    }
                    else
                    {
                        NewPasswordFlag = false;
                    }

                    if(temporarystring.length() < 8 || temporarystring.length() > 50)
                    {
                        NewPasswordTextLayout.setErrorEnabled(true);
                        NewPasswordTextLayout.setError("Password is more than 8 letters and less than 50 letters");
                        NewPasswordFlag = true;
                        break;
                    }
                    else
                    {
                        NewPasswordFlag = false;
                    }
                    if(!(ConfirmNewPasswordText.getText().toString().equals("")) && !temporarystring.equals(ConfirmNewPasswordText.getText().toString()))
                    {
                        ConfirmPasswordTextLayout.setErrorEnabled(true);
                        ConfirmPasswordTextLayout.setError("The Passwords do not matched");
                        ConfirmPasswordFlag = true;
                    }
                    else
                    {
                        ConfirmPasswordFlag = false;
                    }

                    if(!Pattern.matches("(?=.*[a-z])(?=.*[0-9])[a-z0-9]{8,50}", temporarystring)) {
                        NewPasswordTextLayout.setErrorEnabled(true);
                        NewPasswordTextLayout.setError("Password should be mixing with small English letter and number");
                        NewPasswordFlag = true;
                        break;
                    }
                    else
                    {
                        NewPasswordFlag = false;
                    }
                }while(false);
                if(!NewPasswordFlag)
                {
                    NewPasswordTextLayout.setErrorEnabled(false);
                    Util.setEditColor(NewPasswordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(NewPasswordText,"#ff0000");
                }
                if(!ConfirmPasswordFlag)
                {
                    ConfirmPasswordTextLayout.setErrorEnabled(false);
                    Util.setEditColor(ConfirmNewPasswordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(ConfirmNewPasswordText,"#ff0000");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ConfirmNewPasswordText.addTextChangedListener(new TextWatcher() {
            String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = ConfirmNewPasswordText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        ConfirmNewPasswordText.setHint(" Confirm Password");
                        ConfirmPasswordTextLayout.setErrorEnabled(true);
                        ConfirmPasswordTextLayout.setError("Please enter the Confirm Password");
                        ConfirmPasswordFlag = true;
                        break;
                    }
                    if(!NewPasswordText.getText().toString().equals(temporarystring))
                    {
                        ConfirmPasswordTextLayout.setErrorEnabled(true);
                        ConfirmPasswordTextLayout.setError("The Passwords do not matched");
                        ConfirmPasswordFlag = true;
                        break;
                    }
                    else
                    {
                        ConfirmPasswordFlag = false;
                    }
                }while(false);
                if(!ConfirmPasswordFlag)
                {
                    ConfirmPasswordTextLayout.setErrorEnabled(false);
                    Util.setEditColor(ConfirmNewPasswordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(ConfirmNewPasswordText,"#ff0000");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ChangePasswordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                userNewPassword = NewPasswordText.getText().toString();
                userPresentPassword = PresentPasswordText.getText().toString();
                if(PresentPasswordFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChangeActivity.this);
                    dialog = builder.setMessage("Please Check again PresentPassword")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(NewPasswordFlag) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChangeActivity.this);
                    dialog = builder.setMessage("Please Check again NewPassword")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(ConfirmPasswordFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChangeActivity.this);
                    dialog = builder.setMessage("Please Check again ConfirmPassword")
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
                                Toast.makeText(PasswordChangeActivity.this, "Password Change Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PasswordChangeActivity.this,MainActivity.class);      // 로그인 성공으로 메인화면으로 넘어감.
                                PasswordChangeActivity.this.startActivity(intent);
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChangeActivity.this);      // 로그인 실패로 알림을 띄움
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
                PasswordChangeRequest PasswordChangeRequest = new PasswordChangeRequest(userPresentPassword,userNewPassword,reponseListener,PasswordChangeActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                RequestQueue queue = Volley.newRequestQueue(PasswordChangeActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                queue.add(PasswordChangeRequest);
            }
        });

    }
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        Util = null;
    }

}
