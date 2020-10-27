package com.arcotel.network.tools;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.arcotel.network.tools.data.Constants;
import com.arcotel.network.tools.services.AdvancedCellInfoUIService;
import com.arcotel.network.tools.services.FetchAddressIntentService;

import static com.arcotel.network.tools.utils.UtilityMethods.buildAlertMessageNoGps;

public class AdvancedCellInfoActivity extends AppCompatActivity {

    private TextView textViewStrengthInfo;
    private ImageView imageViewACI;
    private TextView textViewOperatorNameACI;
    private TextView textViewOperatorInfo;
    private TextView textViewCellDescInfo;
    private TextView textViewInternetInfo;
    private TextView textViewLocationInfo;
    private TextView textViewTechCellACI;

    private String aux = "none";

    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;

    private final static int SINGLE_LOCATION = 1010;
    private final static int REQUEST_PERMISSION = 1212;
    private final static int REQUEST_PERMISSION_SETTING = 1211;
    private LocationManager manager = null;
    private LocationListener locationListener;
    private AddressResultReceiver mResultReceiver;

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

        //enableGPS();


        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mResultReceiver = new AddressResultReceiver(new Handler());

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

        displayLocation(SINGLE_LOCATION);

        Toast.makeText(this, "AdvancedCellInfoActivity onResume", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pararCellularCoverageService();
        stopLocationService();

        Toast.makeText(this, "AdvancedCellInfoActivity onPause", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pararCellularCoverageService();
        stopLocationService();
        Toast.makeText(this, "AdvancedCellInfoActivity onStop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "AdvancedCellInfoActivity onDestroy", Toast.LENGTH_LONG).show();
        pararCellularCoverageService();

    }

    /*private void enableGPS() {
        gps = new LocationGPS(this, getApplicationContext());

    }*/

    /**Inicia el servicio*/
    private void iniciarCellularCoverageService() {
        Intent service = new Intent(this, AdvancedCellInfoUIService.class);
        startService(service);
    }


    /** Finaliza el servicio  */
    private void pararCellularCoverageService() {
        Intent service = new Intent(this, AdvancedCellInfoUIService.class);
        stopService(service);
    }

    /**Actualiza en la interfaz de usuario los valores de potencia de señal */
    public void actualizaUIOnAdvancedInfoActivity(String signalQuality,String phoneNetwork,String operatorName,String operatorTxtString,String strengthTxtString,String cellIdTxtString,String internetTxtString) {
        textViewTechCellACI.setText(phoneNetwork);
        textViewOperatorInfo.setText(operatorTxtString);
        textViewOperatorNameACI.setText(operatorName);
        textViewStrengthInfo.setText(strengthTxtString);
        textViewCellDescInfo.setText(cellIdTxtString);
        //textViewInternetInfo.setText(internetTxtString);


        switch (signalQuality) {
            case "VERY_GOOD":
                imageViewACI.setImageResource(R.drawable.cell_signal_status_green);
                break;
            case "GOOD":
                imageViewACI.setImageResource(R.drawable.cell_signal_status_yellow);
                break;
            case "AVERAGE":
                imageViewACI.setImageResource(R.drawable.cellsignal_status_orange);
                break;
            case "BAD":
                imageViewACI.setImageResource(R.drawable.cell_signal_status_red);
                break;
            case "VERY_BAD":
                imageViewACI.setImageResource(R.drawable.cell_signal_status_red);
                break;
            case "NONE":
                imageViewACI.setImageResource(R.drawable.cell_no_signal_status);
                break;
            default:
                imageViewACI.setImageResource(R.drawable.cell_no_signal_status);
        }
        //getSpeedTestHostsHandler = null;

        if(internetTxtString.equals("WIFI")){
            if (getSpeedTestHostsHandler == null) {
                getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                getSpeedTestHostsHandler.start();
                Log.d("getSpeedTestHostsHand", "entraIF");
            }
            textViewInternetInfo.setText("Isp: "+getSpeedTestHostsHandler.getSelfLisp()+"\nIp: "+getSpeedTestHostsHandler.getSelfLip());
            if(!aux.equals("WIFI") && getSpeedTestHostsHandler != null){
                getSpeedTestHostsHandler = null;
                aux = "WIFI";
            }
        }
        else if(internetTxtString.equals("MOBILE")){
            if (getSpeedTestHostsHandler == null) {
                getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                getSpeedTestHostsHandler.start();
                Log.d("getSpeedTestHostsHand", "entraIF");
            }
            textViewInternetInfo.setText("Isp: "+getSpeedTestHostsHandler.getSelfLisp()+"\nIp: "+getSpeedTestHostsHandler.getSelfLip());
            if(!aux.equals("MOBILE") && getSpeedTestHostsHandler != null){
                getSpeedTestHostsHandler = null;
                aux = "MOBILE";
            }
        }
        else{
            Log.d("getSpeedTestHostsHand", "entraELSE");
            getSpeedTestHostsHandler = null;
            textViewInternetInfo.setText("---");
            aux = "none";
        }
    }

    //@Override
    public void updateLocationUI(String locationTxtString){
        Log.d("actualizaLocationUI","entra");
        textViewLocationInfo.setText(locationTxtString);
    }
    public void stopLocationService(){
        if (locationListener != null)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                manager.removeUpdates(locationListener);
            }
    }
    /**
     * Method to display the location on UI.
     */
    private void displayLocation(int singleLocation) {

        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION);
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
        String locationTxtString = "";
        locationTxtString = "Latitud: "+location.getLatitude()+"\nLongitud: "+location.getLongitude();
        textViewLocationInfo.setText(locationTxtString);
        startIntentService(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SINGLE_LOCATION) {
            displayLocation(SINGLE_LOCATION);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example

            String permission = Manifest.permission.ACCESS_FINE_LOCATION;
            if(ActivityCompat.checkSelfPermission(getApplicationContext(),permission) == PackageManager.PERMISSION_GRANTED)
            {
                //ALL OKAY
                displayLocation(SINGLE_LOCATION);
            }
            else
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),permission) == PackageManager.PERMISSION_DENIED) {
                // user rejected the permission
                boolean showRationale = shouldShowRequestPermissionRationale( permission );
                if (! showRationale) {
                    // user also CHECKED "never ask again"
                    //Take user to settings screen
                    Toast.makeText(getApplicationContext(),"Location Permission is required to complete the task",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);

                } else {
                    // user did NOT check "never ask again"
                    Toast.makeText(getApplicationContext(),"Location Permission is required to complete the task",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_PERMISSION);
                }
            }
        }
    }
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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


}