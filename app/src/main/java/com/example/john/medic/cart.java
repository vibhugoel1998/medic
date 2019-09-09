package com.example.john.medic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class cart extends AppCompatActivity {

    ListView lvcartdetails;
    Button btorder;
    TextView tvbill;
    ArrayList<String> userdetails=new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adaptor;
    private DatabaseReference mDatabase;
    public static final String TAG="cart";
    Random random=new Random();
    private String LEGACY_SERVER_KEY = "AIzaSyDaSJg06Si2CwgH_WhOfWJdYYoDHA9Wj50";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        lvcartdetails=findViewById(R.id.lvcartdetails);
        btorder=findViewById(R.id.btorder);
        tvbill=findViewById(R.id.tvbill);

        userdetails=getIntent().getStringArrayListExtra("userdetails");

        adaptor=new ArrayAdapter<String>(this,R.layout.cart_list_item,R.id.cart_medname,arrayList);
        lvcartdetails.setAdapter(adaptor);

        mDatabase.child("users").child(userdetails.get(0)).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {

            String data="";
            int bill=0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (postSnapshot.getKey().toString().equalsIgnoreCase("bill"))
                        continue;

                    data=postSnapshot.getKey().toString()+"\n"+"quantity:"+postSnapshot.getChildrenCount();
                    arrayList.add(data);
                    adaptor.notifyDataSetChanged();

                    mDatabase.child("stock").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {

                            for (DataSnapshot snapshot : dataSnapshot2.getChildren()) {
                                if (snapshot.getKey().toString().equalsIgnoreCase(postSnapshot.getKey().toString())) {
                                    bill = bill + (Integer.valueOf(String.valueOf(postSnapshot.getChildrenCount())) * Integer.valueOf(snapshot.getValue().toString()));
                                    Log.d(TAG, "onDataChange: "+bill);
                                }
                            }

                            tvbill.setText("OUR AMOUNT:" + bill);
                            mDatabase.child("users").child(userdetails.get(0)).child("cart").child("bill").setValue(String.valueOf(bill));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    }

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final String[] str = {""};
        final String[] str2 = {""};
        final String[] str3 = {""};

        lvcartdetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final TextView textView= view.findViewById(R.id.cart_medname);
                str[0] ="";
                str2[0]="";
                str3[0]="";



                new AlertDialog.Builder(cart.this)
                        .setTitle("Edit Item")
                        .setPositiveButton("plus one", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                for (int x=0;x<textView.getText().toString().length();x++){

                                    if (textView.getText().toString().charAt(x)=='\n')
                                        break;

                                    str[0] = str[0] +textView.getText().toString().charAt(x);
                                }

                                mDatabase.child("users").child(userdetails.get(0)).child("cart").child(str[0]).push().setValue(random.nextInt(1000));
                                Intent intent=new Intent(cart.this,cart.class);
                                intent.putStringArrayListExtra("userdetails",userdetails);
                                startActivity(intent);
                                finish();


                            }
                        })
                        .setNegativeButton("minus one", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                for (int x=0;x<textView.getText().toString().length();x++){

                                    if (textView.getText().toString().charAt(x)=='\n')
                                        break;

                                    str2[0] = str2[0] +textView.getText().toString().charAt(x);
                                }

                                mDatabase.child("users").child(userdetails.get(0)).child("cart").child(str2[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            mDatabase.child("users").child(userdetails.get(0)).child("cart").child(str2[0]).child(postSnapshot.getKey()).removeValue();
                                            break;
                                        }

                                        }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Intent intent=new Intent(cart.this,cart.class);
                                intent.putStringArrayListExtra("userdetails",userdetails);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .setNeutralButton("remove medicine", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                for (int x=0;x<textView.getText().toString().length();x++){

                                    if (textView.getText().toString().charAt(x)=='\n')
                                        break;

                                    str3[0] = str3[0] +textView.getText().toString().charAt(x);
                                }

                                mDatabase.child("users").child(userdetails.get(0)).child("cart").child(str3[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                            mDatabase.child("users").child(userdetails.get(0)).child("cart").child(str3[0]).child(postSnapshot.getKey()).removeValue();


                                        }

                                        }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Intent intent=new Intent(cart.this,cart.class);
                                intent.putStringArrayListExtra("userdetails",userdetails);
                                startActivity(intent);
                                finish();


                            }
                        }).create().show();

            }
        });


        btorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(cart.this);
                alertDialog2.setMessage("confirm your order?");
                alertDialog2.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertDialog2.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mDatabase.child("users").child(userdetails.get(0)).child("homedelivery").removeValue();


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(cart.this);
                        alertDialog.setMessage("enter your delivering address");
                        final EditText input = new EditText(cart.this);
                        input.setHint("enter here");

                        LinearLayout layout = new LinearLayout(cart.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        layout.setLayoutParams(lparams);
                        layout.addView(input);
                        alertDialog.setView(layout);


                        alertDialog.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final ArrayList<String> medhelpernames=new ArrayList<>();
                                final int[] k = {0};
                                mDatabase.child("medhelpers").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot1) {
                                        for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                                            if (dataSnapshot2.getKey().toString().equalsIgnoreCase("sosinfo") || dataSnapshot2.getKey().toString().equalsIgnoreCase("homedeliveryinfo")){
                                                continue;
                                            }

                                            medhelpernames.add(dataSnapshot2.getKey().toString());
                                            mDatabase.child("medhelpers").child("homedeliveryinfo").child(dataSnapshot2.getKey().toString()).child(userdetails.get(0)).removeValue();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });




                                mDatabase.child("users").child(userdetails.get(0)).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                            if (postSnapshot.getKey().toString().equalsIgnoreCase("bill")){
                                                mDatabase.child("users").child(userdetails.get(0)).child("homedelivery").child(postSnapshot.getKey().toString())
                                                        .setValue(postSnapshot.getValue().toString());

                                                for (int a=0;a<medhelpernames.size();a++) {


                                                    mDatabase.child("medhelpers").child("homedeliveryinfo").child(medhelpernames.get(a)).child(userdetails.get(0)).child(postSnapshot.getKey().toString())
                                                            .setValue(postSnapshot.getValue().toString());
                                                }
                                                continue;
                                            }
                                            mDatabase.child("users").child(userdetails.get(0)).child("homedelivery").child(postSnapshot.getKey().toString())
                                                    .setValue(postSnapshot.getChildrenCount());

                                            for (int a=0;a<medhelpernames.size();a++) {

                                                mDatabase.child("medhelpers").child("homedeliveryinfo").child(medhelpernames.get(a)).child(userdetails.get(0)).child("medicines").child(postSnapshot.getKey().toString())
                                                        .setValue(postSnapshot.getChildrenCount());
                                            }
                                            k[0]++;
                                        }

                                        mDatabase.child("users").child(userdetails.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                                for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                                                    if (dataSnapshot2.getKey().toString().equalsIgnoreCase("number")){
                                                        for (int a=0;a<medhelpernames.size();a++) {
                                                            mDatabase.child("medhelpers").child("homedeliveryinfo").child(medhelpernames.get(a)).child(userdetails.get(0)).child("name").setValue(userdetails.get(0));

                                                            mDatabase.child("medhelpers").child("homedeliveryinfo").child(medhelpernames.get(a)).child(userdetails.get(0)).child("number")
                                                                    .setValue(dataSnapshot2.getValue().toString());
                                                            mDatabase.child("medhelpers").child("homedeliveryinfo").child(medhelpernames.get(a)).child(userdetails.get(0)).child("address")
                                                                    .setValue(input.getText().toString());
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        mDatabase.child("users").child(userdetails.get(0)).child("cart").removeValue();




                                        Intent intent=new Intent(cart.this,vieworder.class);
                                        intent.putStringArrayListExtra("userdetails",userdetails);
                                        startActivity(intent);
                                        finish();


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                Toast.makeText(cart.this, "your order has been placed", Toast.LENGTH_SHORT).show();
                                sendNotification("homedelivery", "new home delivery calls");



                            }
                        });

                        alertDialog.create().show();

                    }
                });
                alertDialog2.create().show();


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

}
