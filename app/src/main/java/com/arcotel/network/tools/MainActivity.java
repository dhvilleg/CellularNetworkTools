package com.arcotel.network.tools;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.arcotel.network.tools.data.Constants;
import com.arcotel.network.tools.data.DeviceMetadata;
import com.arcotel.network.tools.data.ScanMetadata;
import com.arcotel.network.tools.librarys.LocationAddress;
import com.arcotel.network.tools.librarys.ScanCellularActivity;
import com.arcotel.network.tools.services.ConectivityScanService;
import com.arcotel.network.tools.services.FetchAddressIntentService;
import com.arcotel.network.tools.test.HttpDownloadTest;
import com.arcotel.network.tools.test.HttpUploadTest;
import com.arcotel.network.tools.test.PingTest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.arcotel.network.tools.utils.UtilityMethods.buildAlertMessageNoGps;


public class MainActivity extends AppCompatActivity {

    //Variables de entorno y componnentes de UI
    private static final int    PERMISSIONS_REQUEST = 1234;
    private Dialog rankDialog;
    private RatingBar ratingBar;
    private Button buttonStartCapture;
    private TextView textViewTechCell;
    private ImageView imageViewSignalPower;
    private TextView textViewInternetCon;
    private TextView textViewOperatorName;
    private BottomNavigationView bottomNavigationView;
    private Boolean switchSyncPref;
    private int internetConStatusFlag = 0;
    private AntDbHelper scanDbHelper;
    private Cursor cursorQuery;
    private LocationAddress locationAddress;
    ScanCellularActivity scanCellularActivity;
    private double snr = 0.0;
    private ArrayList<String> getAllInfo;
    private String ratingCounter = "NULL";
    private String ipPublicAddr = "NULL";
    private String internetISP = "NULL";
    private String aux = "none";


    //Variables TestInternet
    private String pingTimeMilis = "null" ;
    private String uploadSpeed = "null" ;
    private String downloadSpeed = "null" ;
    static int position = 0;
    static int lastPosition = 0;
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList;

    //UI TestInternet
    private TextView pingTextView;
    private TextView downloadTextView;
    private TextView uploadTextView;
    private LinearLayout chartDownload;
    private LinearLayout chartPing;
    private LinearLayout chartUpload;

    //Location variables
    private final static int SINGLE_LOCATION = 1010;
    private AddressResultReceiver mResultReceiver;
    private LocationManager manager = null;
    private LocationListener locationListener;
    private String latitud = "";
    private String longitud = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciar objetos de UI
        buttonStartCapture = findViewById(R.id.buttonStartCapture);
        textViewTechCell = findViewById(R.id.textViewTechCell);
        imageViewSignalPower = findViewById(R.id.imageViewSignalPower);
        textViewInternetCon = findViewById(R.id.textViewInternetCon);
        textViewOperatorName = findViewById(R.id.textViewOperatorName);

        //Instanciar UI de TestInternet
        pingTextView = this.findViewById(R.id.pingTextView);
        downloadTextView = this.findViewById(R.id.downloadTextView);
        uploadTextView = this.findViewById(R.id.uploadTextView);
        chartDownload = this.findViewById(R.id.chartDownload);
        chartPing = this.findViewById(R.id.chartPing);
        chartUpload = this.findViewById(R.id.chartUpload);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        //Instanciar objetos para locación
        mResultReceiver = new AddressResultReceiver(new Handler());
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Instanciar objeto que crea la BDD
        scanDbHelper = new AntDbHelper(getApplicationContext());

        //Instanciar objetos de aplicación
        ConectivityScanService.setUpdateListener(this);
        checkPermissions();


        //Toast.makeText(MainActivity.this, "OnCreate", Toast.LENGTH_LONG).show();

