package com.example.john.medic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.*;


public class user2screen extends AppCompatActivity {
    EditText etmedname;
    Button btinfo,btdelivery,btvieworder;
    ImageButton btsos;
    ArrayList<String> userdetails;
    private DatabaseReference mDatabase;
    Random r=new Random();
    Handler h=new Handler();

    private String LEGACY_SERVER_KEY = "AIzaSyDaSJg06Si2CwgH_WhOfWJdYYoDHA9Wj50";
    Button topic_menu_btn_off, topic_order_btn_off, topic_water_btn_off, topic_cheque_btn_off;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");




    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2screen);


        final EditText etmedname=findViewById(R.id.etmedname);
        Button btinfo=findViewById(R.id.btinfo);
        Button btdelivery=findViewById(R.id.btdelivery);
        Button btvieworder=findViewById(R.id.btvieworder);
        btsos=findViewById(R.id.btsos);
        userdetails=new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("XALAN_USER2",MainActivity.ID);
        userdetails= getIntent().getStringArrayListExtra("userdetails");

        final Intent i = new Intent(user2screen.this,MyIntentService.class);
        mDatabase.child("users").child(userdetails.get(0)).child("location").child("latitude").setValue("0.0");
        mDatabase.child("users").child(userdetails.get(0)).child("location").child("longitude").setValue("0.0");
        i.putStringArrayListExtra("userdetails",userdetails);
        startService(i);



        Runnable r=new Runnable() {

            @Override
            public void run() {
                Log.d("user2screen", "run: service endssssssss");
                stopService(i);
            }
        };

        h.postDelayed(r,20000);

        mDatabase.child("users").child(userdetails.get(0)).child("location").child("latitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot==null)
                {
                    userdetails.add(2,"0.0");
                }
                else {
                    userdetails.add(2, dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("users").child(userdetails.get(0)).child("location").child("longitude").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot==null)
                {
                    userdetails.add(3,"0.0");
                }
                else {
                    userdetails.add(3, dataSnapshot.getValue().toString());
                }


                for (int in=0;in<userdetails.size();in++)
                    Log.d("user2screen", "onCreate: "+userdetails.get(in));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userdetails.add(4,etmedname.getText().toString());
                mDatabase.child("users").child(userdetails.get(0)).child("medicine").setValue(userdetails.get(4));
                Intent intent = new Intent(user2screen.this,gerealinfo.class);
                intent.putStringArrayListExtra("userdetails",userdetails);
                startActivity(intent);
                finish();
            }
        });

        btdelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userdetails.add(4,etmedname.getText().toString());
                mDatabase.child("users").child(userdetails.get(0)).child("number").setValue(userdetails.get(1));
                mDatabase.child("users").child(userdetails.get(0)).child("medicine").setValue(userdetails.get(4));
                Intent intent = new Intent(user2screen.this,cart.class);
                intent.putStringArrayListExtra("userdetails",userdetails);
                startActivity(intent);
                finish();

            }
        });

        btvieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userdetails.add(4,etmedname.getText().toString());
                mDatabase.child("users").child(userdetails.get(0)).child("number").setValue(userdetails.get(1));
                mDatabase.child("users").child(userdetails.get(0)).child("medicine").setValue(userdetails.get(4));
                Intent intent = new Intent(user2screen.this,vieworder.class);
                intent.putStringArrayListExtra("userdetails",userdetails);
                startActivity(intent);
                finish();

            }
        });

        btsos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(user2screen.this);
                alertDialog.setMessage("any details about your situation ?");
                final EditText input = new EditText(user2screen.this);
                input.setHint("enter here");

                LinearLayout layout = new LinearLayout(user2screen.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layout.setLayoutParams(lparams);
                layout.addView(input);
                alertDialog.setView(layout);

                alertDialog.setPositiveButton("send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendNotification("topic_menu", "new sos calls");

                        mDatabase.child("users").child(userdetails.get(0)).child("sos").child("situation").setValue(input.getText().toString());
                        Toast.makeText(user2screen.this, "details sent", Toast.LENGTH_SHORT).show();
                        mDatabase.child("medhelpers").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                                    if (!postSnapshot.getKey().toString().equalsIgnoreCase("sosinfo")&& !postSnapshot.getKey().toString().equalsIgnoreCase("homedeliveryinfo")) {


                                        String medhelpername = postSnapshot.getKey().toString();
                                        String medhelpernumber="";

                                        for (DataSnapshot snapshot: postSnapshot.getChildren()) {

                                            if (snapshot.getKey().toString().equalsIgnoreCase("number")) {

                                                medhelpernumber = snapshot.getValue().toString();

                                                SmsManager smsManager = SmsManager.getDefault();

                                                if (userdetails.get(2).equalsIgnoreCase("0.0")) {
                                                    smsManager.sendTextMessage(medhelpernumber, null, "in emergency, details are:\nname:" + userdetails.get(0) + ",info:" + input.getText().toString(), null, null);
                                                } else {
                                                    smsManager.sendTextMessage(medhelpernumber, null, "in emergency, details are:\nname:" + userdetails.get(0) + ",location:" + userdetails.get(2) + "," + userdetails.get(3) + ",info:" + input.getText().toString(), null, null);
                                                }

                                                Toast.makeText(user2screen.this, "sms sent", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        mDatabase.child("medhelpers").child("sosinfo").child(medhelpername).child(userdetails.get(0)).child("name").setValue(userdetails.get(0));
                                        mDatabase.child("medhelpers").child("sosinfo").child(medhelpername).child(userdetails.get(0)).child("number").setValue(userdetails.get(1));
                                        mDatabase.child("medhelpers").child("sosinfo").child(medhelpername).child(userdetails.get(0)).child("latitude").setValue(userdetails.get(2));
                                        mDatabase.child("medhelpers").child("sosinfo").child(medhelpername).child(userdetails.get(0)).child("longitude").setValue(userdetails.get(3));



                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });                    }
                });

                alertDialog.setNegativeButton("no,continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendNotification("topic_menu", "new sos calls");

                        send_sms();

                    }
                });


                alertDialog.create().show();






            }
        });






    }
    private void sendNotification(final String topic, final String msg) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    //this is for additional data
                    JSONObject payloadJson = new JSONObject();
                    payloadJson.put("table_number", "dsgf");
                    payloadJson.put("user_uid", MainActivity.ID);
                    // till here
                    dataJson.put("body", msg);
                    dataJson.put("title", "Alert");
                    json.put("notification", dataJson);
                    json.put("data", payloadJson);
                    json.put("to", "/topics/".concat(topic));
                    json.put("sound", "default");
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + LEGACY_SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    com.squareup.okhttp.Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                } catch (Exception e) {
                    Log.d("XALAN_2",e.toString());
                }
                return null;
            }
        }.execute();

    }

    void send_sms(){
        mDatabase.child("medhelpers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                    if (!postSnapshot.getKey().toString().equalsIgnoreCase("sosinfo") && !postSnapshot.getKey().toString().equalsIgnoreCase("homedeliveryinfo")) {


                        String medhelpername = postSnapshot.getKey().toString();
                        String medhelpernumber="";

                        for (DataSnapshot snapshot: postSnapshot.getChildren()) {

                            if (snapshot.getKey().toString().equalsIgnoreCase("number")) {

                                medhelpernumber = snapshot.getValue().toString();
                                Log.d("user2screen", "onDataChange: "+medhelpernumber);

                                SmsManager smsManager = SmsManager.getDefault();

                                if (userdetails.get(2).equalsIgnoreCase("0.0")) {
                                    smsManager.sendTextMessage(medhelpernumber, null, "in emergency, details are:\nname:" + userdetails.get(0), null, null);


                                } else {

                                    smsManager.sendTextMessage(medhelpernumber, null, "in emergency, details are:\nname:" + userdetails.get(0) + ",location:" + userdetails.get(2) + "," + userdetails.get(3), null, null);
                                }
                                Toast.makeText(user2screen.this, "sms sent", Toast.LENGTH_SHORT).show();
                            }


                        }

                        mDatabase.child("medhelpers").child("sosinfo").child(medhelpername).child(userdetails.get(0)).child("name").setValue(userdetails.get(0));
                        mDatabase.child("medhelpers").child("sosinfo").child(medhelpername).child(userdetails.get(0)).child("number").setValue(userdetails.get(1));
                        mDatabase.child("medhelpers").child("sosinfo").child(medhelpername).child(userdetails.get(0)).child("latitude").setValue(userdetails.get(2));
                        mDatabase.child("medhelpers").child("sosinfo").child(medhelpername).child(userdetails.get(0)).child("longitude").setValue(userdetails.get(3));


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
