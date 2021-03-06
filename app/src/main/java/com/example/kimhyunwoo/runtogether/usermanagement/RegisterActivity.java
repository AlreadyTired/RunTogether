package com.example.kimhyunwoo.runtogether.usermanagement;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.ManagementUtil;
import com.example.kimhyunwoo.runtogether.R;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private AlertDialog dialog;

    private boolean validate = false;

    private ManagementUtil Util;

    private String userGender;
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userAge;
    private String confirmPassword;

    EditText emailText;
    EditText passwordText;
    EditText confirmpasswordText;
    EditText NicknameText;
    EditText AgeText;

    TextInputLayout emailTextLayout,passwordTextLayout,confirmPasswordTextLayout,nicknameTextLayout,AgeTextLayout;

    private boolean EmailFlag , PasswordFlag ,ConfirmPasswordFlag,NicknameFlag,AgeFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EmailFlag = PasswordFlag = ConfirmPasswordFlag = NicknameFlag = AgeFlag = true;

        Util = new ManagementUtil();

        emailText = (EditText) findViewById(R.id.LoginEmailText);emailText.setHint("Email");
        passwordText = (EditText) findViewById(R.id.LoginPasswordText);passwordText.setHint("Password");
        NicknameText = (EditText) findViewById(R.id.NicknameText);NicknameText.setHint("Nickname");
        confirmpasswordText = (EditText) findViewById(R.id.confirmpassword);confirmpasswordText.setHint("Confirm Password");
        AgeText = (EditText)findViewById(R.id.AgeText);AgeText.setHint(" Age");
        emailTextLayout = (TextInputLayout)findViewById(R.id.emailTextInputLayout);
        passwordTextLayout = (TextInputLayout)findViewById(R.id.passwordTextInputLayout);
        confirmPasswordTextLayout = (TextInputLayout)findViewById(R.id.confirmpasswordTextInputLayout);
        nicknameTextLayout = (TextInputLayout)findViewById(R.id.nicknameTextInputLayout);
        AgeTextLayout = (TextInputLayout)findViewById(R.id.AgeTextLayout);

        // 성별을 위한 부분
        final RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        int genderID = genderGroup.getCheckedRadioButtonId();
        userGender = ((RadioButton) findViewById(genderID)).getText().toString();

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton genderButton = (RadioButton) findViewById(i);
                userGender = genderButton.getText().toString();
                Log.v("Gender",userGender);
            }
        });

        // Email입력부분 오류 체크.
        emailText.addTextChangedListener(new TextWatcher() {
            private String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                do {
                    temporarystring = emailText.getText().toString();
                    if(temporarystring.equals(""))
                    {
                        emailText.setHint(" Email");
                        emailTextLayout.setErrorEnabled(true);
                        emailTextLayout.setError("Please enter the Email");
                        EmailFlag = true;
                        break;
                    }
                    else
                    {
                        EmailFlag = false;
                    }

                    if(temporarystring.length()>50)
                    {
                        emailTextLayout.setErrorEnabled(true);
                        emailTextLayout.setError("Email can't exceed 50 letters");
                        EmailFlag = true;
                        break;
                    }
                    else
                    {
                        EmailFlag = false;
                    }

                    if(isEmailValid(temporarystring)==false)
                    {
                        emailTextLayout.setErrorEnabled(true);
                        emailTextLayout.setError("Email is not valid");
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
                    emailTextLayout.setErrorEnabled(false);
                    Util.setEditColor(emailText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(emailText,"#ff0000");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Password입력부분 오류체크
        passwordText.addTextChangedListener(new TextWatcher() {
            private String temporarystring;
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
                        passwordTextLayout.setErrorEnabled(true);
                        passwordTextLayout.setError("Please enter the Password");
                        PasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PasswordFlag = false;
                    }

                    if(temporarystring.length() < 8 || temporarystring.length() > 50)
                    {
                        passwordTextLayout.setErrorEnabled(true);
                        passwordTextLayout.setError("Password is more than 8 letters and less than 50 letters");
                        PasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PasswordFlag = false;
                    }
                    if(!(confirmpasswordText.getText().toString().equals("")) && !temporarystring.equals(confirmpasswordText.getText().toString()))
                    {
                        confirmPasswordTextLayout.setErrorEnabled(true);
                        confirmPasswordTextLayout.setError("The Passwords do not matched");
                        ConfirmPasswordFlag = true;
                    }
                    else
                    {
                        ConfirmPasswordFlag = false;
                    }

                    if(!Pattern.matches("(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,50}", temporarystring)) {
                        passwordTextLayout.setErrorEnabled(true);
                        passwordTextLayout.setError("password should be mixing with small English letter and number");
                        PasswordFlag = true;
                        break;
                    }
                    else
                    {
                        PasswordFlag = false;
                    }
                }while(false);
                if(!PasswordFlag)
                {
                    passwordTextLayout.setErrorEnabled(false);
                    Util.setEditColor(passwordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(passwordText,"#ff0000");
                }
                if(!ConfirmPasswordFlag)
                {
                    confirmPasswordTextLayout.setErrorEnabled(false);
                    Util.setEditColor(confirmpasswordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(confirmpasswordText,"#ff0000");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Confirm Password 오류체크부분
        confirmpasswordText.addTextChangedListener(new TextWatcher() {
            private String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = confirmpasswordText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        confirmpasswordText.setHint(" Confirm Password");
                        confirmPasswordTextLayout.setErrorEnabled(true);
                        confirmPasswordTextLayout.setError("Please enter the Confirm Password");
                        ConfirmPasswordFlag = true;
                        break;
                    }
                    else
                    {
                        ConfirmPasswordFlag = false;
                    }
                    if(!passwordText.getText().toString().equals(temporarystring))
                    {
                        confirmPasswordTextLayout.setErrorEnabled(true);
                        confirmPasswordTextLayout.setError("The Passwords do not matched");
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
                    confirmPasswordTextLayout.setErrorEnabled(false);
                    Util.setEditColor(confirmpasswordText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(confirmpasswordText,"#ff0000");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //닉네임 오류체크 부분
        NicknameText.addTextChangedListener(new TextWatcher() {
            private String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = NicknameText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        NicknameText.setHint(" Nickname");
                        nicknameTextLayout.setErrorEnabled(true);
                        nicknameTextLayout.setError("Please enter the Nickname");
                        NicknameFlag = true;
                        break;
                    }
                    else
                    {
                        NicknameFlag = false;
                    }
                    if(temporarystring.length()>12)
                    {
                        nicknameTextLayout.setErrorEnabled(true);
                        nicknameTextLayout.setError("Nickname is less than 12");
                        NicknameFlag = true;
                        break;
                    }
                    else
                    {
                        NicknameFlag = false;
                    }
                    if(!Pattern.matches("^[a-z0-9]{1,12}$", temporarystring)) {
                        nicknameTextLayout.setErrorEnabled(true);
                        nicknameTextLayout.setError("password should be mixing with small English letter and number");
                        NicknameFlag = true;
                        break;
                    }
                    else
                    {
                        NicknameFlag = false;
                    }
                }while(false);

                if(!NicknameFlag)
                {
                    nicknameTextLayout.setErrorEnabled(false);
                    Util.setEditColor(NicknameText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(NicknameText,"#ff0000");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Age 입력부분 오류체크
        AgeText.addTextChangedListener(new TextWatcher() {
            String temporarystring;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temporarystring = AgeText.getText().toString();
                do {
                    if(temporarystring.equals(""))
                    {
                        AgeText.setHint(" Age");
                        AgeTextLayout.setErrorEnabled(true);
                        AgeTextLayout.setError("Please enter the Age");
                        AgeFlag = true;
                        break;
                    }
                    else
                    {
                        AgeFlag = false;
                    }
                    if(Integer.parseInt(temporarystring)>150)
                    {
                        AgeTextLayout.setErrorEnabled(true);
                        AgeTextLayout.setError("Age must less than 150 years old");
                        AgeFlag = true;
                        break;
                    }
                    else
                    {
                        AgeFlag = false;
                    }
                }while(false);
                if(!AgeFlag)
                {
                    AgeTextLayout.setErrorEnabled(false);
                    Util.setEditColor(AgeText,"#00ff00");
                }
                else
                {
                    Util.setEditColor(AgeText,"#ff0000");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 회원가입 버튼
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = emailText.getText().toString();
                userPassword = passwordText.getText().toString();
                userNickname = NicknameText.getText().toString();
                userAge = AgeText.getText().toString();

                if(EmailFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Please Check again EmailAddress")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(PasswordFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Please Check again Password")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(ConfirmPasswordFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Please Check again Confirm Password")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(NicknameFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Please Check again Nickname")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                if(AgeFlag)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Please Check again Age")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonvalidateResponse = new JSONObject(response);
                            String validatemessage = jsonvalidateResponse.getString("message");

                            if (validatemessage.equals("ok"))
                            {
                                Response.Listener<String> RegisterresponseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonregisterResponse = new JSONObject(response);
                                            String registmessage = jsonregisterResponse.getString("message");
                                            if (registmessage.equals("ok")) {
                                                Toast.makeText(RegisterActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                                dialog = builder.setMessage(registmessage)
                                                        .setNegativeButton("OK", null)
                                                        .create();
                                                dialog.show();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                RegisterRequest registerRequest = new RegisterRequest(userEmail, userPassword, userGender, userNickname, userAge, RegisterresponseListener,RegisterActivity.this);
                                RequestQueue Registerqueue = Volley.newRequestQueue(RegisterActivity.this);
                                Registerqueue.add(registerRequest);
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage(validatemessage)
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userEmail, responseListener,RegisterActivity.this);
                queue.add(validateRequest);
                queue.start();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        Util = null;
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
