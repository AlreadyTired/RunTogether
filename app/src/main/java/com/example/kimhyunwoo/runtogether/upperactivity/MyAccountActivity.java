package com.example.kimhyunwoo.runtogether.upperactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.R;
import com.example.kimhyunwoo.runtogether.UserInfo;
import com.example.kimhyunwoo.runtogether.mainactivity.MainActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.FindPasswordActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.IDCancellationActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.LoginActivity;
import com.example.kimhyunwoo.runtogether.usermanagement.LoginRequest;
import com.example.kimhyunwoo.runtogether.usermanagement.PasswordChangeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyAccountActivity extends AppCompatActivity{
    private Button ChangePasswordButton,IDCancellationInputButton,SensorListViewButton;
    private AlertDialog dialog,dialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ChangePasswordButton = (Button) findViewById(R.id.PasswordChangeButton);
        SensorListViewButton = (Button) findViewById(R.id.SensorListViewButton);
        IDCancellationInputButton = (Button) findViewById(R.id.IDCancellationInputButton);
        ChangePasswordButton.setOnClickListener(new View.OnClickListener()                                   // 로그인 버튼
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, PasswordChangeActivity.class);      // 로그인 성공으로 메인화면으로 넘어감.
                MyAccountActivity.this.startActivity(intent);
            }
        });
        IDCancellationInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccountActivity.this, IDCancellationActivity.class);      // 로그인 성공으로 메인화면으로 넘어감.
                MyAccountActivity.this.startActivity(intent);
            }
        });
        SensorListViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MyAccountActivity.this);
                alertBuilder.setTitle("   Account Sensor List");
                alertBuilder.setIcon(R.drawable.sensordraw);

                final ArrayAdapter<String> SensorAdapterList = new ArrayAdapter<String>(
                        MyAccountActivity.this,android.R.layout.select_dialog_singlechoice);

                Response.Listener<String> reponseListener = new Response.Listener<String>()
                {
                    // Volley 를 통해서 정상적으로 웹서버와 통신이 되면 실행되는 함수
                    @Override
                    public void onResponse(String response)
                    {
                        Log.v("LoginResponse",response);
                        try
                        {
                            // JSON 형식으로 값을 response 에 받아서 넘어온다.
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");
                            Log.v("message",message);
                            if(message.equals("ok"))
                            {
                                JSONArray array = jsonResponse.getJSONArray("mac");
                                for(int i=array.length()-1;i>=0;i--)
                                {
                                    String data = array.getString(i);
                                    SensorAdapterList.add(data);
                                }
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
                                dialog = builder.setMessage(message)
                                        .setNegativeButton("OK",null)
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
                SensorListViewRequest SensorlistviewRequest = new SensorListViewRequest(reponseListener,MyAccountActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                RequestQueue queue = Volley.newRequestQueue(MyAccountActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                queue.add(SensorlistviewRequest);

                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertBuilder.setAdapter(SensorAdapterList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String MacAddress = SensorAdapterList.getItem(which);
                        AlertDialog.Builder innBuilder = new AlertDialog.Builder(MyAccountActivity.this);
                        innBuilder.setTitle("Do you want Delete MACaddress?");
                        innBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 서버에 삭제요청하기.
                                Response.Listener<String> reponseListener = new Response.Listener<String>()
                                {
                                    // Volley 를 통해서 정상적으로 웹서버와 통신이 되면 실행되는 함수
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        try
                                        {
                                            // JSON 형식으로 값을 response 에 받아서 넘어온다.
                                            JSONObject jsonResponse = new JSONObject(response);
                                            String message = jsonResponse.getString("message");
                                            Log.v("message",message);
                                            if(message.equals("ok"))
                                            {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
                                                dialog2 = builder.setMessage("Sensor delete complete")
                                                        .setNegativeButton("OK",null)
                                                        .create();
                                                dialog2.show();
                                            }
                                            else
                                            {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
                                                dialog2 = builder.setMessage(message)
                                                        .setNegativeButton("OK",null)
                                                        .create();
                                                dialog2.show();
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                SensorDeregistrationRequest SensorDeregistRequest = new SensorDeregistrationRequest(MacAddress,reponseListener,MyAccountActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                                RequestQueue queue = Volley.newRequestQueue(MyAccountActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                                queue.add(SensorDeregistRequest);
                            }
                        });
                        innBuilder.show();
                    }
                });
                alertBuilder.show();
            }
        });
    }
}
