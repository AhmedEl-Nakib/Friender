package com.example.ahmed.friender;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class signUp extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    EditText email , name  , password , phone ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference myRef ;
    TextView secret ;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double lat = 0, lan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = (EditText)findViewById(R.id.input_email);
        name = (EditText)findViewById(R.id.input_userName);
        password = (EditText)findViewById(R.id.input_password);
        phone = (EditText)findViewById(R.id.input_phone);
        secret = (TextView)findViewById(R.id.secretId);
        secret.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Sign Up");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void signup(View view) {
        Random rand = new Random();
        int  random = rand.nextInt(5000) + 1;
        final String Ran = String.valueOf(random);

        if(email.getText().toString().length() == 0)
            email.setError("Please Enter Email");
        if(name.getText().toString().length() == 0)
            name.setError("Please Enter Name");
        if(password.getText().toString().length() == 0)
            password.setError("Please Enter Password");
        if(phone.getText().toString().length() == 0)
            phone.setError("Please Enter Phone");
        else {
            progressDialog.setTitle("Signing Up ....");
            progressDialog.setMessage("Please Wait Until Sign Up Completed .");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(signUp.this, "Authentication failed or Account Is Already Exist",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                String finalRan = name.getText().toString()+"_"+Ran;

                                Map<String, String> map = new HashMap<String, String>();
                                map.put("E_Mail", email.getText().toString());
                                map.put("Name", name.getText().toString());
                                map.put("Password", password.getText().toString());
                                map.put("Phone", phone.getText().toString());
                                map.put("Secret Number" , finalRan);
                                map.put("Latitude",String.valueOf(lat));
                                map.put("Longitude",String.valueOf(lan));
                                myRef.getRef().child(name.getText().toString()).setValue(map);
                                progressDialog.dismiss();
                                Toast.makeText(signUp.this, "Account Created Success.",
                                        Toast.LENGTH_SHORT).show();
                                myRef.child(name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                            /*if(val.getKey().equals(searchWord.getText().toString())){
                                            // search for child of parent
                                                result.setText("Success Found");
                                             }*/
                                        String secId = dataSnapshot.child("Secret Number").getValue().toString();
                                        secret.setVisibility(View.VISIBLE);
                                        secret.setText("Please Save This ID To Allow Other Person To Find You :  "+secId);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });
                            }
                        }
                    });
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10); // Update location every second


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Toast.makeText(this,"onConnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.lat = location.getLatitude();
        this.lan = location.getLongitude();
        final double lat2 = location.getLatitude();
        final double lan2 = location.getLongitude();

        myRef.child(name.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                myRef.getRef().child(name.getText().toString()).child("Latitude").setValue(String.valueOf(lat2));
                myRef.getRef().child(name.getText().toString()).child("Longitude").setValue(String.valueOf(lan2));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }
}
