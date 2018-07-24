package com.example.kimhyunwoo.runtogether.usermanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.kimhyunwoo.runtogether.R;

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
}
