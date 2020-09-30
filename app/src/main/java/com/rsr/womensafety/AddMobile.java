package com.rsr.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;

public class AddMobile extends AppCompatActivity {

    TextInputEditText add_name, add_mobile;
    CountryCodePicker add_countryCode;
    Button add_alert_mobile;

    String name, mobile;

    ImageButton back_at_add;

    public ArrayList<String> Name = new ArrayList<String>();
    public ArrayList<String> Mobile = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mobile);

        add_name = findViewById(R.id.add_name);
        add_mobile = findViewById(R.id.add_mobile);

        add_countryCode = findViewById(R.id.add_countryCode);

        add_alert_mobile = findViewById(R.id.add_alert_mobile);

        back_at_add = findViewById(R.id.back_at_add);

        back_at_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        add_alert_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()){

                    SessionManager sessionManager = new SessionManager(AddMobile.this);

                    Name = sessionManager.getArrayList("userName");
                    Mobile = sessionManager.getArrayList("userMobile");

                    if(Name == null){

                        Name = new ArrayList<String>();
                        Mobile = new ArrayList<String>();

                        Name.add(name);
                        Mobile.add(mobile);

                        sessionManager.saveArrayList(Name, "userName");
                        sessionManager.saveArrayList(Mobile, "userMobile");

                    }
                    else {

                        Name.add(name);
                        Mobile.add(mobile);

                        sessionManager.saveArrayList(Name, "userName");
                        sessionManager.saveArrayList(Mobile, "userMobile");

                    }

                    startActivity(new Intent(AddMobile.this, AddAlert.class));
                }

            }
        });

    }

    public boolean validate(){

        name = add_name.getText().toString().trim();
        mobile = add_mobile.getText().toString().trim();

        if(name.isEmpty()){
            add_name.setError("Enter First Name");
            return false;
        }
        if(mobile.isEmpty() || mobile.length() <10){
            add_mobile.setError("Enter Mobile");
            return false;
        }

        mobile = add_countryCode.getSelectedCountryCodeWithPlus().toString() + mobile;
        return true;

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddMobile.this, AddAlert.class));
    }


}