package com.example.john.medic;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static android.graphics.Color.BLUE;

public class gerealinfo extends AppCompatActivity {

    LinearLayout llgeneralinfo;
    TextView tvourprice;
    Button btourcart;
    ListView lvstoredetails;
    ArrayList<String> userdetails;
    int index=0;
    String cartprice="";
    String location="",latitude="",longitude="";
    String intent_lat="",intent_lng="";


    resultadaptor resultadap;
    ArrayList<ResultsItem> resultArrayList = new ArrayList<>();
    ArrayList<String> shopname=new ArrayList<>();
    Map<String,List<String>> map =new HashMap<String, List<String>>();
    Map<String,List<String>> map2 =new HashMap<String, List<String>>();;
    Map<String,String> map3 =new HashMap<String, String>();;


    ArrayList<String> intentlist=new ArrayList<>();

    ArrayList<String> med_quantity;
    ArrayList<String> med_price;
    ArrayList<String> med_avail;

    int perm1=0;
    int perm2=0;

    private DatabaseReference mDatabase;


    public static final String TAG = "shubhaammmmm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerealinfo);

        llgeneralinfo = findViewById(R.id.llgeneralinfo);
        tvourprice = findViewById(R.id.tvourprice);
        btourcart = findViewById(R.id.btourcart);
        lvstoredetails = findViewById(R.id.lvstoredetails);
        userdetails=new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userdetails= getIntent().getStringArrayListExtra("userdetails");
       for (int i=0;i<userdetails.size();i++)
        Log.d(TAG, "onCreate: "+userdetails.get(i));


