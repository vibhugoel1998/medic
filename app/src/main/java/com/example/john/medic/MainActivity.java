package com.example.john.medic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ImageButton usertbn,pharmbtn,medhelpbtn;
    public static final String SHARED_PREFS="shared_prefs";
    public static final String NAME="name";
    public static final String NUMBER="number";
    public static final String TAG="mainactivity";
    private DatabaseReference mDatabase;
    public static String ID;
    boolean isUserLoggedIn;

    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usertbn=findViewById(R.id.userbtn);
        pharmbtn=findViewById(R.id.pharmbtn);
        medhelpbtn=findViewById(R.id.medhelpbtn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        loginUser();

        usertbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,useractivity.class);
                startActivity(intent);
            }
        });

        pharmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,pharmacy.class);
                startActivity(intent);
            }
        });

        medhelpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                String text1 = sharedPreferences.getString(NAME,"");
                String text2 = sharedPreferences.getString(NUMBER, "-1");

                Log.d(TAG, "onClick: "+text1+"  "+text2);


                if (text1.equalsIgnoreCase("") || text2.equalsIgnoreCase("-1")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("MedHelper Login Form");

                    final EditText input = new EditText(MainActivity.this);
                    final EditText input2 = new EditText(MainActivity.this);

                    input.setHint("enter medhelper name");
                    input2.setHint("enter mobile number");
                    LinearLayout layout = new LinearLayout(MainActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    layout.setLayoutParams(lparams);
                    layout.addView(input);
                    layout.addView(input2);
                    alertDialog.setView(layout);


                    alertDialog.setPositiveButton("enter details", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences2.edit();

                            editor.putString(NAME, input.getText().toString());
                            editor.putString(NUMBER, input2.getText().toString());
                            editor.apply();
                            Toast.makeText(MainActivity.this, "logged in as "+input.getText(), Toast.LENGTH_SHORT).show();

                            ArrayList<String> medhelperdetails=new ArrayList<>();
                            SharedPreferences sharedPreferences3 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                            medhelperdetails.add(0,sharedPreferences3.getString(NAME,""));
                            medhelperdetails.add(1,sharedPreferences3.getString(NUMBER,"-1"));
                            FirebaseMessaging.getInstance().subscribeToTopic("topic_menu");
                            FirebaseMessaging.getInstance().subscribeToTopic("homedelivery");


                            Toast.makeText(MainActivity.this, "subscribed", Toast.LENGTH_SHORT).show();


                            mDatabase.child("medhelpers").child(medhelperdetails.get(0)).child("number").setValue(medhelperdetails.get(1));

                            Intent intent=new Intent(MainActivity.this,medhelper.class);
                            intent.putStringArrayListExtra("medhelperdetails",medhelperdetails);
                            startActivity(intent);
                        }
                    });

                    alertDialog.create().show();




                }

                else {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setMessage("you want to logout?");


                    alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences2.edit();

                            mDatabase.child("medhelpers").child(sharedPreferences2.getString(NAME,"")).removeValue();
                            mDatabase.child("medhelpers").child("sosinfo").child(sharedPreferences2.getString(NAME,"")).removeValue();
                            //mDatabase.child("medhelpers").child("homedeliveryinfo").child(sharedPreferences2.getString(NAME,"")).removeValue();


                            Toast.makeText(MainActivity.this, sharedPreferences2.getString(NAME,"")+" logged out successfully", Toast.LENGTH_SHORT).show();


                            editor.putString(NAME, "");
                            editor.putString(NUMBER, "-1");
                            editor.apply();
                        }
                    });
                    alertDialog.setNegativeButton("No, continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ArrayList<String> medhelperdetails=new ArrayList<>();
                            SharedPreferences sharedPreferences2 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                            medhelperdetails.add(0,sharedPreferences2.getString(NAME,""));
                            medhelperdetails.add(1,sharedPreferences2.getString(NUMBER,"-1"));

                            Toast.makeText(MainActivity.this, "logged in as "+sharedPreferences2.getString(NAME,""), Toast.LENGTH_SHORT).show();
                            FirebaseMessaging.getInstance().subscribeToTopic("topic_menu");
                            FirebaseMessaging.getInstance().subscribeToTopic("homedelivery");

                            Toast.makeText(MainActivity.this, "subscribed", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(MainActivity.this,medhelper.class);
                            intent.putStringArrayListExtra("medhelperdetails",medhelperdetails);
                            startActivity(intent);



                        }
                    });
                    alertDialog.create().show();


                }

            }
        });

    }

    public void loginUser() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());
                            //Log.d("XALAN_7", "onComplete: "+mAuth.getCurrentUser());
                            Log.d("XALAN_LOGIN_FUN",mAuth.getCurrentUser().getUid());
                            ID=mAuth.getCurrentUser().getUid();
                            isUserLoggedIn = true;
                            Toast.makeText(MainActivity.this, "You have successfully Logged In", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInAnonymously:failure", task.getException());
                            isUserLoggedIn = false;
                            loginUser();
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            mAuth.getCurrentUser();
            ID = mAuth.getCurrentUser().getUid();
            Log.d("XALAN_ONSTART_MAIN", "onStart: " + mAuth.getCurrentUser().getUid());
        }
        catch (Exception e){
            Log.d("aaaaaa", "onStart: "+e);
        }
    }
}
