package com.example.sami.visitmetz_v2;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static int SPLASH_TIME_OUT = 3000;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //changer color de statue bar
        if (Build.VERSION.SDK_INT >=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.bleu));
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_map);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_ajouter:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AjouterSiteFragment()).commit();
                break;

            case R.id.nav_tous:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SitesOverviewFragment()).commit();
                break;

            /*case R.id.nav_favoris:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MesSitesFavorisFragment()).commit();
                break;*/

            case R.id.nav_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapFragment()).commit();
                break;

            case R.id.nav_histoire:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoireFragment()).commit();
                break;

            /*case R.id.nav_monuments:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SitesHistoriquesMonumentFragment()).commit();
                break;

            case R.id.nav_gastronomie:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GastronomieFragment()).commit();
                break;

            case R.id.nav_autour:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AutourMetzFragment()).commit();
                break;

            case R.id.nav_hebergements:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HebergementFragment()).commit();
                break;

            case R.id.nav_jeux_divertissements:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new JeuxDivertissementFragment()).commit();
                break;

            case R.id.nav_nature:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NatureParcFragment()).commit();
                break;

            case R.id.nav_nocturne:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VieNocturneFragment()).commit();
                break;

            case R.id.nav_shopping:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DestinationShoppingFragment()).commit();
                break;*/

            case R.id.nav_metz:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MetzFragment()).commit();
                break;
            case R.id.nav_sync:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SyncFragment()).commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }
}
