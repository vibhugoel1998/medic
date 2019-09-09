package com.example.john.medic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class existinghomedeliveries extends AppCompatActivity {

    Button hdname,hdbill,hdaddress,hdnumber,hdorder,hdcallcc;
    ArrayList<String> medhelperdetails=new ArrayList<>();
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existinghomedeliveries);

        hdname=findViewById(R.id.hdname);
        hdbill=findViewById(R.id.hdbill);


        hdcallcc=findViewById(R.id.hdcallcc);
        hdaddress=findViewById(R.id.hdaddress);
        hdnumber=findViewById(R.id.hdnumber);
        hdorder=findViewById(R.id.hdorder);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        medhelperdetails=getIntent().getStringArrayListExtra("medhelperdetails");


        mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("hdcalls").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                    if (postsnapshot.getKey().toString().equalsIgnoreCase("name"))
                        hdname.setText(postsnapshot.getValue().toString());
                    if (postsnapshot.getKey().toString().equalsIgnoreCase("number"))
                        hdnumber.setText(postsnapshot.getValue().toString());
                    if (postsnapshot.getKey().toString().equalsIgnoreCase("address"))
                        hdaddress.setText(postsnapshot.getValue().toString());
                    if (postsnapshot.getKey().toString().equalsIgnoreCase("bill"))
                        hdbill.setText("Rs."+postsnapshot.getValue().toString());
                    if (postsnapshot.getKey().toString().equalsIgnoreCase("order"))
                        hdorder.setText(postsnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        hdcallcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Intent.ACTION_CALL);
                String tel = "tel:" + hdnumber.getText().toString();
                i1.setData(Uri.parse(tel));

                if (ActivityCompat.checkSelfPermission(existinghomedeliveries.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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


    }
}
