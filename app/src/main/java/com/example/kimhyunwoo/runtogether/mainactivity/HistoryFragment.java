package com.example.kimhyunwoo.runtogether.mainactivity;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.kimhyunwoo.runtogether.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends SimpleFragment {

    public static Fragment newInstance(){ return new HistoryFragment();}

    private LineChart mChart;
    private AlertDialog dialog;
    private TextView DateText;
    private Spinner PeriodSpinner,SelectDataSpinner;
    private Button DataRequestButton;
    private ArrayAdapter PeriodspinnerAdapter,DataTypeSpinnerAdapter;
    private String PeriodString = "1", DataTypeStirng ="co2",DateString;
    private ArrayList<String> DataTypeInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        DateText = (TextView)v.findViewById(R.id.HistoryDataDateViewText);
        PeriodSpinner = (Spinner)v.findViewById(R.id.HistoryDataPeriodSelectSpinner);
        SelectDataSpinner = (Spinner)v.findViewById(R.id.HistoryDataSelectSpinner);
        DataRequestButton = (Button)v.findViewById(R.id.HistoryDataRequestButton);
        DataTypeInfo = new ArrayList<String>();

        // 현재날짜를 default 값으로 설정.
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        DateText.setText(sdf.format(date));
        sdf = new SimpleDateFormat("YYMMdd");
        DateString = sdf.format(date);

        ArrayList<String> DataTypeList = new ArrayList<>();
        DataTypeList.add("SO2");
        DataTypeList.add("CO");
        DataTypeList.add("CO2");
        DataTypeList.add("NO2");
        DataTypeList.add("O3");
        DataTypeList.add("PM2.5");
        DataTypeList.add("PM10");
        DataTypeList.add("Temp");

        ArrayList<String> periodlist = new ArrayList<>();
        periodlist.add("1 day");
        periodlist.add("1 Week");
        periodlist.add("1 Month");
        periodlist.add("3 Month");

        PeriodspinnerAdapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,periodlist);
        PeriodSpinner.setAdapter(PeriodspinnerAdapter);
        PeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(PeriodSpinner.getItemAtPosition(position).toString().equals("CO")) { DataTypeStirng = "co";}
                if(PeriodSpinner.getItemAtPosition(position).toString().equals("CO2")) { DataTypeStirng = "co2";}
                if(PeriodSpinner.getItemAtPosition(position).toString().equals("NO2")) { DataTypeStirng = "no2";}
                if(PeriodSpinner.getItemAtPosition(position).toString().equals("PM2.5")) { DataTypeStirng = "pm25";}
                if(PeriodSpinner.getItemAtPosition(position).toString().equals("PM10")) { DataTypeStirng = "pm10";}
                if(PeriodSpinner.getItemAtPosition(position).toString().equals("Temp")) { DataTypeStirng = "temp";}
                if(PeriodSpinner.getItemAtPosition(position).toString().equals("SO2")) { DataTypeStirng = "so2";}
                if(PeriodSpinner.getItemAtPosition(position).toString().equals("O3")) { DataTypeStirng = "o3";}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DataTypeSpinnerAdapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,DataTypeList);
        SelectDataSpinner.setAdapter(DataTypeSpinnerAdapter);
        SelectDataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(SelectDataSpinner.getItemAtPosition(position).toString().equals("1 day")) { PeriodString = "1";}
                if(SelectDataSpinner.getItemAtPosition(position).toString().equals("1 Week")) { PeriodString = "7";}
                if(SelectDataSpinner.getItemAtPosition(position).toString().equals("1 Month")) { PeriodString = "30";}
                if(SelectDataSpinner.getItemAtPosition(position).toString().equals("3 Month")) { PeriodString = "90";}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 달력띄우기
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String Monthstring = month<10?"0"+String.valueOf(month):String.valueOf(month);
                        String Daystring = dayOfMonth<10?"0"+String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        String date = Monthstring+"-"+Daystring+"-"+String.valueOf(year);
                        DateString = String.valueOf(year-2000)+Monthstring+Daystring;
                        Log.v("DateString",DateString);
                        DateText.setText(date);
                    }
                },yy,mm,dd);
                datePicker.show();
            }
        });

        DataRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리퀘스트 때리면 됨
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
                            JSONArray array = jsonResponse.getJSONArray("data");
                            for(int i=0;i<array.length();i++)
                            {
                                JSONObject object = array.getJSONObject(i);
                                String data = object.getString(DataTypeStirng);
                                String datedata = object.getString("date");
                                DataTypeInfo.add(data);
                            }
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                DataGetRequest datagetRequest = new DataGetRequest(DateString, DataTypeStirng,PeriodString,reponseListener,getContext());           // 위에서 작성한 리스너를 기반으로 요청하는 클래스를 선언.(LoginRequest참고)
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());            // Volley의 사용법으로 request queue로 queue를 하나 선언하고
                queue.add(datagetRequest);

                mChart.setData(getComplexity(DataTypeStirng,DataTypeInfo));
            }
        });
        mChart = v.findViewById(R.id.myLineChart);

        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);
        mChart.animateX(3000);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Light.ttf");

        Legend l = mChart.getLegend();
        l.setTypeface(tf);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);

        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setTextColor(ColorTemplate.getHoloBlue());
        xAxis.setPosition((XAxis.XAxisPosition.BOTTOM));

        return v;
    }
}