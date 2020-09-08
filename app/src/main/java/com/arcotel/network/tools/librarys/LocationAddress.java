package com.arcotel.network.tools.librarys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import androidx.core.util.Pair;

import com.arcotel.network.tools.services.AppLocationService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LocationAddress {

    private static AppLocationService appLocationService;

    private static final String TAG = "LocationAddress";
    private double latitude;
    private double longitude;
    Context context;

    public LocationAddress (Context context){
        this.context=context;
    }

    public Pair<Double, Double> getLatLongFromLocation(){
        Log.d("LocationAddress","entra");

        appLocationService = new AppLocationService(context);
        Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
             latitude = location.getLatitude();
             longitude = location.getLongitude();
             }
        else {
                showSettingsAlert(context);
        }
        return new Pair <Double, Double> (latitude, longitude);
    }

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        //sb.append(address.getPremises()).append("\n");
                        sb.append(address.getAdminArea()).append("\n");
                        sb.append(address.getCountryCode()).append("\n");
                        sb.append(address.getFeatureName()).append("\n");
                        //sb.append(address.getPhone()).append("\n");
                        sb.append(address.getSubAdminArea()).append("\n");
                        sb.append(address.getSubLocality()).append("\n");
                        //sb.append(address.getSubThoroughfare()).append("\n");
                        //sb.append(address.getThoroughfare()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        //sb.append(address.getUrl()).append("\n");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n\nAddress:\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n Unable to get address for this lat-long.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    public static void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}
