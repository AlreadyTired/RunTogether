package com.example.kimhyunwoo.runtogether;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BluetoothUtil {

    private boolean isAQI = false;

    public boolean getType(){
        return isAQI;
    }

    public void setType(boolean flag){
        isAQI = flag;
    }

    public String SortType(String data) throws JSONException {
        JSONObject dataType;

        try {
            dataType = new JSONObject(data);
        } catch (Exception e) {
            Log.e("[ERR] JSON", e.getMessage().toString());
            return null;
        }

        String type = dataType.getString("type");
        String result = null;

        if(type.equals("real")){
            result = airDataJsonParsing(data);
        }else{
            result = aqiDataJsonParsing(data);
        }

        return result;
    }

    private String airDataJsonParsing(String data) throws JSONException {
        JSONObject airData;
        setType(false);
        try {
            airData = new JSONObject(data);
        } catch (Exception e) {
            Log.e("[ERR] JSON", e.getMessage().toString());
            return null;
        }

        //  현재는 임시 데이터로 정의 됨
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

    private String aqiDataJsonParsing(String data) throws JSONException {
        JSONObject airData;
        setType(true);

        try {
            airData = new JSONObject(data);
        } catch (Exception e) {
            Log.e("User's Log", " Error " + e.getMessage().toString());
            return null;
        }

        //  현재는 임시 데이터로 정의 됨
        String totalaqi = airData.getString("totalaqi");
        float total_f = Float.parseFloat(totalaqi);
        totalaqi = String.format("%.2f",total_f);
        String coaqi = airData.getString("coaqi");
        float coaqi_f = Float.parseFloat(coaqi);
        coaqi = String.format("%.2f",coaqi_f);
        String so2aqi = airData.getString("so2aqi");
        float so2aqi_f = Float.parseFloat(so2aqi);
        so2aqi = String.format("%.2f",so2aqi_f);
        String no2aqi = airData.getString("no2aqi");
        float no2aqi_f = Float.parseFloat(no2aqi);
        no2aqi = String.format("%.2f",no2aqi_f);
        String o3aqi = airData.getString("o3aqi");
        float o3aqi_f = Float.parseFloat(o3aqi);
        o3aqi = String.format("%.2f",o3aqi_f);
        String pm25aqi = airData.getString("pm25aqi");
        float pm25aqi_f = Float.parseFloat(pm25aqi);
        pm25aqi = String.format("%.2f",pm25aqi_f);
        String timestamp = airData.getString("timestamp");

        AqiDataTansfer.setAqiData(coaqi, so2aqi, no2aqi, o3aqi, pm25aqi, totalaqi, timestamp);

        String jsonString = coaqi + "," +  so2aqi + "," + no2aqi + "," + o3aqi + "," + pm25aqi + "," + totalaqi;

        return jsonString;
    }

}
