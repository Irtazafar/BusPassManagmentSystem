package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterScreen extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private TextInputEditText rollNumberEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText contactNumberEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        rollNumberEditText = findViewById(R.id.rollNumberEditText);
        nameEditText = findViewById(R.id.nameEditText);
        contactNumberEditText = findViewById(R.id.contactNumberEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        MaterialButton registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFunction();
            }
        });
    }

    public boolean validate() {
        boolean check = true;
        String rollNumber = rollNumberEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String contactNumber = contactNumberEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Regular expression for roll number format XXF-XXXX
        String rollNumberPattern = "^[0-9]{2}[F]-[0-9]{4}$";
        Pattern pattern = Pattern.compile(rollNumberPattern);
        Matcher matcher = pattern.matcher(rollNumber);

        // Check if any fields are empty
        if (TextUtils.isEmpty(rollNumber) || TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(contactNumber) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {

            if (TextUtils.isEmpty(rollNumber)) {
                rollNumberEditText.setError("Please enter your roll number");
                rollNumberEditText.requestFocus();
                check = false;
            }
            if (TextUtils.isEmpty(name)) {
                nameEditText.setError("Please enter your name");
                nameEditText.requestFocus();
                check = false;
            }
            if (TextUtils.isEmpty(contactNumber)) {
                contactNumberEditText.setError("Please enter your contact number");
                contactNumberEditText.requestFocus();
                check = false;
            }
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Please enter your email");
                emailEditText.requestFocus();
                check = false;
            }
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Please enter your password");
                passwordEditText.requestFocus();
                check = false;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                confirmPasswordEditText.setError("Please confirm your password");
                confirmPasswordEditText.requestFocus();
                check = false;
            }
            return check;
        } else {
            if (!matcher.matches()) {
                // If roll number doesn't match the pattern
                rollNumberEditText.setError("Invalid Roll Number. Format should be XXF-XXXX");
                check = false;
            }

            // Name validation: no numbers or special characters
            if (!name.matches("[a-zA-Z ]+")) {
                nameEditText.setError("Invalid Name. Names should only contain letters and spaces");
                check = false;
            }

            // Password and Confirm Password match and length check
            if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match");
                confirmPasswordEditText.requestFocus();
                check = false;
            } else if (password.length() < 8) {
                passwordEditText.setError("Password should be at least 8 characters long");
                passwordEditText.requestFocus();
                check = false;
            }

            return check;
        }
    }

    private void SignUpFunction() {

        final String rollNumber = rollNumberEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String contactNumber = contactNumberEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (validate()) {
            fStore.collection("Users")
                    .whereEqualTo("RollNum", rollNumber)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {
                                    Toast.makeText(RegisterScreen.this, "User with this roll number already exists", Toast.LENGTH_SHORT).show();

                                } else {
                                    fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(RegisterScreen.this, "Account has been Created", Toast.LENGTH_SHORT).show();
                                            FirebaseUser user = fAuth.getCurrentUser();
                                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                                            Map<String, Object> userInfo = new HashMap<>();
                                            userInfo.put("RollNum", rollNumber);
                                            userInfo.put("Name", name);
                                            userInfo.put("Contact", contactNumber);
                                            userInfo.put("Email", email);
                                            userInfo.put("Password", password);
                                            userInfo.put("userLevel", "2");
                                            userInfo.put("pass", false);
                                            userInfo.put("accountStatus", false);
                                            df.set(userInfo);

                                            FirebaseMessaging.getInstance().getToken()
                                                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<String> task) {
                                                                    if (task.isSuccessful()) {
                                                                        String token = task.getResult();
                                                                        saveTokenToDatabase(user.getUid(), token);
                                                                    } else {
                                                                        Toast.makeText(RegisterScreen.this, "Failed to generate token", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                            startActivity(new Intent(getApplicationContext(), LoginScreen.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterScreen.this, "Fail to create account. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(RegisterScreen.this, "Error checking roll number existence", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private void saveTokenToDatabase(String uid, String token) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .update("token",token)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(getApplicationContext(), "Token saved successfully", Toast.LENGTH_SHORT).show();
                        sendRequestNotification();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getApplicationContext(), "Failed to save token", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendRequestNotification() {
        MyFirebaseMessagingService service = new MyFirebaseMessagingService();
        service.showNewMessageNotification(this,"Registered","Please Wait while Admin Approves your Account :)");
    }
}