        /**Settear por primera vez los valores del switch option sync_options se setea a falso y significa
         * que por defecto subirá los valores capturados por WIFI**/
        PreferenceManager.setDefaultValues(this,R.xml.root_preferences, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length == 0) {
            iniciarConectivityScanService();
            displayLocation(SINGLE_LOCATION);
            signalStrengthListener();
        }
        buttonStartCapture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Sin Internet, Colectar red movil
                if(internetConStatusFlag == 1){

                }
                //Preparado
                else if(internetConStatusFlag == 2){
                    saveAllInfoToDataBase();
                    cursorQuery = scanDbHelper.getCellularInfoByIsRegistered(0);
                    ArrayList<String> quryCellularInfo= scanDbHelper.getCellularInfoInJson(cursorQuery);
                    for (int i = 0; i < quryCellularInfo.size();i++){
                        Log.d("ImprimeJson: ",""+quryCellularInfo.get(i));
                    }
                }
                //Sin servicio Movil, Medir WIFI || Modo Avión, colectar WIFI
                else if(internetConStatusFlag == 3 || internetConStatusFlag == 4){

                }
            }
        });

        onBottonNavigationPress();
        getGlobalSettings();
        Toast.makeText(this, switchSyncPref.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationService();
        //Toast.makeText(this, "MainActivity onPause", Toast.LENGTH_LONG).show();
        Intent service = new Intent(this, ConectivityScanService.class);
        stopService(service);

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationService();
        //Toast.makeText(this, "AdvancedCellInfoActivity onStop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        // Antes de cerrar la aplicacion se para el servicio (el cronometro)
        //
        super.onDestroy();
        pararConectivityScanService();
    }

    /**Inicia el servicio*/
    private void iniciarConectivityScanService() {
        Intent service = new Intent(this, ConectivityScanService.class);
        startService(service);
    }

    /**Finaliza el servicio*/
    private void pararConectivityScanService() {
        Intent service = new Intent(this, ConectivityScanService.class);
        stopService(service);
    }

    /**Metodo para llamar a las preferencias globales**/
    private  void getGlobalSettings(){
        /**Tomar las preferencias globles, aplicar el filtro de la preferencia usando el KEY "uploadSetting"
         * y asignarlo a la variable**/
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        switchSyncPref = sharedPref.getBoolean ("uploadSetting", false);
    }

    /** Actualiza en la interfaz de usuario los valores de potencia de señal**/
    public void actualizaUiOnMainActivity(String buttonStartCaptureTxt,boolean buttonStartCaptureBool,String signalQuality,String phoneNetwork, String phoneNetworkConection, String operatorName, int flag, ArrayList<String> allInfoInArray) {
        textViewTechCell.setText(phoneNetwork);
        textViewInternetCon.setText(phoneNetworkConection);
        textViewOperatorName.setText(operatorName);
        buttonStartCapture.setText(buttonStartCaptureTxt);
        buttonStartCapture.setEnabled(buttonStartCaptureBool);
        internetConStatusFlag = flag;
        getAllInfo = allInfoInArray;



        if(signalQuality.equals("VERY_GOOD")){
            imageViewSignalPower.setImageResource(R.drawable.cell_signal_status_green);
        }
        else if(signalQuality.equals("GOOD")){
            imageViewSignalPower.setImageResource(R.drawable.cell_signal_status_green);
        }
        else if(signalQuality.equals("AVERAGE")){
            imageViewSignalPower.setImageResource(R.drawable.cell_signal_status_yellow);
        }
        else if(signalQuality.equals("BAD")){
            imageViewSignalPower.setImageResource(R.drawable.cellsignal_status_orange);
        }
        else if(signalQuality.equals("VERY_BAD")){
            imageViewSignalPower.setImageResource(R.drawable.cell_signal_status_red);
        }
        else if(signalQuality.equals("NONE")){
            imageViewSignalPower.setImageResource(R.drawable.cell_no_signal_status);
        }
        else{
            imageViewSignalPower.setImageResource(R.drawable.cell_no_signal_status);
        }

        if(phoneNetworkConection.equals("WIFI")){
            if (getSpeedTestHostsHandler == null) {
                getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                getSpeedTestHostsHandler.start();

            }
            internetISP = getSpeedTestHostsHandler.getSelfLisp();
            ipPublicAddr = getSpeedTestHostsHandler.getSelfLip();
            if(!aux.equals("WIFI") && getSpeedTestHostsHandler != null){
                getSpeedTestHostsHandler = null;
                aux = "WIFI";
            }
        }
        else if(phoneNetworkConection.equals("MOBILE")){
            if (getSpeedTestHostsHandler == null) {
                getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                getSpeedTestHostsHandler.start();

            }
            internetISP = getSpeedTestHostsHandler.getSelfLisp();
            ipPublicAddr = getSpeedTestHostsHandler.getSelfLip();
            if(!aux.equals("MOBILE") && getSpeedTestHostsHandler != null){
                getSpeedTestHostsHandler = null;
                aux = "MOBILE";
            }
        }
        else{

            getSpeedTestHostsHandler = null;
            ipPublicAddr = "NULL";
            internetISP = "NULL";
            aux = "none";
        }
    }

    @Override
    /**Listener del boton arcotel que llama a la activity disclaimer**/
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    /**Constructor del boton de arcotel que llama a la activity disclaimer**/
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_arcotel) {
            Intent intentAdvanced = new Intent(MainActivity.this, ShowArcotelDisclaimerActivity.class);
            intentAdvanced.putExtra("key", "value"); //Optional parameters
            startActivity(intentAdvanced);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /** Método que publica el ratting bar**/
    public void showRatingBar(){
        float userRankValue=0;
        rankDialog = new Dialog(MainActivity.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(false);
        ratingBar = rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(userRankValue);
        Button updateButton = rankDialog.findViewById(R.id.rank_dialog_button);
        Log.d("InternetValues:","pingTimeMilis: "+pingTimeMilis+" downloadSpeed: "+downloadSpeed+" uploadSpeed: "+uploadSpeed);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                ratingCounter = ""+ratingBar.getRating();
                rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }

    /**Método para invocar el Booton bar que llama a las activities de avanzado, estadísticas y settings**/
    public void onBottonNavigationPress(){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_advanced_cell_info:
                        Intent intentAdvanced = new Intent(MainActivity.this, AdvancedCellInfoActivity.class);
                        intentAdvanced.putExtra("key", "value"); //Optional parameters
                        startActivity(intentAdvanced);
                        break;
                    case R.id.main_show_stats:
                        Intent intendStats = new Intent(MainActivity.this, StatsActivity.class);
                        intendStats.putExtra("key", "value"); //Optional parameters
                        startActivity(intendStats);
                        break;
                    case R.id.main_setup_application:
                        Intent intentSetup = new Intent(MainActivity.this, SettingsActivity.class);
                        intentSetup.putExtra("key", "value"); //Optional parameters
                        startActivity(intentSetup);
                        break;

                }
                return false;
            }
        });
    }

    /**Método para la prueba de velocidad de internet**/
    private void doSpeedTest(){
        final DecimalFormat dec = new DecimalFormat("#.##");
        buttonStartCapture.setEnabled(false);
        tempBlackList = new HashSet<>();
        //Restart test icin eger baglanti koparsa
        if (getSpeedTestHostsHandler == null) {
            getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
            getSpeedTestHostsHandler.start();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonStartCapture.setText("Consiguiendo mejor servidor basado en ping...");
                    }
                });
                //Get egcodes.speedtest hosts
                int timeCount = 600; //1min
                while (!getSpeedTestHostsHandler.isFinished()) {
                    timeCount--;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    if (timeCount <= 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "No Connection...", Toast.LENGTH_LONG).show();
                                buttonStartCapture.setEnabled(true);
                                buttonStartCapture.setTextSize(16);
                                buttonStartCapture.setText("Comenzar de nuevo");
                                Log.d("SpeedVars","El valor de ping es"+pingTimeMilis+" el valor de download es "+downloadSpeed+" el valor de upload es "+uploadSpeed);
                            }
                        });
                        getSpeedTestHostsHandler = null;
                        return;
                    }
                }

                //Find closest server
                HashMap<Integer, String> mapKey = getSpeedTestHostsHandler.getMapKey();
                HashMap<Integer, List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();
                double selfLat = getSpeedTestHostsHandler.getSelfLat();
                double selfLon = getSpeedTestHostsHandler.getSelfLon();
                double tmp = 19349458;
                double dist = 0.0;
                int findServerIndex = 0;
                for (int index : mapKey.keySet()) {
                    if (tempBlackList.contains(mapValue.get(index).get(5))) {
                        continue;
                    }

                    Location source = new Location("Source");
                    source.setLatitude(selfLat);
                    source.setLongitude(selfLon);

                    List<String> ls = mapValue.get(index);
                    Location dest = new Location("Dest");
                    dest.setLatitude(Double.parseDouble(ls.get(0)));
                    dest.setLongitude(Double.parseDouble(ls.get(1)));

                    double distance = source.distanceTo(dest);
                    if (tmp > distance) {
                        tmp = distance;
                        dist = distance;
                        findServerIndex = index;
                    }
                }
                String uploadAddr = mapKey.get(findServerIndex);
                final List<String> info = mapValue.get(findServerIndex);
                final double distance = dist;

                if (info == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //buttonStartCapture.setTextSize(12);
                            buttonStartCapture.setText("Problemas con el servidor de localizacion, intente de nuevo.");
                        }
                    });
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //buttonStartCapture.setTextSize(13);
                        buttonStartCapture.setText(String.format("Host Location: %s [Distance: %s km]", info.get(2), new DecimalFormat("#.##").format(distance / 1000)));
                    }
                });

                //Init Ping graphic

                XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
                XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                pingFill.setColor(Color.parseColor("#4d5a6a"));
                pingRenderer.addFillOutsideLine(pingFill);
                pingRenderer.setDisplayChartValues(false);
                pingRenderer.setShowLegendItem(false);
                pingRenderer.setColor(Color.parseColor("#4d5a6a"));
                pingRenderer.setLineWidth(5);
                final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
                multiPingRenderer.setXLabels(0);
                multiPingRenderer.setYLabels(0);
                multiPingRenderer.setZoomEnabled(false);
                multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
                multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                multiPingRenderer.setPanEnabled(true, true);
                multiPingRenderer.setZoomButtonsVisible(false);
                multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                multiPingRenderer.addSeriesRenderer(pingRenderer);

                //Init Download graphic

                XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
                XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                downloadFill.setColor(Color.parseColor("#4d5a6a"));
                downloadRenderer.addFillOutsideLine(downloadFill);
                downloadRenderer.setDisplayChartValues(false);
                downloadRenderer.setColor(Color.parseColor("#4d5a6a"));
                downloadRenderer.setShowLegendItem(false);
                downloadRenderer.setLineWidth(5);
                final XYMultipleSeriesRenderer multiDownloadRenderer = new XYMultipleSeriesRenderer();
                multiDownloadRenderer.setXLabels(0);
                multiDownloadRenderer.setYLabels(0);
                multiDownloadRenderer.setZoomEnabled(false);
                multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"));
                multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                multiDownloadRenderer.setPanEnabled(false, false);
                multiDownloadRenderer.setZoomButtonsVisible(false);
                multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                multiDownloadRenderer.addSeriesRenderer(downloadRenderer);

                //Init Upload graphic

                XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
                XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                uploadFill.setColor(Color.parseColor("#4d5a6a"));
                uploadRenderer.addFillOutsideLine(uploadFill);
                uploadRenderer.setDisplayChartValues(false);
                uploadRenderer.setColor(Color.parseColor("#4d5a6a"));
                uploadRenderer.setShowLegendItem(false);
                uploadRenderer.setLineWidth(5);
                final XYMultipleSeriesRenderer multiUploadRenderer = new XYMultipleSeriesRenderer();
                multiUploadRenderer.setXLabels(0);
                multiUploadRenderer.setYLabels(0);
                multiUploadRenderer.setZoomEnabled(false);
                multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
                multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                multiUploadRenderer.setPanEnabled(false, false);
                multiUploadRenderer.setZoomButtonsVisible(false);
                multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                multiUploadRenderer.addSeriesRenderer(uploadRenderer);

                //Reset value, graphics
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pingTextView.setText("0 ms");
                        chartPing.removeAllViews();
                        downloadTextView.setText("0 Mbps");
                        chartDownload.removeAllViews();
                        uploadTextView.setText("0 Mbps");
                        chartUpload.removeAllViews();
                    }
                });
                final List<Double> pingRateList = new ArrayList<>();
                final List<Double> downloadRateList = new ArrayList<>();
                final List<Double> uploadRateList = new ArrayList<>();
                Boolean pingTestStarted = false;
                Boolean pingTestFinished = false;
                Boolean downloadTestStarted = false;
                Boolean downloadTestFinished = false;
                Boolean uploadTestStarted = false;
                Boolean uploadTestFinished = false;

                //Init Test
                final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 6);
                final HttpDownloadTest downloadTest = new HttpDownloadTest(uploadAddr.replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], ""));
                final HttpUploadTest uploadTest = new HttpUploadTest(uploadAddr);


                //Tests
                while (true) {
                    if (!pingTestStarted) {
                        pingTest.start();
                        pingTestStarted = true;
                    }
                    if (pingTestFinished && !downloadTestStarted) {
                        downloadTest.start();
                        downloadTestStarted = true;
                    }
                    if (downloadTestFinished && !uploadTestStarted) {
                        uploadTest.start();
                        uploadTestStarted = true;
                    }


                    //Ping Test
                    if (pingTestFinished) {
                        //Failure
                        if (pingTest.getAvgRtt() == 0) {
                            System.out.println("Ping error...");
                            pingTimeMilis = "null";
                        } else {
                            //Success
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pingTextView.setText(dec.format(pingTest.getAvgRtt()) + " ms");
                                    pingTimeMilis=""+dec.format(pingTest.getAvgRtt());
                                }
                            });
                        }
                    } else {
                        pingRateList.add(pingTest.getInstantRtt());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pingTextView.setText(dec.format(pingTest.getInstantRtt()) + " ms");
                            }
                        });

                        //Update chart
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Creating an  XYSeries for Income
                                XYSeries pingSeries = new XYSeries("");
                                pingSeries.setTitle("");

                                int count = 0;
                                List<Double> tmpLs = new ArrayList<>(pingRateList);
                                for (Double val : tmpLs) {
                                    pingSeries.add(count++, val);
                                }

                                XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                dataset.addSeries(pingSeries);

                                GraphicalView chartView = ChartFactory.getLineChartView(getApplicationContext(), dataset, multiPingRenderer);
                                chartPing.addView(chartView, 0);

                            }
                        });
                    }


                    //Download Test
                    if (pingTestFinished) {
                        if (downloadTestFinished) {
                            //Failure
                            if (downloadTest.getFinalDownloadRate() == 0) {
                                System.out.println("Download error...");
                                downloadSpeed = "null";
                            } else {
                                //Success
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        downloadTextView.setText(dec.format(downloadTest.getFinalDownloadRate()) + " Mbps");
                                        downloadSpeed = ""+dec.format(downloadTest.getFinalDownloadRate());
                                    }
                                });
                            }
                        } else {
                            //Calc position
                            double downloadRate = downloadTest.getInstantDownloadRate();
                            downloadRateList.add(downloadRate);
                            position = getPositionByRate(downloadRate);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    downloadTextView.setText(dec.format(downloadTest.getInstantDownloadRate()) + " Mbps");

                                }

                            });
                            lastPosition = position;

                            //Update chart
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Creating an  XYSeries for Income
                                    XYSeries downloadSeries = new XYSeries("");
                                    downloadSeries.setTitle("");

                                    List<Double> tmpLs = new ArrayList<>(downloadRateList);
                                    int count = 0;
                                    for (Double val : tmpLs) {
                                        downloadSeries.add(count++, val);
                                    }

                                    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                    dataset.addSeries(downloadSeries);

                                    GraphicalView chartView = ChartFactory.getLineChartView(getApplicationContext(), dataset, multiDownloadRenderer);
                                    chartDownload.addView(chartView, 0);
                                }
                            });

                        }
                    }


                    //Upload Test
                    if (downloadTestFinished) {
                        if (uploadTestFinished) {
                            //Failure
                            if (uploadTest.getFinalUploadRate() == 0) {
                                System.out.println("Upload error...");
                                uploadSpeed = "null";
                            } else {
                                //Success
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Log.d("UploadSpeed","entra a hilo valor de variable "+uploadSpeed);
                                        uploadTextView.setText(dec.format(uploadTest.getFinalUploadRate()) + " Mbps");
                                        //uploadSpeed = Double.parseDouble(dec.format(uploadTest.getFinalUploadRate()));

                                    }
                                });
                            }
                        } else {
                            //Calc position
                            double uploadRate = uploadTest.getInstantUploadRate();
                            uploadRateList.add(uploadRate);
                            position = getPositionByRate(uploadRate);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    uploadTextView.setText(dec.format(uploadTest.getInstantUploadRate()) + " Mbps");
                                    uploadSpeed = ""+dec.format(uploadTest.getFinalUploadRate());
                                }

                            });
                            lastPosition = position;

                            //Update chart
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Creating an  XYSeries for Income
                                    XYSeries uploadSeries = new XYSeries("");
                                    uploadSeries.setTitle("");

                                    int count = 0;
                                    List<Double> tmpLs = new ArrayList<>(uploadRateList);
                                    for (Double val : tmpLs) {
                                        if (count == 0) {
                                            val = 0.0;
                                        }
                                        uploadSeries.add(count++, val);
                                    }

                                    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                                    dataset.addSeries(uploadSeries);

                                    GraphicalView chartView = ChartFactory.getLineChartView(getApplicationContext(), dataset, multiUploadRenderer);
                                    chartUpload.addView(chartView, 0);
                                }
                            });

                        }
                    }

                    //Test bitti
                    if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                        break;
                    }

                    if (pingTest.isFinished()) {
                        pingTestFinished = true;
                    }
                    if (downloadTest.isFinished()) {
                        downloadTestFinished = true;
                    }
                    if (uploadTest.isFinished()) {
                        uploadTestFinished = true;
                    }

                    if (pingTestStarted && !pingTestFinished) {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                }

                //Thread bitiminde button yeniden aktif ediliyor
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonStartCapture.setEnabled(true);
                        //buttonStartCapture.setTextSize(16);
                        buttonStartCapture.setText("Comenzar de Nuevo");

                        showRatingBar();

                    }
                });


            }
        }).start();
    }

    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }

    /**Sección para asegurar la obtención de permisos requeridos por la aplicación
     * https://stackoverflow.com/questions/53276818/splashscreen-with-runtime-permissions*/
    public String[] getRequiredPermissions() {
        String[] permissions = null;
        try {
            permissions = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            checkPermissions();
            displayLocation(SINGLE_LOCATION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    /**Revisa si los permisos fueron garantizados, si es el caso inicia los servicios de UI y crea el device UUID
     * si los permisos no fueron otorgados, llama a requiredPermissionsStillNeeded para volver a pedirlos**/
    private void checkPermissions() {
        final String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length == 0) {
            iniciarConectivityScanService();
            createDeviceUUID(consultaDeviceUUID());

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert);
                builder.setTitle("Arcotel Network Tools");
                builder.setIcon(R.drawable.arcotel_logo);
                builder.setCancelable(false);
                builder.setMessage("Para el correcto funcionamiento de la aplicación, se debe aceptar los permisos de: Telefono, Ubicación y Almacenamiento.\n¿Desea continuar?");
                builder.setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(ungrantedPermissions, PERMISSIONS_REQUEST);
                    }
                });
                builder.setNegativeButton("Denegar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    @TargetApi(23)
    /**Metodo que garantiza la asignación de permisos necesarias para la aplicación**/
    private String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<>();
        for (String permission : getRequiredPermissions()) {
            permissions.add(permission);
        }
        for (Iterator<String> i = permissions.iterator(); i.hasNext();) {
            String permission = i.next();
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(MainActivity.class.getSimpleName(),
                        "Permission: " + permission + " already granted.");
                i.remove();
            } else {
                Log.d(MainActivity.class.getSimpleName(),
                        "Permission: " + permission + " not yet granted.");
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    /**Crea el UUID**/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void createDeviceUUID(String uniqueId){
        String deviceModel="";
        String deviceOS="";
        String osVersion="";
        int isDualSim=0;
        int fieldIsRegistered=0;
        if(uniqueId=="valorEnCero"){
            scanCellularActivity = new ScanCellularActivity(getApplicationContext());
            uniqueId = UUID.randomUUID().toString();
            deviceModel = Build.MODEL;
            deviceOS = Build.VERSION.RELEASE;
            osVersion = Build.VERSION.INCREMENTAL;
            if(scanCellularActivity.isSimAvailable()==true){
                isDualSim=1;
            }
            fieldIsRegistered=0;
            DeviceMetadata deviceMetadata = new DeviceMetadata(uniqueId,deviceModel,deviceOS,osVersion,isDualSim,fieldIsRegistered);
            scanDbHelper.saveDeviceSqlScan(deviceMetadata);
            Log.d("MainActivity","Entra a createDeviceUUID: "+uniqueId);
        }

    }

    /**Consulta el UUID**/
    private String consultaDeviceUUID(){
        String deviceUUID="";
        cursorQuery = scanDbHelper.getAllDeviceInfo();
        deviceUUID = scanDbHelper.getDeviceUUID(cursorQuery);
        Log.d("MainActivity","Entra a ConsultaDeviceUUID: "+deviceUUID);
        return deviceUUID;
    }

    /**Sección para consultar la ubicación*/
    private void startIntentService(double lat,double lon) {

        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }
    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);

            //Toast.makeText(getApplicationContext(),mAddressOutput,Toast.LENGTH_SHORT).show();
            Log.d("ADDRESS :",mAddressOutput);
            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Log.i("Success","true");
            }

        }
    }
    private void displayLocation(int singleLocation) {

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST);
        }
        else {
            if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                buildAlertMessageNoGps(this, singleLocation);
            }
            try {
                if (manager != null) {

                    //TODO: Location location = UtilityMethods.getLastKnownLocation(manager);
                    // For getting location at once
                    // Define a listener that responds to location updates
                    locationListener = new LocationListener() {
                        public void onLocationChanged(Location location) {
                            // Called when a new location is found by the network location provider.
                            if(location!=null)
                                makeUseOfNewLocation(location);
                        }

                        public void onStatusChanged(String provider, int status, Bundle extras) {}

                        public void onProviderEnabled(String provider) {}

                        public void onProviderDisabled(String provider) {}
                    };

                    // Register the listener with the Location Manager to receive location updates
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    //btn_getlocation.setText("Remove Updates");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void makeUseOfNewLocation(Location location) {
        latitud = ""+location.getLatitude();
        longitud = ""+location.getLongitude();
        startIntentService(location.getLatitude(),location.getLongitude());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SINGLE_LOCATION) {
            displayLocation(SINGLE_LOCATION);
        }
    }
    public void stopLocationService(){
        if (locationListener != null)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                manager.removeUpdates(locationListener);
            }
    }

    /**Levanta el lisneter para consultar estado de potencia señal LTE RSSNR*/
    public void signalStrengthListener(){
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        telephonyManager.listen(new PhoneStateListener(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            public void onSignalStrengthsChanged(SignalStrength signalStrength)
            {
                try {
                    snr = (double) ((Integer) SignalStrength.class.getMethod("getLteRssnr").invoke(signalStrength)/10D);
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

    /**Método para guardar en la BDD la información colectada*/
    public void saveAllInfoToDataBase(){
        final SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String timestamp = s.format(new Date());
        String deviceUUID = consultaDeviceUUID();
        //ratingCounter
        int simNumberID = 0;
        String countryISO = getAllInfo.get(0);
        String phoneOperatorId = getAllInfo.get(1);
        String simOperatorId = getAllInfo.get(2);
        String operatorMcc = getAllInfo.get(3);
        String operatorMnc = getAllInfo.get(4);
        String devManufacturer = Build.MANUFACTURER;
        String devModel = Build.MODEL;
        String isConected = getAllInfo.get(5);
        String phoneNetStandard = getAllInfo.get(6);
        String phoneNetTechnology = getAllInfo.get(7);
        String internetConNetwork = getAllInfo.get(8);
        //latitud
        //longitud
        //pingTimeMilis
        //downloadSpeed
        //uploadSpeed
        //ipPublicAddr
        //internetISP
        String phoneSignalStrength = getAllInfo.get(9);
        String phoneAsuStrength = getAllInfo.get(10);
        String phoneSignalLevel = getAllInfo.get(11);
        String signalQuality = getAllInfo.get(12);
        int fieldIsRegistered = 0;
        String phoneRsrpStrength = getAllInfo.get(13);
        String phoneRssnrStrength="";
        if ( snr == 2.147483647E8){
            phoneRssnrStrength = "NULL";
        }
        else{
            phoneRssnrStrength = ""+snr;
        }
        String phoneTimingAdvance = getAllInfo.get(14);
        String phoneCqiStrength = getAllInfo.get(15);
        String phoneRsrqStrength = getAllInfo.get(16);
        String cellLtePci = getAllInfo.get(17);
        String cellLteCid = getAllInfo.get(18);
        String cellLteTac = getAllInfo.get(19);
        String cellLteeNodeB = getAllInfo.get(20);
        String cellLteEarfcn = getAllInfo.get(21);
        String cellBslat = getAllInfo.get(22);
        String cellBslon = getAllInfo.get(23);
        String cellSid = getAllInfo.get(24);
        String cellNid = getAllInfo.get(25);
        String cellBid = getAllInfo.get(26);
        String cellWcdmaLac = getAllInfo.get(27);
        String cellWcdmaUcid = getAllInfo.get(28);
        String cellWcdmaUarfcn = getAllInfo.get(29);
        String cellWcdmaPsc = getAllInfo.get(30);
        String cellWcdmaCid = getAllInfo.get(31);
        String cellWcdmaRnc = getAllInfo.get(32);
        String cellGsmArcfn = getAllInfo.get(33);
        String cellGsmLac = getAllInfo.get(34);
        String cellGsmCid = getAllInfo.get(35);
        ScanMetadata scanMetadata = new ScanMetadata(timestamp,deviceUUID,ratingCounter,simNumberID,countryISO,phoneOperatorId,simOperatorId,operatorMcc,
                operatorMnc,devManufacturer,devModel,isConected,phoneNetStandard,phoneNetTechnology,internetConNetwork,latitud,longitud,pingTimeMilis,
                downloadSpeed,uploadSpeed,ipPublicAddr,internetISP,phoneSignalStrength,phoneAsuStrength,phoneSignalLevel,signalQuality,fieldIsRegistered,
                phoneRsrpStrength,phoneRssnrStrength,phoneTimingAdvance,phoneCqiStrength,phoneRsrqStrength,cellLtePci,cellLteCid,cellLteTac,cellLteeNodeB,
                cellLteEarfcn,cellBslat,cellBslon,cellSid,cellNid,cellBid,cellWcdmaLac,cellWcdmaUcid,cellWcdmaUarfcn,cellWcdmaPsc,cellWcdmaCid,cellWcdmaRnc,
                cellGsmArcfn,cellGsmLac,cellGsmCid);
        scanDbHelper.saveCellularSqlScan(scanMetadata);
    }
}