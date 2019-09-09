package com.example.john.medic;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class existingsoscalls extends AppCompatActivity {

    Button bt1,bt2,bt3,bt4,bt5,bt6,bt7;
    ArrayList<String> medhelperdetails=new ArrayList<>();

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existingsoscalls);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        bt1=findViewById(R.id.bt1);
        bt2=findViewById(R.id.bt2);
        bt3=findViewById(R.id.bt3);
        bt4=findViewById(R.id.bt4);
        bt5=findViewById(R.id.bt5);
        bt6=findViewById(R.id.bt6);
        bt7=findViewById(R.id.bt7);

        medhelperdetails=getIntent().getStringArrayListExtra("medhelperdetails");


        mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("soscall").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                    if (postsnapshot.getKey().toString().equalsIgnoreCase("name"))
                    bt1.setText(postsnapshot.getValue().toString());
                    if (postsnapshot.getKey().toString().equalsIgnoreCase("number"))
                        bt2.setText(postsnapshot.getValue().toString());
                    if (postsnapshot.getKey().toString().equalsIgnoreCase("latitude"))
                        bt3.setText(postsnapshot.getValue().toString());
                    if (postsnapshot.getKey().toString().equalsIgnoreCase("longitude"))
                        bt4.setText(postsnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Intent.ACTION_CALL);
                String tel = "tel:" + bt2.getText().toString();
                i1.setData(Uri.parse(tel));

                if (ActivityCompat.checkSelfPermission(existingsoscalls.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        });

        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(existingsoscalls.this);
                alertDialog.setTitle("Enter Your Message");

                final EditText input = new EditText(existingsoscalls.this);
                final EditText input2 = new EditText(existingsoscalls.this);

                input.setHint("enter message");
                LinearLayout layout = new LinearLayout(existingsoscalls.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layout.setLayoutParams(lparams);
                layout.addView(input);
                alertDialog.setView(layout);

                alertDialog.setPositiveButton("send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(bt2.getText().toString(), null, input.getText().toString(), null, null);
                        Toast.makeText(existingsoscalls.this, "SMS sent", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertDialog.create().show();


            }
        });

        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(existingsoscalls.this,MapsActivity3.class);
                ArrayList arrayList=new ArrayList();
                arrayList.add(bt3.getText().toString());
                arrayList.add(bt4.getText().toString());
                intent.putStringArrayListExtra("soscall",arrayList);
                startActivity(intent);
            }
        });

    }
}
