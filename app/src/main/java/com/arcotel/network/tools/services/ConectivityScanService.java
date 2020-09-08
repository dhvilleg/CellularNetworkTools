package com.arcotel.network.tools.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.arcotel.network.tools.MainActivity;
import com.arcotel.network.tools.librarys.ScanCellularActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ConectivityScanService extends Service {

    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 1000; // En ms
    public static MainActivity UPDATE_LISTENER_MAIN_ACTIVITY;
    private Handler handler;
    private String conectivityStatus ="";
    private String internetStatus ="";
    private String signalQuality = "";
    private String phoneNetwork = "";
    private String phoneNetworkConection = "";
    private String operatorName = "";
    private String buttonStartCaptureTxt = "";
    private boolean buttonStartCaptureBool;
    private String phoneSignalType = "";


    private ScanCellularActivity scanCellularActivity;

    public ConectivityScanService() {
        scanCellularActivity = new ScanCellularActivity(UPDATE_LISTENER_MAIN_ACTIVITY.getApplicationContext());
    }
    public static void setUpdateListener(MainActivity conectivityScanService) {
        UPDATE_LISTENER_MAIN_ACTIVITY = conectivityScanService;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate() {
        super.onCreate();
        iniciarCapturaEstadoConexion();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER_MAIN_ACTIVITY.actualizaUiOnMainActivity(buttonStartCaptureTxt,buttonStartCaptureBool,signalQuality,phoneNetwork,phoneNetworkConection,operatorName);
            }
        };
    }

    @Override
    public void onDestroy() {
        pararCronometro();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private void iniciarCapturaEstadoConexion() {

        temporizador.scheduleAtFixedRate(new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                if(scanCellularActivity.getDevIsConected()){
                    internetStatus = "Conectado";
                }
                else{
                    internetStatus = "NoConectado";
                }
                phoneSignalType = scanCellularActivity.getPhoneSignalType();
                conectivityStatus = scanCellularActivity.serviceStateChanged();
                if (conectivityStatus == "STATE_IN_SERVICE" && internetStatus == "NoConectado" && phoneSignalType ==  "GSM") {
                    ArrayList<Integer> strengthInfo = scanCellularActivity.getDevStrengthSignal();
                    if(strengthInfo.isEmpty() ){
                        Log.d("ESVACIO","entra aca");
                        signalQuality = "BAD";
                    }
                    else {
                        signalQuality = scanCellularActivity.getSignalQuality(strengthInfo.get(0));
                    }
                    phoneNetwork = scanCellularActivity.getPhoneNetworType();
                    operatorName = scanCellularActivity.getDevSimOperatorName();
                    phoneNetworkConection = "Ninguna";
                    buttonStartCaptureTxt = "Colectar solo red movil";
                    buttonStartCaptureBool = true;
                }
                else if (conectivityStatus == "STATE_IN_SERVICE" && internetStatus == "Conectado" && phoneSignalType ==  "GSM"){
                    ArrayList<Integer> strengthInfo = scanCellularActivity.getDevStrengthSignal();
                    if(strengthInfo.isEmpty() ){
                        Log.d("ESVACIO","entra aca");
                        signalQuality = "BAD";
                    }
                    else {
                        signalQuality = scanCellularActivity.getSignalQuality(strengthInfo.get(0));
                    }
                    phoneNetwork = scanCellularActivity.getPhoneNetworType();
                    operatorName = scanCellularActivity.getDevSimOperatorName();
                    phoneNetworkConection = scanCellularActivity.getNetworkConectivityType();
                    buttonStartCaptureTxt = "Preparado";
                    buttonStartCaptureBool = true;
                }
                else if (conectivityStatus == "STATE_OUT_OF_SERVICE" && internetStatus == "NoConectado"){
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    operatorName = "Sin Servicio";
                    phoneNetworkConection = "Ninguna";
                    buttonStartCaptureTxt = "Sin Servicio";
                    buttonStartCaptureBool = false;
                }
                else if (conectivityStatus == "STATE_OUT_OF_SERVICE" && internetStatus == "Conectado") {
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    phoneNetworkConection = scanCellularActivity.getNetworkConectivityType();
                    buttonStartCaptureTxt = "Sin Servicio Movil, Medir WIFI";
                    buttonStartCaptureBool = false;

                }
                else if (conectivityStatus == "STATE_POWER_OFF" && internetStatus == "NoConectado"){
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    operatorName = "Sin Servicio";
                    phoneNetworkConection = "Ninguna";
                    buttonStartCaptureTxt = "Modo Avión";
                    buttonStartCaptureBool = false;
                }
                else if (conectivityStatus == "STATE_POWER_OFF" && internetStatus == "Conectado"){
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    operatorName = "Sin Servicio";
                    phoneNetworkConection = scanCellularActivity.getNetworkConectivityType();
                    buttonStartCaptureTxt = "Modo Avión, Medir WIFI";
                    buttonStartCaptureBool = false;
                }
                else{
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    operatorName = "Sin Servicio";
                    phoneNetworkConection = "Ninguna";
                    buttonStartCaptureTxt = "Telefono No registrado";
                    buttonStartCaptureBool = false;
                }
               handler.sendEmptyMessage(0);
                //Log.d("ValorPotencia","El estado es :"+ strengthInfo.get(0)+" red conectada "+phoneNetworkConection);
            }
        }, 0, INTERVALO_ACTUALIZACION);
    }


    private void pararCronometro() {
        if (temporizador != null)
            temporizador.cancel();
    }

}
