package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginScreen extends AppCompatActivity {

    SharedPreferences sharedPreference;
    EditText email, password;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    MaterialButton loginBtn,registerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        sharedPreference = getSharedPreferences("MyFile",0);
        email = findViewById(R.id.edittextemail);
        password = findViewById(R.id.edittextpassword);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        loginBtn = findViewById(R.id.loginbtn);
        registerBtn = findViewById(R.id.signUpBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFunction();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFunction();

            }
        });
    }

    public boolean validateInput() {
        boolean check =true;
        if (email.getText().toString().isEmpty()) {
            email.setError("Email is Required !");
            check=false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Password is Required");
            check = false;
        }

        return check;

    }
    public void loginFunction() {
        if (validateInput()) {
            fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    SharedPreferences.Editor editor = sharedPreference.edit();
                    editor.putString("email", email.getText().toString());
                    editor.apply();
                    checkLevel(authResult.getUser().getUid());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // TODO
            //Toast.makeText(this, "Not", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userLevel = documentSnapshot.getString("userLevel");
                boolean accountStatus = documentSnapshot.getBoolean("accountStatus");
                if(userLevel.equals("1"))
                {
                    startActivity(new Intent(getApplicationContext(), AdminMainScreen.class));
                    finish();
                } else if (userLevel.equals("2")) {
                    if (accountStatus) {
                        startActivity(new Intent(getApplicationContext(),StudentMainScreen.class));
                        finish();
                    }else {
                        Toast.makeText(LoginScreen.this, "Account is not active. Please contact admin.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void SignUpFunction() {
        Intent intent = new Intent(this,RegisterScreen.class);
        startActivity(intent);
    }
}