package com.example.allrim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MealActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_notice:
                        Toast.makeText(MealActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_schedule:
                        Toast.makeText(MealActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_writing:
                        Toast.makeText(MealActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_comment:
                        Toast.makeText(MealActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_set:
                        Toast.makeText(MealActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.navigation_item_board:
                        Toast.makeText(MealActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });

    }

}