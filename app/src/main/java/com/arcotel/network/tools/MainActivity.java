package com.arcotel.network.tools;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arcotel.network.tools.librarys.ScanCellularActivity;
import com.arcotel.network.tools.services.ConectivityScanService;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_PHONE_STATE=1;
    private Dialog rankDialog;
    private RatingBar ratingBar;
    private TelephonyManager telephonyManager;
    private Button buttonStartCapture;
    private TextView textViewTechCell;
    private ImageView imageViewSignalPower;
    private TextView textViewInternetCon;
    private TextView textViewOperatorName;

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
    private ScanCellularActivity scanCellularActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shoAlertDialogGetPermissions();





        this.telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        buttonStartCapture = (Button) findViewById(R.id.buttonStartCapture);
        textViewTechCell = (TextView) findViewById(R.id.textViewTechCell);
        imageViewSignalPower = (ImageView) findViewById(R.id.imageViewSignalPower);
        textViewInternetCon = (TextView) findViewById(R.id.textViewInternetCon);
        textViewOperatorName = (TextView) findViewById(R.id.textViewOperatorName);

        //Instanciar UI de TestInternet
        pingTextView = (TextView) this.findViewById(R.id.pingTextView);
        downloadTextView = (TextView) this.findViewById(R.id.downloadTextView);
        uploadTextView = (TextView) this.findViewById(R.id.uploadTextView);
        chartDownload = (LinearLayout) this.findViewById(R.id.chartDownload);
        chartPing = (LinearLayout) this.findViewById(R.id.chartPing);
        chartUpload = (LinearLayout) this.findViewById(R.id.chartUpload);

        scanCellularActivity = new ScanCellularActivity(getApplicationContext());


        ConectivityScanService.setUpdateListener(this);
        //iniciarConectivityScanService();

        Toast.makeText(MainActivity.this, "OnCreate", Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();
        final double[] snr = new double[1];
        //iniciarConectivityScanService();



        Toast.makeText(MainActivity.this, "onResume", Toast.LENGTH_LONG).show();
        //Button startButton = (Button) findViewById(R.id.buttonStartCapture);
        buttonStartCapture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doSpeedTest();
            }
        });


        onBottonNavigationPress();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "MainActivity onPause", Toast.LENGTH_LONG).show();
        Intent service = new Intent(this, ConectivityScanService.class);
        stopService(service);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "AdvancedCellInfoActivity onStop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        // Antes de cerrar la aplicacion se para el servicio (el cronometro)
        //
        super.onDestroy();
        pararConectivityScanService();
    }

    /**
     * Inicia el servicio
     */
    private void iniciarConectivityScanService() {
        Intent service = new Intent(this, ConectivityScanService.class);
        startService(service);
    }

    /**
     * Finaliza el servicio
     */
    private void pararConectivityScanService() {
        Intent service = new Intent(this, ConectivityScanService.class);
        stopService(service);
    }

    /**
     * Actualiza en la interfaz de usuario los valores de potencia de señal
     *
     */
    public void actualizaUiOnMainActivity(String buttonStartCaptureTxt,boolean buttonStartCaptureBool,String signalQuality,String phoneNetwork, String phoneNetworkConection, String operatorName) {
        Log.d("actualizaUiOnMai","status "+buttonStartCaptureTxt+" TipoConexxion "+buttonStartCaptureBool+" Teconologia "+phoneNetwork+" calidad "+signalQuality+" redConection "+phoneNetworkConection);

        textViewTechCell.setText(phoneNetwork);
        textViewInternetCon.setText(phoneNetworkConection);
        textViewOperatorName.setText(operatorName);
        buttonStartCapture.setText(buttonStartCaptureTxt);
        buttonStartCapture.setEnabled(buttonStartCaptureBool);


        if(signalQuality == "VERY_GOOD"){
            imageViewSignalPower.setImageResource(R.drawable.cell_signal_status_green);
        }
        else if(signalQuality == "GOOD"){
            imageViewSignalPower.setImageResource(R.drawable.cell_signal_status_yellow);
        }
        else if(signalQuality == "AVERAGE"){
            imageViewSignalPower.setImageResource(R.drawable.cellsignal_status_orange);
        }
        else if(signalQuality == "BAD"){
            imageViewSignalPower.setImageResource(R.drawable.cell_signal_status_red);
        }
        else if(signalQuality == "VERY_BAD"){
            imageViewSignalPower.setImageResource(R.drawable.cell_signal_status_red);
        }
        else if(signalQuality == "NONE"){
            imageViewSignalPower.setImageResource(R.drawable.cell_no_signal_status);
        }
        else{
            imageViewSignalPower.setImageResource(R.drawable.cell_no_signal_status);
        }
    }

    //Listener del boton arcotel que llama a la activity disclaimer
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Constructor del boton de arcotel que llama a la activity disclaimer
    @Override
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

    // Método que publica el ratting bar
    public void showRatingBar(){
        float userRankValue=0;
        rankDialog = new Dialog(MainActivity.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rank_dialog);
        rankDialog.setCancelable(false);
        ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(userRankValue);
        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Toast.makeText(MainActivity.this, "rating is "+ratingBar.getRating(), Toast.LENGTH_LONG).show();
                rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }

    //metodo para invocar el Booton bar que llama a las activities de avanzado, estadísticas y settings
    public void onBottonNavigationPress(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
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
                        Intent intentSetup = new Intent(MainActivity.this, SetupActivity.class);
                        intentSetup.putExtra("key", "value"); //Optional parameters
                        startActivity(intentSetup);
                        break;
                }
                return false;
            }
        });
    }


    //Seccion Prueba de velocidad de internet
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
                            buttonStartCapture.setTextSize(12);
                            buttonStartCapture.setText("Problemas con el servidor de localizacion, intente de nuevo.");
                        }
                    });
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonStartCapture.setTextSize(13);
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
                        buttonStartCapture.setTextSize(16);
                        buttonStartCapture.setText("Comenzar de Nuevo");

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


    public void shoAlertDialogGetPermissions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog 2 botones");
        builder.setMessage("¿Quieres cerrar la app?");

        builder.setPositiveButton("La Roja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String permissionACLocation =  Manifest.permission.ACCESS_COARSE_LOCATION;
                String permissionAFLocation = Manifest.permission.ACCESS_FINE_LOCATION;
                String permissionInternet = Manifest.permission.INTERNET;
                String permissionAccessWifi = Manifest.permission.ACCESS_WIFI_STATE;
                String readPhoneState = Manifest.permission.READ_PHONE_STATE;
                showPhoneStatePermission(permissionACLocation);
                showPhoneStatePermission(permissionAFLocation);
                showPhoneStatePermission(permissionInternet);
                showPhoneStatePermission(permissionAccessWifi);
                showPhoneStatePermission(readPhoneState);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPhoneStatePermission(String permission) {
        Log.d("Entra showPhoneState","la variable permiso tiene: "+permission);
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, permission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {
                Log.d("Entra showExplanation","la variable permiso tiene: "+permission);
                showExplanation("Permission Needed", "Rationale", permission, REQUEST_PERMISSION_PHONE_STATE);
            } else {
                Log.d("Entra requestpermision","la variable permiso tiene: "+permission);
                requestPermission(permission, REQUEST_PERMISSION_PHONE_STATE);
            }
        } else {
            Toast.makeText(this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private void showExplanation(String title,String message,final String permission,final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }
    private void requestPermission(String permissionName, int permissionRequestCode) {
        Log.d("Entra requestPermission","la variable permiso tiene: "+permissionName);
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }


}