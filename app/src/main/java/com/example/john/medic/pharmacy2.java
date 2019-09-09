package com.example.john.medic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class pharmacy2 extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adaptor;

    ArrayList<String> pharmdetails=new ArrayList<>();
    Button btaddmed;
    ListView lvpharmdetails;
    public static final String TAG="abcd";
    private DatabaseReference mDatabase;
    Map<String,String> map =new HashMap<String,String>();
    Map<String,String> map2 =new HashMap<String,String>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy2);

        btaddmed=findViewById(R.id.btaddmed);
        lvpharmdetails=findViewById(R.id.lvpharmdetails);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pharmdetails=getIntent().getStringArrayListExtra("pharmdetails");
        Log.d("abcd", "onCreate: "+pharmdetails);
        adaptor=new ArrayAdapter<String>(this,R.layout.pharmacy_list_item,R.id.pharm_medname,arrayList);
        lvpharmdetails.setAdapter(adaptor);

        mDatabase.child("pharmacy").child(pharmdetails.get(0).toString()).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String[] data = {""};
                final ArrayList<String> data2=new ArrayList<>();
                final int[] k = {0};

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().toString().equalsIgnoreCase("location")){
                        continue;}

                       else if (postSnapshot.getKey().toString().equalsIgnoreCase("availability")){

                        for (DataSnapshot postSnapshot2 : postSnapshot.getChildren()) {

                            data2.add(postSnapshot2.getValue().toString());

                        }
                    }

                        else{
                        data[0] =postSnapshot.getKey().toString()+"\n"+postSnapshot.getValue().toString()+"\n"+data2.get(k[0]);
                        k[0]++;

                        arrayList.add(data[0]);
                        adaptor.notifyDataSetChanged();
                        Log.d(TAG, "onDataChange: "+arrayList.get(0));

                       }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final String[] str = {""};
        final String[] str2 = {""};

        lvpharmdetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final TextView textView= view.findViewById(R.id.pharm_medname);
                str[0] ="";
                str2[0]="";
                final ArrayList<String> change_avail=new ArrayList<>();


                new AlertDialog.Builder(pharmacy2.this)
                        .setMessage("Edit this item ?")
                        .setNegativeButton("change availability", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                for (int x=0;x<=textView.getText().toString().length();x++){

                                    if (x==textView.getText().toString().length()){
                                        change_avail.add(str2[0]);
                                        break;
                                    }


                                    if (textView.getText().toString().charAt(x)=='\n') {
                                        change_avail.add(str2[0]);
                                        str2[0]="";
                                        continue;
                                    }



                                    str2[0] = str2[0] +textView.getText().toString().charAt(x);
                                }
                                Log.d(TAG, "onClick: "+change_avail.get(2));

                                mDatabase.child("pharmacy").child(pharmdetails.get(0)).child("availability")
                                        .child(change_avail.get(0)).setValue(toggle_state(change_avail.get(2)));
                                Intent intent=new Intent(pharmacy2.this,pharmacy2.class);
                                intent.putStringArrayListExtra("pharmdetails",pharmdetails);
                                startActivity(intent);
                                finish();


                            }
                        })
                        .setPositiveButton("remove it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                    for (int x=0;x<textView.getText().toString().length();x++){

                                        if (textView.getText().toString().charAt(x)=='\n')
                                            break;

                                        str[0] = str[0] +textView.getText().toString().charAt(x);
                                    }


                                    mDatabase.child("pharmacy").child(pharmdetails.get(0)).child(str[0]).removeValue();
                                mDatabase.child("pharmacy").child(pharmdetails.get(0)).child("availability").child(str[0]).removeValue();
                                Intent intent=new Intent(pharmacy2.this,pharmacy2.class);
                                intent.putStringArrayListExtra("pharmdetails",pharmdetails);
                                startActivity(intent);
                                finish();

                            }
                        }).create().show();


            }
        });


        btaddmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(pharmacy2.this)
                        .setMessage("you want to add medicine ?")
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(pharmacy2.this);
                                alertDialog.setTitle("Enter new Medicine details");

                                final EditText input = new EditText(pharmacy2.this);
                                final EditText input2 = new EditText(pharmacy2.this);
                                final EditText input3 = new EditText(pharmacy2.this);

                                input.setHint("enter medicine name");
                                input2.setHint("enter price");
                                input3.setHint("enter availability(yes/no)");

                                LinearLayout layout = new LinearLayout(pharmacy2.this);
                                layout.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                layout.setLayoutParams(lparams);
                                layout.addView(input);
                                layout.addView(input2);
                                layout.addView(input3);
                                alertDialog.setView(layout);

                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);

                                alertDialog.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent intent=new Intent(pharmacy2.this,pharmacy2.class);
                                        intent.putStringArrayListExtra("pharmdetails",pharmdetails);
                                        startActivity(intent);
                                        finish();



                                        mDatabase.child("pharmacy").child(pharmdetails.get(0)).child(input.getText().toString())
                                                .setValue(input2.getText().toString());

                                        mDatabase.child("pharmacy").child(pharmdetails.get(0)).child("availability").child(input.getText().toString())
                                                .setValue(input3.getText().toString());



                                    }
                                });
                                alertDialog.create().show();



                            }
                        }).create().show();
            }
        });

    }
    String toggle_state(String str){

    if (str.equalsIgnoreCase("yes"))
        return "no";
    else if (str.equalsIgnoreCase("no"))
        return "yes";

    return "";}

    }

