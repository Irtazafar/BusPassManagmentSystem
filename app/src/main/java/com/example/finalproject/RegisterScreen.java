package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterScreen extends AppCompatActivity {

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

        MaterialButton registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFunction();
            }
        });
    }

    private void SignUpFunction() {

        String rollNumber = rollNumberEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String contactNumber = contactNumberEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Check if any fields are empty
        if (TextUtils.isEmpty(rollNumber) || TextUtils.isEmpty(name) ||
                TextUtils.isEmpty(contactNumber) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {

            if (TextUtils.isEmpty(rollNumber)) {
                rollNumberEditText.setError("Please enter your roll number");
                rollNumberEditText.requestFocus();
            }
            if (TextUtils.isEmpty(name)) {
                nameEditText.setError("Please enter your name");
                nameEditText.requestFocus();
            }
            if (TextUtils.isEmpty(contactNumber)) {
                contactNumberEditText.setError("Please enter your contact number");
                contactNumberEditText.requestFocus();
            }
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Please enter your email");
                emailEditText.requestFocus();
            }
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("Please enter your password");
                passwordEditText.requestFocus();
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                confirmPasswordEditText.setError("Please confirm your password");
                confirmPasswordEditText.requestFocus();
            }
            return;
        }

        // Regular expression for roll number format XXF-XXXX
        String rollNumberPattern = "^[0-9]{2}[F]-[0-9]{4}$";
        Pattern pattern = Pattern.compile(rollNumberPattern);
        Matcher matcher = pattern.matcher(rollNumber);

        if (!matcher.matches()) {
            // If roll number doesn't match the pattern
            rollNumberEditText.setError("Invalid Roll Number. Format should be XXF-XXXX");
        } else {
            // TODO: Add your registration logic here
        }

        // Name validation: no numbers or special characters
        if (!name.matches("[a-zA-Z ]+")) {
            nameEditText.setError("Invalid Name. Names should only contain letters and spaces");
            return;
        }


        // Password and Confirm Password match and length check
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            confirmPasswordEditText.requestFocus();
            return;
        } else if (password.length() < 8) {
            passwordEditText.setError("Password should be at least 8 characters long");
            passwordEditText.requestFocus();
            return;
        }

    }
}