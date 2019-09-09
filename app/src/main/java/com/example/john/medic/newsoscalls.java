package com.example.john.medic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class newsoscalls extends AppCompatActivity {

    ArrayList<String> medhelperdetails=new ArrayList<>();
    ListView lvnewsosdetails;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adaptor;
    private DatabaseReference mDatabase;
    public static final String TAG="newsoscalls";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsoscalls);

        medhelperdetails=getIntent().getStringArrayListExtra("medhelperdetails");
        Log.d(TAG, "onCreate: "+medhelperdetails);
        lvnewsosdetails=findViewById(R.id.lvnewsosdetails);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        adaptor=new ArrayAdapter<String>(this,R.layout.newsoscalls_list_item,R.id.patient_details,arrayList);
        lvnewsosdetails.setAdapter(adaptor);


        mDatabase.child("medhelpers").child("sosinfo").child(medhelperdetails.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<String> data2=new ArrayList<>();
                    String data="";
                    int k=0;

                    for (DataSnapshot snapshot:postSnapshot.getChildren()){
                        data2.add(snapshot.getValue().toString());
                    }
                    data=data2.get(2)+"\n"+data2.get(3)+"\n"+data2.get(0)+"\n"+data2.get(1);
                    arrayList.add(data);
                    adaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lvnewsosdetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {

                new AlertDialog.Builder(newsoscalls.this)
                        .setMessage("select your task ")
                        .setPositiveButton("view location in MAPS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                 TextView textView= view.findViewById(R.id.patient_details);
                                 ArrayList<String> list=new ArrayList<>();
                                 String str2="";

                                for (int x=0;x<=textView.getText().toString().length();x++){

                                    if (x==textView.getText().toString().length()){
                                        list.add(str2);
                                        break;
                                    }


                                    if (textView.getText().toString().charAt(x)=='\n') {
                                        list.add(str2);
                                        str2="";
                                        continue;
                                    }



                                    str2 = str2 +textView.getText().toString().charAt(x);
                                }

                                Intent intent=new Intent(newsoscalls.this,MapsActivity2.class);
                                intent.putStringArrayListExtra("listdetails",list);
                                startActivity(intent);



                            }
                        })
                        .setNegativeButton("take this SOS call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                TextView textView= view.findViewById(R.id.patient_details);
                                final ArrayList<String> list=new ArrayList<>();
                                String str2="";

                                for (int x=0;x<=textView.getText().toString().length();x++){

                                    if (x==textView.getText().toString().length()){
                                        list.add(str2);
                                        break;
                                    }


                                    if (textView.getText().toString().charAt(x)=='\n') {
                                        list.add(str2);
                                        str2="";
                                        continue;
                                    }



                                    str2 = str2 +textView.getText().toString().charAt(x);
                                }
                                mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("number").setValue(medhelperdetails.get(1));
                                mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("soscall").child("name").setValue(list.get(0));
                                mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("soscall").child("number").setValue(list.get(1));
                                mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("soscall").child("latitude").setValue(list.get(2));
                                mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("soscall").child("longitude").setValue(list.get(3));
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(list.get(1), null,medhelperdetails.get(0)+"has accepted your SOS call" , null, null);


                                mDatabase.child("medhelpers").child("sosinfo").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                                mDatabase.child("medhelpers").child("sosinfo").child(postSnapshot.getKey().toString())
                                                        .child(list.get(0)).removeValue();

                                                Intent intent=new Intent(newsoscalls.this,existingsoscalls.class);
                                                intent.putStringArrayListExtra("medhelperdetails",medhelperdetails);
                                                startActivity(intent);
                                                finish();

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }).create().show();

            }
        });


    }
}
