package com.example.kimhyunwoo.runtogether.mainactivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FileUtils;

import java.util.ArrayList;

public abstract class SimpleFragment extends Fragment {
    
    private Typeface tf;
    
    public SimpleFragment() {
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    protected LineData getComplexity(String datatype,ArrayList<String> array) {

//        for(int i=0;i<array.size();i++)
//        {
//            entries.add(new Entry(i,Float.parseFloat(array.get(i))));
//        }

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(08.14f,3f));
        entries.add(new Entry(08.15f,8f));
        entries.add(new Entry(08.16f,6f));
        entries.add(new Entry(08.17f,2f));
        entries.add(new Entry(08.18f,18f));
        entries.add(new Entry(08.19f,9f));
        entries.add(new Entry(08.20f,4f));
        entries.add(new Entry(08.21f,15f));
        LineDataSet dataSet = new LineDataSet(entries,datatype);
        LineData lineData = new LineData(dataSet);
        return lineData;
    }

    
    private String[] mLabels = new String[] { "Company A", "Company B", "Company C", "Company D", "Company E", "Company F" };
//    private String[] mXVals = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };
    
    private String getLabel(int i) {
        return mLabels[i];
    }
}
