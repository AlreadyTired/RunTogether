package com.example.kimhyunwoo.runtogether;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

import com.example.kimhyunwoo.runtogether.bluetoothmanagement.BluetoothChatService;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.DeviceListActivity;

import java.io.IOException;

public class BluetoothSingleton {
    /**
     * Local Bluetooth adapter
     */
    public BluetoothAdapter mBluetoothAdapter = null;
    /**
     * Member object for the chat services
     */
    public BluetoothChatService mChatService = null;

    private BluetoothDevice device = null;

    private BluetoothSingleton(){
    }

    private static class LazyHolder{
        public static final BluetoothSingleton INSTANCE = new BluetoothSingleton();
    }

    public static BluetoothSingleton getInstance(){
        return LazyHolder.INSTANCE;
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     */
    public void connectDevice(Intent data) {
        // Get the device MAC address
        String address = "";

        try {
            address = data.getExtras()
                    .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            device = mBluetoothAdapter.getRemoteDevice(address);
        }catch (IllegalArgumentException e){
            Log.e("[ERR]getExtras ",e.getMessage().toString());
        }
        // Get the BluetoothDevice object

        // Attempt to connect to the device
        try{
            mChatService.connect(device);
        }catch (Exception e){
            Log.e("[ERR]connectDevice", e.getMessage().toString());
        }
        String test1 = device.getAddress();
        String test2 = device.getName();

    }

    public String getDeviceAdress(){
        return device.getAddress();
    }

    public String getDeviceName(){
        return device.getName();
    }

}
