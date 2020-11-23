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
    private int flag = 0;
    private ArrayList<String> allInfoInArray;


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
                UPDATE_LISTENER_MAIN_ACTIVITY.actualizaUiOnMainActivity(buttonStartCaptureTxt,buttonStartCaptureBool,signalQuality,phoneNetwork,phoneNetworkConection,operatorName,flag,allInfoInArray);
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
                    ArrayList<String> phoneServiceInfo = scanCellularActivity.getPhoneServiceInfo();
                    ArrayList<Integer> strengthInfo = scanCellularActivity.getDevStrengthSignal();
                    ArrayList<Integer> cellInfo = scanCellularActivity.getDevCellIdentity();
                    if(strengthInfo.isEmpty() ){
                        Log.d("ESVACIO","entra aca");
                        signalQuality = "BAD";
                    }
                    else {
                        signalQuality = scanCellularActivity.getSignalQuality(strengthInfo.get(0),phoneServiceInfo.get(7));
                        phoneNetwork = scanCellularActivity.getPhoneNetworType();
                        operatorName = scanCellularActivity.getDevSimOperatorName();
                        phoneNetworkConection = "Ninguna";
                        buttonStartCaptureTxt = "Sin Internet, Colectar red movil";
                        buttonStartCaptureBool = true;
                        flag = 1;
                        allInfoInArray = setAllVariables(phoneNetwork,phoneServiceInfo,strengthInfo,cellInfo);
                    }
                }
                else if (conectivityStatus == "STATE_IN_SERVICE" && internetStatus == "Conectado" && phoneSignalType ==  "GSM"){
                    ArrayList<String> phoneServiceInfo = scanCellularActivity.getPhoneServiceInfo();
                    ArrayList<Integer> strengthInfo = scanCellularActivity.getDevStrengthSignal();
                    ArrayList<Integer> cellInfo = scanCellularActivity.getDevCellIdentity();
                    if(strengthInfo.isEmpty() ){
                        Log.d("ESVACIO","entra aca");
                        signalQuality = "BAD";
                    }
                    else {
                        signalQuality = scanCellularActivity.getSignalQuality(strengthInfo.get(0),phoneServiceInfo.get(7));
                        phoneNetwork = scanCellularActivity.getPhoneNetworType();
                        operatorName = scanCellularActivity.getDevSimOperatorName();
                        phoneNetworkConection = scanCellularActivity.getNetworkConectivityType();
                        buttonStartCaptureTxt = "Iniciar Test de internet";
                        buttonStartCaptureBool = true;
                        flag = 2;
                        allInfoInArray = setAllVariables(phoneNetwork,phoneServiceInfo,strengthInfo,cellInfo);
                    }

                }
                else if (conectivityStatus == "STATE_OUT_OF_SERVICE" && internetStatus == "NoConectado"){
                    ArrayList<String> phoneServiceInfo = new ArrayList<>();
                    ArrayList<Integer> strengthInfo = new ArrayList<>();
                    ArrayList<Integer> cellInfo = new ArrayList<>();
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    operatorName = "Sin Servicio";
                    phoneNetworkConection = "Ninguna";
                    buttonStartCaptureTxt = "Sin Servicio";
                    buttonStartCaptureBool = false;
                    flag = 0;
                    allInfoInArray = setAllVariables(phoneNetwork,phoneServiceInfo,strengthInfo,cellInfo);
                }
                else if (conectivityStatus == "STATE_OUT_OF_SERVICE" && internetStatus == "Conectado") {
                    ArrayList<String> phoneServiceInfo = new ArrayList<>();
                    ArrayList<Integer> strengthInfo = new ArrayList<>();
                    ArrayList<Integer> cellInfo = new ArrayList<>();
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    phoneNetworkConection = scanCellularActivity.getNetworkConectivityType();
                    buttonStartCaptureTxt = "Sin servicio Movil, Medir WIFI";
                    buttonStartCaptureBool = true;
                    flag = 3;
                    allInfoInArray = setAllVariables(phoneNetwork,phoneServiceInfo,strengthInfo,cellInfo);

                }
                else if (conectivityStatus == "STATE_POWER_OFF" && internetStatus == "NoConectado"){
                    ArrayList<String> phoneServiceInfo = new ArrayList<>();
                    ArrayList<Integer> strengthInfo = new ArrayList<>();
                    ArrayList<Integer> cellInfo = new ArrayList<>();
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    operatorName = "Sin Servicio";
                    phoneNetworkConection = "Ninguna";
                    buttonStartCaptureTxt = "Modo Avión";
                    buttonStartCaptureBool = false;
                    flag = 0;
                    allInfoInArray = setAllVariables(phoneNetwork,phoneServiceInfo,strengthInfo,cellInfo);
                }
                else if (conectivityStatus == "STATE_POWER_OFF" && internetStatus == "Conectado"){
                    ArrayList<String> phoneServiceInfo = new ArrayList<>();
                    ArrayList<Integer> strengthInfo = new ArrayList<>();
                    ArrayList<Integer> cellInfo = new ArrayList<>();
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    operatorName = "Sin Servicio";
                    phoneNetworkConection = scanCellularActivity.getNetworkConectivityType();
                    buttonStartCaptureTxt = "Modo Avión, colectar WIFI";
                    buttonStartCaptureBool = true;
                    flag = 4;
                    allInfoInArray = setAllVariables(phoneNetwork,phoneServiceInfo,strengthInfo,cellInfo);
                }
                else{
                    ArrayList<String> phoneServiceInfo = new ArrayList<>();
                    ArrayList<Integer> strengthInfo = new ArrayList<>();
                    ArrayList<Integer> cellInfo = new ArrayList<>();
                    signalQuality = "NONE";
                    phoneNetwork = "Sin Servicio";
                    operatorName = "Sin Servicio";
                    phoneNetworkConection = "Ninguna";
                    buttonStartCaptureTxt = "Telefono No registrado";
                    buttonStartCaptureBool = false;
                    flag = 0;
                    allInfoInArray = setAllVariables(phoneNetwork,phoneServiceInfo,strengthInfo,cellInfo);
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

    /**Método para guardar en un array todas las variables correspondientes a información de teléfono, potencia de señal e información de celda*/
    private ArrayList<String> setAllVariables(String phoneNetwork,ArrayList<String> phoneServiceInfo,ArrayList<Integer> strengthInfo,ArrayList<Integer> cellinfo){
        ArrayList<String> allPhoneArray = new ArrayList<>();


        //4G
        if(phoneNetwork == "LTE" && strengthInfo.size() > 3) {
            allPhoneArray.add(phoneServiceInfo.get(0));//countryISO
            allPhoneArray.add(phoneServiceInfo.get(1));//phoneOperatorId
            allPhoneArray.add(phoneServiceInfo.get(2));//simOperatorId
            allPhoneArray.add(phoneServiceInfo.get(3));//operatorMcc
            allPhoneArray.add(phoneServiceInfo.get(4));//operatorMnc
            allPhoneArray.add(phoneServiceInfo.get(5));//isConected
            allPhoneArray.add(phoneServiceInfo.get(6));//phoneNetStandard
            allPhoneArray.add(phoneServiceInfo.get(7));//phoneNetTechnology
            allPhoneArray.add(phoneServiceInfo.get(8));//internetConNetwork
            allPhoneArray.add(strengthInfo.get(0).toString());//phoneSignalStrength
            allPhoneArray.add(strengthInfo.get(1).toString());//phoneAsuStrength
            allPhoneArray.add(strengthInfo.get(2).toString());//phoneSignalLevel
            allPhoneArray.add(scanCellularActivity.getSignalQuality(strengthInfo.get(0),phoneServiceInfo.get(7)));//signalQuality
            allPhoneArray.add(strengthInfo.get(3).toString());//RSRP: Reference Signal Received Power
            allPhoneArray.add(strengthInfo.get(5).toString());//TA: Timing Advance
            allPhoneArray.add(strengthInfo.get(6).toString());// CQI: Channel Quality Indicator
            allPhoneArray.add(strengthInfo.get(4).toString());//RSRQ: Reference Signal Received Quality
            allPhoneArray.add(cellinfo.get(0).toString());//PCI: Physical Cell ID (0-503)
            allPhoneArray.add(cellinfo.get(1).toString());//cellLteCid
            allPhoneArray.add(cellinfo.get(2).toString());//TAC: Tracking Area Code (16-bit)
            allPhoneArray.add(cellinfo.get(3).toString());//eNB: eNodeB Identifier (20-bit)
            allPhoneArray.add(cellinfo.get(4).toString());//EARFCN: E-UTRA Absolute Radio Frequency Channel Number (0-65535)
            allPhoneArray.add("NULL");//addcellBslat CDMA
            allPhoneArray.add("NULL");//addcellBslon CDMA
            allPhoneArray.add("NULL");//addcellSid   CDMA
            allPhoneArray.add("NULL");//addcellNid   CDMA
            allPhoneArray.add("NULL");//addcellBid   CDMA
            allPhoneArray.add("NULL");//addcellWcdmaLac		LAC: GSM Location Area (16-bit)
            allPhoneArray.add("NULL");//addcellWcdmaUcid	UCID UMTS Cell Identifier (28-bit)
            allPhoneArray.add("NULL");//addcellWcdmaUarfcn  ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add("NULL");//addcellWcdmaPsc     PSC: Primary Srambling Code of Cell
            allPhoneArray.add("NULL");//addcellWcdmaCid     CID UMTS Cell Identifier (28-bit)
            allPhoneArray.add("NULL");//addcellWcdmaRnc     RNC ID  Controlling RNC/BSS Identifier (12-bit)
            allPhoneArray.add("NULL");//addcellGsmArcfn     ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add("NULL");//addcellGsmLac       LAC: GSM Location Area (16-bit)
            allPhoneArray.add("NULL");//addcellGsmCid       CID: GSM Cell Identifier (16-bit, for Base transceiver station (BTS) or sector of a BTS)
        }
        //3G - 3.5G
        else if(phoneNetwork == "HSPA+" || phoneNetwork == "HSPA" || phoneNetwork == "UMTS"){
            allPhoneArray.add(phoneServiceInfo.get(0));//countryISO
            allPhoneArray.add(phoneServiceInfo.get(1));//phoneOperatorId
            allPhoneArray.add(phoneServiceInfo.get(2));//simOperatorId
            allPhoneArray.add(phoneServiceInfo.get(3));//operatorMcc
            allPhoneArray.add(phoneServiceInfo.get(4));//operatorMnc
            allPhoneArray.add(phoneServiceInfo.get(5));//isConected
            allPhoneArray.add(phoneServiceInfo.get(6));//phoneNetStandard
            allPhoneArray.add(phoneServiceInfo.get(7));//phoneNetTechnology
            allPhoneArray.add(phoneServiceInfo.get(8));//internetConNetwork
            allPhoneArray.add(strengthInfo.get(0).toString());//phoneSignalStrength
            allPhoneArray.add(strengthInfo.get(1).toString());//phoneAsuStrength
            allPhoneArray.add(strengthInfo.get(2).toString());//phoneSignalLevel
            allPhoneArray.add(scanCellularActivity.getSignalQuality(strengthInfo.get(0),phoneServiceInfo.get(7)));//signalQuality
            allPhoneArray.add("NULL");//RSRP: Reference Signal Received Power
            allPhoneArray.add("NULL");//RSRQ: Reference Signal Received Quality
            allPhoneArray.add("NULL");//TA: Timing Advance
            allPhoneArray.add("NULL");// CQI: Channel Quality Indicator
            allPhoneArray.add("NULL");//PCI: Physical Cell ID (0-503)
            allPhoneArray.add("NULL");//cellLteCid
            allPhoneArray.add("NULL");//TAC: Tracking Area Code (16-bit)
            allPhoneArray.add("NULL");//eNB: eNodeB Identifier (20-bit)
            allPhoneArray.add("NULL");//EARFCN: E-UTRA Absolute Radio Frequency Channel Number (0-65535)
            allPhoneArray.add("NULL");//addcellBslat CDMA
            allPhoneArray.add("NULL");//addcellBslon CDMA
            allPhoneArray.add("NULL");//addcellSid   CDMA
            allPhoneArray.add("NULL");//addcellNid   CDMA
            allPhoneArray.add("NULL");//addcellBid   CDMA
            allPhoneArray.add(cellinfo.get(0).toString());//addcellWcdmaLac		3G LAC: GSM Location Area (16-bit)
            allPhoneArray.add(cellinfo.get(1).toString());//addcellWcdmaUcid	3G UCID UMTS Cell Identifier (28-bit)
            allPhoneArray.add(cellinfo.get(2).toString());//addcellWcdmaUarfcn  3G ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add(cellinfo.get(3).toString());//addcellWcdmaPsc     3G PSC: Primary Srambling Code of Cell
            allPhoneArray.add(cellinfo.get(4).toString());//addcellWcdmaCid     3G CID UMTS Cell Identifier (28-bit)
            allPhoneArray.add(cellinfo.get(5).toString());//addcellWcdmaRnc     3G RNC ID  Controlling RNC/BSS Identifier (12-bit)
            allPhoneArray.add("NULL");//addcellGsmArcfn     2G ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add("NULL");//addcellGsmLac       2G LAC: GSM Location Area (16-bit)
            allPhoneArray.add("NULL");//addcellGsmCid       2G CID: GSM Cell Identifier (16-bit, for Base transceiver station (BTS) or sector of a BTS)
        }
        //2G
        else if(phoneNetwork == "GSM" || phoneNetwork == "GPRS" || phoneNetwork == "EDGE"){
            allPhoneArray.add(phoneServiceInfo.get(0));//countryISO
            allPhoneArray.add(phoneServiceInfo.get(1));//phoneOperatorId
            allPhoneArray.add(phoneServiceInfo.get(2));//simOperatorId
            allPhoneArray.add(phoneServiceInfo.get(3));//operatorMcc
            allPhoneArray.add(phoneServiceInfo.get(4));//operatorMnc
            allPhoneArray.add(phoneServiceInfo.get(5));//isConected
            allPhoneArray.add(phoneServiceInfo.get(6));//phoneNetStandard
            allPhoneArray.add(phoneServiceInfo.get(7));//phoneNetTechnology
            allPhoneArray.add(phoneServiceInfo.get(8));//internetConNetwork
            allPhoneArray.add(strengthInfo.get(0).toString());//phoneSignalStrength
            allPhoneArray.add(strengthInfo.get(1).toString());//phoneAsuStrength
            allPhoneArray.add(strengthInfo.get(2).toString());//phoneSignalLevel
            allPhoneArray.add(scanCellularActivity.getSignalQuality(strengthInfo.get(0),phoneServiceInfo.get(7)));//signalQuality
            allPhoneArray.add("NULL");//RSRP: Reference Signal Received Power
            allPhoneArray.add("NULL");//RSRQ: Reference Signal Received Quality
            allPhoneArray.add("NULL");//TA: Timing Advance
            allPhoneArray.add("NULL");// CQI: Channel Quality Indicator
            allPhoneArray.add("NULL");//PCI: Physical Cell ID (0-503)
            allPhoneArray.add("NULL");//cellLteCid
            allPhoneArray.add("NULL");//TAC: Tracking Area Code (16-bit)
            allPhoneArray.add("NULL");//eNB: eNodeB Identifier (20-bit)
            allPhoneArray.add("NULL");//EARFCN: E-UTRA Absolute Radio Frequency Channel Number (0-65535)
            allPhoneArray.add("NULL");//addcellBslat CDMA
            allPhoneArray.add("NULL");//addcellBslon CDMA
            allPhoneArray.add("NULL");//addcellSid   CDMA
            allPhoneArray.add("NULL");//addcellNid   CDMA
            allPhoneArray.add("NULL");//addcellBid   CDMA
            allPhoneArray.add("NULL");//addcellWcdmaLac		3G LAC: GSM Location Area (16-bit)
            allPhoneArray.add("NULL");//addcellWcdmaUcid	3G UCID UMTS Cell Identifier (28-bit)
            allPhoneArray.add("NULL");//addcellWcdmaUarfcn  3G ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add("NULL");//addcellWcdmaPsc     3G PSC: Primary Srambling Code of Cell
            allPhoneArray.add("NULL");//addcellWcdmaCid     3G CID UMTS Cell Identifier (28-bit)
            allPhoneArray.add("NULL");//addcellWcdmaRnc     3G RNC ID  Controlling RNC/BSS Identifier (12-bit)
            allPhoneArray.add(cellinfo.get(0).toString());//addcellGsmArcfn     2G ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add(cellinfo.get(1).toString());//addcellGsmLac       2G LAC: GSM Location Area (16-bit)
            allPhoneArray.add(cellinfo.get(2).toString());//addcellGsmCid       2G CID: GSM Cell Identifier (16-bit, for Base transceiver station (BTS) or sector of a BTS)
        }
        //CDMA - No presente en Ecuador
        else if(phoneNetwork == "CDMA" || phoneNetwork == "EVDO_0" || phoneNetwork == "EVDO_A" || phoneNetwork == "EVDO_B"){
            allPhoneArray.add(phoneServiceInfo.get(0));//countryISO
            allPhoneArray.add(phoneServiceInfo.get(1));//phoneOperatorId
            allPhoneArray.add(phoneServiceInfo.get(2));//simOperatorId
            allPhoneArray.add(phoneServiceInfo.get(3));//operatorMcc
            allPhoneArray.add(phoneServiceInfo.get(4));//operatorMnc
            allPhoneArray.add(phoneServiceInfo.get(5));//isConected
            allPhoneArray.add(phoneServiceInfo.get(6));//phoneNetStandard
            allPhoneArray.add(phoneServiceInfo.get(7));//phoneNetTechnology
            allPhoneArray.add(phoneServiceInfo.get(8));//internetConNetwork
            allPhoneArray.add(strengthInfo.get(0).toString());//phoneSignalStrength
            allPhoneArray.add(strengthInfo.get(1).toString());//phoneAsuStrength
            allPhoneArray.add(strengthInfo.get(2).toString());//phoneSignalLevel
            allPhoneArray.add(scanCellularActivity.getSignalQuality(strengthInfo.get(0),phoneServiceInfo.get(7)));//signalQuality
            allPhoneArray.add("NULL");//RSRP: Reference Signal Received Power
            allPhoneArray.add("NULL");//RSRQ: Reference Signal Received Quality
            allPhoneArray.add("NULL");//TA: Timing Advance
            allPhoneArray.add("NULL");// CQI: Channel Quality Indicator
            allPhoneArray.add("NULL");//PCI: Physical Cell ID (0-503)
            allPhoneArray.add("NULL");//cellLteCid
            allPhoneArray.add("NULL");//TAC: Tracking Area Code (16-bit)
            allPhoneArray.add("NULL");//eNB: eNodeB Identifier (20-bit)
            allPhoneArray.add("NULL");//EARFCN: E-UTRA Absolute Radio Frequency Channel Number (0-65535)
            allPhoneArray.add(cellinfo.get(0).toString());//addcellBslat CDMA
            allPhoneArray.add(cellinfo.get(1).toString());//addcellBslon CDMA
            allPhoneArray.add(cellinfo.get(2).toString());//addcellSid   CDMA
            allPhoneArray.add(cellinfo.get(3).toString());//addcellNid   CDMA
            allPhoneArray.add(cellinfo.get(4).toString());//addcellBid   CDMA
            allPhoneArray.add("NULL");//addcellWcdmaLac		3G LAC: GSM Location Area (16-bit)
            allPhoneArray.add("NULL");//addcellWcdmaUcid	3G UCID UMTS Cell Identifier (28-bit)
            allPhoneArray.add("NULL");//addcellWcdmaUarfcn  3G ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add("NULL");//addcellWcdmaPsc     3G PSC: Primary Srambling Code of Cell
            allPhoneArray.add("NULL");//addcellWcdmaCid     3G CID UMTS Cell Identifier (28-bit)
            allPhoneArray.add("NULL");//addcellWcdmaRnc     3G RNC ID  Controlling RNC/BSS Identifier (12-bit)
            allPhoneArray.add("NULL");//addcellGsmArcfn     2G ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add("NULL");//addcellGsmLac       2G LAC: GSM Location Area (16-bit)
            allPhoneArray.add("NULL");//addcellGsmCid       2G CID: GSM Cell Identifier (16-bit, for Base transceiver station (BTS) or sector of a BTS)
        }
        else{
            allPhoneArray.add("NULL");//countryISO
            allPhoneArray.add("NULL");//phoneOperatorId
            allPhoneArray.add("NULL");//simOperatorId
            allPhoneArray.add("NULL");//operatorMcc
            allPhoneArray.add("NULL");//operatorMnc
            allPhoneArray.add("NULL");//isConected
            allPhoneArray.add("NULL");//phoneNetStandard
            allPhoneArray.add("NULL");//phoneNetTechnology
            allPhoneArray.add("NULL");//internetConNetwork
            allPhoneArray.add("NULL");//phoneSignalStrength
            allPhoneArray.add("NULL");//phoneAsuStrength
            allPhoneArray.add("NULL");//phoneSignalLevel
            allPhoneArray.add("NULL");//signalQuality
            allPhoneArray.add("NULL");//RSRP: Reference Signal Received Power
            allPhoneArray.add("NULL");//RSRQ: Reference Signal Received Quality
            allPhoneArray.add("NULL");//TA: Timing Advance
            allPhoneArray.add("NULL");// CQI: Channel Quality Indicator
            allPhoneArray.add("NULL");//PCI: Physical Cell ID (0-503)
            allPhoneArray.add("NULL");//cellLteCid
            allPhoneArray.add("NULL");//TAC: Tracking Area Code (16-bit)
            allPhoneArray.add("NULL");//eNB: eNodeB Identifier (20-bit)
            allPhoneArray.add("NULL");//EARFCN: E-UTRA Absolute Radio Frequency Channel Number (0-65535)
            allPhoneArray.add("NULL");//addcellBslat CDMA
            allPhoneArray.add("NULL");//addcellBslon CDMA
            allPhoneArray.add("NULL");//addcellSid   CDMA
            allPhoneArray.add("NULL");//addcellNid   CDMA
            allPhoneArray.add("NULL");//addcellBid   CDMA
            allPhoneArray.add("NULL");//addcellWcdmaLac		3G LAC: GSM Location Area (16-bit)
            allPhoneArray.add("NULL");//addcellWcdmaUcid	3G UCID UMTS Cell Identifier (28-bit)
            allPhoneArray.add("NULL");//addcellWcdmaUarfcn  3G ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add("NULL");//addcellWcdmaPsc     3G PSC: Primary Srambling Code of Cell
            allPhoneArray.add("NULL");//addcellWcdmaCid     3G CID UMTS Cell Identifier (28-bit)
            allPhoneArray.add("NULL");//addcellWcdmaRnc     3G RNC ID  Controlling RNC/BSS Identifier (12-bit)
            allPhoneArray.add("NULL");//addcellGsmArcfn     2G ARFCN: Absolute Radio Frequency Channel Number (0-1023)
            allPhoneArray.add("NULL");//addcellGsmLac       2G LAC: GSM Location Area (16-bit)
            allPhoneArray.add("NULL");//addcellGsmCid       2G CID: GSM Cell Identifier (16-bit, for Base transceiver station (BTS) or sector of a BTS)
        }
        return allPhoneArray;

    }

}
