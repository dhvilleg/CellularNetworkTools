package com.arcotel.network.tools.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.RequiresApi;

import com.arcotel.network.tools.AdvancedCellInfoActivity;
import com.arcotel.network.tools.librarys.LocationAddress;
import com.arcotel.network.tools.librarys.ScanCellularActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AdvancedCellInfoUIService extends Service {
    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 500; // En ms
    public static AdvancedCellInfoActivity UPDATE_LISTENER_ADVANCED_ACTIVITY;
    private Handler handler;
    //Definicion de variables de entorno para actualizacion de UI
    private String operatorTxtString = "";
    private String strengthTxtString = "";
    private String cellIdTxtString = "";
    private String internetTxtString = "";
    private String locationTxtString = "";
    private String signalQuality = "";
    private String phoneNetwork = "";
    private String operatorName = "";



    private ScanCellularActivity scanCellularActivity;
    private LocationAddress locationAddress;


    public AdvancedCellInfoUIService() {
        scanCellularActivity = new ScanCellularActivity(UPDATE_LISTENER_ADVANCED_ACTIVITY.getApplicationContext());
        //locationAddress = new LocationAddress(UPDATE_LISTENER_ADVANCED_ACTIVITY.getApplicationContext());
    }
    public static void setUpdateListener(AdvancedCellInfoActivity conectivityScanService) {
        UPDATE_LISTENER_ADVANCED_ACTIVITY = conectivityScanService;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate() {
        super.onCreate();
        iniciarCapturaEstadoConexion();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER_ADVANCED_ACTIVITY.actualizaUIOnAdvancedInfoActivity(signalQuality,phoneNetwork,operatorName,operatorTxtString,strengthTxtString,cellIdTxtString,internetTxtString);
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
                String conectivityStatus ="";
                String internetStatus ="";
                String phoneSignalType = "";

                if(scanCellularActivity.getDevIsConected()){
                    internetStatus = "Conectado";
                }
                else{
                    internetStatus = "NoConectado";
                }
                conectivityStatus = scanCellularActivity.serviceStateChanged();
                phoneSignalType = scanCellularActivity.getPhoneSignalType();
                if (conectivityStatus == "STATE_IN_SERVICE" && phoneSignalType == "GSM") {
                    phoneNetwork = scanCellularActivity.getPhoneNetworType();
                    operatorName = scanCellularActivity.getDevSimOperatorName();
                    //Abre seccion operador
                    operatorTxtString = "Operador: "+operatorName+"\nMcc: "+scanCellularActivity.getDevMccId()+"\nMnc: "+scanCellularActivity.getDevMncId();
                    //Seccion strengthTxtString = "---"; & cellIdTxtString = "---";
                    ArrayList<Integer> strengthInfo = scanCellularActivity.getDevStrengthSignal();
                    ArrayList<Integer> cellIdInfo = scanCellularActivity.getDevCellIdentity();
                    if(phoneNetwork == "HSPA+" || phoneNetwork == "HSPA" || phoneNetwork == "UMTS"){
                        if(strengthInfo.isEmpty() ){
                            signalQuality = "BAD";
                            strengthTxtString = "---";
                        }
                        else {
                            signalQuality = scanCellularActivity.getSignalQuality(strengthInfo.get(0),phoneNetwork);
                            strengthTxtString = "RSCP: "+strengthInfo.get(0)+"dBm\nASU: "+strengthInfo.get(1)+"dBm";
                        }
                        if(cellIdInfo.isEmpty() ){
                            signalQuality = "BAD";
                            cellIdTxtString = "---";
                        }
                        else{
                            cellIdTxtString = "LAC-UCID: "+cellIdInfo.get(0)+"-"+cellIdInfo.get(1)+"\nRNC-CID: "+cellIdInfo.get(4)+"-"+cellIdInfo.get(3)+"\nPSC: "+cellIdInfo.get(2);
                        }
                    }
                    else if(phoneNetwork == "LTE"){
                        if(strengthInfo.isEmpty() ){
                            signalQuality = "BAD";
                            strengthTxtString = "---";
                        }
                        else {
                            signalQuality = scanCellularActivity.getSignalQuality(strengthInfo.get(0),phoneNetwork);
                            strengthTxtString = "RSRP: "+strengthInfo.get(3)+"dBm\nRSRQ: "+strengthInfo.get(4);
                        }
                        if(cellIdInfo.isEmpty() ){
                            signalQuality = "BAD";
                            cellIdTxtString = "---";
                        }
                        else{
                            cellIdTxtString = "PCI-TAC: "+cellIdInfo.get(0)+"-"+cellIdInfo.get(1)+"\neNB-LCID: "+cellIdInfo.get(2)+"-"+cellIdInfo.get(3)+"\nEARFCN: "+cellIdInfo.get(4);
                        }
                    }
                    else if(phoneNetwork == "GSM" || phoneNetwork == "GPRS" || phoneNetwork == "EDGE"){
                        if(strengthInfo.isEmpty() ){
                            signalQuality = "BAD";
                            strengthTxtString = "---";
                        }
                        else {
                            signalQuality = scanCellularActivity.getSignalQuality(strengthInfo.get(0),phoneNetwork);
                            strengthTxtString = "Rx Level: "+strengthInfo.get(0)+"dBm\nASU: "+strengthInfo.get(1)+"dBm";
                        }
                        if(cellIdInfo.isEmpty() ){
                            signalQuality = "BAD";
                            cellIdTxtString = "---";
                        }
                        else{
                            cellIdTxtString = "LAC: "+cellIdInfo.get(0)+"\nCID: "+cellIdInfo.get(1)+"\nEARFCN: "+cellIdInfo.get(2);
                        }
                    }
                    else {
                        strengthTxtString = "---";
                        cellIdTxtString = "---";
                    }
                    //Seccion Internet
                    if(internetStatus == "NoConectado"){
                        internetTxtString = "---";
                    }
                    else{
                        internetTxtString = scanCellularActivity.getNetworkConectivityType();
                    }

                }
                else{
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    operatorName = "Sin Servicio";
                    strengthTxtString = "---";
                    cellIdTxtString = "---";
                    internetTxtString = "---";
                    //locationTxtString = "---";
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
