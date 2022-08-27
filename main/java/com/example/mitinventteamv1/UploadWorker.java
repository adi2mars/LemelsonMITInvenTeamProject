package com.example.mitinventteamv1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.net.Socket;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

import static com.example.mitinventteamv1.TabFragment2.bluetooth;

public class UploadWorker extends Worker {


    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // Do the work here--in this case, upload the images.

        connectBluetooth();
        // Indicate whether the task finished successfully with the Result
        return Result.success();
    }
    public void connectBluetooth(){
        bluetooth.connect("00:14:03:05:5A:C4");
//        if(bluetooth.getServiceState() != 2 ){
//            connectBluetooth();
            Log.d("MyActivity", "bluetooth: " + bluetooth.getServiceState());

        }

    }

