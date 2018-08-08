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
        String co = airData.getString("co");
        String so2 = airData.getString("so2");
        String no2 = airData.getString("no2");
        String o3 = airData.getString("o3");
        String pm25 = airData.getString("pm25");
        String timestamp = airData.getString("timestamp");

        String jsonString = co + "," +  so2 + "," + no2 + "," + o3 + "," + pm25 + "," + temp;

        return jsonString;
    }


}
