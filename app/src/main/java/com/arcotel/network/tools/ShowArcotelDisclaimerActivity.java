package com.arcotel.network.tools;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowArcotelDisclaimerActivity extends AppCompatActivity {

    private TextView textViewDeviceInfoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_arcotel_disclaimer);

        textViewDeviceInfoDetail = this.findViewById(R.id.textViewDeviceInfoDetail);
        textViewDeviceInfoDetail.setText("Modelo: "+ Build.MODEL
                +"\nversión incremental : "+Build.VERSION.INCREMENTAL
                +"\nversión de lanzamiento : "+Build.VERSION.RELEASE
                +"\ndisplay : "+Build.DISPLAY
                +"\nhardware : "+Build.HARDWARE
                +"\nproducto : "+Build.PRODUCT
                +"\nserial : "+Build.SERIAL

        );

    }
}