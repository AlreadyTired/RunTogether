package com.example.kimhyunwoo.runtogether.mainactivity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.kimhyunwoo.runtogether.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarkerClickFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // fragment 메세지 창을 띄우기 위해 선언
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // xml에 정의된 리소스들을 view 형식으로 반환해줌
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // LayoutInflater 에서 반환된 데이터를 View로 받음
        View view = inflater.inflate(R.layout.fragment_marker_click, null);
        //
        builder.setView(view);

        // 버튼 아이디 가져옴
        final Button submit = (Button) view.findViewById(R.id.btn_request);
        // intent에 실어서 보낼 아이디
        // EditText에 적힌 내용이 반환됨
        final EditText email = (EditText) view.findViewById(R.id.edit_email);

        // 버튼이 클릭했을 경우 실행
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String strEmail = email.getText().toString();

                Intent data = new Intent();
                data.putExtra("id", strEmail);

                // 버튼이 눌렸을 시에 실행됨
                getTargetFragment().onActivityResult(getTargetRequestCode(),
                        Activity.RESULT_OK, data);

                dismiss();

            }
        });

        return builder.create();
    }
}
