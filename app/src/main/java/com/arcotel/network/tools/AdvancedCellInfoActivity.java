package com.arcotel.network.tools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arcotel.network.tools.services.CellularCoverageService;

public class AdvancedCellInfoActivity extends AppCompatActivity {

    private TextView textViewStrengthInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_cell_info);
        textViewStrengthInfo = (TextView) findViewById(R.id.textViewStrengthInfo);
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

        CellularCoverageService.setUpdateListener(this);
        iniciarCellularCoverageService();
        Toast.makeText(this, "AdvancedCellInfoActivity onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "AdvancedCellInfoActivity onPause", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "AdvancedCellInfoActivity onStop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "AdvancedCellInfoActivity onDestroy", Toast.LENGTH_LONG).show();
        pararCellularCoverageService();
    }

    /**
     * Inicia el servicio
     */

    private void iniciarCellularCoverageService() {
        Intent service = new Intent(this, CellularCoverageService.class);
        startService(service);
    }

    /**
     * Finaliza el servicio
     */

    private void pararCellularCoverageService() {
        Intent service = new Intent(this, CellularCoverageService.class);
        stopService(service);
    }

    /**
     * Actualiza en la interfaz de usuario los valores de potencia de señal
     *
     * @param cellStrengthRsrp
     */

    public void actualizaRsrp(String cellStrengthRsrp,String cellStrengthRssnr,String cellStrengthRsrq) {
        textViewStrengthInfo.setText("Rsrp :"+cellStrengthRsrp+" dBm" +
                "\nRsrq: "+cellStrengthRsrq+" dB" +
                "\nRssnr: "+cellStrengthRssnr+" dB");

    }

}