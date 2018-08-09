package com.example.kimhyunwoo.runtogether;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BluetoothUtil {
    public JSONObject airData;


    public String airDataJsonParsing(String data) throws JSONException {
        try {
            airData = new JSONObject(data);
        } catch (Exception e) {
            Log.e("[ERR] JSON", e.getMessage().toString());
            return null;
        }

        //  현재는 임시 데이터로 정의 됨
        String type = airData.getString("type");
        String temp = airData.getString("temp");
        float temp_f = Float.parseFloat(temp);
        temp = String.format("%.2f",temp_f);
        String co = airData.getString("co");
        float co_f = Float.parseFloat(co);
        co = String.format("%.2f",co_f);
        String so2 = airData.getString("so2");
        float so2_f = Float.parseFloat(so2);
        so2 = String.format("%.2f",so2_f);
        String no2 = airData.getString("no2");
        float no2_f = Float.parseFloat(no2);
        no2 = String.format("%.2f",no2_f);
        String o3 = airData.getString("o3");
        float o3_f = Float.parseFloat(o3);
        o3 = String.format("%.2f",o3_f);
        String pm25 = airData.getString("pm25");
        float pm25_f = Float.parseFloat(pm25);
        pm25 = String.format("%.2f",pm25_f);
        String timestamp = airData.getString("timestamp");

        RealTimeDataTransfer.setAirData(co, so2, no2, o3, pm25, temp, timestamp);

        String jsonString = co + "," +  so2 + "," + no2 + "," + o3 + "," + pm25 + "," + temp;

        return jsonString;
    }


}
