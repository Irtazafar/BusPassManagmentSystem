package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class StudentMainScreen extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main_screen);
        bottomNavigationView = findViewById(R.id.studentmainbottomnav);
        getSupportFragmentManager().beginTransaction().replace(R.id.studentmaincontainer, new StudentHomeFragment()).commit();
        setTitle("Home");
        bottomNavigationView.setSelectedItemId(R.id.studentHome);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.studentHome:
                        setTitle("Home");
                        fragment = new StudentHomeFragment();
                        break;
                    case R.id.studentBus:
                        fragment = new StudentBusFragment();
                        setTitle("Bus List");
                        break;
                    case R.id.studentSettings:
                        fragment = new StudentSettingFragment();
                        setTitle("Settings");
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.studentmaincontainer, fragment).commit();
                return true;
            }
        });
    }
}