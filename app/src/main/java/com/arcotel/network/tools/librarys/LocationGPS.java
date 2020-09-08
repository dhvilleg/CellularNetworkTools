package com.arcotel.network.tools.librarys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.arcotel.network.tools.interfaces.AdvancedCellInfoActivityInteractionInterface;

public class LocationGPS implements LocationListener {
    private AdvancedCellInfoActivityInteractionInterface interactionInterface;

    private LocationManager locationManager;

    private Context applicationContext;
    private double latitude;
    private double longitude;

    public LocationGPS(AdvancedCellInfoActivityInteractionInterface interactionInterface, Context applicationContext) {
        this.interactionInterface = interactionInterface;
        this.applicationContext = applicationContext;
        locationManager = (LocationManager) applicationContext.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        String locationTxtString = "";
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        else {
            showSettingsAlert(applicationContext);
        }
        locationTxtString = "Latitud: "+latitude+"\nLongitud: "+longitude;
        interactionInterface.actualizaLocationUI(locationTxtString); // Or call this method whenever you need
    }



    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String locationTxtString = "";
        locationTxtString = "onStatusChanged";
        interactionInterface.actualizaLocationUI(locationTxtString); // Or call this method whenever you need

    }

    @Override
    public void onProviderEnabled(String provider) {
        String locationTxtString = "";
        locationTxtString = "onProviderEnabled";
        interactionInterface.actualizaLocationUI(locationTxtString); // Or call this method whenever you need

    }

    @Override
    public void onProviderDisabled(String provider) {
        String locationTxtString = "";
        locationTxtString = "onProviderDisabled";
        interactionInterface.actualizaLocationUI(locationTxtString); // Or call this method whenever you need

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
