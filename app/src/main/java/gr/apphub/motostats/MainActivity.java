package gr.apphub.motostats;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import gr.apphub.motostats.db.HotOrNotMyVehicles;
import gr.apphub.motostats.service.DownloadService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment objFragment = null;
    HotOrNotMyVehicles entry;
    int vehSum = 0;
ProgressDialog pd;
    String VEHICLES_URL = "http://kostas-menu.gr/fuellog/vehicles.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        objFragment = new menu1_fragment();

        entry = new HotOrNotMyVehicles(this);
        entry.open();
        Cursor getall = entry.getDataveh();
        vehSum = getall.getCount();
        entry.close();


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment)
                .commit();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id. action_refresh) {
            startDownloadService();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action

            objFragment = new menu1_fragment();


        } else if (id == R.id.nav_gallery) {
            if (vehSum > 0) {
                objFragment = new my_vehicles();

            } else {
                objFragment = new menu2_fragment();
            }

        } else if (id == R.id.nav_slideshow) {
            if (vehSum > 0) {
                objFragment = new menu3_fragment();
            } else {
                Toast.makeText(MainActivity.this, "Please add vehicles first", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, objFragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void DownloadJson() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.TRANSACTION_DONE);
       registerReceiver(jsonReceiver, intentFilter);
        Intent i = new Intent(this, DownloadService.class);
        i.putExtra("url", VEHICLES_URL);
       startService(i);
        pd = ProgressDialog.show(this, "Refresh Data",
                "Go intent service go!");
    }

    public void startDownloadService() {

        DownloadJson();

    }

    private BroadcastReceiver jsonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String location = intent.getExtras().getString("location");
            String url = intent.getExtras().getString("url");
            Log.d("---location---", location);
            Log.d("---url---", url);

            if (location == null || location.length() == 0) {
                Toast.makeText(context, "Failed to download json",
                        Toast.LENGTH_LONG).show();
            }
            pd.dismiss();

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
           unregisterReceiver(jsonReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
