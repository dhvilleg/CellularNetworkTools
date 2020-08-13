package com.arcotel.network.tools.librarys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
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
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.annotation.RequiresApi;

import com.arcotel.network.tools.MainActivity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ScanCellularActivity {
    private static final int TODO = 100;
    private static int VERY_BAD=-110;
    private static int BAD=-100;
    private static int AVERAGE=-80;
    private static int GOOD=-75;
    private static int VERY_GOOD=-70;
    //public double snr = 0;


    TelephonyManager telephonyManager;
    ConnectivityManager connMgr;
    Context context;

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


    public String getDevIsConected() {
        String conectado;
        NetworkInfo networkInfo = connMgr != null ? connMgr.getActiveNetworkInfo() : null;
        boolean isConected = networkInfo != null && networkInfo.isConnected();
        if (isConected) {
            conectado = "conectado";
        } else {
            conectado = "No conectado";
        }
        return conectado;
    }

    public String getDevCountryIso() {
        String countryIso = telephonyManager.getNetworkCountryIso();
        return countryIso;
    }

    public String getDevOperatorName() {
        String operatorName = telephonyManager.getNetworkOperatorName();
        return operatorName;

    }

    public ArrayList<String> getOperatorInfo() {
        ArrayList<String> operatorInfo = new ArrayList<String>();
        operatorInfo.add(telephonyManager.getNetworkCountryIso());
        operatorInfo.add(telephonyManager.getNetworkOperator());
        operatorInfo.add(telephonyManager.getNetworkOperatorName());
        operatorInfo.add(telephonyManager.getSimOperatorName());
        operatorInfo.add(telephonyManager.getSimOperator());
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
    public String getLteRssnr(TelephonyManager telephonyManager){
        final double[] snr = new double[1];
        final boolean quitLooper = false;
        telephonyManager.listen(new PhoneStateListener(){
            public void onSignalStrengthsChanged(SignalStrength signalStrength)
            {
                try {
                    snr[0] = (double) ((Integer) SignalStrength.class.getMethod("getLteRssnr").invoke(signalStrength)/10D);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                Log.d("SEÑAL_RUIDO","valor señar ruido es "+ snr[0]);
            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        return ""+snr[0];
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ArrayList<String> getDevStrengthSignal(){

        ArrayList<String> strength = new ArrayList<String>();
        @SuppressLint("MissingPermission") List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if(cellInfos != null) {
            for (int i = 0 ; i < cellInfos.size() ; i++) {
                if (cellInfos.get(i).isRegistered()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        if (cellInfos.get(i) instanceof CellInfoWcdma) {
                            Log.d("ANALI>AR ESTO "," el valor de phoneNetworType es CellInfoWcdma");
                            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfos.get(i);
                            CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                            strength.add(""+cellSignalStrengthWcdma.getDbm());
                            strength.add(""+cellSignalStrengthWcdma.getAsuLevel());
                            strength.add(""+cellSignalStrengthWcdma.getLevel());
                        } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                            Log.d("ANALI>AR ESTO "," el valor de phoneNetworType es CellInfoGsm");
                            CellInfoGsm cellInfogsm = (CellInfoGsm) cellInfos.get(i);
                            CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                            strength.add(""+cellSignalStrengthGsm.getDbm());
                            strength.add(""+cellSignalStrengthGsm.getAsuLevel());
                            strength.add(""+cellSignalStrengthGsm.getLevel());
                        } else if (cellInfos.get(i) instanceof CellInfoLte) {
                            Log.d("ANALI>AR ESTO "," el valor de phoneNetworType es CellInfoLte");
                            CellInfoLte cellInfoLte = (CellInfoLte) cellInfos.get(i);
                            CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                            strength.add(""+cellSignalStrengthLte.getDbm());
                            strength.add(""+cellSignalStrengthLte.getAsuLevel());
                            strength.add(""+cellSignalStrengthLte.getLevel());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                strength.add(""+cellSignalStrengthLte.getRsrp()); //RSRP: Reference Signal Received Power
                                strength.add(""+cellSignalStrengthLte.getRsrq()); //RSRQ: Reference Signal Received Quality
                                //strength.add(""+getLteRssnr(telephonyManager));
                                strength.add(""+cellSignalStrengthLte.getTimingAdvance()); //TA: Timing Advance
                                strength.add(""+cellSignalStrengthLte.getCqi());// CQI: Channel Quality Indicator
                            }
                        } else if (cellInfos.get(i) instanceof CellInfoCdma) {
                            CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfos.get(i);
                            CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
                            strength.add(""+cellSignalStrengthCdma.getDbm());
                            strength.add(""+cellSignalStrengthCdma.getAsuLevel());
                            strength.add(""+cellSignalStrengthCdma.getLevel());
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
                            cellItentity.add(identityLte.getTac()); //TAC: Tracking Area Code (16-bit)
                            cellItentity.add(identityLte.getCi() >> 8); //eNB: eNodeB Identifier (20-bit)
                            //cellItentity.add(identityLte.getCi());
                            hexPivote = Integer.toHexString(identityLte.getCi());
                            String [] cadena = hexPivote.split("");
                            hexPivote = cadena[cadena.length-2]+cadena[cadena.length -1];
                            int lteCid=Integer.parseInt(hexPivote,16);
                            cellItentity.add(lteCid);

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


    public String getSignalQuality(int dbm){
        String signalQuality="";
        if(dbm >= VERY_GOOD){
            signalQuality = "VERY_GOOD";
        }else if(dbm >= GOOD ){
            signalQuality = "GOOD";
        }else if(dbm >= AVERAGE ){
            signalQuality = "AVERAGE";
        }else if(dbm >= BAD ){
            signalQuality = "BAD";
        }else if (dbm >= VERY_BAD){
            signalQuality = "VERY_BAD";
        }else {
            signalQuality = "UNKNOWN";
        }
        return signalQuality;
    }


}
