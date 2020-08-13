package com.arcotel.network.tools.services;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;

import com.arcotel.network.tools.AdvancedCellInfoActivity;
import com.arcotel.network.tools.librarys.ScanCellularActivity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Timer;

public class CellularCoverageService extends Service {

    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 10; // En ms
    public static AdvancedCellInfoActivity UPDATE_LISTENER_RSRP;
    public static AdvancedCellInfoActivity UPDATE_LISTENER_RSSNR;
    public static AdvancedCellInfoActivity UPDATE_LISTENER_RSRQ;
    private Handler handler;
    private String cellStrengthRsrp="";
    private String cellStrengthRssnr="";
    private String cellStrengthRsrq="";
    private ScanCellularActivity scanCellularActivity;


    public CellularCoverageService() {
        scanCellularActivity = new ScanCellularActivity(UPDATE_LISTENER_RSRP.getApplicationContext());
    }

    public static void setUpdateListener(AdvancedCellInfoActivity cellularCoverageService) {
        UPDATE_LISTENER_RSRP = cellularCoverageService;
        UPDATE_LISTENER_RSSNR = cellularCoverageService;
        UPDATE_LISTENER_RSRQ = cellularCoverageService;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate() {
        super.onCreate();
        iniciarCapturaRSRP();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER_RSRP.actualizaRsrp(cellStrengthRsrp,cellStrengthRssnr,cellStrengthRsrq);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void iniciarCapturaRSRP() {
        final double[] snr = new double[1];
        new Thread(new Runnable() {
            public void run() {
                final ArrayList<String> strengthInfo = scanCellularActivity.getDevStrengthSignal();
                final boolean quitLooper = false;
                Looper.prepare();
                TelephonyManager mTelephonyManager = (TelephonyManager) UPDATE_LISTENER_RSRP.getSystemService(UPDATE_LISTENER_RSRP.TELEPHONY_SERVICE);
                mTelephonyManager.listen(new PhoneStateListener(){
                    public void onSignalStrengthsChanged(SignalStrength signalStrength)
                    {
                        try {
                            snr[0] = (double) ((Integer) SignalStrength.class.getMethod("getLteRssnr").invoke(signalStrength)/10D);
                            cellStrengthRsrp = strengthInfo.get(3).toString();
                            cellStrengthRsrq = strengthInfo.get(4).toString();
                            cellStrengthRssnr = ""+snr[0];
                            handler.sendEmptyMessage(0);
                            if(quitLooper) {
                                Looper.myLooper().quit();
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                Looper.loop();
            }
        }).start();
    }

    private void pararCronometro() {
        if (temporizador != null)
            temporizador.cancel();
    }






}
