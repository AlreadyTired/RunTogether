package com.example.kimhyunwoo.runtogether;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import com.example.kimhyunwoo.runtogether.bluetoothmanagement.BluetoothChatService;
import com.example.kimhyunwoo.runtogether.bluetoothmanagement.DeviceListActivity;

public class BluetoothSingleton {
    /**
     * Local Bluetooth adapter
     */
    public BluetoothAdapter mBluetoothAdapter = null;
    /**
     * Member object for the chat services
     */
    public BluetoothChatService mChatService = null;


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
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device);
    }
}
