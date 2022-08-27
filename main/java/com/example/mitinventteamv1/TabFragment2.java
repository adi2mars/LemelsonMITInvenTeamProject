package com.example.mitinventteamv1;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment2 extends Fragment {

    final String ON = "1";
    final String OFF = "0";

    public static BluetoothSPP bluetooth;

    Button connect;
    Button on;
    Button off;
    Button survey;
    TextView info;
    TextView value;
    TextView pulseVal;
    TextView skinTempVal;
    TextView breathPMVal;
    TextView percentVal;
    EditText lactateVal;
    TextView descrip;
    public double weightedAverage ;
    double weightedAverage2 =0;
    public TabFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.tab_fragment2, container, false);
        bluetooth = new BluetoothSPP(view.getContext());
        info = view.findViewById(R.id.info);
        value = view.findViewById(R.id.OximeterVal);
        pulseVal = view.findViewById(R.id.valPulse);
        skinTempVal = view.findViewById(R.id.valSkinTemp);
        breathPMVal = view.findViewById(R.id.valBreathPM);
        survey = view.findViewById(R.id.buttonSurvey);
        lactateVal = view.findViewById(R.id.valLactate);
        percentVal = view.findViewById(R.id.txtPercent);
        descrip = view.findViewById(R.id.txtDescrip);

        Log.d("MyActivity", "weightedAverage: " +weightedAverage);

        connect = (Button) view.findViewById(R.id.connect);
        Log.d("MyActivity", "working1.0");

        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(view.getContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
        }
        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                info.setText("Connected to " + name);
            }

            public void onDeviceDisconnected() {
                info.setText("Connection lost");
               // periodicConnectBluetooth();

            }

            public void onDeviceConnectionFailed() {
                info.setText("Unable to connect");
               // periodicConnectBluetooth();
            }
        });
       //bluetooth.connect("00:14:03:05:5A:C4");
        //periodicConnectBluetooth();


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bluetooth.disconnect();
                } else {
                    Intent intent = new Intent(getContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                    bluetooth.connect("00:14:03:05:5A:C4");
                    //connectBluetooth();
                }
            }
        });

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), surveyLayout.class);
                startActivityForResult(intent, 1);
            }
        });




        bluetooth.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                String information = message;
                String values[] = information.split("  ");
                ArrayList<Integer> numValues = new ArrayList<Integer> ();
                for(int i =0; i<values.length; i++){
                    if(!(values[i].equals("Pulse:") || values[i].equals("Skin Temperature:") || values[i].equals("Breath Per Minute:"))){
                        numValues.add(Integer.parseInt(values[i]));
                    }
                    Log.d("MyActivity", "working " + values[i]);


                }
               for(int i =0; i<numValues.size(); i++){
                    Log.d("MyActivity", "working " + numValues.get(i));
                }
                //value.setText(information);
                pulseVal.setText(numValues.get(0).toString());
               skinTempVal.setText(numValues.get(1).toString());
               breathPMVal.setText(numValues.get(2).toString());
                Log.d("MyActivity", "weightedAverage: " +weightedAverage);

                 weightedAverage2 = 0;
                if(numValues.get(0)>93){
                    weightedAverage2 += 2.5;
                }
                if(numValues.get(1)>100.4){
                    weightedAverage2 += 3;
                }
                if(numValues.get(2)>=22){
                    weightedAverage2 += 2;
                }
                String val = lactateVal.getText().toString();
                if(Integer.parseInt(val)>2.1){
                    weightedAverage2 += 4;
                }
                weightedAverage2 = weightedAverage2/(4*4);
                Log.d("MyActivity", "weightedAverage2: " +weightedAverage2);
                double percent = (weightedAverage*0.4) + (weightedAverage2*0.6);
                percent = percent*100;
                int percent2 = (int) percent;
                Log.d("MyActivity", "percent: " +percent2);

                percentVal.setText(Integer.toString(percent2));
                if(percent2<=10){
                    descrip.setText("0-10: No need for Sepsis Test");
                }else if((percent2<=30)){
                    descrip.setText("11-30: Unlikely But Monitor Symptoms");
                }else if((percent2<=50)){
                    descrip.setText("31-50: Moderate Chance Consult Physician");
                }else if(percent2>50){
                    descrip.setText("51+: High Risk of Sepsis. Immediate Care Required. ");

                }



            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();

    }

    @Override
    public void onStart() throws NullPointerException {
        super.onStart();
       if (!bluetooth.isBluetoothEnabled()) {
                bluetooth.enable();
           //connectBluetooth();

       } else {
                if (!bluetooth.isServiceAvailable()) {
                    bluetooth.setupService();
                    bluetooth.startService(BluetoothState.DEVICE_OTHER);
                    //connectBluetooth();

                }
            }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == RESULT_OK){
           //bluetooth.getConnectedDeviceAddress()
           // Log.d("MyActivity", "Bluetooth" + bluetooth.getConnectedDeviceAddress());

        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
            }
        }

    }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Double reply =
                        data.getDoubleExtra("val", -1);
                if(reply>1){
                    weightedAverage = reply;
                    weightedAverage = weightedAverage/(9*4);
                    Log.d("MyActivity", "Actual: " +weightedAverage);

                }
                // process data
            }
        }

}
public void periodicConnectBluetooth(){
    PeriodicWorkRequest saveRequest =
            new PeriodicWorkRequest.Builder(UploadWorker.class, 5, TimeUnit.SECONDS)
                    .build();

    WorkManager.getInstance()
            .enqueue(saveRequest);

        }



}

