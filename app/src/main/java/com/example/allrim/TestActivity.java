package com.example.allrim;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

public class TestActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);

        navigationView.getMenu().getItem(3).setChecked(true);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            mDrawerLayout.closeDrawers();

            int id = menuItem.getItemId();
            if(!menuItem.isChecked()){
                Intent intent;
                switch (id) {
                    case R.id.navigation_item_info:
                        intent = new Intent(this, MyPageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_writing:
                        Toast.makeText(TestActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_schedule:
                        Toast.makeText(TestActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_meal:
                        intent = new Intent(this, MealActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_lost:
                        Toast.makeText(TestActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_set:
                        Toast.makeText(TestActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
            }
            return false;
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}