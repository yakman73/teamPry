package com.bilal.teampry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.ims.RegistrationManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    EditText mFullname, mEmail, mPassword, mPassword2, mPhone;
    Button mRegisterBtn;
    TextView malreadyRegister;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;


    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        String TAG = "TAG";
        String KEY_NAME = "fullname";
        String KEY_EMAIL = "Email";
        String KEY_PHONE = "phone";


        mFullname = findViewById(R.id.fullname);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPassword2 = findViewById(R.id.password2);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.RegisterBtn);
        malreadyRegister = findViewById(R.id.alreadyRegister);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        String fullname = mFullname.getText().toString();
        String Email = mEmail.getText().toString();
        String phone = mPhone.getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_NAME, fullname);
        note.put(KEY_EMAIL, Email);
        note.put(KEY_PHONE, phone);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required!");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required!");
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            //register the user firebase auth

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Profile is creating...", Toast.LENGTH_SHORT).show();

                    //register the user firestore cloud

                    db.collection("People").document("Customer").set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterActivity.this, "We kinda know you. :)", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Ooops, an error occured!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        malreadyRegister.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }

}