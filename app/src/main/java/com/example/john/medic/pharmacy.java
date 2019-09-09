package com.example.john.medic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class pharmacy extends AppCompatActivity {

    EditText etpharmacyname;
          Button btviewdatabase;

          ArrayList<String> pharmdetails=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        etpharmacyname=findViewById(R.id.etpharmacyname);
        btviewdatabase=findViewById(R.id.btviewdatabase);

        btviewdatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pharmdetails.add(0,etpharmacyname.getText().toString());
                Intent intent=new Intent(pharmacy.this,pharmacy2.class);
                intent.putStringArrayListExtra("pharmdetails",pharmdetails);
                startActivity(intent);

            }
        });

    }
}
