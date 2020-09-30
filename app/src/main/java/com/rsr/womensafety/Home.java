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
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends AppCompatActivity {

    TextView name_age, mobile_home, address_home;
    ImageView menu_at_home;

    AlertDialog.Builder alertDialog;
    AlertDialog alertDialog1;

    RelativeLayout abuse, pain, emergency;

    private static final int REQUEST_CALL = 77;
    private static final int REQUEST_SMS = 78;

    LocationManager locationManager;
    String latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

        name_age = findViewById(R.id.name_age);
        mobile_home = findViewById(R.id.mobile_home);
        address_home = findViewById(R.id.address_home);

        menu_at_home = findViewById(R.id.menu_at_home);

        abuse = findViewById(R.id.abuse);
        pain = findViewById(R.id.pain);
        emergency = findViewById(R.id.emergency);

        SessionManager sessionManager = new SessionManager(Home.this);
        HashMap<String, String> userData = sessionManager.getUserData();

        if(userData.get(SessionManager.KEY_FIRSTNAME) == null){
            startActivity(new Intent(Home.this, MainActivity.class));
        }

        String name = userData.get(SessionManager.KEY_FIRSTNAME) + " " + userData.get(SessionManager.KEY_LASTNAME) + " (" + userData.get(SessionManager.KEY_AGE) +")";
        String mobile = userData.get(SessionManager.KEY_MOBILE);
        String address = userData.get(SessionManager.KEY_ADDRESS);

        name_age.setText(name);
        mobile_home.setText(mobile);
        address_home.setText(address);

       abuse.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if (ContextCompat.checkSelfPermission(Home.this,
                       Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL );
               }
               else {
                   Intent callIntent = new Intent(Intent.ACTION_CALL);
                   callIntent.setData(Uri.parse("tel:"+"*100"));//change the number
                   startActivity(callIntent);
               }

               if (ContextCompat.checkSelfPermission(Home.this,
                       Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS );
               }
               else {

                   ArrayList<String> Mobile = new ArrayList<String>();

                   SessionManager sessionManager = new SessionManager(Home.this);
                   Mobile = sessionManager.getArrayList("userMobile");
                   for(int i=0;i<Mobile.size();i++) {
                       SmsManager smsManager = SmsManager.getDefault();


                           getLocation();

                       String uri = "http://maps.google.com/maps?saddr=" + latitude +","+ longitude;
                       StringBuffer smsBody = new StringBuffer();
                       smsBody.append(Uri.parse(uri));

                       smsManager.sendTextMessage(Mobile.get(i), null, "Help! Someone Abusing me! Help!\n"+smsBody.toString(), null, null);
                   }
                   Toast.makeText(Home.this, "Alert Message has been Sent", Toast.LENGTH_SHORT).show();
               }

           }
       });

       pain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if (ContextCompat.checkSelfPermission(Home.this,
                       Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL );
               }
               else {
                   Intent callIntent = new Intent(Intent.ACTION_CALL);
                   callIntent.setData(Uri.parse("tel:"+"*102"));//change the number
                   startActivity(callIntent);
               }

               if (ContextCompat.checkSelfPermission(Home.this,
                       Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS );
               }
               else {

                   ArrayList<String> Mobile = new ArrayList<String>();

                   SessionManager sessionManager = new SessionManager(Home.this);
                   Mobile = sessionManager.getArrayList("userMobile");
                   for(int i=0;i<Mobile.size();i++) {
                       SmsManager smsManager = SmsManager.getDefault();

                      getLocation();

                       String uri = "http://maps.google.com/maps?saddr=" + latitude +","+ longitude;
                       StringBuffer smsBody = new StringBuffer();
                       smsBody.append(Uri.parse(uri));

                       smsManager.sendTextMessage(Mobile.get(i), null, "Emergency! Labour Pain! Help!\n"+smsBody.toString(), null, null);
                   }
                   Toast.makeText(Home.this, "Alert Message has been Sent", Toast.LENGTH_SHORT).show();
               }

           }
       });

       emergency.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
               final View customLayout = getLayoutInflater().inflate(R.layout.emergency, null);
               builder1.setView(customLayout);
               builder1.setCancelable(true);

               final AlertDialog alert11 = builder1.create();

               customLayout.findViewById(R.id.police).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (ContextCompat.checkSelfPermission(Home.this,
                               Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                           ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL );
                       }
                       else {
                           Intent callIntent = new Intent(Intent.ACTION_CALL);
                           callIntent.setData(Uri.parse("tel:"+"*100"));//change the number
                           startActivity(callIntent);
                       }

                       if (ContextCompat.checkSelfPermission(Home.this,
                               Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                           ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS );
                       }
                       else {

                           ArrayList<String> Mobile = new ArrayList<String>();

                           SessionManager sessionManager = new SessionManager(Home.this);
                           Mobile = sessionManager.getArrayList("userMobile");
                           for(int i=0;i<Mobile.size();i++) {
                               SmsManager smsManager = SmsManager.getDefault();

                               getLocation();

                               String uri = "http://maps.google.com/maps?saddr=" + latitude +","+ longitude;
                               StringBuffer smsBody = new StringBuffer();
                               smsBody.append(Uri.parse(uri));

                               smsManager.sendTextMessage(Mobile.get(i), null, "Help! Someone Help me! Help!\n"+smsBody.toString(), null, null);
                           }
                           Toast.makeText(Home.this, "Alert Message has been Sent", Toast.LENGTH_SHORT).show();
                       }

                   }
               });

               customLayout.findViewById(R.id.medical).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (ContextCompat.checkSelfPermission(Home.this,
                               Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                           ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL );
                       }
                       else {
                           Intent callIntent = new Intent(Intent.ACTION_CALL);
                           callIntent.setData(Uri.parse("tel:"+"*102"));//change the number
                           startActivity(callIntent);
                       }

                       if (ContextCompat.checkSelfPermission(Home.this,
                               Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                           ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS );
                       }
                       else {

                           ArrayList<String> Mobile = new ArrayList<String>();

                           SessionManager sessionManager = new SessionManager(Home.this);
                           Mobile = sessionManager.getArrayList("userMobile");
                           for(int i=0;i<Mobile.size();i++) {
                               SmsManager smsManager = SmsManager.getDefault();

                               getLocation();

                               String uri = "http://maps.google.com/maps?saddr=" + latitude +","+ longitude;
                               StringBuffer smsBody = new StringBuffer();
                               smsBody.append(Uri.parse(uri));

                               smsManager.sendTextMessage(Mobile.get(i), null, "Emergency! Medical Emergency! Help!\n"+smsBody.toString(), null, null);
                           }
                           Toast.makeText(Home.this, "Alert Message has been Sent", Toast.LENGTH_SHORT).show();
                       }

                   }
               });

               alert11.show();

           }
       });



        menu_at_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Home.this);
                final View customLayout = getLayoutInflater().inflate(R.layout.menu, null);
                builder1.setView(customLayout);
                builder1.setCancelable(true);

                final AlertDialog alert11 = builder1.create();

                customLayout.findViewById(R.id.home_menu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(customLayout.getContext(), Home.class));
                    }
                });

                customLayout.findViewById(R.id.AddAlert_menu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(customLayout.getContext(), AddAlert.class));
                    }
                });

                customLayout.findViewById(R.id.logout_menu).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SessionManager sessionManager1 = new SessionManager(customLayout.getContext());
                        sessionManager1.logout();
                        startActivity(new Intent(customLayout.getContext(), MainActivity.class));
                    }
                });

                alert11.show();
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
                Home.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Home.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    @Override
    public void onBackPressed() {
        showTheDialog();
    }

    public void showTheDialog(){
        alertDialog = new AlertDialog.Builder(Home.this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
//        if(requestCode == REQUEST_SMS){
//            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }

    }

    public void permissions(){
        if (ContextCompat.checkSelfPermission(Home.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS}, REQUEST_CALL );
        }
//        if (ContextCompat.checkSelfPermission(Home.this,
//                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(Home.this, new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS );
//        }
    }

    private void showConnDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
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

    private boolean isConnected(Home home) {

        ConnectivityManager connectivityManager = (ConnectivityManager) home.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }


}