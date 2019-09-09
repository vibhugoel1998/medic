package com.example.john.medic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class useractivity extends AppCompatActivity {

    EditText etusername,etusernumber;
    Button btenter;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useractivity);

        final EditText etusername=findViewById(R.id.etusername);
        final EditText etusernumber=findViewById(R.id.etusernumber);
        Button btenter=findViewById(R.id.btenter);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        final ArrayList<String> userdetails=new ArrayList<>();

        btenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userdetails.add(etusername.getText().toString());
                userdetails.add(etusernumber.getText().toString());
                mDatabase.child("users").child(userdetails.get(0)).child("number").setValue(userdetails.get(1));

                Intent intent = new Intent(useractivity.this,user2screen.class);
                intent.putStringArrayListExtra("userdetails",userdetails);
                startActivity(intent);
            }
        });



    }
}
