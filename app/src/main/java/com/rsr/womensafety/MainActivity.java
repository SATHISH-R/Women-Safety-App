package com.rsr.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextInputEditText fname, lname, age, mobile, address;
    CountryCodePicker countryCode;
    Button save;

    AlertDialog.Builder alertDialog;
    AlertDialog alertDialog1;

    public String Fname, Lname, Age, Mobile, Address;

    private static final int REQUEST_CALL = 77;
    private static final int REQUEST_SMS = 78;

    LocationManager locationManager;
    String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isConnected(this)) {
            showConnDialog();
        }

        permissions();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }


        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        age = findViewById(R.id.age);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);

        countryCode = findViewById(R.id.countryCode);

        save = findViewById(R.id.save);

        SessionManager sessionManager = new SessionManager(MainActivity.this);
        if (sessionManager.checkLogin()) {

            getLocation();

            HashMap<String, String> userData = sessionManager.getUserData();

            if (userData.get(SessionManager.KEY_FIRSTNAME) == null) {
            } else {
                startActivity(new Intent(MainActivity.this, Home.class));
            }
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {

//                    SessionManager sessionManager = new SessionManager(MainActivity.this);
                    sessionManager.createLoginSession(Fname, Lname, Age, Mobile, Address);

                    startActivity(new Intent(MainActivity.this, AddAlert.class));
                }

            }
        });


    }


    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_SMS);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
//                showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean validate() {

        Fname = fname.getText().toString().trim();
        Lname = lname.getText().toString().trim();
        Age = age.getText().toString().trim();
        Mobile = mobile.getText().toString().trim();
        Address = address.getText().toString().trim();

        if (Fname.isEmpty()) {
            fname.setError("Enter First Name");
            return false;
        }
        if (Lname.isEmpty()) {
            lname.setError("Enter Last Name");
            return false;
        }
        if (Age.isEmpty() || Integer.parseInt(Age) < 1) {
            age.setError("Enter Age");
            return false;
        }
        if (Mobile.isEmpty()) {
            mobile.setError("Enter Mobile");
            return false;
        }
        if (Address.isEmpty()) {
            address.setError("Enter Address");
            return false;
        }

        Mobile = countryCode.getSelectedCountryCodeWithPlus().toString() + Mobile;
        return true;

    }

    public void permissions(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, REQUEST_CALL );
        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS );
//        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        showTheDialog();
    }

    public void showTheDialog() {
        alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Exit !");
        alertDialog.setMessage("Do you want to EXIT this app ?");
        alertDialog.setCancelable(false);

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        alertDialog1 = alertDialog.create();
        alertDialog1.show();
    }

    private void showConnDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please connect to the Internet or WiFi to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private boolean isConnected(MainActivity mainActivity) {

        ConnectivityManager connectivityManager = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

}