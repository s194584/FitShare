package com.example.youfit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected boolean alreadyLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (!alreadyLoggedIn) {
//            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//            startActivity(intent);
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.settings:
                int CurrentFragmentID = Navigation.findNavController(findViewById(R.id.fragment)).getCurrentDestination().getId();
                if (CurrentFragmentID!=R.id.settingsFragment) {
                    Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.settingsFragment);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "You are already in settings dummy",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            case R.id.signOut:
                // TODO: where does signOut go
                // return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

}