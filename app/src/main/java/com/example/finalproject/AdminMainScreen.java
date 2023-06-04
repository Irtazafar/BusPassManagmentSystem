package com.example.finalproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminMainScreen extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_screen);
        bottomNavigationView = findViewById(R.id.adminmainbottomnav);
        getSupportFragmentManager().beginTransaction().replace(R.id.adminmainmaincontainer, new AdminHomeFragment()).commit();
        setTitle("Home");
        bottomNavigationView.setSelectedItemId(R.id.adminnav_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.adminnav_home:
                        setTitle("Home");
                        fragment = new AdminHomeFragment();
                        break;
                    case R.id.adminnav_students:
                        fragment = new ViewAllStudentFragment();
                        setTitle("Students List");
                        break;
                    case R.id.adminnav_pass:
                        fragment = new ViewAllPassRequestsFragment();
                        setTitle("Pass Request");
                        break;
                    case R.id.adminnav_bus:
                        fragment = new ViewAllBusDetailsFragment();
                        setTitle("Bus Schedule");
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.adminmainmaincontainer, fragment).commit();
                return true;
            }
        });
    }

}