package com.example.mitinventteamv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

public class surveyLayout extends AppCompatActivity {

    public double weightedAverage =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_layout);
    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked)weightedAverage += 3;
                Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                // Put some meat on the sandwich
                // Remove the meat
                break;
            case R.id.checkBox4:
                if (checked)weightedAverage += 3;
                Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                // Put some meat on the sandwich
                // Remove the meat
                break;
            case R.id.checkBox5:
                if (checked)weightedAverage += 3;
                Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                // Put some meat on the sandwich
                // Remove the meat
                break;
            case R.id.checkBox6:
                if (checked)weightedAverage += 1.5;
                Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                // Put some meat on the sandwich
                // Remove the meat
                break;
            case R.id.checkBox7:
                if (checked)weightedAverage += 1;
                Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                // Put some meat on the sandwich
                // Remove the meat
                break;
            case R.id.checkBox8:
                if (checked)weightedAverage += 1;
                Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                // Put some meat on the sandwich
                // Remove the meat
                break;
            case R.id.checkBox9:
                if (checked)weightedAverage += 4;
                Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                // Put some meat on the sandwich
                // Remove the meat
                break;
            case R.id.checkBox10:
                CheckBox checkBox = findViewById(R.id.checkBox9);
                if (checkBox.isChecked()){
                    if (checked)weightedAverage += 2;
                    Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                    // Put some meat on the sandwich
                    // Remove the meat
                }else{
                    if (checked)weightedAverage += 1;
                    Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                }

                break;
            case R.id.checkBox11:
                if (checked)weightedAverage += 3;
                Log.d("MyActivity", "ActualInitial: " +weightedAverage);

                // Put some meat on the sandwich
                // Remove the meat
                break;

        }

    }

    public void save(View view) {
        Log.d("MyActivity", "ActualInitial: " +weightedAverage);

        Intent replyIntent = new Intent();
        replyIntent.putExtra("val", weightedAverage);
        setResult(RESULT_OK,replyIntent);
        finish();
    }
}
