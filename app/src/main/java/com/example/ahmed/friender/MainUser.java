package com.example.ahmed.friender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainUser extends AppCompatActivity {

    EditText friendSecretName , MySecret , FriendSecret;
    TextView textView , mySecretText;
    String sessionId;
    FirebaseDatabase database;
    DatabaseReference myRef;
    UserDataBaseManager userDataBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        MySecret = (EditText) findViewById(R.id.mySecret);
        FriendSecret = (EditText) findViewById(R.id.friendSecret);
        friendSecretName = (EditText)findViewById(R.id.friendSecretName);
        textView = (TextView)findViewById(R.id.textView);
        mySecretText = (TextView)findViewById(R.id.mySecretText);
        sessionId = getIntent().getStringExtra("myEmail");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Sign Up");
        userDataBaseManager = new UserDataBaseManager(this);

    }

    public void getMyId(View view) {
        try {
            if (MySecret.getText().toString().length() == 0)
                MySecret.setError("Please Enter Name First");
            else {

                    myRef.child(MySecret.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String secId = dataSnapshot.child("Secret Number").getValue().toString();

                            mySecretText.setText(secId);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }
        }catch (Exception e) {
            Toast.makeText(this, "Exception " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void GoToMap(View view) {
        try {
            if (FriendSecret.getText().toString().length() == 0)
                FriendSecret.setError("Please Enter FriendSecret ID");
            if (friendSecretName.getText().toString().length() == 0)
                friendSecretName.setError("Please Enter FriendSecret Name");

            else {
                myRef.child(friendSecretName.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("Secret Number").getValue().equals(FriendSecret.getText().toString())) {
                                    String Name = dataSnapshot.child("Name").getValue().toString();
                                    String Latitude = dataSnapshot.child("Latitude").getValue().toString();
                                    String Longitude = dataSnapshot.child("Longitude").getValue().toString();

                                    Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                                    intent.putExtra("Name", Name);
                                    intent.putExtra("Latitude", Latitude);
                                    intent.putExtra("Longitude", Longitude);
                                    startActivity(intent);
                                    Toast.makeText(MainUser.this,userDataBaseManager.insertToMyData(friendSecretName.getText().toString(),FriendSecret.getText().toString()),Toast.LENGTH_SHORT).show();
                                    textView.setText(Name + "\t" + Latitude + "\t" + Longitude);
                                }
                                else {
                                    textView.setText("Data Not Found");

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
            }
        }catch (Exception e)
        {
            Toast.makeText(this,"Exception "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void move(View view) {
        startActivity(new Intent(this,UserDataBase.class));
    }
}