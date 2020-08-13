package com.arcotel.network.tools;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_PHONE_STATE=1;
    private TextView textViewRsrp;
    private TextView textViewRssnr;
    private TextView textViewRsrq;
    private Dialog rankDialog;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String permissionACLocation =  Manifest.permission.ACCESS_COARSE_LOCATION;
        String permissionAFLocation = Manifest.permission.ACCESS_FINE_LOCATION;
        String permissionInternet = Manifest.permission.INTERNET;
        String permissionAccessWifi = Manifest.permission.ACCESS_WIFI_STATE;
        String readPhoneState = Manifest.permission.READ_PHONE_STATE;
        Log.d("Pre-permiso","la variable permoso tiene: "+permissionACLocation);
        Log.d("Pre-permiso","la variable permoso tiene: "+permissionAFLocation);
        Log.d("Pre-permiso","la variable permoso tiene: "+permissionInternet);
        Log.d("Pre-permiso","la variable permoso tiene: "+permissionAccessWifi);
        this.showPhoneStatePermission(permissionACLocation);
        this.showPhoneStatePermission(permissionAFLocation);
        this.showPhoneStatePermission(permissionInternet);
        this.showPhoneStatePermission(permissionAccessWifi);
        this.showPhoneStatePermission(readPhoneState);


        //textViewRsrp = (TextView) findViewById(R.id.textViewRsrp);
        //textViewRsrq = (TextView) findViewById(R.id.textViewRsrq);
        //textViewRssnr = (TextView) findViewById(R.id.textViewRssnr);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Button startButton = (Button) findViewById(R.id.buttonOnService);
       // startButton.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View view) {

        //    }
        //});

        //CellularCoverageService.setUpdateListener(this);
        //iniciarCellularCoverageService();
        Toast.makeText(MainActivity.this, "OnCreate", Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity.this, "onResume", Toast.LENGTH_LONG).show();
        Button startButton = (Button) findViewById(R.id.buttonStartCapture);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showRatingBar();
            }
        });

        onBottonNavigationPress();
    }

    @Override
    protected void onDestroy() {
        // Antes de cerrar la aplicacion se para el servicio (el cronometro)
        //
        super.onDestroy();
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

    //Sección para asegurar la obtención de permisos requeridos por la aplicación
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