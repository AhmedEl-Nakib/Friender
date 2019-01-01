package com.example.ahmed.friender;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signIn extends AppCompatActivity {

    EditText email , password;
    ProgressDialog progressDialog ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email = (EditText)findViewById(R.id.input_email);
        password = (EditText)findViewById(R.id.input_password);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }

    public void signup(View view) {
        startActivity(new Intent(this,signUp.class));
    }

    public void signIn(View view) {

        if (email.getText().toString().length() == 0)
            email.setError("Please Enter Email");
        if (password.getText().toString().length() == 0)
            password.setError("Please Enter Password");
        else {
            progressDialog.setTitle("Signing In");
            progressDialog.setMessage("Please Wait Until Account Confirmation");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(signIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(signIn.this, "Welcome User",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(getBaseContext(), MainUser.class);
                                intent.putExtra("myEmail", email.getText().toString());
                                startActivity(intent);

                            }

                            // ...
                        }
                    });
        }
    }
}
