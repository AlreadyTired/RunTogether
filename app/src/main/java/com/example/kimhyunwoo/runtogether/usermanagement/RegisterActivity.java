package com.example.kimhyunwoo.runtogether.usermanagement;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.kimhyunwoo.runtogether.R;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {


    private Spinner AgeSpinner;
    private ArrayAdapter<String> AgeArray;
    private String userGender;
    private String userEmail;
    private AlertDialog dialog;
    private boolean validate = false;
    private ArrayList<String> AgeList;

    EditText emailText;
    EditText passwordText;
    EditText confirmpasswordText;
    EditText NicknameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 나이 스피너를 위한 부분
        AgeList = new ArrayList<String>();
        for(int i=1;i<101;i++)
        {
            String A = Integer.toString(i);
            AgeList.add(A);
        }
        AgeArray = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,AgeList);
        AgeSpinner = (Spinner)findViewById(R.id.agespinner);
        AgeSpinner.setAdapter(AgeArray);
        try
        {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            ListPopupWindow window = (ListPopupWindow)popup.get(AgeSpinner);
            window.setHeight(400);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        emailText = (EditText) findViewById(R.id.EmailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        NicknameText = (EditText) findViewById(R.id.NicknameText);
        confirmpasswordText = (EditText) findViewById(R.id.confirmpassword);

        // 성별을 위한 부분
        RadioGroup genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        int genderID = genderGroup.getCheckedRadioButtonId();
        userGender = ((RadioButton) findViewById(genderID)).getText().toString();

        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton genderButton = (RadioButton) findViewById(i);
                userGender = genderButton.getText().toString();
            }
        });

        // 이메일 중복체크 버튼
        final Button validateButton = (Button) findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail = emailText.getText().toString();
                if (validate) {
                    return;
                }
                if (userEmail.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Email is can not be empty").setPositiveButton("OK", null).create();
                    dialog.show();
                    return;
                }
                if(isEmailValid(userEmail)==false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Email is not Valid").setPositiveButton("OK", null).create();
                    dialog.show();
                    return;
                }
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("You can use this Email")
                                        .setPositiveButton("OK", null)
                                        .create();
                                dialog.show();
                                emailText.setEnabled(false);
                                validate = true;
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("You can not use this ID")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userEmail, responseListener);
                queue.add(validateRequest);
                queue.start();
            }
        });

        // 회원가입 버튼
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String confirmPassword = confirmpasswordText.getText().toString();
                String userNickname = NicknameText.getText().toString();
                String userAge = AgeSpinner.getSelectedItem().toString();
/*
                // 중복체크 했는지 여부
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("try to duplication check first")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    return;
                }
*/
                if (userEmail.equals("") || userPassword.equals("") || confirmPassword.equals("") || userNickname.equals("") || userAge.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Can not be empty space")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    if(userEmail.equals(""))
                    {
                        emailText.setHintTextColor(Color.parseColor("#ff0000"));
                    }
                    if(userNickname.equals(""))
                    {
                        NicknameText.setHintTextColor(Color.parseColor("#ff0000"));
                    }
                    return;
                }
                else
                {
                    emailText.setHintTextColor(Color.parseColor("#ffffff"));
                    NicknameText.setHintTextColor(Color.parseColor("#ffffff"));
                }

                if(!userPassword.equals(confirmPassword))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Check the Password")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    confirmpasswordText.setTextColor(Color.parseColor("#ff0000"));
                    return;
                }
                else
                {
                    confirmpasswordText.setTextColor(Color.parseColor("#ffffff"));
                }

                if(userPassword.length() < 8 && userPassword.length() < 50)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("Password is more than 8 letters and less than 50 letters")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                    confirmpasswordText.setTextColor(Color.parseColor("#ff0000"));
                    passwordText.setTextColor(Color.parseColor("#ff0000"));
                    return;
                }
                else
                {
                    confirmpasswordText.setTextColor(Color.parseColor("#00ff00"));
                    passwordText.setTextColor(Color.parseColor("#00ff00"));
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(RegisterActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("Registration Failed")
                                        .setNegativeButton("OK", null)
                                        .create();
                                dialog.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(userEmail, userPassword, userGender, userNickname, userAge, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
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
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
