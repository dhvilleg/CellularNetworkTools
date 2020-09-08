package com.arcotel.network.tools;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arcotel.network.tools.interfaces.AdvancedCellInfoActivityInteractionInterface;
import com.arcotel.network.tools.librarys.LocationAddress;
import com.arcotel.network.tools.librarys.LocationGPS;
import com.arcotel.network.tools.services.AdvancedCellInfoUIService;

public class AdvancedCellInfoActivity extends AppCompatActivity implements AdvancedCellInfoActivityInteractionInterface {

    private TextView textViewStrengthInfo;
    private ImageView imageViewACI;
    private TextView textViewOperatorNameACI;
    private TextView textViewOperatorInfo;
    private TextView textViewCellDescInfo;
    private TextView textViewInternetInfo;
    private TextView textViewLocationInfo;
    private TextView textViewTechCellACI;
    private LocationAddress locationAddress;
    private LocationGPS gps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_cell_info);

        imageViewACI = (ImageView) findViewById(R.id.imageViewACI);
        textViewTechCellACI = (TextView) findViewById(R.id.textViewTechCellACI);
        textViewOperatorNameACI = (TextView) findViewById(R.id.textViewOperatorNameACI);
        textViewOperatorInfo = (TextView) findViewById(R.id.textViewOperatorInfo);
        textViewStrengthInfo = (TextView) findViewById(R.id.textViewStrengthInfo);
        textViewCellDescInfo = (TextView) findViewById(R.id.textViewCellDescInfo);
        textViewInternetInfo = (TextView) findViewById(R.id.textViewInternetInfo);
        textViewLocationInfo = (TextView) findViewById(R.id.textViewLocationInfo);
        //locationAddress = new LocationAddress(this);

        enableGPS();

        Toast.makeText(this, "AdvancedCellInfoActivity OnCreate", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "AdvancedCellInfoActivity onStart", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AdvancedCellInfoUIService.setUpdateListener(this);
        iniciarCellularCoverageService();
        Toast.makeText(this, "AdvancedCellInfoActivity onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pararCellularCoverageService();

        Toast.makeText(this, "AdvancedCellInfoActivity onPause", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pararCellularCoverageService();

        Toast.makeText(this, "AdvancedCellInfoActivity onStop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "AdvancedCellInfoActivity onDestroy", Toast.LENGTH_LONG).show();
        pararCellularCoverageService();

    }

    private void enableGPS() {
        gps = new LocationGPS(this, getApplicationContext());

    }

    /**
     * Inicia el servicio
     */

    private void iniciarCellularCoverageService() {
        Intent service = new Intent(this, AdvancedCellInfoUIService.class);
        startService(service);
    }



    /**
     * Finaliza el servicio
     */

    private void pararCellularCoverageService() {
        Intent service = new Intent(this, AdvancedCellInfoUIService.class);
        stopService(service);
    }



    /**
     * Actualiza en la interfaz de usuario los valores de potencia de señal
     *
     *
     */

    public void actualizaUIOnAdvancedInfoActivity(String signalQuality,String phoneNetwork,String operatorName,String operatorTxtString,String strengthTxtString,String cellIdTxtString,String internetTxtString) {
        textViewTechCellACI.setText(phoneNetwork);
        textViewOperatorInfo.setText(operatorTxtString);
        textViewOperatorNameACI.setText(operatorName);
        textViewStrengthInfo.setText(strengthTxtString);
        textViewCellDescInfo.setText(cellIdTxtString);
        textViewInternetInfo.setText(internetTxtString);
        //textViewLocationInfo.setText(locationTxtString);

        //Pair<Double,Double> latLonLocation = locationAddress.getLatLongFromLocation();
        //Log.d("actualizaUIOnAdvanced", "Latitud: "+latLonLocation.first+"\nLongitude: "+latLonLocation.second);


        if(signalQuality == "VERY_GOOD"){
            imageViewACI.setImageResource(R.drawable.cell_signal_status_green);
        }
        else if(signalQuality == "GOOD"){
            imageViewACI.setImageResource(R.drawable.cell_signal_status_yellow);
        }
        else if(signalQuality == "AVERAGE"){
            imageViewACI.setImageResource(R.drawable.cellsignal_status_orange);
        }
        else if(signalQuality == "BAD"){
            imageViewACI.setImageResource(R.drawable.cell_signal_status_red);
        }
        else if(signalQuality == "VERY_BAD"){
            imageViewACI.setImageResource(R.drawable.cell_signal_status_red);
        }
        else if(signalQuality == "NONE"){
            imageViewACI.setImageResource(R.drawable.cell_no_signal_status);
        }

    }

    @Override
    public void actualizaLocationUI(String locationTxtString){
        Log.d("actualizaLocationUI","entra");
        textViewLocationInfo.setText(locationTxtString);
    }


}