package com.example.allrim;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.view.GravityCompat;

public class Test extends AppCompatActivity {
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
       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(MenuItem menuItem) {
               menuItem.setChecked(true);
               mDrawerLayout.closeDrawers();

               int id = menuItem.getItemId();
               switch (id) {
                   case R.id.navigation_item_info:
                       Toast.makeText(Test.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                       break;
                   case R.id.navigation_item_writing:
                       Toast.makeText(Test.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                       break;
                   case R.id.navigation_item_schedule:
                       Toast.makeText(Test.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                       break;
                   case R.id.navigation_item_meal:
                       Toast.makeText(Test.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                       break;
                   case R.id.navigation_item_lost:
                       Toast.makeText(Test.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                       break;
                   case R.id.navigation_item_set:
                       Toast.makeText(Test.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                       break;
               }
               
               return true;
           }
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