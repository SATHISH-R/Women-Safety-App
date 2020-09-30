package com.rsr.womensafety;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class AddAlert extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    AlertDialog.Builder alertDialog;
    AlertDialog alertDialog1;

    Button add_alert, add_home;

    ImageView menu_at_alert;

    ImageButton back_at_alert;

    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> mobile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);

        add_alert = findViewById(R.id.add_alert);
        recyclerView = findViewById(R.id.recyclerView_alert);
        menu_at_alert = findViewById(R.id.menu_at_alert);
        back_at_alert = findViewById(R.id.back__at_alert);

        add_home = findViewById(R.id.add_home);

        SessionManager sessionManager = new SessionManager(AddAlert.this);
        name = sessionManager.getArrayList("userName");
        mobile = sessionManager.getArrayList("userMobile");


        try {
            if (!name.isEmpty()) {
                adapter = new RecyclerAdapter(getApplicationContext(), name, mobile);
                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager layoutManager =
                        new LinearLayoutManager(AddAlert.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);

                menu_at_alert.setVisibility(View.VISIBLE);
                add_home.setVisibility(View.VISIBLE);
            }
            else {
                menu_at_alert.setVisibility(View.GONE);
                add_home.setVisibility(View.GONE);
            }
        }
        catch (NullPointerException e){
            menu_at_alert.setVisibility(View.GONE);
            add_home.setVisibility(View.GONE);
        }

        add_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAlert.this, Home.class));
            }
        });

        back_at_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        add_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddAlert.this, AddMobile.class);
                startActivity(i);
            }
        });

        menu_at_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddAlert.this);
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

    @Override
    public void onBackPressed() {
        try {
            if (!name.isEmpty()) {
                startActivity(new Intent(AddAlert.this, Home.class));
            } else {
                showTheDialog();
            }
        }
        catch (NullPointerException e){
            showTheDialog();
        }
    }

    public void showTheDialog(){
        alertDialog = new AlertDialog.Builder(AddAlert.this);
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

}