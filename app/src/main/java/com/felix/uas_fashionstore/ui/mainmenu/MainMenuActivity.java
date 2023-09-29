package com.felix.uas_fashionstore.ui.mainmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.felix.uas_fashionstore.MainActivity;
import com.felix.uas_fashionstore.R;
import com.felix.uas_fashionstore.data.DBController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.felix.uas_fashionstore.databinding.ActivityMainMenuBinding;

public class MainMenuActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainMenuBinding binding;
    public DBController controller;

    public BottomNavigationView navView;
    public SharedPreferences sharedPref;
    public ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        actionBar = getSupportActionBar();

        navView = findViewById(R.id.nav_view);
        get_preferences();
        controller = new DBController(this,"",null,1);

        if (controller.get_state() == 1) navView.setVisibility(View.GONE);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_wishlist, R.id.navigation_transactions)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            //Kode buat signout di sini
            controller.logout();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_menu);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void get_preferences() {
        sharedPref = this.getSharedPreferences("UserSharedPref", MODE_PRIVATE);
    }

    public Integer get_tipe() {
        get_preferences();
        return sharedPref.getInt("type", 0);
    }

    public String get_user() {
        get_preferences();
        return sharedPref.getString("user", "");
    }
}