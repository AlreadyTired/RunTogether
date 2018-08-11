package com.example.kimhyunwoo.runtogether.usermanagement;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.R;
import com.example.kimhyunwoo.runtogether.UserInfo;
import com.example.kimhyunwoo.runtogether.mainactivity.MainActivity;
import com.example.kimhyunwoo.runtogether.upperactivity.LogoutRequest;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Touch to screen 텍스트 깜빡이기위한 코드.
        TextView touchtoscreentext = (TextView) findViewById(R.id.mytext);
        Animation anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(500);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        touchtoscreentext.startAnimation(anim);
    }

    // 화면의 어느곳이든 누르면 다음 화면으로 넘어감(레이아웃으로 설정함)
    public void on(View v){
        switch(v.getId()){
            case R.id.Layouts:{
                Intent i=new Intent(this,LoginActivity.class);
                startActivity(i);

                break;
            }
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        Log.v("User's Log","OnDestroy before response");
        if(!UserInfo.getUserToken().isEmpty())
        {
            Log.v("User's Log","OnDestroy before response after 'if'command");
            Response.Listener<String> reponseListener = new Response.Listener<String>() {
                // Volley 를 통해서 정상적으로 웹서버와 통신이 되면 실행되는 함수
                @Override
                public void onResponse(String response)
                {
                }
            };
            LogoutRequest logoutRequest = new LogoutRequest(reponseListener,StartActivity.this);           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
            RequestQueue queue = Volley.newRequestQueue(StartActivity.this);            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
            queue.add(logoutRequest);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 2000);
        }
        Log.v("User's Log","OnDestroy after response");
        super.onDestroy();
    }
}
