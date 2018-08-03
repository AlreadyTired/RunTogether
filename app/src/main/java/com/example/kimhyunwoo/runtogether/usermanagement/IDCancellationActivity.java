package com.example.kimhyunwoo.runtogether.usermanagement;

import android.content.DialogInterface;
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
import com.example.kimhyunwoo.runtogether.UserInfo;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class IDCancellationActivity extends AppCompatActivity {

    private String userPassword;
    private EditText PasswordText,ConfirmPasswordText;
    private TextInputLayout PasswordTextLayout,ConfirmPasswordTextLayout;
    private Button IDcancellationButton;
    private ManagementUtil Util;
    private AlertDialog dialog;

    private boolean PasswordFlag,ConfirmPasswordFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcancellation);

        Util = new ManagementUtil();
        IDcancellationButton = (Button)findViewById(R.id.IDcancellationButton) ;
        PasswordText = (EditText)findViewById(R.id.IDCancelPasswordText);PasswordText.setHint("Password");
        ConfirmPasswordText = (EditText)findViewById(R.id.IDCancelConfirmPasswordText);ConfirmPasswordText.setHint("Confirm Password");
        PasswordTextLayout = (TextInputLayout)findViewById(R.id.IDCancelPasswordTextLayout);
        ConfirmPasswordTextLayout = (TextInputLayout)findViewById(R.id.IDCancelConfirmPasswrodTextLayout);
        PasswordFlag = ConfirmPasswordFlag = true;

        PasswordText.addTextChangedListener(new TextWatcher() {
            private String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = PasswordText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        PasswordText.setHint("Password");
                        PasswordTextLayout.setErrorEnabled(false);
                        PasswordTextLayout.setError("Please enter the Password");
                        PasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PasswordFlag = false;
                    }
                    if(!Pattern.matches("(?=.*[a-z])(?=.*[0-9])[a-z0-9]{8,50}", temporarystring)) {
                        PasswordTextLayout.setErrorEnabled(true);
                        PasswordTextLayout.setError("password should be mixing with small English letter and number");
                        PasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PasswordFlag = false;
                    }
                    if(temporarystring.length()<8 || temporarystring.length() > 50)
                    {
                        PasswordTextLayout.setErrorEnabled(false);
                        PasswordTextLayout.setError("Password is more than 8 letters and less than 50 letters");
                        PasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PasswordFlag = false;
                    }
                    if(!(ConfirmPasswordText.getText().toString().equals("")) && !temporarystring.equals(ConfirmPasswordText.getText().toString()))
                    {
                        ConfirmPasswordTextLayout.setErrorEnabled(true);
                        ConfirmPasswordTextLayout.setError("The Passwords do not matched");
                        ConfirmPasswordFlag = true;
                    }
                    else
                    {
                        ConfirmPasswordFlag = false;
                    }
                }while(false);
                if(!PasswordFlag)
                {
                    PasswordTextLayout.setErrorEnabled(false);
                    Util.setEditColor(PasswordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(PasswordText,"#ff0000");
                }
                if(!ConfirmPasswordFlag)
                {
                    ConfirmPasswordTextLayout.setErrorEnabled(false);
                    Util.setEditColor(ConfirmPasswordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(ConfirmPasswordText,"#ff0000");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ConfirmPasswordText.addTextChangedListener(new TextWatcher() {
            String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = ConfirmPasswordText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        ConfirmPasswordText.setHint(" Confirm Password");
                        ConfirmPasswordTextLayout.setErrorEnabled(true);
                        ConfirmPasswordTextLayout.setError("Please enter the Confirm Password");
                        ConfirmPasswordFlag = true;
                        break;
                    }
                    else
                    {
                        ConfirmPasswordFlag = false;
                    }
                    if(!PasswordText.getText().toString().equals(temporarystring))
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
                    Util.setEditColor(ConfirmPasswordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(ConfirmPasswordText,"#ff0000");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        IDcancellationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PasswordFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(IDCancellationActivity.this);
                    dialog = builder.setMessage("Please Check again Password")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(ConfirmPasswordFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(IDCancellationActivity.this);
                    dialog = builder.setMessage("Please Check again Confirm Password")
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(IDCancellationActivity.this);      // 로그인 실패로 알림을 띄움
                                dialog = builder.setMessage("Do you 'REALLY' Cancel your ID?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Response.Listener<String> reponseRealListener = new Response.Listener<String>() {

                                                    // Volley 를 통해서 정상적으로 웹서버와 통신이 되면 실행되는 함수
                                                    @Override
                                                    public void onResponse(String response)
                                                    {
                                                        try
                                                        {
                                                            // JSON 형식으로 값을 response 에 받아서 넘어온다.
                                                            JSONObject jsonRealResponse = new JSONObject(response);
                                                            String realmessage = jsonRealResponse.getString("message");               // success 이름으로 boolean 타입의 값이 넘어온다
                                                            if(realmessage.equals("ok"))
                                                            {
                                                                Toast.makeText(IDCancellationActivity.this, "ID Cancellation Success", Toast.LENGTH_SHORT).show();
                                                                UserInfo.UserDataReset();
                                                                Intent intent = new Intent(IDCancellationActivity.this,LoginActivity.class);      // 로그인 성공으로 메인화면으로 넘어감.
                                                                IDCancellationActivity.this.startActivity(intent);
                                                            }
                                                            else
                                                            {
                                                                AlertDialog realdialog;
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(IDCancellationActivity.this);      // 로그인 실패로 알림을 띄움
                                                                realdialog = builder.setMessage(realmessage)
                                                                        .setNegativeButton("Try Again",null)
                                                                        .create();
                                                                realdialog.show();
                                                            }
                                                        }
                                                        catch(Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                };
                                                IDRealCancellatioinRequest realcancellateRequest = new IDRealCancellatioinRequest(reponseRealListener,IDCancellationActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                                                RequestQueue queue = Volley.newRequestQueue(IDCancellationActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                                                queue.add(realcancellateRequest);
                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
                                            }
                                        })
                                        .create();
                                dialog.show();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(IDCancellationActivity.this);      // 로그인 실패로 알림을 띄움
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
                IDCancellateRequest cancellateRequest = new IDCancellateRequest(userPassword,reponseListener,IDCancellationActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                RequestQueue queue = Volley.newRequestQueue(IDCancellationActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                queue.add(cancellateRequest);
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
