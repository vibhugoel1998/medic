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

public class newhomedeliveries extends AppCompatActivity {

    ListView lvnewhomedeliveries;
    ArrayList<String> medhelperdetails=new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adaptor;
    private DatabaseReference mDatabase;
    public static final String TAG="newhomedeliveries";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newhomedeliveries);

        medhelperdetails=getIntent().getStringArrayListExtra("medhelperdetails");
        Log.d(TAG, "onCreate: "+medhelperdetails);
        lvnewhomedeliveries=findViewById(R.id.lvnewhomedeliveries);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        adaptor=new ArrayAdapter<String>(this,R.layout.newhomedeliveries_list_item,R.id.order_details,arrayList);
        lvnewhomedeliveries.setAdapter(adaptor);

        mDatabase.child("medhelpers").child("homedeliveryinfo").child(medhelperdetails.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ArrayList<String> data2=new ArrayList<>();
                    ArrayList<String> data3=new ArrayList<>();
                    ArrayList<String> data4=new ArrayList<>();
                    String data="";
                    int k=0;

                    for (DataSnapshot snapshot:postSnapshot.getChildren()){
                        data2.add(snapshot.getValue().toString());
                    }
                    data=data2.get(3)+"\n"+data2.get(4)+"\n"+data2.get(0)+"\n"+data2.get(1)+"\n";

                    for (DataSnapshot snapshot:postSnapshot.getChildren()){
                        if (snapshot.getKey().equalsIgnoreCase("medicines")){
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                data3.add(snapshot1.getKey().toString());
                                data4.add(snapshot1.getValue().toString());

                                data=data+snapshot1.getKey().toString()+":"+snapshot1.getValue().toString();
                            }
                        }
                    }

                    arrayList.add(data);
                    adaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

lvnewhomedeliveries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        final TextView textView= view.findViewById(R.id.order_details);

        new AlertDialog.Builder(newhomedeliveries.this)
                .setMessage("accept this order ?")
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
                        mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("hdcalls").child("name").setValue(list.get(0));
                        mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("hdcalls").child("number").setValue(list.get(1));

                        mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("hdcalls").child("address").setValue(list.get(2));

                        mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("hdcalls").child("bill").setValue(list.get(3));
                        mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("hdcalls").child("order").setValue(list.get(4));

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(list.get(1), null,medhelperdetails.get(0)+"has accepted your home delivery call" , null, null);


                        mDatabase.child("medhelpers").child("homedeliveryinfo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                    mDatabase.child("medhelpers").child("homedeliveryinfo").child(postSnapshot.getKey().toString())
                                            .child(list.get(0)).removeValue();


                                    Intent intent=new Intent(newhomedeliveries.this,existinghomedeliveries.class);
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
