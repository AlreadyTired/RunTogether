package com.example.kimhyunwoo.runtogether.mainactivity;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kimhyunwoo.runtogether.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    private Typeface tf;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        tf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Regular.ttf");
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_friends,container,false);
//
//        LineChart lineChart = (LineChart)view.findViewById(R.id.myLineChart);
//
//        Description description = new Description();
//        description.setText("YongSun");
//        lineChart.setDescription(description);
//
//        LineDataSet lineDataSet = new LineDataSet(getDataSet(),"My Line Chart");
//        lineDataSet.setDrawFilled(true);
//        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        LineData lineData = new LineData(lineDataSet);
//        lineData.setValueFormatter(new ReportChartXAxisValueFormatter(getXAisValues()));
//
//        lineChart.setData(lineData);
//        lineChart.animateXY(2000,2000);
//        lineChart.invalidate();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    private List<Entry> getDataSet()
    {
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(4f,0));
        entries.add(new Entry(8f,1));
        entries.add(new Entry(6f,2));
        entries.add(new Entry(12f,3));
        entries.add(new Entry(18f,4));
        entries.add(new Entry(9f,5));
        return entries;
    }

    private List<String> getXAisValues()
    {
        List<String> xAxis = new ArrayList<String>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }

    private class ReportChartXAxisValueFormatter implements IValueFormatter
    {
        private List<String> labels;

        public ReportChartXAxisValueFormatter(List<String> labels)
        {
            this.labels = labels;
        }
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            try{
                int index = (int) value;
                return this.labels.get(index);
            }catch (Exception e)
            {
                return null;
            }
        }
    }
}
