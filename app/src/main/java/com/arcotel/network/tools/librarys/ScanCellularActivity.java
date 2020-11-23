package com.arcotel.network.tools.librarys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.arcotel.network.tools.GetSpeedTestHostsHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ScanCellularActivity {




    String ispName = "---";
    String ipAddress = "---";

    private TelephonyManager    telephonyManager;
    private final static String LTE_TAG             = "LTE_Tag";
    private final static String LTE_SIGNAL_STRENGTH = "getLteSignalStrength";
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;

    //TelephonyManager telephonyManager;
    ConnectivityManager connMgr;
    Context context;
    NetworkInfo networkInfo;

    private double snr = 0.1;

    public ScanCellularActivity(Context context) {
        this.context = context;
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        this.connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);

    }

    public String getPhoneSignalType() {
        String varPhoneSignalType = "";
        int phoneType = telephonyManager.getPhoneType();
        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                varPhoneSignalType = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                varPhoneSignalType = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                varPhoneSignalType = "NONE";
                break;
        }
        return varPhoneSignalType;
    }

    public String getPhoneNetworType() {
        String varPhoneNetworType = "";
        int phoneNetType = telephonyManager.getNetworkType();
        switch (phoneNetType) {
            case 7:
                varPhoneNetworType = "1xRTT";
                break;
            case 4:
                varPhoneNetworType = "CDMA";
                break;
            case 2:
                varPhoneNetworType = "EDGE";
                break;
            case 16:
                varPhoneNetworType = "GSM";
                break;
            case 14:
                varPhoneNetworType = "eHRPD";
                break;
            case 5:
                varPhoneNetworType = "EVDO_0";
                break;
            case 6:
                varPhoneNetworType = "EVDO_A";
                break;
            case 12:
                varPhoneNetworType = "EVDO_B";
                break;
            case 1:
                varPhoneNetworType = "GPRS";
                break;
            case 8:
                varPhoneNetworType = "HSDPA";
                break;
            case 10:
                varPhoneNetworType = "HSPA";
                break;
            case 15:
                varPhoneNetworType = "HSPA+";
                break;
            case 9:
                varPhoneNetworType = "HSUPA";
                break;
            case 11:
                varPhoneNetworType = "iDen";
                break;
            case 13:
                varPhoneNetworType = "LTE";
                break;
            case 3:
                varPhoneNetworType = "UMTS";
                break;
            case 0:
                varPhoneNetworType = "Unknown";
                break;
        }
        return varPhoneNetworType;
    }

    public String getDevSimOperatorName(){
        String simOperatorId = "";
        simOperatorId = telephonyManager.getSimOperatorName();
        return simOperatorId;
    }

    public String getDevMccId() {
        String operatorMccMnc = "";
        operatorMccMnc = telephonyManager.getNetworkOperator().substring(0, 3);
        return operatorMccMnc;

    }

    public String getDevMncId() {
        String operatorMccMnc = "";
        operatorMccMnc = telephonyManager.getNetworkOperator().substring(3);
        return operatorMccMnc;

    }

    /*public boolean getDevIsConected() {

        try {
            InetAddress address = InetAddress.getByName("www.speedtest.net");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error

        }
        return false;
    }*/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean getDevIsConected(){
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL(
                    "http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            Log.d("getDevIsConected","entra aca al final");
            urlc.setConnectTimeout(1000);
            urlc.setReadTimeout(1000);
            urlc.connect();
            // networkcode2 = urlc.getResponseCode();
            return (urlc.getResponseCode() == 200);
        } catch (IOException e) {
            return (false);
        }
    }

    public String getDevCountryIso() {
        String countryIso = telephonyManager.getNetworkCountryIso();
        return countryIso;
    }

    public String getDevOperatorName() {
        String operatorName = telephonyManager.getSimOperatorName();
        return operatorName;

    }

    public ArrayList<String> getPhoneServiceInfo() {
        ArrayList<String> operatorInfo = new ArrayList<String>();
        operatorInfo.add(telephonyManager.getNetworkCountryIso());
        operatorInfo.add(telephonyManager.getNetworkOperatorName());
        operatorInfo.add(telephonyManager.getSimOperatorName());
        operatorInfo.add(getDevMccId());
        operatorInfo.add(getDevMncId());
        operatorInfo.add(""+getDevIsConected());
        operatorInfo.add(getPhoneSignalType());
        operatorInfo.add(getPhoneNetworType());
        operatorInfo.add(getNetworkConectivityType());
        return operatorInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getDevMmsProfile() {
        String mmsProfile = telephonyManager.getMmsUserAgent();
        return mmsProfile;
    }

    public String getDevOperatorID() {
        String operatoId = telephonyManager.getNetworkOperator();
        return operatoId;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String serviceStateChanged(){
        String phonestateString = "";
        int phonestateInt = telephonyManager.getServiceState().getState();

        if(phonestateInt == 0){
            phonestateString = "STATE_IN_SERVICE";
        }
        else if(phonestateInt == 1){
            phonestateString = "STATE_OUT_OF_SERVICE";
        }
        else if(phonestateInt == 2){
            phonestateString = "STATE_EMERGENCY_ONLY";
        }
        else if(phonestateInt == 3){
            phonestateString = "STATE_POWER_OFF";
        }

        //Log.d("serviceStateChanged","valor Estado "+phonestate);

        return phonestateString;
    }

    public void getLteRssnr(){

        telephonyManager.listen(new PhoneStateListener(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onSignalStrengthsChanged(SignalStrength signalStrength)
            {
                try {
                    snr = (double) ((Integer) CellSignalStrengthLte.class.getMethod("getLteRssnr").invoke(signalStrength)/10D);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                Log.d("en ScanCellularAct","valor señar ruido es "+ snr );

            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ArrayList<Integer> getDevStrengthSignal(){

        ArrayList<Integer> strength = new ArrayList<>();
        @SuppressLint("MissingPermission") List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if(cellInfos != null) {
            for (int i = 0 ; i < cellInfos.size() ; i++) {
                if (cellInfos.get(i).isRegistered()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        if (cellInfos.get(i) instanceof CellInfoWcdma) {
                            //Log.d("ANALI>AR ESTO "," el valor de phoneNetworType es CellInfoWcdma");
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfos.get(i);
                            CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                            strength.add(cellSignalStrengthWcdma.getDbm());
                            strength.add(cellSignalStrengthWcdma.getAsuLevel());
                            strength.add(cellSignalStrengthWcdma.getLevel());
                        } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                            //Log.d("ANALI>AR ESTO "," el valor de phoneNetworType es CellInfoGsm");
                            CellInfoGsm cellInfogsm = (CellInfoGsm) cellInfos.get(i);
                            CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                            strength.add(cellSignalStrengthGsm.getDbm());
                            strength.add(cellSignalStrengthGsm.getAsuLevel());
                            strength.add(cellSignalStrengthGsm.getLevel());
                        } else if (cellInfos.get(i) instanceof CellInfoLte) {
                            //Log.d("ANALI>AR ESTO "," el valor de phoneNetworType es CellInfoLte");
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfos.get(i);
                            CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                            strength.add(cellSignalStrengthLte.getDbm());
                            strength.add(cellSignalStrengthLte.getAsuLevel());
                            strength.add(cellSignalStrengthLte.getLevel());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                strength.add(cellSignalStrengthLte.getRsrp()); //RSRP: Reference Signal Received Power
                                strength.add(cellSignalStrengthLte.getRsrq()); //RSRQ: Reference Signal Received Quality
                                //strength.add(getLteRssnr(telephonyManager));
                                int ta = cellSignalStrengthLte.getTimingAdvance();
                                if (ta == 2147483647){
                                    strength.add(0); //TA: Timing Advance
                                }
                                else{
                                    strength.add(ta); //TA: Timing Advance
                                }
                                int cqi = cellSignalStrengthLte.getCqi();
                                if (cqi == 2147483647){
                                    strength.add(0);// CQI: Channel Quality Indicator
                                }
                                else{
                                    strength.add(cqi);// CQI: Channel Quality Indicator
                                }

                            }
                        } else if (cellInfos.get(i) instanceof CellInfoCdma) {
                            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfos.get(i);
                            CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
                            strength.add(cellSignalStrengthCdma.getDbm());
                            strength.add(cellSignalStrengthCdma.getAsuLevel());
                            strength.add(cellSignalStrengthCdma.getLevel());
                        }
                    }
                }
            }
        }
        return strength;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ArrayList<Integer> getDevCellIdentity(){
        ArrayList<Integer> cellItentity = new ArrayList<Integer>();
        @SuppressLint("MissingPermission") List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if(cellInfos != null) {
            for (int i = 0 ; i < cellInfos.size() ; i++) {
                if (cellInfos.get(i).isRegistered()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        if (cellInfos.get(i) instanceof CellInfoWcdma) {
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfos.get(i);
                            CellIdentityWcdma identityWcdma = cellInfoWcdma.getCellIdentity();
                            cellItentity.add(identityWcdma.getLac()); //LAC: GSM Location Area (16-bit)
                            cellItentity.add(identityWcdma.getCid()); // UCID UMTS Cell Identifier (28-bit) ?
                            cellItentity.add(identityWcdma.getPsc()); //PSC: Primary Srambling Code of Cell
                            cellItentity.add(identityWcdma.getCid() & 0xffff); //CID UMTS Cell Identifier (28-bit)
                            cellItentity.add((identityWcdma.getCid() >> 16) & 0xffff); //RNC ID  Controlling RNC/BSS Identifier (12-bit)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                cellItentity.add(identityWcdma.getUarfcn()); //ARFCN: Absolute Radio Frequency Channel Number (0-1023)
                            }
                        } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                            CellInfoGsm cellInfogsm = (CellInfoGsm) cellInfos.get(i);
                            CellIdentityGsm identityGsm = cellInfogsm.getCellIdentity();
                            cellItentity.add(identityGsm.getLac());
                            cellItentity.add(identityGsm.getCid());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                cellItentity.add(identityGsm.getArfcn()); //ARFCN: Absolute Radio Frequency Channel Number (0-1023)
                            }
                        } else if (cellInfos.get(i) instanceof CellInfoLte) {
                            String hexPivote = "";
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfos.get(i);
                            CellIdentityLte identityLte = cellInfoLte.getCellIdentity();
                            cellItentity.add(identityLte.getPci()); //PCI: Physical Cell ID (0-503)
                            hexPivote = Integer.toHexString(identityLte.getCi());
                            String [] cadena = hexPivote.split("");
                            hexPivote = cadena[cadena.length-2]+cadena[cadena.length -1];
                            int lteCid=Integer.parseInt(hexPivote,16);
                            cellItentity.add(lteCid);//cellLteCid
                            cellItentity.add(identityLte.getTac()); //TAC: Tracking Area Code (16-bit)
                            cellItentity.add(identityLte.getCi() >> 8); //eNB: eNodeB Identifier (20-bit)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                cellItentity.add(identityLte.getEarfcn()); //EARFCN: E-UTRA Absolute Radio Frequency Channel Number (0-65535)
                            }
                        } else if (cellInfos.get(i) instanceof CellInfoCdma) {
                            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfos.get(i);
                            CellIdentityCdma identityCdma = cellInfoCdma.getCellIdentity();
                            cellItentity.add(identityCdma.getLatitude()); //BSLat: Base station latitude
                            cellItentity.add(identityCdma.getLongitude()); //BSLon: Base station longitude
                            cellItentity.add(identityCdma.getSystemId());//SID: System Identifier (15-bit)
                            cellItentity.add(identityCdma.getNetworkId());//NID: Network Identifier (16-bit)
                            cellItentity.add(identityCdma.getBasestationId());//BID: Base Station Identifier (16-bit)
                        }
                    }
                }
            }
        }
        return cellItentity;
    }

    public String getSignalQuality(int dbm, String phoneNetwork){

        int NEGRO=0;
        int ROJO=0;
        int AMARILLO=0;
        int VERDE=0;

        if(phoneNetwork == "LTE"){
            VERDE = -75;
            AMARILLO = -80;
            ROJO = -100;
            NEGRO = -130;
        }
        else if(phoneNetwork == "HSPA+" || phoneNetwork == "HSPA" || phoneNetwork == "UMTS"){
            VERDE = -75;
            AMARILLO = -80;
            ROJO = -98;
            NEGRO = -120;
        }
        else if(phoneNetwork == "GSM" || phoneNetwork == "GPRS" || phoneNetwork == "EDGE"){
            VERDE = -75;
            AMARILLO = -85;
            ROJO = -98;
            NEGRO = -110;
        }
        else if(phoneNetwork == "CDMA" || phoneNetwork == "EVDO_0" || phoneNetwork == "EVDO_A" || phoneNetwork == "EVDO_B"){
            VERDE = -75;
            AMARILLO = -85;
            ROJO = -98;
            NEGRO = -110;
        }
        else{
            VERDE = 0;
            AMARILLO = 0;
            ROJO = 0;
            NEGRO = 0;
        }

        String signalQuality="";
        if(dbm >= VERDE){
            signalQuality = "VERY_GOOD";
        }
        else if(dbm >=AMARILLO){
            signalQuality = "GOOD";
        }
        else if(dbm >= ROJO && dbm < AMARILLO ){
            signalQuality = "AVERAGE";
        }else if(dbm >= NEGRO && dbm < ROJO ){
            signalQuality = "BAD";
        }else if(dbm <= NEGRO ){
            signalQuality = "VERY_BAD";
        }else {
            signalQuality = "UNKNOWN";
        }
        return signalQuality;
    }

    public String getNetworkConectivityType(){
        networkInfo = connMgr.getActiveNetworkInfo();
        String networkConectivityType ="";
        if(networkInfo == null){
            //Log.d("RED","NULL");
            networkConectivityType = "NO_CONECTION";
        }
        else if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
            //Log.d("RED","wifi");
            networkConectivityType = "WIFI";
        } else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            //Log.d("RED","Mobile");
            networkConectivityType = "MOBILE";
        }

        return networkConectivityType;
    }

    public ArrayList<String> getInternetIspIpInfo(){

        ArrayList<String> internetInformationArray = new ArrayList<>();

        internetInformationArray.add(ispName);
        internetInformationArray.add(ipAddress);

        return internetInformationArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public boolean isSimAvailable(){
        SubscriptionManager sManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        @SuppressLint("MissingPermission") SubscriptionInfo infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0);
        @SuppressLint("MissingPermission") SubscriptionInfo infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1);
        if(infoSim1 != null && infoSim2 != null) {
            return true;
        }
        return false;
    }

    public String exportRssnr(){
        getLteRssnr();
        return ""+snr;
    }
}
