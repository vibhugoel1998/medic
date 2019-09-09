package com.example.john.medic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class vieworder extends AppCompatActivity {

    Button btcancelorder, btcc;
    private DatabaseReference mDatabase;
    ArrayList<String> userdetails = new ArrayList<>();
    ListView lvhddetails;
    TextView hd_tvbill;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adaptor;
    public static final String TAG="vieworder";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vieworder);

        lvhddetails=findViewById(R.id.lvhddetails);
        hd_tvbill=findViewById(R.id.hd_tvbill);
        btcancelorder = findViewById(R.id.btcancelorder);
        btcc = findViewById(R.id.btcc);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        userdetails = getIntent().getStringArrayListExtra("userdetails");

        adaptor=new ArrayAdapter<String>(this,R.layout.vieworder_list_item,R.id.hd_medname,arrayList);
        lvhddetails.setAdapter(adaptor);

        mDatabase.child("users").child(userdetails.get(0)).child("homedelivery").addListenerForSingleValueEvent(new ValueEventListener() {

            String data="";
            int bill=0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    if (postSnapshot.getKey().toString().equalsIgnoreCase("bill")) {
                        hd_tvbill.setText("TOTAL AMOUNT:" + postSnapshot.getValue());
                        continue;
                    }

                    if (postSnapshot.getKey().toString().equalsIgnoreCase("status")) {
                        continue;
                    }


                    data=postSnapshot.getKey().toString()+"\n"+"quantity:"+postSnapshot.getValue();
                    arrayList.add(data);
                    adaptor.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btcancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(vieworder.this)
                        .setTitle("cancel order")
                        .setMessage("are you sure ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabase.child("users").child(userdetails.get(0)).child("homedelivery").removeValue();

                                final ArrayList<String> medhelpernames=new ArrayList<>();
                                final int[] k = {0};
                                mDatabase.child("medhelpers").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot1) {
                                        for (final DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
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

                        mDatabase.child("medhelpers").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (final DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                                    if (dataSnapshot2.getKey().toString().equalsIgnoreCase("sosinfo") || dataSnapshot2.getKey().toString().equalsIgnoreCase("homedeliveryinfo")){
                                        continue;
                                    }

                                    mDatabase.child("medhelpers").child(dataSnapshot2.getKey().toString()).child("hdcalls").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                                                Log.d(TAG, "onDataChange:12871 "+snapshot.getValue());
                                                if (snapshot.getValue().toString().equalsIgnoreCase(userdetails.get(0))) {
                                                    mDatabase.child("medhelpers").child(dataSnapshot2.getKey().toString()).child("hdcalls").removeValue();
                                                }
                                            }
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



                                Log.d(TAG, "onClick: "+medhelpernames);


                                Toast.makeText(vieworder.this, "your order has been cancelled", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(vieworder.this, useractivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();

            }
        });

        btcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(vieworder.this)
                        .setTitle("contact customer care")
                        .setMessage("make call ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent i1 = new Intent(Intent.ACTION_CALL);
                                String tel = "tel:" + "9910077157";
                                i1.setData(Uri.parse(tel));
                                if (ActivityCompat.checkSelfPermission(vieworder.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(i1);
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();

            }
        });

    }

}