         perm1= ContextCompat.checkSelfPermission(gerealinfo.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        perm2=ContextCompat.checkSelfPermission(gerealinfo.this, Manifest.permission.READ_EXTERNAL_STORAGE);





        lvstoredetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                TextView textView= view.findViewById(R.id.shopname);
                Button btavail=view.findViewById(R.id.btavail);

                mDatabase.child("pharmacy").child(textView.getText().toString()).child("location").child("latitude").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        intent_lat=dataSnapshot.getValue().toString();
                        intentlist.add(0,intent_lat);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabase.child("pharmacy").child(textView.getText().toString()).child("location").child("longitude").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        intent_lng=dataSnapshot.getValue().toString();
                        intentlist.add(1,intent_lng);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                new AlertDialog.Builder(gerealinfo.this)
                        .setMessage("view shop's location in MAP?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               Intent intent = new Intent(gerealinfo.this,MapsActivity.class);
                                intent.putStringArrayListExtra("intent_location",intentlist);
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();



            }
        });




        mDatabase.child("stock").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "onDataChange: " + postSnapshot.getKey().toString() + "     " + postSnapshot.getValue().toString());


                    if (userdetails.get(4).toString().equals(postSnapshot.getKey().toString())){
                            tvourprice.setText("OUR PRICE :"+postSnapshot.getValue().toString());
                            cartprice=postSnapshot.getValue().toString();
                        }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Random random=new Random();

        btourcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users").child(userdetails.get(0)).child("cart").child(userdetails.get(4)).push().setValue(random.nextInt(1000));
                Toast.makeText(gerealinfo.this, "item added to cart", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(gerealinfo.this)
                        .setMessage("Proceed to checkout ?")
                        .setPositiveButton("see existing cart", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent intent = new Intent(gerealinfo.this,cart.class);
                                intent.putStringArrayListExtra("userdetails",userdetails);
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



            mDatabase.child("pharmacy").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        shopname.add(postSnapshot.getKey().toString());
                        med_quantity = new ArrayList<>();
                        med_price=new ArrayList<>();

                        for (DataSnapshot snapshot : postSnapshot.getChildren()) {
                            med_quantity.add(snapshot.getKey().toString());
                            med_price.add(snapshot.getValue().toString());
                        }
                        map.put(postSnapshot.getKey().toString(), med_quantity);
                        map2.put(postSnapshot.getKey().toString(),med_price);


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });



        mDatabase.child("pharmacy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange:123 "+dataSnapshot);

                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    med_avail=new ArrayList<>();

                    mDatabase.child("pharmacy").child(postSnapshot.getKey().toString()).child("availability").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot2) {

                            for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {
                                if (snapshot2.getKey().toString().equalsIgnoreCase(userdetails.get(4))){
                                    map3.put(postSnapshot.getKey().toString(),snapshot2.getValue().toString());

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





        Log.d(TAG, "getView: ");

        resultadap = new resultadaptor();
        lvstoredetails.setAdapter(resultadap);
        new downloadpost().execute();


        }


    class resultadaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return resultArrayList.size();
        }

        @Override
        public ResultsItem getItem(int i) {
            return resultArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup parent) {
            if (convertview == null ) {
                convertview = getLayoutInflater().inflate(R.layout.list_view_layout, parent, false);
            }
            final ResultsItem post = getItem(position);



            ((TextView) convertview.findViewById(R.id.shopname)).setText(post.getName());
            ((TextView) convertview.findViewById(R.id.shopaddress)).setText(post.getFormattedAddress());




            Set keys = map.keySet();

            for (Iterator k = keys.iterator(); k.hasNext(); ) {
                String key = (String) k.next();

                ArrayList<String> value = (ArrayList<String>) map.get(key);
                ArrayList<String> value2 = (ArrayList<String>) map2.get(key);

                if (key.equalsIgnoreCase(post.getName())) {

                    for (int l=0;l<value.size();l++) {

                        if (userdetails.get(4).equalsIgnoreCase(value.get(l).toString()))
                            ((TextView) convertview.findViewById(R.id.price)).setText(value2.get(l).toString());


                    }


                }
            }

            Set keys2 = map3.keySet();

            for (Iterator k = keys.iterator(); k.hasNext(); ) {
                String key = (String) k.next();

                String value = String.valueOf(map3.get(key));

                if (key.equalsIgnoreCase(post.getName())) {

                   if (value.equalsIgnoreCase("yes")){
                        ((Button)convertview.findViewById(R.id.btavail)).setBackgroundColor(Color.GREEN);

                    }
                    if (value.equalsIgnoreCase("no")){
                        ((Button)convertview.findViewById(R.id.btavail)).setBackgroundColor(Color.RED);

                    }


                }
            }


            return convertview;
        }

    }

        public class downloadpost extends AsyncTask<Void,Void,ArrayList<ResultsItem>> {

            @Override
            protected ArrayList<ResultsItem> doInBackground(Void... voids) {

                final ArrayList<ResultsItem> posts= new ArrayList<>();
                //28.6153672,76.988214
                //String urlstring="https://maps.googleapis.com/maps/api/place/textsearch/json?query=medical%20stores&location="+userdetails.get(2)+","+userdetails.get(3)+"&radius=1000&type=pharmacy&key=AIzaSyD6je3-7bfbYN9riv3g5kR1zYXZRji3iXY"
                // String urlstring="https://maps.googleapis.com/maps/api/place/textsearch/json?query=medical%20stores&location=28.6153672,76.988214&radius=1000&type=pharmacy&key=AIzaSyD6je3-7bfbYN9riv3g5kR1zYXZRji3iXY";

                String urlstring="https://maps.googleapis.com/maps/api/place/textsearch/json?query=medical%20stores&location=28.8430,77.1045&radius=1000&type=pharmacy&key=AIzaSyD6je3-7bfbYN9riv3g5kR1zYXZRji3iXY";
                URL url = null;

                try {
                    url = new URL(urlstring);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb=new StringBuilder();
                    String bf=br.readLine();

                    final int[] in = {0};
                    while (bf!=null){
                        sb.append(bf);
                        bf=br.readLine();
                    }

                    String data=sb.toString();

                    JSONObject jsonResponse = new JSONObject(data);
                    JSONArray movies = jsonResponse.getJSONArray("results");
                    for(int i=0;i<movies.length();i++){


                         JSONObject movie = movies.getJSONObject(i);
                          ResultsItem post=new ResultsItem();
                        post.setName(movie.getString(ResultsItem.NAME));
                        post.setFormattedAddress(movie.getString(ResultsItem.FORMATTEDADDRESS));

                         location=movie.getString(ResultsItem.GEOMETRY)+"";
                        latitude=location.substring(19,27);
                        longitude="";


                        for (int j=0;j<location.length();j++)
                        {
                            if (location.charAt(j)==','){
                                index=j;
                                break;
                            }
                        }
                        longitude=location.substring(index+7,index+15);

                        Log.d(TAG, "doInBackground: "+map3);
                        Log.d(TAG, "doInBackground: "+latitude+"s"+longitude+"s");
                        Log.d(TAG, "doInBackground: "+post);



                        if (map.size()==0){
                           // String str=readfile();
                        }
                        if (map.size()!=0){


                            Set keys = map.keySet();

                            for (Iterator k = keys.iterator(); k.hasNext(); ) {
                                String key = (String) k.next();
                                ArrayList<String> value = (ArrayList<String>) map.get(key);
                                if (key.equalsIgnoreCase(post.getName())) {
                                    mDatabase.child("pharmacy").child(post.getName()).child("location").child("latitude").setValue(latitude);
                                    mDatabase.child("pharmacy").child(post.getName()).child("location").child("longitude").setValue(longitude);
                                    for (int l=0;l<value.size();l++) {

                                        if (value.get(l).toString().equalsIgnoreCase(userdetails.get(4))){
                                            posts.add(post);

                                        }
                                    }


                                }
                            }
                        }




                        Log.d(TAG, "doInBackground: hii32232iiiiiiiii");




                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                return posts;
            }


            @Override
            protected void onPostExecute(ArrayList<ResultsItem> posts) {
                super.onPostExecute(posts);
                resultArrayList.clear();
                resultArrayList.addAll(posts);
                resultadap.notifyDataSetChanged();
            }

        }

    void writefile(Map<String,List<String>> map){
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File medictxt = new File(sdcard, "medic.txt");
            FileOutputStream foutstream = new FileOutputStream(medictxt, false);
            foutstream.write((map+"").getBytes());
        }

        catch(IOException ioe){
            Log.e(TAG, "cannot write file",ioe);
        }
    }

    String readfile(){

        try {

            File sdcard = Environment.getExternalStorageDirectory();
            File medictxt = new File(sdcard, "medic.txt");
            FileInputStream fileInputStream = new FileInputStream(medictxt);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuilder sb = new StringBuilder("");
            String bf = br.readLine();

            while (bf != null) {
                sb.append(bf);
                sb.append("\n");
                bf = br.readLine();
            }

            return sb.toString();

        }

        catch(IOException ioe){
            Log.e(TAG, "cannot read file",ioe);
            return  "";
        }
    }
}
