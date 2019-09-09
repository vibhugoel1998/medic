package com.example.john.medic;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class medhelper extends AppCompatActivity {

    ImageButton soscall,hdcall;
    ArrayList<String> medhelperdetails=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medhelper);

        soscall=findViewById(R.id.soscall);
        hdcall=findViewById(R.id.hdcall);
        medhelperdetails=getIntent().getStringArrayListExtra("medhelperdetails");
        Log.d("medhelper", "onCreate: "+medhelperdetails);

        soscall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(medhelper.this)
                        .setTitle("SOS calls")
                        .setMessage("select your task")
                        .setPositiveButton("check accepted SOS call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(medhelper.this,existingsoscalls.class);
                                intent.putStringArrayListExtra("medhelperdetails",medhelperdetails);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("take new SOS calls", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent= new Intent(medhelper.this,newsoscalls.class);
                                intent.putStringArrayListExtra("medhelperdetails",medhelperdetails);
                                startActivity(intent);

                            }
                        }).create().show();

            }
        });

        hdcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(medhelper.this)
                        .setTitle("Home Delivery calls")
                        .setMessage("select your task")
                        .setPositiveButton("check accepted home deliveries", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(medhelper.this,existinghomedeliveries.class);
                                intent.putStringArrayListExtra("medhelperdetails",medhelperdetails);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("take new home deliveries", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent= new Intent(medhelper.this,newhomedeliveries.class);
                                intent.putStringArrayListExtra("medhelperdetails",medhelperdetails);
                                startActivity(intent);

                            }
                        }).create().show();

            }
        });

    }
}
