package de.janoroid.schrottkurs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    final AluminiumFragment aluminiumFragment = new AluminiumFragment();

    final NickelFragment nickelFragment = new NickelFragment();

    final otherMetalFragment otherMetalFragment = new otherMetalFragment();

    final KupferFragment kupferfragment = new KupferFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottomnavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,
                    new AluminiumFragment()).commit();


        }



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {


                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    int id = menuItem.getItemId();

                    if (id == R.id.Aluminium) {
                        setFragment(aluminiumFragment);

                        return true;

                    } else if (id == R.id.Nickel) {
                        setFragment(nickelFragment);
                        return true;
                    } else if (id == R.id.sonstige) {
                        setFragment(otherMetalFragment);
                        return true;
                    } else if (id == R.id.Kupfer) {
                        setFragment(kupferfragment);
                        return true;
                    }
                    return false;

                }


                private void setFragment(Fragment fragment) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                }


            };
}
